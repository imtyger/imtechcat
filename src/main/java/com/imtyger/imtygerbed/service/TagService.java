package com.imtyger.imtygerbed.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imtyger.imtygerbed.bean.tag.TagRequest;
import com.imtyger.imtygerbed.bean.tag.TagUpdateRequest;
import com.imtyger.imtygerbed.common.PageResult;
import com.imtyger.imtygerbed.entity.BlogEntity;
import com.imtyger.imtygerbed.entity.TagEntity;
import com.imtyger.imtygerbed.mapper.BlogMapper;
import com.imtyger.imtygerbed.mapper.TagMapper;
import com.imtyger.imtygerbed.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TagService {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private UserService userService;


    /**
     * 获取tag 云
     */
    public List<Tag> getTagCloud(){
        List<TagEntity> tagEntityList = tagMapper.selectList(null);
        List<Tag> list = new ArrayList<>();
        if(!tagEntityList.isEmpty()){
            list = getRecords(tagEntityList);
        }

        return list;
    }

    /**
     * 获取Home页分页标签
     */
    public Map<String,Object> queryHomeTags(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage iPage = tagMapper.selectPage(page, new QueryWrapper<>());

        return PageResult.getResult(pageNum, pageSize, iPage);
    }


    /**
     * 对查询结果二次封装返回首页的tag对象
     */
    private List<Tag> getRecords(List<TagEntity> list){
        List<Tag> tagList = new ArrayList<>();
        list.forEach(entity -> {
            Tag tag = new Tag();
            tag.setId(entity.getId());
            tag.setTitle(entity.getTitle());
            tag.setUsedCount(entity.getUsedCount());
            tagList.add(tag);
        });
        return tagList;
    }

    /**
     * 获取默认tag列表
     */
    public List<String> createTagNameList(){
        List<TagEntity> tagEntityList = tagMapper.selectList(null);

        return tagEntityList.stream().map(TagEntity::getTitle).collect(Collectors.toList());
    }

    /**
     * 构建new tag 并入库
     */
    public Integer newTag(TagRequest tagRequest, HttpServletRequest request){
        //检查user信息
        String userKey = userService.checkUserKey(request);
        //获取用户id
        Integer userId = userService.queryUserIdByName(userKey);
        //创建TagEntity
        TagEntity tagEntity = createNewTag(userId, tagRequest);

        return tagMapper.insert(tagEntity);
    }

    /**
     * 创建new tag entity
     */
    private TagEntity createNewTag(Integer userId, TagRequest tagRequest){
        TagEntity entity = new TagEntity();
        entity.setUserId(userId);
        entity.setTitle(tagRequest.getTitle());
        entity.setDescr(tagRequest.getDescr());
        return entity;
    }

    /**
     * 更新Tag
     */
    public Integer updateTag(TagUpdateRequest request){
        TagEntity entity = createUpdateTag(request);
        return tagMapper.updateById(entity);
    }

    private TagEntity createUpdateTag(TagUpdateRequest request){
        TagEntity entity = new TagEntity();
        entity.setId(request.getId());
        entity.setTitle(request.getTitle());
        entity.setDescr(request.getDescr());
        return entity;
    }

    public Integer deleteTagById(String id){
        return tagMapper.deleteById(id);
    }

    public void updateTagUsedCount(){
        //获取tag实体
        List<TagEntity> tagEntityList = tagMapper.selectList(null);
        int sum = 0;
        //循环list 取得每一个标签名 和 usedCount
        for(TagEntity entity : tagEntityList){
            Integer usedCount = entity.getUsedCount();
            String tagName = entity.getTitle();
            Integer queryCount = queryTagCountFromBlog(tagName);
            log.info("Tag title: {}, usedCount: {} , queryCount: {}", tagName, usedCount,
                    queryCount);
            if(!queryCount.equals(usedCount)){
                entity.setUsedCount(queryCount);
                int updateCount = tagMapper.updateById(entity);
                sum = sum + updateCount;
            }
            log.info("update tag used count : {}",sum);
        }

    }

    private Integer queryTagCountFromBlog(String tagName){
        QueryWrapper<BlogEntity> qw = new QueryWrapper<>();
        qw.like("tags",tagName);

        return blogMapper.selectCount(qw);
    }

    public TagEntity queryTagByName(String tagName){
        QueryWrapper<TagEntity> qw = new QueryWrapper<>();
        qw.eq("title", tagName);

        return tagMapper.selectOne(qw);
    }
}
