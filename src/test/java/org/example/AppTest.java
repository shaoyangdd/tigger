package org.example;

import org.junit.Test;
import org.tigger.command.Starter;
import org.tigger.common.ObjectFactory;
import org.tigger.common.config.TigerConfiguration;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private Starter starter;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void execute() {
        TigerConfiguration tigerConfiguration = ObjectFactory.instance().getTigerConfiguration();
        tigerConfiguration.configTaskExecutor(new SpringBatchTaskExecutor());
        starter.run();
        assertTrue(true);
    }
}
