package com.website.imtechcat.service;

import com.website.imtechcat.entity.MarkEntity;

import java.util.List;

public interface MarkService {

	List<MarkEntity> findMarkEntitiesByUserId(String userId);

	List<MarkEntity> findMarkEntitiesByUserIdAndMarkName(String userId, String markName);

	MarkEntity newMarks(MarkEntity markEntity);
}
