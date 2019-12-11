package com.imtyger.imtygerbed.vo;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
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
@ToString
public class Blog {

	@NotEmpty
	private String id;

	@NotEmpty
	private String title;

	@NotEmpty
	private String profile;

	@NotEmpty
	private List<String> tags;

	@NotEmpty
	private Date createdAt;

}
