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

    private Map<Class, Object> beanMap = new ConcurrentHashMap<>();


    public void autowireBean() {
        //扫描包下所有的bean List<Class> 不包含接口，枚举
        List<String> className = PackageUtil.getClassName("org.tigger");
        for (String s : className) {
            beanMap.put(loadClass(s), null);
        }
        beanMap.forEach((k, v) -> {
            instant(beanMap, k, 0);
        });
    }

    private Class loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实例化
     *
     * @param beanMap
     * @param clazz
     * @param count   递归次数，超过N就是循环依赖，做中断处理
     * @return
     */
    private void instant(Map<Class, Object> beanMap, Class clazz, int count) {
        if (count > 100) {
            throw new RuntimeException("出现了循环依赖，请检查!");
        }
        Object obj = getInstanceByClass(clazz);
        beanMap.put(clazz, obj);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (beanMap.containsKey(field.getType())) {
                //是包下的类
                Object objField = beanMap.get(field.getType());
                if (objField == null) {
                    instant(beanMap, field.getType(), count++);
                }
            }
        }
    }

    private Object getInstanceByClass(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
