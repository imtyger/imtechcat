package com.imtyger.imtygerbed.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @ClassName TagEntity
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/3 14:51
 * @Version 1.0
 **/
@TableName(value = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagEntity {

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

	@TableField(value = "usedCount")
	@NotEmpty
	private Integer usedCount;

	@TableField(value = "createdAt")
	@NotEmpty
	private Date createdAt;

	@TableField(value = "updatedAt")
	@NotEmpty
	private Date updatedAt;

}
