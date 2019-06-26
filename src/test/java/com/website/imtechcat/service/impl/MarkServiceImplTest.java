package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.MarkEntity;
import com.website.imtechcat.service.MarkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkServiceImplTest {

	@Resource
	private MarkService markServiceImpl;

	@Test
	public void newMarks() {
		String[] markArr = {"springboot","数据库"};
		for(int i = 0; i < markArr.length; i ++){
			MarkEntity markEntity = new MarkEntity();
			markEntity.setUserId("5cf3e140eddb66199c41e6ba");
			markEntity.setMarkName(markArr[i]);
			if(markArr[i].equals("springboot")){
				List<Map> list = new ArrayList<>();
				Map sbmap = new HashMap<>();
				sbmap.put("tagName","springboot");
				Map javamap = new HashMap<>();
				javamap.put("tagName","java");
				list.add(sbmap);
				list.add(javamap);
				markEntity.setTags(list);
			}
			if(markArr[i].equals("数据库")){
				List<Map> list = new ArrayList<>();
				Map ormap = new HashMap<>();
				ormap.put("tagName","oracle");
				Map msmap = new HashMap<>();
				msmap.put("tagName","mysql");
				list.add(ormap);
				list.add(msmap);
				markEntity.setTags(list);
			}
			MarkEntity entity = markServiceImpl.newMarks(markEntity);
			log.info(entity.toString());
		}

	}
}