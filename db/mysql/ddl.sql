## USERS 用户信息表
## DROP TABLE users;
create table if not exists users (
id bigint not null primary key auto_increment comment '用户ID',
username varchar(20) not null comment '用户名',
password varchar(255) not null comment '密码',
nickName varchar(20) not null comment '昵称',
avatar varchar(100) not null comment '头像URL',
createdAt datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
updatedAt datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
UNIQUE key ix_users_username(username)
)ENGINE=InnoDB default CHARSET=utf8mb4 comment='用户信息表';

## login  用户登录信息表
## DROP TABLE login_info;
create table if not exists login_info (
id bigint not null primary key auto_increment comment '编号',
userId bigint not null comment '用户ID',
userAgent varchar (255) not null default '' comment '登录客户端',
lastLoginAt datetime not null DEFAULT CURRENT_TIMESTAMP comment '最后登录时间',
lastLoginIp varchar(15) not null default '' comment '最后登录IP',
key ix_login_info_userid(userId)
)ENGINE=InnoDB default CHARSET=utf8mb4 comment='用户登录信息表';


## MARKS  收藏信息表
## DROP TABLE bookmarks;
create table if not exists bookmarks (
id bigint not null primary key auto_increment comment '编号',
userId bigint not null comment '用户ID',
title varchar (50) not null comment '标题',
descr varchar (50) not null comment '描述',
link varchar (255) not null comment '链接',
createdAt datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
updatedAt datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
key ix_bookmarks_userid(userId),
key ix_bookmarks_title(title)
)ENGINE=InnoDB default CHARSET=utf8mb4 comment='收藏信息表';

## tags  标签信息表
## DROP TABLE tags;
create table if not exists tags (
id bigint not null primary key auto_increment comment '编号',
userId bigint not null comment '用户ID',
title varchar (50) not null comment '名称',
descr varchar (50) not null comment '描述',
usedCount INT (11) not null default 0 comment '打标签次数',
createdAt datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
updatedAt datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
key ix_tags_userid(userId),
UNIQUE key ix_tags_title(title)
)ENGINE=InnoDB default CHARSET=utf8mb4 comment='标签信息表';

## BLOGS  博客信息表
## DROP TABLE blogs;
create table if not exists blogs (
id bigint not null primary key auto_increment comment '编号',
userId bigint not null comment '用户ID',
title varchar (100) not null comment '标题',
profile varchar (255) not null comment '简介',
content text not null comment '内容',
html text not null comment 'HTML',
tags varchar (255) default '' comment '标签',
views INT (11) not null default 0 comment '浏览量',
status INT (1) not null default 0 comment '前端展示状态：0 展示, 1 不展示',
deleted TINYINT(1) not null default 0 comment '已删除：0 未删除, 1 已删除',
createdAt datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
updatedAt datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
key ix_blogs_userid(userId),
key ix_blogs_title(title)
)ENGINE=InnoDB default CHARSET=utf8mb4 comment='博客信息表';

