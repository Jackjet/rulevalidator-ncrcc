package com.web.rule.view.charts;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 柱状图数据模型
 * 
 * @author:sunshine
 * @date:2014-1-15 上午11:02:52
 */
public class BarChartView extends PubChartView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-15 上午11:03:04
	 */
	private static final long serialVersionUID = 3819949802751619522L;

	private String[] xAxisData;

	private HashMap<String, Double[]> seriesData;

	public String[] getxAxisData() {
		return xAxisData;
	}

	public void setxAxisData(String[] xAxisData) {
		this.xAxisData = xAxisData;
	}

	public HashMap<String, Double[]> getSeriesData() {
		return seriesData;
	}

	public void setSeriesData(HashMap<String, Double[]> seriesData) {
		this.seriesData = seriesData;
	}

}
