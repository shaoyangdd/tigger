package org.tiger.common.util;

import static org.tiger.common.Constant.SPACE;

/**
 * 字符串工具类
 *
 * @author 康绍飞
 * @date 2021-02-03
 */
public class StringUtil {

    /**
     * 左补空格
     *
     * @param s           要补的字符串
     * @param totalLength 补完后的总长度
     * @return 补后的字符串
     */
    public static String leftPadSpace(String s, int totalLength) {
        return padSpace(s, totalLength, false);
    }


    public static String padSpace(String s, int totalLength, boolean right) {
        StringBuilder stringBuilder = new StringBuilder();
        int padLength = totalLength - s.length();
        if (padLength > 0) {
            for (int i = 0; i < padLength; i++) {
                stringBuilder.append(SPACE);
            }
            if (right) {
                return s.concat(stringBuilder.toString());
            } else {
                return stringBuilder.append(s).toString();
            }
        } else if (padLength < 0) {
            throw new RuntimeException("s too long!");
        } else {
            return s;
        }
    }

    /**
     * 右补空格
     *
     * @param s           要补的字符串
     * @param totalLength 补完后的总长度
     * @return 补后的字符串
     */
    public static String rightPadSpace(String s, int totalLength) {
        return padSpace(s, totalLength, true);
    }


    /**
     * 字符串是否为null 或 ""
     *
     * @param string 字符串
     * @return true or false
     */
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * 字符串是否为null 或 ""
     *
     * @param string 字符串
     * @return true or false
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
}
