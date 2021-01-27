package org.tigger.common.ioc;

import org.tigger.common.util.PackageUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean工厂，简单轻量的IOC容器
 * 不需要像Spring一样加Autowire注解即可实现注入
 *
 * @author 康绍飞
 * @date 2021-01-27
 */
public class BeanFactory {

    /**
     * bean容器
     */
    private static final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * 自动实例化所有的本包下的bean
     */
    public void autowireBean() {
        //1. 扫描包下所有的bean List<Class> 不包含接口，枚举
        List<String> className = PackageUtil.getClassName("org.tigger");
        //2. 实例化所有bean
        for (String s : className) {
            Class<?> clazz = loadClass(s);
            beanMap.put(clazz, getInstanceByClass(clazz));
        }
        //3. 注入依赖
        beanMap.forEach((k, v) -> {
            inject(beanMap, k, v, 0);
        });
    }

    /**
     * 加载class
     *
     * @param className 类名 xx.xx.Abc
     * @return class
     */
    private Class<?> loadClass(String className) {
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
    private void inject(Map<Class<?>, Object> beanMap, Class<?> clazz, Object obj, int count) {
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
    private Object getInstanceByClass(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
