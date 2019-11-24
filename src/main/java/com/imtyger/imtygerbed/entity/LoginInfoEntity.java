package com.imtyger.imtygerbed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by 910888783@qq.com on 2019/11/17.
 */
@TableName(value = "login_info")
@Data
@ToString
public class LoginInfoEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "userId")
    private Integer userId;

    @TableField(value = "userAgent")
    private String userAgent;

    @TableField(value = "lastLoginAt")
    private Date lastLoginAt;

    @TableField(value = "lastLoginIp")
    private String lastLoginIp;
}
