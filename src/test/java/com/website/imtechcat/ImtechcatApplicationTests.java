package com.website.imtechcat;

import com.website.imtechcat.entity.MarkEntity;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.repository.MarkRepository;
import com.website.imtechcat.service.MarkService;
import com.website.imtechcat.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ImtechcatApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private TagService tagService;

	@Autowired
	private MarkService markService;



	// @Test
	public void newTags(){
		String[] tagsArr = {"oracle","mysql","redis","monggo","java","python","springboot"};
		for(int i = 0; i < tagsArr.length; i++){
			TagEntity entity = new TagEntity();
			entity.setTagName(tagsArr[i]);
			entity.setTagDesc(tagsArr[i]);
			entity.setUserId("5cf3e140eddb66199c41e6ba");
			Date date = new Date();
			entity.setCreatedAt(date);
			entity.setLastUpdatedAt(date);
			TagEntity tagEntity = tagService.newTags(entity);
			log.info("==============="+tagEntity.getId());
		}
	}


	// @Test
	public void updateMark(){
		MarkEntity markEntity = markService.findById("5d2b241336a7290900566c5c");
		List<String> tagList = new ArrayList();
		if(markEntity.getTags() != null){
			tagList = markEntity.getTags();
		}

		List<TagEntity> tagEntities = tagService.findByTagNameLike("mongodb");
		for(TagEntity tagEntity : tagEntities){
			tagList.add(tagEntity.getTagName());
		}

		markEntity.setTags(tagList);
		markService.updateMark(markEntity);

	}



	public static boolean isTagNameExist(MarkEntity markEntity, String tagName){
		if(markEntity.getTags().contains(tagName)){
			return true;
		}
		return false;
	}




}
