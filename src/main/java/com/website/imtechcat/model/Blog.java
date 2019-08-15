package com.website.imtechcat.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @ClassName Blog
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/8/5 10:39
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

	private String id;
	private String blogTitle;
	private String blogProfile;
	private List<String> tags;
	private Date createdAt;
}
