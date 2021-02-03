package org.tiger.common.parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 环境参数获取
 *
 * @author 康绍飞
 * @date 2021-02-03
 */
public class EnvironmentParameterReader implements ParameterReader {

    private Logger logger = LoggerFactory.getLogger(EnvironmentParameterReader.class.getSimpleName());

    @Override
    public Map<String, String> read() {
        logger.info("开始读取环境变量...");
        Map<String, String> map = System.getenv();
        map.forEach((k, v) -> {
            logger.info("key:{},value:{}", k, v);
        });
        logger.info("读取环境变量结束。");
        return map;
    }
}
