package com.imtyger.imtygerbed.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imtyger.imtygerbed.bean.bookmark.BookmarkRequest;
import com.imtyger.imtygerbed.bean.bookmark.BookmarkUpdateRequest;
import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.exception.BusinessException;
import com.imtyger.imtygerbed.entity.BookmarkEntity;
import com.imtyger.imtygerbed.mapper.BookmarkMapper;
import com.imtyger.imtygerbed.model.Bookmark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class BookmarkService {

    @Resource
    private BookmarkMapper bookmarkMapper;

    @Resource
    private UserService userService;

    @Resource
    private Constant constant;

    /**
     * 获取首页分页书签
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String,Object> queryBookmarks(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage<BookmarkEntity> iPage = bookmarkMapper.selectPage(page, new QueryWrapper<>());

        Map result = getBookmarkResult(pageNum, pageSize, iPage);
        List<BookmarkEntity> list = (List<BookmarkEntity>) result.get(constant.getList());
        if(!list.isEmpty()){
            result.put(constant.getList(),getRecords(list));
        }
        return result;
    }

    /**
     * 获取Home页分页书签
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String,Object> queryHomeBookmarks(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage<BookmarkEntity> iPage = bookmarkMapper.selectPage(page, new QueryWrapper<>());

        return getBookmarkResult(pageNum, pageSize, iPage);
    }


    /**
     * 封装返回结果
     * @param pageNum
     * @param pageSize
     * @param iPage
     * @return
     */
    private Map<String, Object> getBookmarkResult(Integer pageNum, Integer pageSize, IPage iPage){
        Map result = new HashMap();

        result.put(constant.getCount(),iPage.getTotal());
        result.put(constant.getPageNum(),pageNum);
        result.put(constant.getPageSize(), pageSize);
        result.put(constant.getPageTotal(),iPage.getPages());
        if(iPage.getTotal() == 0){
            result.put(constant.getList(),new ArrayList<>());
            return result;
        }

        result.put(constant.getList(),iPage.getRecords());
        return result;
    }

    /**
     * 对分页查询结果二次封装返回首页的bookmark对象
     * @param list
     * @return
     */
    private List<Bookmark> getRecords(List<BookmarkEntity> list){
        List<Bookmark> bookmarkList = new ArrayList<>();
        list.forEach(entity -> {
            Bookmark bookmark = new Bookmark();
            bookmark.setId(entity.getId());
            bookmark.setTitle(entity.getTitle());
            bookmark.setLink(entity.getLink());
            bookmarkList.add(bookmark);
        });
        return bookmarkList;
    }

    /**
     * 构建BlogEntity并入库
     * @param bookmarkRequest
     * @param request
     * @return
     */
    public int newBookmark(BookmarkRequest bookmarkRequest, HttpServletRequest request){
        //检查user信息
        String userKey = userService.checkUserKey(request);
        //获取用户id
        Integer userId = userService.queryUserIdByName(userKey);
        //创建new blog 对象
        BookmarkEntity bookmarkEntity = createNewBookmarkEntity(userId,bookmarkRequest);
        //blog新增数据库
        int count = bookmarkMapper.insert(bookmarkEntity);

        return count;
    }


    /**
     * 构建新BookmarkEntity
     * @param userId
     * @param bookmarkRequest
     * @return
     */
    private BookmarkEntity createNewBookmarkEntity(Integer userId, BookmarkRequest bookmarkRequest){
        BookmarkEntity entity = new BookmarkEntity();
        entity.setUserId(userId);
        entity.setTitle(bookmarkRequest.getTitle());
        entity.setDescr(bookmarkRequest.getDescr());
        entity.setLink(bookmarkRequest.getLink());
//        entity.setCreatedAt(new Date());
//        entity.setUpdatedAt(new Date());
        return entity;
    }

    /**
     * 删除bookmark by id
     * @param id
     * @return
     */
    public Integer deleteBookmarkById(String id){
        BookmarkEntity entity = bookmarkMapper.selectById(id);
        if(entity == null){
            throw new BusinessException(Result.fail().getCode(),Result.fail().getMsg());
        }

        int count = bookmarkMapper.deleteById(id);
        return count;
    }

    /**
     * 更新bookmark
     * @param request
     * @return
     */
    public Integer updateBookmark(BookmarkUpdateRequest request){
        BookmarkEntity entity = createUpdateBookmarkEntity(request);

        int count = bookmarkMapper.updateById(entity);
        return count;
    }

    /**
     * 构建更新BookmarkEntity
     * @param request
     * @return
     */
    private BookmarkEntity createUpdateBookmarkEntity(BookmarkUpdateRequest request){
        BookmarkEntity entity = new BookmarkEntity();
        entity.setId(request.getId());
        entity.setTitle(request.getTitle());
        entity.setDescr(request.getDescr());
        entity.setLink(request.getLink());
//        entity.setUpdatedAt(new Date());
        return entity;
    }

}
