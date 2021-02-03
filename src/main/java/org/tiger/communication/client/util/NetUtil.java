package org.tiger.communication.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NetUtil {

    private static Logger logger = LoggerFactory.getLogger(NetUtil.class.getName());

    /**
     * 在window操作平台上获取局域网所有IP
     *
     * @return ip列表
     */
    public static List<String> getIPsByWindows() {
        logger.info("在window操作平台上获取局域网所有IP开始");
        List<String> list = new ArrayList<>();
        boolean flag = false;
        int count = 0;
        Runtime r = Runtime.getRuntime();
        Process p;
        try {
            p = r.exec("arp -a");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String inline;
            while ((inline = br.readLine()) != null) {
                if (inline.indexOf("接口") > -1) {
                    flag = !flag;
                    if (!flag) {
                        //碰到下一个"接口"退出循环
                        break;
                    }
                }
                if (flag) {
                    count++;
                    if (count > 2) {
                        //有效IP
                        String[] str = inline.split(" {4}");
                        list.add(str[0]);
                    }
                }
                logger.info(inline);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("ip 列表为:" + list);
        return list;
    }
}
