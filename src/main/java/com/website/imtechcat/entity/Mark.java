package com.website.imtechcat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ClassName Mark
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/27 14:17
 * @Version 1.0
 **/
@Document(collection = "marks")
@Data
@AllArgsConstructor
public class Mark {
	@Id
	private String id;
	private String userId;
	private String userName;
	private String markName;
	private String markDesc;
	private String markLink;
	private long createTime;

}
