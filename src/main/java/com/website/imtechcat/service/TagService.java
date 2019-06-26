package com.website.imtechcat.service;

import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TagService {

	Page<TagEntity> findTagEntitiesPageByUserId(String userId,Integer pageNum, Integer pageSize, Sort sort);

	boolean findTagEntityByIdAndUserIdExists(TagEntity tagEntity);

	Long tagsCount();

	Long tagsCountByUser(String userId);

	TagEntity newTags(TagEntity tagEntity);

	TagEntity updateTags(TagEntity tagEntity);

	Page<TagEntity> findAll(Integer pageNum, Integer pageSize, Sort sort);

	void deleteAllTags();

	void deleteTags(TagEntity tagEntity);

	Page<TagEntity> findTagEntityByTagName(String tagName,Integer pageNum, Integer pageSize, Sort sort);

	Page<TagEntity> findTagEntityByUserIdAndTagName(String userId, String tagName,Integer pageNum, Integer pageSize, Sort sort);

	Long tagsCountByTagName(String tagName);

	Long tagsCountByUserIdAndTagName(String userId, String tagName);

	List<TagEntity> findByTagNameLike(String tagName);
}
