package com.imtyger.imtygerbed.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @ClassName BlogEntity
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/7/8 10:43
 * @Version 1.0
 **/
@TableName(value = "blogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogEntity {

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@TableField(value = "userId")
	@NotEmpty
	private Integer userId;

	@TableField(value = "title")
	@NotEmpty
	private String title;

	@TableField(value = "profile")
	@NotEmpty
	private String profile;

	@TableField(value = "content")
	@NotEmpty
	private String content;

	@TableField(value = "html")
	@NotEmpty
	private String html;

	@TableField(value = "tags", updateStrategy = FieldStrategy.IGNORED)
	private String tags;

	@TableField(value = "status")
	@NotEmpty
	private Integer status;

	@TableField(value = "deleted")
	@TableLogic
	@NotEmpty
	private Integer deleted;

	@TableField(value = "views")
	@NotEmpty
	private Integer views;

	@TableField(value = "createdAt")
	@NotEmpty
	private Date createdAt;

	@TableField(value = "updatedAt")
	@NotEmpty
	private Date updatedAt;


}
