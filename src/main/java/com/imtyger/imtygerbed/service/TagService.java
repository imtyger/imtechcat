package com.imtyger.imtygerbed.service;

import com.imtyger.imtygerbed.entity.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface TagService {


	Long tagsCount();

	TagEntity newTags(TagEntity tagEntity);

	TagEntity updateTags(TagEntity tagEntity);

	Page<TagEntity> findAll(Integer pageNum, Integer pageSize, Sort sort);

	void deleteTags(TagEntity tagEntity);

	Page<TagEntity> findTagEntityByTagName(String tagName,Integer pageNum, Integer pageSize, Sort sort);

	Page<TagEntity> findTagEntityByTagNameLike(String tagName,Integer pageNum, Integer pageSize, Sort sort);

	Long countTagEntitiesByTagName(String tagName);

	Long countTagEntitiesByTagNameLike(String tagName);

	List<TagEntity> findByTagNameLike(String tagName);

	boolean findTagEntityByTagName(String tagName);

	boolean findById(TagEntity tagEntity);

	void updateTagCount(List<String> tags, int actionSign);

	List<String> findAllTagNameList();

	List<String> findAllTagIdList();

	List<Map> getTagCloudList();
}
