package com.imtyger.imtygerbed.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by 910888783@qq.com on 2019/11/22.
 */
@Getter
@Setter
public class BlogEdit {

    @NotEmpty
    private Integer id;

    @NotEmpty
    private String title;

    @NotEmpty
    private List<String> tags;

    @NotEmpty
    private String content;
}
