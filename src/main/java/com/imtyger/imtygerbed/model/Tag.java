package com.imtyger.imtygerbed.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * Created by 910888783@qq.com on 2019/11/21.
 */
@Data
@ToString
public class Tag {

    @NotEmpty
    private Integer id;

    @NotEmpty
    private String title;

    @NotEmpty
    private Integer usedCount;
}
