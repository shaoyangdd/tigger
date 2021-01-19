package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.tigger.command.Starter;
import org.tigger.common.ObjectFactory;
import org.tigger.common.config.TigerConfiguration;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void execute()
    {
        TigerConfiguration tigerConfiguration = ObjectFactory.instance().getTigerConfiguration();
        tigerConfiguration.configTaskExecutor(new SpringBatchTaskExecutor());
        Starter.run();
        assertTrue( true );
    }
}
