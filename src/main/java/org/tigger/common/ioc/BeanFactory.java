package org.tigger.common.ioc;

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

        //（反射）按依赖的本包下的类的个数排序，依赖的少的在前，先组装依赖的少的，（TODO 先不解决循环依赖）

        //（反射）依次组装bean,并注册到Map

    }

}
