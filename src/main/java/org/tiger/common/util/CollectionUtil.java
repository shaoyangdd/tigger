package org.tiger.common.util;

import java.util.Collection;

/**
 * 集合工具类
 *
 * @author 康绍飞
 * @date 2021-02-06
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

}
