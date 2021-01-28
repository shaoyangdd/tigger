-- 作业表
DROP TABLE IF EXISTS `tiger_task`;
create table tiger_task
(
    id bigint auto_increment comment '主键ID（任务ID）' primary key,
    task_name varchar (200) default '' not null comment '作业名',
    task_layer_id bigint default 0 not null comment '作业层级ID',
    task_parameter text default '' not null comment '作业参数，自定义',
    status char default '1' not null comment '状态 1：有效 0：无效',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '更新时间',
    last_update_user varchar(50) default 'SYSTEM' not null comment '最后更新用户',
    version int default 0 comment '版本号'
) comment '作业表';

-- 作业展示节点 树型节点，方便作业多时归类，方便用页面查看
DROP TABLE IF EXISTS `tiger_task_show_node`;
create table tiger_task_show_node
(
    id bigint auto_increment comment '层级ID' primary key,
    task_layer_name varchar (200) default '' not null comment '作业层级名',
    task_label_parent_id bigint default 0 not null comment '作业归类，中竖线割的层级关系，越往前层级越高',
    status char default '1' not null comment '状态 1：有效 0：无效',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '更新时间',
    last_update_user varchar(50) default 'SYSTEM' not null comment '最后更新用户',
    version int default 0 comment '版本号'
) comment '作业展示节点';

-- 作业流表
DROP TABLE IF EXISTS `tiger_task_flow`;
create table tiger_task_flow
(
    id bigint auto_increment comment '主键ID（任务ID）' primary key,
    task_name varchar (200) default '' not null comment '作业名',
    previous_task_id varchar(1000) default '' not null comment '前置作业ID，字符串，逗号分割',
    previous_task_status varchar(1000) default '' not null comment '前置作业状态，字符串，逗号分割',
    next_task_id varchar(1000) default '' not null comment '后置作业ID，字符串，逗号分割',
    task_parameter text default '' not null comment '作业参数，自定义',
    status char(1) default '1' not null comment '状态 1：有效 0：无效',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '更新时间',
    last_update_user varchar(50) default 'SYSTEM' not null comment '最后更新用户',
    version int default 0 comment '版本号'
) comment '作业流表';

-- 作业执行表
DROP TABLE IF EXISTS `tiger_task_execute`;
create table tiger_task_execute
(
    id bigint auto_increment comment '主键ID（执行ID）' primary key,
    task_id bigint not null comment '作业ID',
    start_time timestamp default CURRENT_TIMESTAMP not null comment '开始时间',
    end_time timestamp comment '结束时间',
    task_status char(1) default '' not null comment '作业状态 R:运行中；F:失败；S:成功',
    task_executor_ip varchar(15) default '' not null comment '执行机IP',
    task_parameter text default '' not null comment '作业参数，自定义',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '更新时间',
    last_update_user varchar(50) default 'SYSTEM' not null comment '最后更新用户',
    version int default 0 comment '版本号'
) comment '作业执行表';

-- 作业执行资源消耗表
DROP TABLE IF EXISTS `tiger_task_resource_use`;
create table tiger_task_resource_use
(
    id               bigint auto_increment comment '主键ID（执行ID）' primary key,
    task_execute_id  bigint                                   not null comment '作业执行ID(对应作业执行表的ID)',
    cpu_use          decimal(18, 2) default '0.00'            not null comment 'cpu使用率，百分比',
    memory_use       decimal(18, 2) default '0.00'            not null comment '内存使用率，百分比',
    disk_io_use      decimal(18, 2) default '0.00'            not null comment '磁盘使用率，百分比',
    net_use          decimal(18, 2) default '0.00'            not null comment '带宽使用率，百分比',
    status           char           default '1'               not null comment '状态 1：有效 0：无效',
    create_time      timestamp      default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      timestamp      default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '更新时间',
    last_update_user varchar(50)    default 'SYSTEM'          not null comment '最后更新用户',
    version          int            default 0 comment '版本号'
) comment '作业执行资源消耗表';