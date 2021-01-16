package org.tigger.common;

import org.tigger.communication.client.Client;

/**
 * 对象工厂
 * @author kangshaofei
 * @date 2020-01-16
 */
public class ObjectFactory {

    private static Client client;

    public static Client getClientSingleton() {
        //DCL模式
        if (client == null) {
            synchronized (ObjectFactory.class) {
                if (client == null) {
                    client = new Client();
                }
            }
        }
        return client;
    }

    public static Client getClient() {
        return new Client();
    }
}
