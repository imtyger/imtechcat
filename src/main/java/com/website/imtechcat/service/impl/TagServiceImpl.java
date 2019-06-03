package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.repository.TagRepository;
import com.website.imtechcat.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TagServiceImpl
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/27 17:45
 * @Version 1.0
 **/
@Service
public class TagServiceImpl implements TagService {

	@Resource
	private TagRepository tagRepository;

	@Override
	public List<TagEntity> findTagEntitiesByUserId(String userId) {
		return tagRepository.findTagEntitiesByUserId(userId);
	}

	@Override
	public int newTag(List<TagEntity> tagEntities) {
		return tagRepository.insert(tagEntities).size();
	}

}
