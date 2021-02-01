package org.tiger.common.parameter;

import org.tiger.common.datastruct.AutowireBeanParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数，从外部读取的参数，如从配置文件或者调接口查到的，或查询数据库的
 *
 * @author 康绍飞
 * @date 2021-02-02
 */
public class Parameters {

    private static AutowireBeanParameter autowireBeanParameter;

    private static Map<String, String> parameter = new HashMap<>();

    public static AutowireBeanParameter getAutowireBeanParameter() {
        return autowireBeanParameter;
    }

    public static Map<String, String> getParameter() {
        return parameter;
    }

    public static String get(String key) {
        return parameter.get(key);
    }
}
