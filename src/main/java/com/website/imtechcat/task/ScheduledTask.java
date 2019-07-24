package com.website.imtechcat.task;

import com.website.imtechcat.common.Result;
import com.website.imtechcat.controller.TagController;
import com.website.imtechcat.service.TagService;
import jdk.internal.org.objectweb.asm.Handle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScheduledTask
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/7/19 18:02
 * @Version 1.0
 **/
@Slf4j
@Component
public class ScheduledTask {

	@Resource
	private TagService tagServiceImpl;

	// @Async
	// @Scheduled(cron="0 0 1 * * ?")(fixedRate = 2000)
	@Scheduled(cron="0 0 1 * * ?")
	public void tagCloudTask() {
		log.info("this tagCloudTask==> start time:" + new Date());

		try {
			List<Map> list = tagServiceImpl.getTagCloudList();
			log.info("this get tag cloud list : " + list.toString());
		}catch (Exception ex){
			log.error("get tag cloud task catch exception", ex);
		}
	}

}
