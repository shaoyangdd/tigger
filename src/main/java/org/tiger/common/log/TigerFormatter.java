package org.tiger.common.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 继承Formatter来自定义自己的日志格式
 *
 * @author liu
 */
public class TigerFormatter extends Formatter {

    @Override
    public String format(LogRecord arg0) {
        // TODO Auto-generated method stub
        //创建StringBuilder对象来存放后续需要打印的日志内容
        StringBuilder builder = new StringBuilder();

        //获取时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date now = new Date();
        String dateStr = sdf.format(now);

        builder.append(dateStr);
        builder.append(" - ");

        //拼接日志级别
        builder.append(arg0.getLevel()).append(" - ");
        //拼接类名
        builder.append(arg0.getLoggerName()).append(" - ");
        //拼接方法名
        builder.append(arg0.getSourceMethodName()).append(" - ");
        //拼接日志内容
        builder.append(arg0.getMessage());
        //日志换行
        builder.append("\r\n");

        return builder.toString();
    }

    @Override
    public String getHead(Handler h) {
        // TODO Auto-generated method stub
        //return "此处为日志的头部信息\r\n";
        return "";
    }

    @Override
    public String getTail(Handler h) {
        // TODO Auto-generated method stub
        //return "此处为日志的尾部信息\r\n";
        return "";
    }

}