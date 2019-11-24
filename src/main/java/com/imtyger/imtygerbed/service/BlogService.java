package com.imtyger.imtygerbed.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imtyger.imtygerbed.bean.blog.BlogNewRequest;
import com.imtyger.imtygerbed.bean.blog.BlogUpdateRequest;
import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.exception.BusinessException;
import com.imtyger.imtygerbed.entity.BlogEntity;
import com.imtyger.imtygerbed.entity.TagEntity;
import com.imtyger.imtygerbed.mapper.BlogMapper;
import com.imtyger.imtygerbed.mapper.TagMapper;
import com.imtyger.imtygerbed.model.*;
import com.imtyger.imtygerbed.utils.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    /**
     * 获取展示博客总数
     * @return
     */
    public Integer queryBlogTotal(){
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);

        Integer count = blogMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * 根据状态：0（展示）为条件获取分页
     * @param page
     * @return
     */
    private IPage<BlogEntity> queryBlogIPage(Page page){
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);

        IPage<BlogEntity> iPage = blogMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    /**
     * 获取首页分页博客
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String,Object> queryBlogs(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage<BlogEntity> iPage = queryBlogIPage(page);

        Map result = getBlogResult(pageNum, pageSize, iPage);
        List<BlogEntity> list = (List<BlogEntity>) result.get(constant.getList());
        if(!list.isEmpty()){
            result.put(constant.getList(),parseRecords(list));
        }
        return result;
    }

    /**
     * 获取Home页分页博客
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String,Object> queryHomeBlogs(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage<BlogEntity> iPage = blogMapper.selectPage(page,new QueryWrapper<>());

        Map result = getBlogResult(pageNum, pageSize, iPage);
        List<BlogEntity> list = (List<BlogEntity>) result.get(constant.getList());
        if(!list.isEmpty()){
            result.put(constant.getList(),parseHomeRecords(list));
        }
        return result;
    }

    /**
     * 获取search模糊查询
     * @param query
     * @return
     */
    public List<Blog> searchLike(String query){
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0);
        queryWrapper.and(wrapper -> wrapper.like("title", query).or().like("content", query));

        List<BlogEntity> blogEntityList = blogMapper.selectList(queryWrapper);
        List<Blog> blogList = new ArrayList<>();
        if(!blogEntityList.isEmpty()){
            blogList = parseRecords(blogEntityList);
        }
        return blogList;
    }

    /**
     * 获取关于
     * @return
     */
    public BlogShow getAboutBlog(){
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", "关于本博客");

        BlogEntity blogEntity = blogMapper.selectOne(queryWrapper);
        BlogShow blogShow = createBlogShow(blogEntity);

        return blogShow;
    }

    /**
     * 通过id获取博客详情
     * @param id
     * @return
     */
    public BlogShow postBlogId(String id){
        BlogEntity blogEntity = blogMapper.selectById(id);
        updateViews(blogEntity);
        BlogShow blogShow = createBlogShow(blogEntity);
        return blogShow;
    }

    /**
     * 删除blog by id
     * @param id
     * @return
     */
    public Integer deleteBlogById(String id){
        BlogEntity blogEntity = blogMapper.selectById(id);
        if(blogEntity == null){
            throw new BusinessException(Result.fail().getCode(),Result.fail().getMsg());
        }

        int count = blogMapper.deleteById(id);

        return count;
    }

    /**
     * ID获取blog entity
     * @param id
     * @return
     */
    public BlogEdit getBlogById(String id){
        BlogEntity blogEntity = blogMapper.selectById(id);

        BlogEdit edit = new BlogEdit();
        edit.setId(blogEntity.getId());
        edit.setTitle(blogEntity.getTitle());
        edit.setContent(blogEntity.getContent());
        edit.setTags(creatTagList(blogEntity.getTags()));
        return edit;
    }

    /**
     * 更新blog
     * @param blogUpdateRequest
     * @return
     */
    public Integer updateBlog(BlogUpdateRequest blogUpdateRequest){
        BlogEntity blogEntity = createUpdateBlogEntity(blogUpdateRequest);

        int count = blogMapper.updateById(blogEntity);
        return count;
    }

    /**
     * 更新博客展示状态
     * @param id
     * @param status
     * @return
     */
    public Integer updateBlogStatus(String id, Integer status){
        BlogEntity blogEntity = blogMapper.selectById(id);
        blogEntity.setStatus(status);
        int count = blogMapper.updateById(blogEntity);
        return count;
    }

    public List<BlogList> getBlogListByTag(String tag){
        QueryWrapper<BlogEntity> qw = new QueryWrapper<>();
        qw.like("tags",tag);

        List<BlogEntity> list = blogMapper.selectList(qw);
        return createBlogList(list);
    }

    private List<BlogList> createBlogList(List<BlogEntity> list){
        List<BlogList> blogLists = new ArrayList<>();
        if(!list.isEmpty()){
            list.forEach(blogEntity -> {
                BlogList blog = new BlogList();
                blog.setId(blogEntity.getId());
                blog.setTitle(blogEntity.getTitle());
                blog.setCreatedAt(blogEntity.getCreatedAt());
                blogLists.add(blog);
            });
        }
        return blogLists;
    }

    private BlogEntity createUpdateBlogEntity(BlogUpdateRequest blogUpdateRequest){
        Integer id = blogUpdateRequest.getId();
        BlogEntity blogEntity = blogMapper.selectById(id);
        blogEntity.setTitle(blogUpdateRequest.getTitle());
        blogEntity.setContent(blogUpdateRequest.getContent());
        blogEntity.setHtml(blogUpdateRequest.getHtml());

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
     * @param blogEntity
     * @return
     */
    private BlogShow createBlogShow(BlogEntity blogEntity){
        BlogShow blogShow = new BlogShow();
        blogShow.setId(blogEntity.getId());
        blogShow.setTitle(blogEntity.getTitle());
        blogShow.setHtml(blogEntity.getHtml());
        blogShow.setTags(creatTagList(blogEntity.getTags()));
        blogShow.setCreatedAt(blogEntity.getCreatedAt());
        return blogShow;
    }

    /**
     * 封装博客返回结果
     * @param pageNum
     * @param pageSize
     * @param iPage
     * @return
     */
    private Map<String, Object> getBlogResult(Integer pageNum, Integer pageSize, IPage iPage){
        Map result = new HashMap();

        result.put(constant.getCount(),iPage.getTotal());
        result.put(constant.getPageNum(),pageNum);
        result.put(constant.getPageSize(),pageSize);
        result.put(constant.getPageTotal(),iPage.getPages());
        if(iPage.getTotal() == 0){
            result.put(constant.getList(),new ArrayList<>());
            return result;
        }

        result.put(constant.getList(),iPage.getRecords());
        return result;
    }

    /**
     * 对分页查询结果二次封装返回首页的blog对象
     * @param list
     * @return
     */
    private List<Blog> parseRecords(List<BlogEntity> list){
        List<Blog> blogList = new ArrayList<>();
        list.forEach(blogEntity -> {
            Blog blog = new Blog();
            blog.setId(blogEntity.getId());
            blog.setTitle(blogEntity.getTitle());
            blog.setProfile(blogEntity.getProfile());
            blog.setTags(creatTagList(blogEntity.getTags()));
            blog.setCreatedAt(blogEntity.getCreatedAt());
            blogList.add(blog);
        });
        return blogList;
    }

    /**
     * 对分页查询结果二次封装返回HOME页的blog对象
     * @param list
     * @return
     */
    private List<BlogHome> parseHomeRecords(List<BlogEntity> list){
        List<BlogHome> blogList = new ArrayList<>();
        list.forEach(blogEntity -> {
            BlogHome blogHome = new BlogHome();
            blogHome.setId(blogEntity.getId());
            blogHome.setTitle(blogEntity.getTitle());
            blogHome.setTags(creatTagList(blogEntity.getTags()));
            blogHome.setCreatedAt(blogEntity.getCreatedAt());
            blogHome.setUpdatedAt(blogEntity.getUpdatedAt());
            blogHome.setStatus(blogEntity.getStatus());
            blogHome.setDeleted(blogEntity.getDeleted());
            blogList.add(blogHome);
        });
        return blogList;
    }

    /**
     * 更新浏览量
     * @param blogEntity
     */
    private void updateViews(BlogEntity blogEntity){
        Integer views = blogEntity.getViews();
        blogEntity.setViews(views + 1);
        blogMapper.updateById(blogEntity);
    }


    /**
     * 构建BlogEntity并入库
     * @param blogNewRequest
     * @param request
     * @return
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
        //更新tag usedCount的方法
        if(!blogNewRequest.getTags().isEmpty()){
            updateNewBlogTagCount(blogNewRequest.getTags());
        }

        return count;
    }

    /**
     * 构建新BlogEntity
     * @param userId
     * @param blogNewRequest
     * @return
     */
    private BlogEntity createNewBlogEntity(Integer userId,BlogNewRequest blogNewRequest){
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setUserId(userId);
        blogEntity.setTitle(blogNewRequest.getTitle());
        blogEntity.setContent(blogNewRequest.getContent());
        blogEntity.setHtml(blogNewRequest.getHtml());

        String htmlBody = CheckUtil.getHtmlBody(blogNewRequest.getHtml());
        blogEntity.setProfile(createProfile(htmlBody));

        blogEntity.setTags(createTagStr(blogNewRequest.getTags()));

        return blogEntity;
    }

    /**
     * 将tag字符串按逗号分解成list
     * @param tags
     * @return
     */
    private List<String> creatTagList(@NotNull String tags){
        List<String> list = new ArrayList<>();
        if(!tags.isEmpty()){
            list = Arrays.asList(tags.split(","));
        }
        return list;
    }

    private void updateNewBlogTagCount(List<String> list){
        for(String str: list){
            TagEntity entity = tagService.queryTagByName(str);
            int count = entity.getUsedCount();
            entity.setUsedCount(count + 1);
            tagMapper.updateById(entity);
        }
    }



}
