package com.imtyger.imtygerbed.bean.blog;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author imtyger@gmail.com
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
