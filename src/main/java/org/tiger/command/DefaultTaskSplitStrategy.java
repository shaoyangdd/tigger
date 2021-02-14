package org.tiger.command;

import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.ioc.SingletonBean;

import java.util.Map;

@SingletonBean
public class DefaultTaskSplitStrategy extends AbstractTaskSplitStrategy {

    @Override
    public Map<String, TigerTask> splitTask(TigerTask tigerTask) {
        return null;
    }
}
