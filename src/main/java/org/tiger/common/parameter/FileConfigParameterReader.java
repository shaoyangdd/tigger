package org.tiger.common.parameter;

import org.tiger.common.log.TigerLogger;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 从配置文件读取参数
 *
 * @author 康绍飞
 * @date 2021-02-02
 */
public class FileConfigParameterReader implements ParameterReader {

    private static Logger logger = TigerLogger.getLogger(FileConfigParameterReader.class.getSimpleName());

    private static Properties properties = new Properties();

    @Override
    public Map<String, String> read() {
        Map<String, String> resultMap = new HashMap<>();
        String path = System.getProperty("user.dir");
        logger.info("读取配置文件开始,当前配置文件路径:" + path);
        BufferedReader bufferedReader0;
        try {
            bufferedReader0 = new BufferedReader(new FileReader(path + File.separator + "config.properties"));
        } catch (FileNotFoundException e) {
            logger.info("读取配置文件失败");
            throw new RuntimeException(e);
        }
        try {
            properties.load(bufferedReader0);
        } catch (IOException e) {
            logger.info("加载配置文件失败");
            throw new RuntimeException(e);
        }
        Enumeration propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            resultMap.put(key, (String) properties.get(key));
            logger.info("key:" + key + ", value:" + properties.get(key));
        }
        logger.info("读取配置文件结束");
        Parameters.getParameter().putAll(resultMap);
        return resultMap;
    }

}
