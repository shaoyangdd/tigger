package org.tiger.common.ioc;

import java.lang.annotation.*;

/**
 * bean实例化、注入依赖之后需要调用的方法
 *
 * @author 康绍飞
 * @date 2021-02-02
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterInstance {
}
