package org.tiger.command.task_node;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.tiger.command.Starter;
import org.tiger.common.datastruct.LogicTaskNode;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskFlow;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.common.util.CollectionUtil;

@EnableIoc(scanPackages = "org.tiger")
public class TaskTest {

    private Starter starter;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        starter = BeanFactory.getBean(Starter.class);
    }

    @Test
    public void node() {
        LogicTaskNode logicTaskNode = starter.buildLogicTaskNode();
        print(logicTaskNode);
    }

    private void print(LogicTaskNode logicTaskNode) {
        TigerTask tigerTask = logicTaskNode.getCurrentTigerTask();
        System.out.println(tigerTask == null ? "【头节点】" : tigerTask.getTaskName());
        System.out.println("↓");
        if (CollectionUtil.isNotEmpty(logicTaskNode.getNextTigerTaskList())) {
            for (LogicTaskNode taskNode : logicTaskNode.getNextTigerTaskList()) {
                print(taskNode);
            }
        } else {
            System.out.println("尾节点");
        }
    }

    @Test
    public void makeData() {

        TigerTask tigerTask0 = new TigerTask();
        tigerTask0.setId(0);
        tigerTask0.setTaskName("清算文件下载");

        TigerTask tigerTask1 = new TigerTask();
        tigerTask1.setId(1);
        tigerTask1.setTaskName("清算文件拆分");

        TigerTask tigerTask2 = new TigerTask();
        tigerTask2.setId(2);
        tigerTask2.setTaskName("清算文件待入账");

        TigerTask tigerTask3 = new TigerTask();
        tigerTask3.setId(3);
        tigerTask3.setTaskName("入账");

        TigerTask tigerTask4 = new TigerTask();
        tigerTask4.setId(4);
        tigerTask4.setTaskName("账单");

        TigerTask tigerTask5_1 = new TigerTask();
        tigerTask5_1.setId(5);
        tigerTask5_1.setTaskName("生成账单文件");
        TigerTask tigerTask5_2 = new TigerTask();
        tigerTask5_2.setId(6);
        tigerTask5_2.setTaskName("生成账单短信");

        TigerTask tigerTask6 = new TigerTask();
        tigerTask6.setId(7);
        tigerTask6.setTaskName("文件上传");

        System.out.println(JSON.toJSONString(tigerTask0));
        System.out.println(JSON.toJSONString(tigerTask1));
        System.out.println(JSON.toJSONString(tigerTask2));
        System.out.println(JSON.toJSONString(tigerTask3));
        System.out.println(JSON.toJSONString(tigerTask4));
        System.out.println(JSON.toJSONString(tigerTask5_1));
        System.out.println(JSON.toJSONString(tigerTask5_2));
        System.out.println(JSON.toJSONString(tigerTask6));

        System.out.println("======================");
        // Flow
        TigerTaskFlow tigerTaskFlow0 = new TigerTaskFlow();
        tigerTaskFlow0.setId(0);
        tigerTaskFlow0.setPreviousTaskId("");
        tigerTaskFlow0.setNextTaskId(String.valueOf(1));
        tigerTaskFlow0.setTaskName("清算文件下载");

        TigerTaskFlow tigerTaskFlow1 = new TigerTaskFlow();
        tigerTaskFlow1.setId(1);
        tigerTaskFlow1.setPreviousTaskId("0");
        tigerTaskFlow1.setNextTaskId(2 + "");
        tigerTaskFlow1.setTaskName("清算文件拆分");

        TigerTaskFlow tigerTaskFlow2 = new TigerTaskFlow();
        tigerTaskFlow2.setId(2);
        tigerTaskFlow2.setPreviousTaskId("1");
        tigerTaskFlow2.setNextTaskId(3 + "");
        tigerTaskFlow2.setTaskName("清算文件待入账");

        TigerTaskFlow tigerTaskFlow3 = new TigerTaskFlow();
        tigerTaskFlow3.setId(3);
        tigerTaskFlow3.setPreviousTaskId("2");
        tigerTaskFlow3.setNextTaskId(4 + "");
        tigerTaskFlow3.setTaskName("入账");

        TigerTaskFlow tigerTaskFlow4 = new TigerTaskFlow();
        tigerTaskFlow4.setId(4);
        tigerTaskFlow4.setPreviousTaskId("3");
        tigerTaskFlow4.setNextTaskId(5 + "," + 6);
        tigerTaskFlow4.setTaskName("账单");

        TigerTaskFlow tigerTaskFlow5_1 = new TigerTaskFlow();
        tigerTaskFlow5_1.setId(5);
        tigerTaskFlow5_1.setPreviousTaskId("4");
        tigerTaskFlow5_1.setNextTaskId(7 + "");
        tigerTaskFlow5_1.setTaskName("账单文件");

        TigerTaskFlow tigerTaskFlow5_2 = new TigerTaskFlow();
        tigerTaskFlow5_2.setId(6);
        tigerTaskFlow5_2.setPreviousTaskId("4");
        tigerTaskFlow5_2.setNextTaskId(7 + "");
        tigerTaskFlow5_2.setTaskName("账单短信");

        TigerTaskFlow tigerTaskFlow6 = new TigerTaskFlow();
        tigerTaskFlow6.setId(7);
        tigerTaskFlow6.setPreviousTaskId("5,6");
        tigerTaskFlow6.setNextTaskId("");
        tigerTaskFlow6.setTaskName("文件上传");
        System.out.println(JSON.toJSONString(tigerTaskFlow0));
        System.out.println(JSON.toJSONString(tigerTaskFlow1));
        System.out.println(JSON.toJSONString(tigerTaskFlow2));
        System.out.println(JSON.toJSONString(tigerTaskFlow3));
        System.out.println(JSON.toJSONString(tigerTaskFlow4));
        System.out.println(JSON.toJSONString(tigerTaskFlow5_1));
        System.out.println(JSON.toJSONString(tigerTaskFlow5_2));
        System.out.println(JSON.toJSONString(tigerTaskFlow6));
    }

}
