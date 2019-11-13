package com.imtyger.imtygerbed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @ClassName BlogEntity
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/7/8 10:43
 * @Version 1.0
 **/
@Document(collection = "blog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogEntity {

	@Id
	private String id;
	private String author;
	private String blogTitle;
	private String blogProfile;
	private String blogContent;
	private String blogHtml;
	private List<String> tags;
	private boolean flag;
	private boolean status;
	private long visitCount;
	private Date createdAt;
	private Date lastUpdatedAt;


}
