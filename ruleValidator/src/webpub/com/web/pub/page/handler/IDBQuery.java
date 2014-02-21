package com.web.pub.page.handler;

public interface IDBQuery {
	/**
	 * 返回每页显示的数量
	 * 
	 * @return
	 * @date 2012-7-3 下午04:49:59
	 */
	int getCountPerPage();

	String getOldSearchId();

	String getNewSearchId();
}
