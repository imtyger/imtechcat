package com.imtyger.imtygerbed.service.impl;

import com.imtyger.imtygerbed.repository.BlogRepository;
import com.imtyger.imtygerbed.repository.MarkRepository;
import com.imtyger.imtygerbed.repository.TagRepository;
import com.imtyger.imtygerbed.common.PageUtil;
import com.imtyger.imtygerbed.entity.TagEntity;
import com.imtyger.imtygerbed.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


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

	@Resource
	private MarkRepository markRepository;

	@Resource
	private BlogRepository blogRepository;


	@Override
	public Long tagsCount() {
		return tagRepository.count();
	}


	@Override
	public TagEntity newTags(TagEntity tagEntity) {
		tagEntity.setCreatedAt(new Date());
		tagEntity.setLastUpdatedAt(new Date());
		return tagRepository.insert(tagEntity);
	}

	@Override
	public TagEntity updateTags(TagEntity tagEntity) {
		tagEntity.setLastUpdatedAt(new Date());
		return tagRepository.save(tagEntity);
	}

	@Override
	public Page<TagEntity> findAll(Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = PageUtil.newPage(pageNum,pageSize,sort);

		Page<TagEntity> tagEntityPage = tagRepository.findAll(pageUtil);
		return tagEntityPage;
	}

	@Override
	public void deleteTags(TagEntity tagEntity) {
		tagRepository.delete(tagEntity);
	}

	@Override
	public Page<TagEntity> findTagEntityByTagName(String tagName, Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = PageUtil.newPage(pageNum,pageSize,sort);

		Page<TagEntity> tagEntityPage = tagRepository.findTagEntityByTagName(tagName,pageUtil);
		return tagEntityPage;
	}

	@Override
	public Page<TagEntity> findTagEntityByTagNameLike(String tagName, Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = PageUtil.newPage(pageNum,pageSize,sort);

		Page<TagEntity> tagEntityPage = tagRepository.findTagEntityByTagNameLike(tagName,pageUtil);
		return tagEntityPage;
	}


	@Override
	public Long countTagEntitiesByTagName(String tagName) {
		return tagRepository.countTagEntitiesByTagName(tagName);
	}

	@Override
	public Long countTagEntitiesByTagNameLike(String tagName) {
		return tagRepository.countTagEntitiesByTagNameLike(tagName);
	}


	@Override
	public List<TagEntity> findByTagNameLike(String tagName) {
		return tagRepository.findByTagNameLike(tagName);
	}

	@Override
	public boolean findTagEntityByTagName(String tagName) {
		TagEntity tagEntity = tagRepository.findTagEntityByTagName(tagName);
		if(tagEntity != null){
			return true;
		}
		return false;
	}

	@Override
	public boolean findById(TagEntity tagEntity) {
		TagEntity entity = tagRepository.findTagEntityById(tagEntity.getId());
		if(entity != null){
			return true;
		}
		return false;
	}


	@Override
	public List<String> findAllTagNameList() {
		List<TagEntity> tagEntities = tagRepository.findAll();
		if(tagEntities != null && tagEntities.size() != 0){
			List<String> tags = tagEntities.stream().map(tagEntity -> {
				return tagEntity.getTagName();
			}).collect(Collectors.toList());
			return tags;
		}
		return null;
	}

	@Override
	public List<String> findAllTagIdList() {
		List<TagEntity> tagEntities = tagRepository.findAll();
		if(tagEntities != null && tagEntities.size() != 0){
			List<String> tags = tagEntities.stream().map(tagEntity -> {
				return tagEntity.getId();
			}).collect(Collectors.toList());
			return tags;
		}
		return null;
	}

	@Override
	public List<Map> getTagCloudList() {

		List<String> tagNameList = this.findAllTagNameList();
		if(tagNameList == null){
			return null;
		}

		List<TagEntity> tagEntityList = new ArrayList<>();
		List<Map> tagCloudList = new ArrayList<>();

		try {
			for(String tagName : tagNameList){
				int inMark = markRepository.countByTagsContains(tagName);
				int inBlog = blogRepository.countByTagsContainsAndStatusIsTrue(tagName);
				int count = inMark + inBlog;

				TagEntity tagEntity = tagRepository.findTagEntityByTagName(tagName);
				tagEntity.setCount(count);
				tagEntity.setLastUpdatedAt(new Date());
				tagEntityList.add(tagEntity);

				Map map = new HashMap();
				map.put("tagName",tagName);
				map.put("count",count);
				tagCloudList.add(map);
			}

			tagRepository.saveAll(tagEntityList);

		}catch (Exception ex){
			ex.printStackTrace();
		}

		return tagCloudList;
	}


	public void updateTagCount(List<String> tags, int actionSign){
		if(tags == null || tags.size() == 0){
			return;
		}

		try {
			List<TagEntity> tagEntities = new ArrayList<>();
			for(String tag: tags){
				TagEntity tagEntity = tagRepository.findTagEntityByTagName(tag);
				int count = tagEntity.getCount();
				if(actionSign == 1){
					tagEntity.setCount(count + 1);
				}else if(actionSign == -1){
					tagEntity.setCount(count - 1);
				}

				tagEntity.setLastUpdatedAt(new Date());
				tagEntities.add(tagEntity);
			}
			tagRepository.saveAll(tagEntities);

		}catch (Exception ex){
			log.error("update tag count catch exception", ex);
		}
	}

}
