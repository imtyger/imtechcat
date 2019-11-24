package com.imtyger.imtygerbed.bean.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Created by 910888783@qq.com on 2019/11/14.
 */
@Getter
@Setter
public class LoginRequest{

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
