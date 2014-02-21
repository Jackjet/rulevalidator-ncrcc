package com.web.rule.view.charts;

import java.io.Serializable;

/**
 * 图例视图公共属性
 * 
 * @author:sunshine
 * @date:2014-1-17 下午2:06:44
 */
public class PubChartView implements Serializable {
	/**
	 * @author:sunshine
	 * @date:2014-1-17 下午2:06:31
	 */
	private static final long serialVersionUID = 5921184678534145206L;

	private String titleText;
	private String titleSubText;

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public String getTitleSubText() {
		return titleSubText;
	}

	public void setTitleSubText(String titleSubText) {
		this.titleSubText = titleSubText;
	}

}
