package org.tiger.common.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskExecute;
import org.tiger.common.datastruct.TigerTaskFlow;
import org.tiger.common.datastruct.TigerTaskResourceUse;
import org.tiger.common.parameter.ParameterReaders;
import org.tiger.common.parameter.Parameters;
import org.tiger.common.util.PackageUtil;
import org.tiger.persistence.DataPersistence;
import org.tiger.persistence.file.FileDataPersistence;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean工厂，简单轻量的IOC容器
 *
 * @author 康绍飞
 * @date 2021-01-27
 */
public class BeanFactory {

    private static Logger logger = LoggerFactory.getLogger(BeanFactory.class.getSimpleName());

    /**
     * bean容器
     */
    private static final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * bean自定义容器，存放使用者自己定义的bean
     */
    private static final Map<String, Object> customBeanMap = new ConcurrentHashMap<>();

    private static ParameterReaders parameterReaders = new ParameterReaders();

    /**
     * 是否已注入bean，防止重复调用注入
     */
    private static volatile boolean injectFlag = false;

    /**
     * 自动实例化所有的本包下的bean
     */
    public synchronized static void autowireBean() {
        //注入完成后不再注入
        if (injectFlag) {
            return;
        }
        long start = System.currentTimeMillis();
        //加载参数
        parameterReaders.loadParameters();
        //获取启动注解
        EnableIoc annotation = getEnableIocAnnotation();
        if (annotation == null) {
            throw new RuntimeException("调用:autowireBean的类没有配置启动注解:@EnableIoc");
        }
        //1. 扫描包下所有的bean List<Class> 不包含接口，枚举
        logger.info("开始扫描包下的类...");
        String[] packages = annotation.scanPackages();
        Set<String> classNameSet = new HashSet<>();
        for (String aPackage : packages) {
            List<String> className = PackageUtil.getClassName(aPackage);
            classNameSet.addAll(className);
        }
        for (String s : classNameSet) {
            logger.info("class Name:" + s);
        }
        //2. 实例化所有bean //TODO 处理接口、抽象类
        logger.info("实例化所有的bean...");
        //先实例化、注入用户自定义的bean
        customBean();
        //再实例化、注入加了注解的bean
        for (String s : classNameSet) {
            Class<?> clazz = loadClass(s);
            if (clazz != null) {
                Annotation[] annotations = clazz.getAnnotations();
                if (hasAnnotation(annotations, SingletonBean.class)) {
                    Object instance = beanMap.get(clazz);
                    beanMap.put(clazz, instance == null ? getInstanceByClass(clazz) : instance);
                }
            }
        }
        //3. 注入依赖
        logger.info("注入依赖...");
        beanMap.forEach((k, v) -> {
            inject(beanMap, k, v, 0);
        });
        //4. 实例化之后操作,调用@AfterInstance注解的方法
        logger.info("调用@AfterInstance注解的方法...");
        beanMap.forEach((k, v) -> {
            Method[] methods = k.getMethods();
            if (methods.length > 0) {
                for (Method method : methods) {
                    Annotation[] annotations = method.getAnnotations();
                    if (hasAnnotation(annotations, AfterInstance.class)) {
                        logger.info("@AfterInstance 调用:" + k.getSimpleName() + "的:" + method.getName() + "方法!");
                        executeMethod(method, v);
                    }
                }
            }
        });
        logger.info("Ioc容器初始化bean完成, 耗时:" + (System.currentTimeMillis() - start) + "ms");
        injectFlag = true;
    }

    public static <T> T getBean(Class<T> tClass) {
        return (T) beanMap.get(tClass);
    }


