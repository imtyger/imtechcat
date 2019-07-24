package com.website.imtechcat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @ClassName PageUtil
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/19 17:26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageUtil implements Pageable {

	private Integer pageNum = 1;
	private Integer pageSize = 50;
	private Sort sort;

	@Override
	public int getPageNumber() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum){
		if(pageNum <= 0){
			pageNum = getPageNum();
		}
		this.pageNum = pageNum;
	}

	@Override
	public int getPageSize() {

		return pageSize;
	}

	public void setPageSize(Integer pageSize){
		if(pageSize == 0){
			pageSize = getPageSize();
		}
		if (pageSize >= 100){
			pageSize = 100;
		}
		this.pageSize = pageSize;
	}

	@Override
	public long getOffset() {

		return (getPageNum() - 1) * pageSize;
	}

	@Override
	public Sort getSort() {

		return sort;
	}

	@Override
	public Pageable next() {
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		return null;
	}

	@Override
	public Pageable first() {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	public static PageUtil newPage(Integer pageNum, Integer pageSize,Sort sort){
		PageUtil pageUtil = new PageUtil();
		pageUtil.setPageNum(pageNum);
		pageUtil.setPageSize(pageSize);
		if(sort == null){
			sort = new Sort(Sort.Direction.DESC,"lastUpdatedAt");
		}
		pageUtil.setSort(sort);
		return pageUtil;
	}

}
