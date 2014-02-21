package com.web.pub.page.handler;

import java.io.Serializable;

public class PubQryPara implements IDBQuery, Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-7 下午2:08:24
	 */
	private static final long serialVersionUID = -1588509199655547693L;
	// 新查询标识ID
	private String newSearchId;
	// 原查询标识ID
	private String oldSearchId;

	// 分页对象
	private PageInfoPara pageInfo;

	@Override
	public String getNewSearchId() {
		return newSearchId;
	}

	public void setNewSearchId(String newSearchId) {
		this.newSearchId = newSearchId;
	}

	@Override
	public String getOldSearchId() {
		return oldSearchId;
	}

	public void setOldSearchId(String oldSearchId) {
		this.oldSearchId = oldSearchId;
	}

	public PageInfoPara getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfoPara pageInfo) {
		this.pageInfo = pageInfo;
	}

	@Override
	public int getCountPerPage() {
		return this.getPageInfo().getPageSize();
	}
}
