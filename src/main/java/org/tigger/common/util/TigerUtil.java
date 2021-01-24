package org.tigger.common.util;

import org.tigger.persistence.database.dao.entity.TigerTask;

import java.util.HashMap;
import java.util.Map;

public class TigerUtil {

    public static final String TIGER_TASK_PARAM_MAP_KEY = "tigerTaskKey";

    public static Map<String, TigerTask> buildTigerTaskParamMap(TigerTask tigerTask) {
        Map<String, TigerTask> tigerTaskMap = new HashMap<>();
        tigerTaskMap.put(TIGER_TASK_PARAM_MAP_KEY, tigerTask);
        return tigerTaskMap;
    }

    public static TigerTask getTigerTaskFromParamMap(Map<String, TigerTask> paramMap) {
        return paramMap.get(TIGER_TASK_PARAM_MAP_KEY);
    }
}
