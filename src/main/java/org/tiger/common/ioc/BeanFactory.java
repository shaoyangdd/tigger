package org.tiger.common.ioc;

import org.tiger.common.datastruct.AutowireBeanParameter;
import org.tiger.common.log.TigerLogger;
import org.tiger.common.util.PackageUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * bean工厂，简单轻量的IOC容器
 *
 * @author 康绍飞
 * @date 2021-01-27
 */
public class BeanFactory {

    private static Logger logger = TigerLogger.getLogger(BeanFactory.class.getSimpleName());

    /**
     * bean容器
     */
    private static final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * 自动实例化所有的本包下的bean
     */
    public static void autowireBean(AutowireBeanParameter autowireBeanParameter) {
        //1. 扫描包下所有的bean List<Class> 不包含接口，枚举
        logger.info("开始扫描包下的类...");
        List<String> className = PackageUtil.getClassName(autowireBeanParameter.getPackageName());
        for (String s : className) {
            logger.info("class Name:" + s);
        }
        //2. 实例化所有bean //TODO 处理接口、抽象类
        logger.info("实例化所有的bean...");
        for (String s : className) {
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
        logger.info("Ioc容器初始化bean完成。");
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
     * 依赖注入
     *
     * @param beanMap bean容器
     * @param clazz   类对象
     * @param count   递归次数，超过N就是循环依赖，做中断处理
     */
    private static void inject(Map<Class<?>, Object> beanMap, Class<?> clazz, Object obj, int count) {
        //简单粗暴的判断循环依赖
        if (count > 100) {
            throw new RuntimeException("出现了循环依赖，请检查!");
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Object fieldValue = beanMap.get(field.getType());
            if (fieldValue != null) {//是包下的类
                field.setAccessible(true);
                try {
                    field.set(obj, fieldValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                //递归注入
                inject(beanMap, field.getType(), fieldValue, ++count);
            }
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
}