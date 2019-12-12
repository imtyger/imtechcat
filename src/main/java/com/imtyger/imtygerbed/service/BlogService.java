package com.imtyger.imtygerbed.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imtyger.imtygerbed.Constant;
import com.imtyger.imtygerbed.bean.blog.BlogNewRequest;
import com.imtyger.imtygerbed.bean.blog.BlogUpdateRequest;
import com.imtyger.imtygerbed.common.PageResult;
import com.imtyger.imtygerbed.entity.BlogEntity;
import com.imtyger.imtygerbed.entity.TagEntity;
import com.imtyger.imtygerbed.mapper.BlogMapper;
import com.imtyger.imtygerbed.mapper.TagMapper;
import com.imtyger.imtygerbed.utils.CheckUtil;
import com.imtyger.imtygerbed.utils.HashidsUtil;
import com.imtyger.imtygerbed.utils.RedisUtil;
import com.imtyger.imtygerbed.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BlogService {

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private UserService userService;

    @Resource
    private TagService tagService;

    @Resource
    private Constant constant;

    @Resource
    private RedisUtil redisUtil;

    private final static String[] IGNORE_ID = new String[]{"id"};
    private final static String[] IGNORE_TAGS= new String[]{"tags"};
    private final static String[] IGNORE_ID_TAGS = new String[]{"id", "tags"};
    private final static String[] IGNORE_PROFILE_TAGS = new String[]{"profile",
            "tags"};
    private final static String[] IGNORE_USERID_PROFILE_TAGS = new String[]{"userId", "profile",
            "tags"};


    /**
     * 获取展示博客总数
     */
    public int queryBlogTotal(){
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);

        return blogMapper.selectCount(queryWrapper);
    }

    /**
     * 根据状态：0（展示）为条件获取分页
     */
    private IPage<BlogEntity> queryBlogIPage(Page page){
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);
        queryWrapper.orderByDesc("createdAt");

        return (IPage<BlogEntity>) blogMapper.selectPage(page, queryWrapper);
    }

    /**
     * 获取首页分页博客
     */
    public Map<String,Object> queryBlogs(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage<BlogEntity> iPage = queryBlogIPage(page);

        Map result = PageResult.getResult(pageNum, pageSize, iPage);
        List<BlogEntity> list = (List<BlogEntity>) result.get(constant.getList());
        if(!list.isEmpty()){
            result.put(constant.getList(),parseRecords(list));
        }
        return result;
    }

    /**
     * 获取Home页分页博客
     */
    public Map<String,Object> queryHomeBlogs(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage iPage = blogMapper.selectPage(page,new QueryWrapper<>());

        Map result = PageResult.getResult(pageNum, pageSize, iPage);
        List<BlogEntity> list = (List<BlogEntity>) result.get(constant.getList());
        if(!list.isEmpty()){
            result.put(constant.getList(),parseHomeRecords(list));
        }
        return result;
    }

    /**
     * 获取search模糊查询
     */
    public List<Blog> searchLike(String query){
        QueryWrapper<BlogEntity> qw = new QueryWrapper<>();
        qw.eq("status",0);
        qw.and(wrapper -> wrapper.like("title", query).or().like("content", query));
        qw.orderByDesc("createdAt");

        List<BlogEntity> blogEntityList = blogMapper.selectList(qw);
        List<Blog> blogList = new ArrayList<>();
        if(!blogEntityList.isEmpty()){
            blogList = parseRecords(blogEntityList);
        }
        return blogList;
    }

    /**
     * 获取关于
     */
    public BlogShow getAboutBlog(){
        //先从redis缓存中取 取到了返回 取不到去数据库中查询
        String about = redisUtil.get("about");
        if(!StringUtils.isEmpty(about)){
            return JSONObject.parseObject(about, BlogShow.class);
        }

        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", "关于本博客");

        BlogEntity blogEntity = blogMapper.selectOne(queryWrapper);
        BlogShow blogShow = createBlogShow(blogEntity);
        //对象存进redis缓存
        redisUtil.save("about", blogShow);

        return blogShow;
    }

    /**
     * 通过id获取博客详情并增加浏览数
     */
    public BlogShow postBlogId(String id){
        id = String.valueOf(HashidsUtil.decode(id));

        //先从redis缓存中取 取到了返回 取不到去数据库中查询
        String blog = redisUtil.get("blog:id:" + id);
        if(!StringUtils.isEmpty(blog)){
            return JSONObject.parseObject(blog, BlogShow.class);
        }

        BlogEntity blogEntity = blogMapper.selectById(id);
        updateViews(blogEntity);

        BlogShow blogShow = createBlogShow(blogEntity);

        //对象存进redis缓存
        redisUtil.save("blog:id:" + id, blogShow);

        return blogShow;
    }

    /**
     * 删除blog by id
     */
    public Integer deleteBlogById(String id){

        //删除redis缓存
        redisUtil.delete("blog:id:" + id);
        return blogMapper.deleteById(id);
    }

    /**
     * ID获取blog 编辑页
     */
    public BlogEdit getBlogById(String id){
        BlogEntity blogEntity = blogMapper.selectById(id);

        BlogEdit edit = new BlogEdit();
        BeanUtils.copyProperties(blogEntity, edit, IGNORE_TAGS);
        edit.setTags(creatTagList(blogEntity.getTags()));
        return edit;
    }

    /**
     * 更新blog
     */
    public Integer updateBlog(BlogUpdateRequest blogUpdateRequest){
        BlogEntity blogEntity = createUpdateBlogEntity(blogUpdateRequest);

        //删除redis缓存
        redisUtil.delete("blog:id:"+ blogUpdateRequest.getId());
        return updateBlogById(blogUpdateRequest.getId(),blogEntity);
    }

    /**
     * 更新博客展示状态
     */
    public Integer updateBlogStatus(Integer id, Integer status){
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setStatus(status);

        //删除redis缓存
        redisUtil.delete("blog:id:" + id);
        return updateBlogById(id, blogEntity);
    }

    /**
     * 通过id 更新博客
     */
    private Integer updateBlogById(Integer id, BlogEntity blogEntity){
        UpdateWrapper<BlogEntity> uw = new UpdateWrapper<>();
        uw.eq("id",id);
        //删除redis缓存
        redisUtil.delete("blog:id:" + id);
        return blogMapper.update(blogEntity, uw);
    }


    /**
     * tag获取blog list
     */
    public List<BlogList> getBlogListByTag(String tag){
        QueryWrapper<BlogEntity> qw = new QueryWrapper<>();
        qw.like("tags",tag);

        List<BlogEntity> list = blogMapper.selectList(qw);
        return createBlogList(list);
    }

    private List<BlogList> createBlogList(@Nullable List<BlogEntity> list){
        List<BlogList> blogLists = new ArrayList<>();
        if(!list.isEmpty()) list.forEach(blogEntity -> {
            BlogList blog = new BlogList();
            BeanUtils.copyProperties(blogEntity, blog, IGNORE_ID);
            blog.setId(HashidsUtil.encode(blogEntity.getId()));
            blogLists.add(blog);
        });
        return blogLists;
    }

    private BlogEntity createUpdateBlogEntity(@NotNull BlogUpdateRequest blogUpdateRequest){
        BlogEntity blogEntity = new BlogEntity();
        BeanUtils.copyProperties(blogUpdateRequest, blogEntity, IGNORE_PROFILE_TAGS);

        String htmlBody = CheckUtil.getHtmlBody(blogUpdateRequest.getHtml());
        blogEntity.setProfile(createProfile(htmlBody));

        List<String> tagList = blogUpdateRequest.getTags();
        blogEntity.setTags(createTagStr(tagList));

        return blogEntity;
    }

    private String createProfile(String htmlBody){
        if(htmlBody.length() > 255 ){
            return htmlBody.substring(0,255);
        }
        return htmlBody;
    }

    private String createTagStr(List<String> tagList){
        String tags = "";
        if(tagList.size() == 1){
            tags = tagList.get(0);
        }

        if(tagList.size() > 1){
            tags = String.join(",",tagList);
        }
        return tags;
    }

    /**
     * 展示博文Obj
     */
    private BlogShow createBlogShow(@NotNull BlogEntity blogEntity){
        BlogShow blogShow = new BlogShow();
        BeanUtils.copyProperties(blogEntity, blogShow, IGNORE_ID_TAGS);
        blogShow.setId(HashidsUtil.encode(blogEntity.getId()));
        blogShow.setTags(creatTagList(blogEntity.getTags()));
        return blogShow;
    }


    /**
     * 对分页查询结果二次封装返回首页的blog对象
     */
    private List<Blog> parseRecords(List<BlogEntity> list){
        List<Blog> blogList = new ArrayList<>();
        for(BlogEntity blogEntity: list){
            Blog blog = new Blog();
            BeanUtils.copyProperties(blogEntity, blog, IGNORE_ID_TAGS);
            blog.setId(HashidsUtil.encode(blogEntity.getId()));
            blog.setTags(creatTagList(blogEntity.getTags()));
            blogList.add(blog);
        }
        return blogList;
    }

    /**
     * 对分页查询结果二次封装返回HOME页的blog对象
     */
    private List<BlogHome> parseHomeRecords(@NotNull List<BlogEntity> list){
        List<BlogHome> blogList = new ArrayList<>();
        for(BlogEntity blogEntity : list) {
            BlogHome blogHome = new BlogHome();
            BeanUtils.copyProperties(blogEntity, blogHome, IGNORE_TAGS);
            blogHome.setTags(creatTagList(blogEntity.getTags()));
            blogList.add(blogHome);
        }
        return blogList;
    }

    /**
     * 更新浏览量
     */
    private void updateViews(@NotNull BlogEntity blogEntity){
        Integer views = blogEntity.getViews();
        blogEntity.setViews(views + 1);
        blogMapper.updateById(blogEntity);
    }


    /**
     * 构建BlogEntity并入库
     */
    public int newBlog(BlogNewRequest blogNewRequest, HttpServletRequest request){
        //检查user信息
        String userKey = userService.checkUserKey(request);
        //获取用户id
        Integer userId = userService.queryUserIdByName(userKey);
        //创建new blog 对象
        BlogEntity blogEntity = createNewBlogEntity(userId,blogNewRequest);
        //blog新增数据库
        int count = blogMapper.insert(blogEntity);
        //存进redis缓存
        redisUtil.save("blog:id:" + blogEntity.getId(), blogEntity);
        //更新tag usedCount的方法
        if(!blogNewRequest.getTags().isEmpty()){
            updateNewBlogTagCount(blogNewRequest.getTags());
        }

        return count;
    }

    /**
     * 构建新BlogEntity
     */
    private BlogEntity createNewBlogEntity(Integer userId, @NotNull BlogNewRequest blogNewRequest){
        BlogEntity blogEntity = new BlogEntity();
        BeanUtils.copyProperties(blogNewRequest, blogEntity, IGNORE_USERID_PROFILE_TAGS);
        blogEntity.setUserId(userId);

        String htmlBody = CheckUtil.getHtmlBody(blogNewRequest.getHtml());
        blogEntity.setProfile(createProfile(htmlBody));

        blogEntity.setTags(createTagStr(blogNewRequest.getTags()));

        return blogEntity;
    }

    /**
     * 将tag字符串按逗号分解成list
     */
    private List<String> creatTagList(String tags){
        List<String> list = new ArrayList<>();
        if(!StringUtils.isEmpty(tags)){
            list = Arrays.asList(tags.split(","));
        }
        return list;
    }

    private void updateNewBlogTagCount(@NotNull List<String> list){
        for(String tagName: list){
            TagEntity entity = tagService.queryTagByName(tagName);
            int count = entity.getUsedCount();
            entity.setUsedCount(count + 1);
            tagMapper.updateById(entity);
        }
    }



}
