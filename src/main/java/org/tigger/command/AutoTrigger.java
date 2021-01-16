package org.tigger.command;

import org.tigger.common.MemoryShareDataRegion;
import org.tigger.db.dao.TigerTaskFlowDao;
import org.tigger.db.dao.entity.TigerTaskFlow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自动触发器
 * @author kangshaofei
 * @date 2020-01-16
 */
public class AutoTrigger {

    public static void run() {
       TimerTask timerTask =  new TimerTask() {
            @Override
            public void run() {
               List<TigerTaskFlow> tigerTaskFlowList = TigerTaskFlowDao.getTigerTaskFlow();

            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,20000);
    }
}
