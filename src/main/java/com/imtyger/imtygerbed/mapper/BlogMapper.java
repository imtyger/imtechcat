package com.imtyger.imtygerbed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imtyger.imtygerbed.entity.BlogEntity;

import java.util.List;

/**
 * Created by 910888783@qq.com on 2019/11/14.
 */
public interface BlogMapper extends BaseMapper<BlogEntity> {

    List<BlogEntity> queryByStatusAndFlagAndContentLike(String query);



}
