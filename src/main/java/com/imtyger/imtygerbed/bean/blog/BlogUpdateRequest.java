package com.imtyger.imtygerbed.bean.blog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by 910888783@qq.com on 2019/11/22.
 */
@Getter
@Setter
@ToString
public class BlogUpdateRequest {

    @NotNull
    private Integer id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String html;

    private List<String> tags;
}
