package org.tiger.common.util;

/**
 * 系统工具类
 *
 * @author 康绍飞
 * @date 2021-02-29
 */
public class SystemUtil {

    /**
     * 判断当前系统是不是unix
     *
     * @return boolean
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * 判断当前系统是不是unix
     *
     * @return boolean
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 判断当前系统是不是 mac操作系统
     *
     * @return boolean
     */
    public static boolean isMacOs() {
        return System.getProperty("os.name").toLowerCase().contains("Mac OS");
    }
}
