package com.website.imtechcat.service;

import com.website.imtechcat.entity.MarkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MarkService {


	MarkEntity newMarks(MarkEntity markEntity);

	Long marksCount();

	Page<MarkEntity> findMarks(Integer pageNum, Integer pageSize, Sort sort);

	boolean findMarkEntityById(MarkEntity markEntity);

	void deleteMark(MarkEntity markEntity);

	MarkEntity updateMark(MarkEntity markEntity);

	List<MarkEntity> findMarkEntitiesByMarkTitleLike(String markTitle);

	boolean findMarkByMarkTitle(String markTitle);

	MarkEntity findById(String id);

	List<MarkEntity> findMarkEntitiesByTagName(String tagName);


}
