package org.tiger.util;

import com.alibaba.fastjson.JSON;
import org.tiger.common.datastruct.Standard;

import java.math.BigDecimal;

/**
 * 测试工具，对象转JSON字符串
 *
 * @author 康绍飞
 * @date 2021/2/17 23:56
 */
public class JsonPrint {


    public static void main(String[] args) {

        getStandard();
    }

    private static void getStandard() {
        Standard standard = new Standard();
        standard.setTaskId(1L);
        standard.setTaskTime(10);
        standard.setCpuUse(new BigDecimal("0.6"));
        System.out.println(JSON.toJSONString(standard));
    }

}
