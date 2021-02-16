package org.tiger.common.ioc;

import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskExecute;
import org.tiger.common.datastruct.TigerTaskFlow;
import org.tiger.common.datastruct.TigerTaskResourceUse;
import org.tiger.persistence.DataPersistence;
import org.tiger.persistence.file.FileDataPersistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认自定义bean
 *
 * @author 康绍飞
 * @date 2021/2/16 9:11
 */
public class DefaultCustomBean implements CustomBean {

    /**
     * 自定义bean
     *
     * @return map key:bean名称；value:bean对象
     */
    @Override
    public Map<String, Object> custom() {
        Map<String, Object> customBeanMap = new ConcurrentHashMap<>();
        DataPersistence<TigerTaskFlow> tigerTaskFlowDataPersistence = new FileDataPersistence<>();
        DataPersistence<TigerTask> tigerTaskDataPersistence = new FileDataPersistence<>();
        FileDataPersistence<TigerTaskResourceUse> systemMonitorFileDataPersistence = new FileDataPersistence<>();
        FileDataPersistence<TigerTaskExecute> tigerTaskExecuteDataPersistence = new FileDataPersistence<>();
        customBeanMap.put("tigerTaskFlowDataPersistence", tigerTaskFlowDataPersistence);
        customBeanMap.put("tigerTaskDataPersistence", tigerTaskDataPersistence);
        customBeanMap.put("systemMonitorFileDataPersistence", systemMonitorFileDataPersistence);
        customBeanMap.put("tigerTaskExecuteDataPersistence", tigerTaskExecuteDataPersistence);
        return customBeanMap;
    }
}
