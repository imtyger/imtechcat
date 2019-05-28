package com.website.imtechcat.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ClassName Tag
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/27 13:49
 * @Version 1.0
 **/
@Document(collection = "tags")
@Data
@AllArgsConstructor
public class Tag {
	@Id
	private String id;
	private String userId;
	private String userName;
	private String tagName;
	private long createTime;

}
