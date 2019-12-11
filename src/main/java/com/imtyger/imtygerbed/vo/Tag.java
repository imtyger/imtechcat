package com.imtyger.imtygerbed.vo;

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
    private String id;

    @NotEmpty
    private String title;

    @NotEmpty
    private Integer usedCount;
}
