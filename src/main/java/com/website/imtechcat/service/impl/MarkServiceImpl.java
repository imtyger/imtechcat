package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.Mark;
import com.website.imtechcat.repository.MarkRepository;
import com.website.imtechcat.service.MarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
	public List<Mark> findMarksByUserId(String userId) {
		return markRepository.findMarksByUserId(userId);
	}

	@Override
	public List<Mark> findMarksByMarkName(String markName) {
		return markRepository.findMarksByMarkName(markName);
	}
}
