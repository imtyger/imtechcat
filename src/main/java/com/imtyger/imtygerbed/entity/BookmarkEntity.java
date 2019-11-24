package com.imtyger.imtygerbed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @ClassName BookmarkEntity
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/3 15:39
 * @Version 1.0
 **/
@TableName(value = "bookmarks")
@Getter
@Setter
@ToString
public class BookmarkEntity {

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@TableField(value = "userId")
	@NotEmpty
	private Integer userId;

	@TableField(value = "title")
	@NotEmpty
	private String title;

	@TableField(value = "descr")
	@NotEmpty
	private String descr;

	@TableField(value = "link")
	@NotEmpty
	private String link;

	@TableField(value = "createdAt")
	@NotEmpty
	private Date createdAt;

	@TableField(value = "updatedAt")
	@NotEmpty
	private Date updatedAt;


}
