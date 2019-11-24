package com.imtyger.imtygerbed.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by 910888783@qq.com on 2019/11/16.
 */
@Data
@ToString
public class User {
    @NotNull
    private Integer id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String nickName;
    @NotEmpty
    private String avatar;
}
