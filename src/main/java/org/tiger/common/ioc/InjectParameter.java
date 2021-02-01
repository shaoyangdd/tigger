package org.tiger.common.ioc;

import java.lang.annotation.*;

/**
 * 注入参数
 *
 * @author 康绍飞
 * @date 2021-02-02
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectParameter {
}
