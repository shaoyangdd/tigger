package org.tiger.persistence.file;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

    /**
     * 长度
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * 是不是联合主键
     *
     * @return true 是联合主键,false 不是联合主键
     */
    boolean isUnionKey() default false;
}
