package com.imtyger.imtygerbed.bean.blog;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Created by 910888783@qq.com on 2019/11/21.
 */
@Setter
@Getter
public class BlogRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String html;

    private String tags;

}
