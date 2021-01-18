package org.tigger.command;

import org.tigger.db.dao.entity.TigerTask;

import java.util.Map;

public class TestTaskSplitStrategy extends AbstractTaskSplitStrategy {

    @Override
    public Map<String, TigerTask> splitTask(TigerTask tigerTask) {
        return null;
    }
}
