package com.imtyger.imtygerbed.bean.bookmark;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * Created by 910888783@qq.com on 2019/11/24.
 */
@Getter
@Setter
@ToString
public class BookmarkRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String descr;

    @NotEmpty
    private String link;
}
