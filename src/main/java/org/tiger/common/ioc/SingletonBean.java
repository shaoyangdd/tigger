package org.tiger.common.ioc;

import java.lang.annotation.*;

/**
 * IOC窗口注解，代表单例BEAN
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingletonBean {


}
