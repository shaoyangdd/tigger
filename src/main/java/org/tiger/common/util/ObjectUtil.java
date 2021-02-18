package org.tiger.common.util;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 对象操作工具类
 *
 * @author 康绍飞
 * @date 2021/2/19 0:11
 */
public class ObjectUtil {

    /**
     * 对象拷贝，把源对象的非空字段拷到目标对象
     *
     * @param source source 所需要的转成的对象类
     * @param dest   dest 被复制的对象
     */
    public static void copyNotNullField(Object source, Object dest) {
        Method[] methods = dest.getClass().getDeclaredMethods();
        Method[] sourceMethods = source.getClass().getDeclaredMethods();
        HashMap<String, Object> map = new HashMap<>();
        try {
            for (int i = 0; i < methods.length; i++) {
                String methodName = methods[i].getName();
                if (methodName.contains("get")) {
                    String fieldName = methodName.substring(3);
                    map.put(fieldName, methods[i].invoke(dest));
                }
            }
            for (int i = 0; i < sourceMethods.length; i++) {
                String methodName = sourceMethods[i].getName();
                if (methodName.contains("set")) {
                    String fieldName = methodName.substring(3);
                    if (map.get(fieldName) != null) {
                        //非空set进去
                        sourceMethods[i].invoke(dest, map.get(fieldName));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
