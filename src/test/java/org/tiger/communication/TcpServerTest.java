package org.tiger.communication;

import org.junit.Before;
import org.junit.Test;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.communication.server.Server;

@EnableIoc(scanPackages = "org.tiger")
public class TcpServerTest {

    private Server server;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        server = BeanFactory.getBean(Server.class);
    }

    @Test
    public void run() {
        server.run();
    }
}
