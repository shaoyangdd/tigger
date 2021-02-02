package org.tiger.communication;

import org.junit.Before;
import org.junit.Test;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.communication.client.Client;

@EnableIoc(scanPackages = "org.tiger")
public class TcpClientTest {

    private Client client;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        client = BeanFactory.getBean(Client.class);
    }

    @Test
    public void connect() {
        client.connect("localhost", 9999);
    }

}
