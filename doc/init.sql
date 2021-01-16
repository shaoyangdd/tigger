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

-- 作业展示节点 树型节点，方便作业多时归类，方便看
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
DROP TABLE IF EXISTS `tiger_task_flow`;
create table tiger_task_flow
(
    id bigint auto_increment comment '主键ID（执行ID）' primary key,
    task_id bigint not null comment '作业ID',
    previous_task_id varchar(1000) default '' not null comment '前置作业ID，字符串，逗号分割',
    previous_task_status varchar(1000) default '' not null comment '前置作业状态，字符串，逗号分割',
    next_task_id varchar(1000) default '' not null comment '后置作业ID，字符串，逗号分割',
    task_parameter text default '' not null comment '作业参数，自定义',
    status char default '1' not null comment '状态 1：有效 0：无效',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '更新时间',
    last_update_user varchar(50) default 'SYSTEM' not null comment '最后更新用户',
    version int default 0 comment '版本号'
) comment '作业状态表';