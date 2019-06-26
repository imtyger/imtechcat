package com.website.imtechcat.service.impl;

import com.website.imtechcat.common.PageUtil;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.repository.TagRepository;
import com.website.imtechcat.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
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
@Slf4j
@Service
public class TagServiceImpl implements TagService {

	@Resource
	private TagRepository tagRepository;

	@Override
	public Page<TagEntity> findTagEntitiesPageByUserId(String userId,Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = newPage(pageNum,pageSize,sort);

		Page<TagEntity> tagEntitiesByUserId = tagRepository.findTagEntitiesByUserId(userId,pageUtil);
		return tagEntitiesByUserId;
	}

	@Override
	public boolean findTagEntityByIdAndUserIdExists(TagEntity tagEntity) {
		return tagRepository.findTagEntityByIdAndUserIdExists(tagEntity.getId(),tagEntity.getUserId());
	}

	@Override
	public Long tagsCount() {
		return tagRepository.count();
	}

	@Override
	public Long tagsCountByUser(String userId) {
		TagEntity tagEntity = new TagEntity();
		tagEntity.setUserId(userId);
		Example<TagEntity> example = Example.of(tagEntity);
		return tagRepository.count(example);
	}

	@Override
	public TagEntity newTags(TagEntity tagEntity) {
		return tagRepository.insert(tagEntity);
	}

	@Override
	public TagEntity updateTags(TagEntity tagEntity) {
		return tagRepository.save(tagEntity);
	}

	@Override
	public Page<TagEntity> findAll(Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = newPage(pageNum,pageSize,sort);

		Page<TagEntity> tagEntityPage = tagRepository.findAll(pageUtil);
		return tagEntityPage;
	}

	@Override
	public void deleteAllTags() {
		tagRepository.deleteAll();
	}

	@Override
	public void deleteTags(TagEntity tagEntity) {
		tagRepository.delete(tagEntity);
	}

	@Override
	public Page<TagEntity> findTagEntityByTagName(String tagName, Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = newPage(pageNum,pageSize,sort);

		Page<TagEntity> tagEntityPage = tagRepository.findTagEntityByTagName(tagName,pageUtil);
		return tagEntityPage;
	}

	@Override
	public Page<TagEntity> findTagEntityByUserIdAndTagName(String userId, String tagName, Integer pageNum, Integer pageSize,
														   Sort sort) {
		PageUtil pageUtil = newPage(pageNum,pageSize,sort);

		Page<TagEntity> tagEntityPage = tagRepository.findTagEntityByUserIdAndTagName(userId,tagName,pageUtil);
		return tagEntityPage;
	}

	@Override
	public Long tagsCountByTagName(String tagName) {
		TagEntity tagEntity = new TagEntity();
		tagEntity.setTagName(tagName);
		Example<TagEntity> example = Example.of(tagEntity);
		return tagRepository.count(example);
	}

	@Override
	public Long tagsCountByUserIdAndTagName(String userId, String tagName) {
		TagEntity tagEntity = new TagEntity();
		tagEntity.setUserId(userId);
		tagEntity.setTagName(tagName);
		Example<TagEntity> example = Example.of(tagEntity);
		return tagRepository.count(example);
	}

	@Override
	public List<TagEntity> findByTagNameLike(String tagName) {
		return tagRepository.findByTagNameLike(tagName);
	}


	private static PageUtil newPage(Integer pageNum, Integer pageSize,Sort sort){
		PageUtil pageUtil = new PageUtil();
		pageUtil.setPageNum(pageNum);
		pageUtil.setPageSize(pageSize);
		if(sort == null){
			sort = new Sort(Sort.Direction.DESC,"lastUpdatedAt");
		}
		pageUtil.setSort(sort);
		return pageUtil;
	}

}
