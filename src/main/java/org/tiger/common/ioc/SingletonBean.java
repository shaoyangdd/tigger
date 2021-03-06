package org.tiger.common.ioc;

import java.lang.annotation.*;

/**
 * IOC窗口注解，代表单例BEAN
 *
 * @author 康绍飞
 * @date 2021-02-02
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingletonBean {


}
