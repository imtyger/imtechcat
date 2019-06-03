package com.website.imtechcat.service;

import com.website.imtechcat.entity.TagEntity;

import java.util.List;

public interface TagService {

	List<TagEntity>  findTagEntitiesByUserId(String userId);

	int newTag(List<TagEntity> tagEntities);

}