    /**
     * 加载class
     *
     * @param className 类名 xx.xx.Abc
     * @return class
     */
    private static Class<?> loadClass(String className) {
        //这个类特殊，不能加载
        if (className.contains("MessageProtobuf")) {
            return null;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行方法
     *
     * @param method 方法
     * @param object 对象
     */
    private static void executeMethod(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 依赖注入
     *
     * @param beanMap bean容器
     * @param clazz   类对象
     * @param obj     要被注入的对象
     * @param count   递归次数，超过N就是循环依赖，做中断处理
     */
    private static void inject(Map<Class<?>, Object> beanMap, Class<?> clazz, Object obj, int count) {
        //简单粗暴的判断循环依赖
        if (count > 100) {
            throw new RuntimeException("出现了循环依赖，请检查!");
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length != 0) {
                if (hasAnnotation(annotations, Inject.class)) {
                    Object fieldValue = beanMap.get(field.getType());
                    if (fieldValue != null) {//是包下的类
                        setValue(field, obj, fieldValue);
                        //递归注入
                        inject(beanMap, field.getType(), fieldValue, ++count);
                    }
                } else if (hasAnnotation(annotations, InjectParameter.class)) {
                    //注入参数
                    setValue(field, obj, Parameters.get(field.getName()));
                } else if (hasAnnotation(annotations, InjectCustomBean.class)) {
                    logger.info("注入InjectCustomBean:{}", field.getName());
                    //注入用户自定义的bean
                    Object fieldValue = customBeanMap.get(field.getName()) != null ? customBeanMap.get(field.getName()) : beanMap.get(field.getType());
                    setValue(field, obj, fieldValue);
                    inject(beanMap, fieldValue.getClass(), fieldValue, ++count);
                }
            }
        }
    }

    /**
     * 属性设置值
     *
     * @param field      属性
     * @param obj        对象
     * @param fieldValue 值
     */
    private static void setValue(Field field, Object obj, Object fieldValue) {
        field.setAccessible(true);
        try {
            field.set(obj, fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据类对象获取实例对象
     *
     * @param clazz 类对象
     * @return 实例对象
     */
    private static Object getInstanceByClass(Class<?> clazz) {
        try {
            logger.info("实例化：" + clazz.getName());
            return clazz.newInstance();
        } catch (Exception e) {
            //InstantiationException
            if (e.getCause() instanceof NoSuchMethodException) {
                logger.info("请确保:" + clazz.getName() + "有默认的空构造方法!");
            }
            throw new RuntimeException("实例化：" + clazz.getName() + "失败！", e);
        }
    }

    /**
     * 包含某个注解
     *
     * @param annotations     注解列表
     * @param annotationClass 注解
     * @return true, false
     */
    private static boolean hasAnnotation(Annotation[] annotations, Class<? extends Annotation> annotationClass) {
        for (Annotation annotation1 : annotations) {
            if (annotationClass.equals(annotation1.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断调用链的类上是不是加了@EnableIoc注解
     *
     * @return EnableIoc注解
     */
    private static EnableIoc getEnableIocAnnotation() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            String className = stackTraceElement.getClassName();
            Class clazz = loadClass(className);
            if (clazz == null) {
                continue;
            }
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation annotation1 : annotations) {
                if (EnableIoc.class.equals(annotation1.annotationType())) {
                    return (EnableIoc) annotation1;
                }
            }
        }
        return null;
    }

    /**
     * 自定义Bean
     */
    private static void customBean() {
        DataPersistence<TigerTaskFlow> tigerTaskFlowDataPersistence = new FileDataPersistence<>();
        DataPersistence<TigerTask> tigerTaskDataPersistence = new FileDataPersistence<>();
        FileDataPersistence<TigerTaskResourceUse> systemMonitorFileDataPersistence = new FileDataPersistence<>();
        FileDataPersistence<TigerTaskExecute> tigerTaskExecuteDataPersistence = new FileDataPersistence<>();
        customBeanMap.put("tigerTaskFlowDataPersistence", tigerTaskFlowDataPersistence);
        customBeanMap.put("tigerTaskDataPersistence", tigerTaskDataPersistence);
        customBeanMap.put("systemMonitorFileDataPersistence", systemMonitorFileDataPersistence);
        customBeanMap.put("tigerTaskExecuteDataPersistence", tigerTaskExecuteDataPersistence);
    }
}
