package com.imtyger.imtygerbed.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: imtygerx@gmail.com
 * @Date: 2019/11/25
 */
@Slf4j
public class PageResult {


    /**
     * 封装返回结果
     * @param pageNum
     * @param pageSize
     * @param iPage
     * @return
     */
    public static Map<String, Object> getResult(Integer pageNum, Integer pageSize, IPage iPage){
        Map result = new HashMap();

        result.put("count",iPage.getTotal());
        result.put("pageNum",pageNum);
        result.put("pageSize",pageSize);
        result.put("pageTotal",iPage.getPages());
        if(iPage.getTotal() == 0){
            result.put("list",new ArrayList<>());
            return result;
        }

        result.put("list",iPage.getRecords());
        return result;
    }
}
