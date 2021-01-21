package org.tigger.common.datastruct;

/**
 * 数据库模式
 *
 * @author 康绍飞
 * @date 2020-01-21
 */
public enum DBMode {

    /**
     * 非数据库模式，使用一致性协议来管理状态，协作，使用文件来持久化数据
     */
    NO_DB,

    /**
     * 数据库模式，使用数据库来管理状态，持久化数据
     */
    DB

}
