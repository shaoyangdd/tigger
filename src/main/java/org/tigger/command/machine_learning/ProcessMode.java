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
    DB_TO_DB,

    /**
     * 从数据库中读取数据，生成文件
     * （涉及到文件合并）
     */
    DB_TO_FILE,

    /**
     * 读文件写到数据库
     */
    FILE_TO_DB,

    /**
     * 读数据库，调用网络API
     * （批量转联机场景）
     */
    DB_TO_API,

    /**
     * 文件转文件
     * （如文件做系统间的适配，修数等场景）
     */
    FILE_TO_FILE,

    /**
     * 文件转API
     * （这种少见）
     */
    FILE_TO_API,

    /**
     * 混合模式，为DB、文件、网络的排列组合，有N种可能
     */
    MIX_MODE
}
