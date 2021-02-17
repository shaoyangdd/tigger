-- 以下表做为演示使用

-- 待入账表
create database `tiger_demo`;
create table `tiger_demo`.pre_post_account
(
    id               bigint auto_increment comment '主键ID（任务ID）' primary key,
    card_no          char(16)    default ''                                            not null comment '卡号',
    amount           char(20)    default ''                                            not null comment '交易金额',
    status           char        default '1'                                           not null comment '状态 1：有效 0：无效',
    create_time      timestamp   default CURRENT_TIMESTAMP                             not null comment '创建时间',
    update_time      timestamp   default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '更新时间',
    last_update_user varchar(50) default 'SYSTEM'                                      not null comment '最后更新用户',
    version          int         default 0 comment '版本号'
) comment '待入账表';