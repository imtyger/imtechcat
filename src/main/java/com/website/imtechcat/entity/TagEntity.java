package com.website.imtechcat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @ClassName TagEntity
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/3 14:51
 * @Version 1.0
 **/
@Document(collection = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagEntity {

	@Id
	private String id;
	private String userId;
	private String tagName;
	private String tagDesc;
	private int count;
	private boolean flag;
	private Date createdAt;
	private Date lastUpdatedAt;
}
