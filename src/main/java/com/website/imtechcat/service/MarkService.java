package com.website.imtechcat.service;

import com.website.imtechcat.entity.Mark;

import java.util.List;

public interface MarkService {

	List<Mark> findMarksByUserId(String userId);

	List<Mark> findMarksByMarkName(String markName);
}
