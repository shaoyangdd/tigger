package org.tiger.command.mechine_learning;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.command.machine_learning.Calculator;
import org.tiger.command.machine_learning.ShardingParameter;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;

/**
 * 参数计算单测
 *
 * @author 康绍飞
 * @date 2021/2/17 8:23
 */
@EnableIoc(scanPackages = "org.tiger")
public class CalculatorTest {

    private Logger logger = LoggerFactory.getLogger(CalculatorTest.class);

    private Calculator calculator;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        calculator = BeanFactory.getBean(Calculator.class);
    }

    @Test
    public void test() {
        TigerTask tigerTask = new TigerTask();
        ShardingParameter shardingParameter = calculator.getShardingParameter(tigerTask);
        logger.info(JSON.toJSONString(shardingParameter));
    }
}
