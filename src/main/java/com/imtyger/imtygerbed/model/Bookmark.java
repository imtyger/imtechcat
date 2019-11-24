package com.imtyger.imtygerbed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @ClassName Bookmark
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/8/5 10:56
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

	@NotEmpty
	private Integer id;

	@NotEmpty
	private String title;

	@NotEmpty
	private String link;

}
