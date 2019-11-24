package com.imtyger.imtygerbed.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * Created by 910888783@qq.com on 2019/11/22.
 */
@Getter
@Setter
public class BlogHome {

    @NotEmpty
    private Integer id;

    @NotEmpty
    private String title;


    private List<String> tags;

    @NotEmpty
    private Date createdAt;

    @NotEmpty
    private Date updatedAt;

    @NotEmpty
    private Integer status;

    @NotEmpty
    private Integer deleted;
}
