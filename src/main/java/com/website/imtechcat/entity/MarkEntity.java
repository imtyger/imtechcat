package com.website.imtechcat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MarkEntity
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/3 15:39
 * @Version 1.0
 **/
@Document(collection = "marks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkEntity {

	@Id
	private String id;
	private String userId;
	private String markName;
	private String markDesc;
	private String markLink;
	private List<Map> tags;
	private long createdAt;
	private long lastUpdatedAt;
}
