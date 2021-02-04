package org.tiger.command.net;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.communication.client.util.NetUtil;

import java.util.List;


public class NetTest {

    private Logger logger = LoggerFactory.getLogger(NetTest.class.getSimpleName());

    @Test
    public void getAllLocalAreaIp() {
        List<String> ipList = NetUtil.getIPsByWindows();
        for (String s : ipList) {
            logger.info(s);
        }
    }


}
