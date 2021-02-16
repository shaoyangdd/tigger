package org.tiger.common.ioc;

import java.util.Map;

/**
 * 自定义bean，实现此bean，能将自己自定义的bean注入到容器
 *
 * @author 康绍飞
 * @date 2021-02-16
 */
public interface CustomBean {

    /**
     * 自定义bean
     *
     * @return map key:bean名称；value:bean对象
     */
    Map<String, Object> custom();

}
