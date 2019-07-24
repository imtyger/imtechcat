package com.website.imtechcat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @ClassName Bookmark
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/7/23 11:24
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

	private String id;
	private String markTitle;
	private String markDesc;
	private String markLink;
	private List<String> tags;
	private Date createdAt;
}
