package com.website.imtechcat;

import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImtechcatApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private TagService tagService;


	@Test
	public void newTags(){
		String[] tagsArr = {"oracle","mysql","redis","monggo","java","python","springboot"};
		for(int i = 0; i < tagsArr.length; i++){
			TagEntity entity = new TagEntity();
			entity.setTagName(tagsArr[i]);
			entity.setTagDesc(tagsArr[i]);
			entity.setUserId("5cf3e140eddb66199c41e6ba");
			entity.setCreatedAt(System.currentTimeMillis());
			entity.setLastUpdatedAt(System.currentTimeMillis());
			TagEntity tagEntity = tagService.newTags(entity);
			log.info("==============="+tagEntity.getId());
		}
	}



}
