package com.web.pub.page.handler;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IPageAwareResult {

	public int getTotalPages();

	public int getTotalRecords();

	/**
	 * 
	 * @return
	 */
	public abstract List<?> firstPage();

	/**
	 * 最后一页
	 * 
	 * @return
	 */

	public abstract List lastPage();

	/**
	 * 下一页
	 * 
	 * @return
	 */
	public abstract List nextPage();

	/**
	 * 上一页
	 * 
	 * @return
	 */
	public abstract List previousPage();

	public abstract List toPage(int page);

}