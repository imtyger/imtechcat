package com.imtyger.imtygerbed.bean.blog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by 910888783@qq.com on 2019/11/22.
 */
@Getter
@Setter
@ToString
public class BlogNewRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String html;

    private List<String> tags;
}
