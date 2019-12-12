package com.imtyger.imtygerbed.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imtyger.imtygerbed.bean.bookmark.BookmarkRequest;
import com.imtyger.imtygerbed.bean.bookmark.BookmarkUpdateRequest;
import com.imtyger.imtygerbed.common.PageResult;
import com.imtyger.imtygerbed.entity.BookmarkEntity;
import com.imtyger.imtygerbed.mapper.BookmarkMapper;
import com.imtyger.imtygerbed.utils.HashidsUtil;
import com.imtyger.imtygerbed.vo.Bookmark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BookmarkService {

    @Resource
    private BookmarkMapper bookmarkMapper;

    @Resource
    private UserService userService;


    /**
     * 获取首页分页书签
     * @return
     */
    public Map queryBookmarks(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);

        QueryWrapper<BookmarkEntity> qw = new QueryWrapper<>();
        qw.orderByDesc("createdAt");
        
        IPage iPage = bookmarkMapper.selectPage(page, qw);

        Map result = PageResult.getResult(pageNum, pageSize, iPage);
        List<BookmarkEntity> list = (List<BookmarkEntity>) result.get("list");
        if(!list.isEmpty()) result.put("list", getRecords(list));
        return result;
    }

    /**
     * 获取Home页分页书签
     */
    public Map<String,Object> queryHomeBookmarks(Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage iPage = bookmarkMapper.selectPage(page, new QueryWrapper<>());

        return PageResult.getResult(pageNum, pageSize, iPage);
    }


    /**
     * 对分页查询结果二次封装返回首页的bookmark对象
     */
    private List<Bookmark> getRecords(List<BookmarkEntity> list){
        List<Bookmark> bookmarkList = new ArrayList<>();
        list.forEach(entity -> {
            Bookmark bookmark = new Bookmark();
            BeanUtils.copyProperties(entity, bookmark, "id");
            bookmark.setId(HashidsUtil.encode(entity.getId()));
            bookmarkList.add(bookmark);
        });
        return bookmarkList;
    }

    /**
     * 构建BookmarkEntity并入库
     */
    public int newBookmark(BookmarkRequest bookmarkRequest, HttpServletRequest request){
        //检查user信息
        String userKey = userService.checkUserKey(request);
        //获取用户id
        Integer userId = userService.queryUserIdByName(userKey);
        //创建new blog 对象
        BookmarkEntity bookmarkEntity = createNewBookmarkEntity(userId,bookmarkRequest);
        //bookmark新增数据库

        return bookmarkMapper.insert(bookmarkEntity);
    }


    /**
     * 构建新BookmarkEntity
     */
    private BookmarkEntity createNewBookmarkEntity(Integer userId, BookmarkRequest bookmarkRequest){
        BookmarkEntity entity = new BookmarkEntity();
        BeanUtils.copyProperties(bookmarkRequest, entity, "userId");
        entity.setUserId(userId);
        return entity;
    }

    /**
     * 删除bookmark by id
     */
    public Integer deleteBookmarkById(String id){
        return bookmarkMapper.deleteById(id);
    }

    /**
     * 更新bookmark
     */
    public Integer updateBookmark(BookmarkUpdateRequest request){
        BookmarkEntity entity = createUpdateBookmarkEntity(request);

        return bookmarkMapper.updateById(entity);
    }

    /**
     * 构建更新BookmarkEntity
     */
    private BookmarkEntity createUpdateBookmarkEntity(BookmarkUpdateRequest request){
        BookmarkEntity entity = new BookmarkEntity();
        BeanUtils.copyProperties(request, new BookmarkEntity());
        return entity;
    }

}
