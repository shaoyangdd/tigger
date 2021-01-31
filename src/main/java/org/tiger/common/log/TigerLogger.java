package org.tiger.common.log;

import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * 日志打印
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
public class TigerLogger {

    /**
     * 获取日志打印logger
     *
     * @param name logger名称
     * @return Logger
     */
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        try {
            Formatter formatter = new TigerFormatter();
            //输出到文件
            FileHandler fileHandler = new FileHandler("tiger.log", true);
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            //默认的控制台打印器ConsoleHandler，改一下格式，原格式太难看了
            Handler[] handlers = logger.getParent().getHandlers();
            handlers[0].setFormatter(formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return logger;
    }
}
