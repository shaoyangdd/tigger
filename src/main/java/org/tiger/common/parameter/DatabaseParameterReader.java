package org.tiger.common.parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * 从数据库读取参数读取参数
 *
 * @author 康绍飞
 * @date 2021-02-02
 */
public class DatabaseParameterReader implements ParameterReader {

    @Override
    public Map<String, String> read() {
        return new HashMap<>();
    }
}
