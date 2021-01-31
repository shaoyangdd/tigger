package org.tigger.command.machine_learning;

/**
 * 处理模式
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
public enum ProcessMode {

    /**
     * 从数据库中读取数据，处理后写入数据库
     * （最觉的处理模式）
     */
    DB_TO_DB("1"),

    /**
     * 从数据库中读取数据，生成文件
     * （涉及到文件合并）
     */
    DB_TO_FILE("2"),

    /**
     * 读文件写到数据库
     */
    FILE_TO_DB("3"),

    /**
     * 读数据库，调用网络API
     * （批量转联机场景）
     */
    DB_TO_API("4"),

    /**
     * 文件转文件
     * （如文件做系统间的适配，修数等场景）
     */
    FILE_TO_FILE("5"),

    /**
     * 文件转API
     * （这种少见）
     */
    FILE_TO_API("6"),

    /**
     * 混合模式，为数据库、文件、网络的排列组合，有N种可能
     */
    MIX_MODE("7");

    private String code;

    ProcessMode(String code) {
        this.code = code;
    }

    /**
     * 是不是从数据库读取
     *
     * @param code 处理模式
     * @return
     */
    public boolean readFromDB(String code) {
        ProcessMode processMode = null;
        for (ProcessMode value : ProcessMode.values()) {
            if (value.code.equals(code)) {
                processMode = value;
                break;
            }
        }
        if (processMode == null) {
            throw new RuntimeException("无效的处理模式:" + code);
        }
        return processMode == DB_TO_DB || processMode == DB_TO_FILE || processMode == DB_TO_API;
    }
}
