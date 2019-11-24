package com.imtyger.imtygerbed.bean.bookmark;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by 910888783@qq.com on 2019/11/24.
 */
@Getter
@Setter
public class BookmarkUpdateRequest {

    @NotNull
    private Integer id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String descr;

    @NotEmpty
    private String link;
}
