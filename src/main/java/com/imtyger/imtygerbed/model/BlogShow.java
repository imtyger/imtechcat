package com.imtyger.imtygerbed.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * Created by 910888783@qq.com on 2019/11/21.
 */
@Data
@ToString
public class BlogShow {

    @NotEmpty
    private Integer id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String html;

    @NotEmpty
    private List<String> tags;

    @NotEmpty
    private Date createdAt;
}
