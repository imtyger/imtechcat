package com.website.imtechcat.service.impl;

import com.website.imtechcat.common.PageUtil;
import com.website.imtechcat.entity.MarkEntity;
import com.website.imtechcat.repository.MarkRepository;
import com.website.imtechcat.service.MarkService;
import com.website.imtechcat.util.CheckUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName MarkServiceImpl
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/27 17:11
 * @Version 1.0
 **/
@Service
public class MarkServiceImpl implements MarkService {

	@Resource
	private MarkRepository markRepository;


	@Override
	public MarkEntity newMarks(MarkEntity markEntity) {
		Date date = new Date();
		markEntity.setCreatedAt(date);
		markEntity.setLastUpdatedAt(date);
		return markRepository.insert(markEntity);
	}

	@Override
	public Long marksCount() {
		return markRepository.count();
	}

	@Override
	public Page<MarkEntity> findMarks(Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = PageUtil.newPage(pageNum,pageSize,sort);

		Page<MarkEntity> markEntities = markRepository.findAll(pageUtil);
		return markEntities;
	}

	@Override
	public boolean findMarkEntityById(MarkEntity markEntity) {
		String id = markEntity.getId();
		if(CheckUtil.isNull(id)){
			return false;
		}

		MarkEntity mark = markRepository.findMarkEntityById(id);
		if(mark == null){
			return false;
		}

		return true;
	}

	@Override
	public void deleteMark(MarkEntity markEntity) {
		markRepository.delete(markEntity);
	}

	@Override
	public MarkEntity updateMark(MarkEntity markEntity) {
		markEntity.setLastUpdatedAt(new Date());
		return markRepository.save(markEntity);
	}

	@Override
	public List<MarkEntity> findMarkEntitiesByMarkTitleLike(String markTitle) {
		return markRepository.findMarkEntitiesByMarkTitleLike(markTitle.trim());
	}

	@Override
	public boolean findMarkByMarkTitle(String markTitle) {
		MarkEntity markEntity = markRepository.findByMarkTitle(markTitle);
		if(markEntity != null){
			return true;
		}
		return false;
	}

	@Override
	public MarkEntity findById(String id) {
		return markRepository.findMarkEntityById(id);
	}

	@Override
	public List<MarkEntity> findMarkEntitiesByTagName(String tagName) {
		return markRepository.findMarkEntitiesByTagsContains(tagName);
	}


}
