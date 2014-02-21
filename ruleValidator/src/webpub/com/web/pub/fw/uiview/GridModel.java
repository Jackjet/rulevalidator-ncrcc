package com.web.pub.fw.uiview;

import java.util.ArrayList;
import java.util.List;

/**
 * Grid组件数据模型类
 * 
 * @author:孟志昂
 * @date:2012-11-5 上午11:05:28
 */
@SuppressWarnings("rawtypes")
public class GridModel {
	private int total = 0;
	private List records = new ArrayList();

	public GridModel(int total, List list) {
		super();
		this.total = total;
		this.records = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getRecords() {
		return records;
	}

	public void setRecords(List records) {
		this.records = records;
	}

}
