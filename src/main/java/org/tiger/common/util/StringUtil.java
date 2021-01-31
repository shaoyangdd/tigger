package org.tiger.common.util;

import static org.tiger.common.Constant.SPACE;

public class StringUtil {

    public static String leftPadSpace(String s, int totalLength) {
        StringBuilder stringBuilder = new StringBuilder();
        int padLength = totalLength - s.length();
        if (padLength > 0) {
            for (int i = 0; i < padLength; i++) {
                stringBuilder.append(SPACE);
            }
            return stringBuilder.append(s).toString();
        } else if (padLength < 0) {
            throw new RuntimeException("s too long!");
        } else {
            return s;
        }
    }


}
