package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TagServiceImplTest {

	@Resource
	private TagService tagServiceImpl;

	@Test
	public void findAll() {
		Integer pageNum = 1;
		Integer pageSize = 5;
		Page<TagEntity> tagEntityPage = tagServiceImpl.findAll(pageNum,pageSize,null);
		log.info("this tags find all test ==>" + tagEntityPage.getContent());
	}
}