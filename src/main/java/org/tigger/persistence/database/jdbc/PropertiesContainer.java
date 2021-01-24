package org.tigger.persistence.database.jdbc;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 属性工具类
 *
 * @author kangshaofei
 * @date
 */
public class PropertiesContainer {

    private static Logger logger = Logger.getLogger(PropertiesContainer.class.getSimpleName());

    private static Properties properties = new Properties();

    static {
        String path = System.getProperty("user.dir");
        logger.info("读取配置文件开始,当前配置文件路径:"+path);
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
            boolean keyIsValid = false;
            for (PropertyKey propertyKey : PropertyKey.values()) {
                if (key.equals(propertyKey.key)) {
                    keyIsValid = true;
                    logger.info("键:" + key +"值:"+ properties.get(key) +", 描述:"+propertyKey.desc);
                }
            }
            if (!keyIsValid) {
                throw new RuntimeException("key:" + key + "不合法！");
            }
        }
        logger.info("读取配置文件结束");
    }

    public static String getProperty(PropertyKey propertyKey) {
        return properties.getProperty(propertyKey.key);
    }

    public enum PropertyKey {

        DATABASE_USER_NAME("database.username", "数据库用户名"),

        DATABASE_PASSWORD("database.password", "数据库密码"),

        DATABASE_URL("database.url", "数据库url");

        /**
         * 属性key
         */
        private String key;

        /**
         * 属性描述
         */
        private String desc;

        PropertyKey(String key, String desc) {
            this.key = key;
            this.desc = desc;
        }
    }
}
