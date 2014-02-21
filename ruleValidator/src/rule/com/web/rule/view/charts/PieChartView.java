package com.web.rule.view.charts;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 饼图视图模型
 * 
 * @author:sunshine
 * @date:2014-1-15 下午1:28:34
 */
public class PieChartView extends PubChartView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-17 下午2:07:48
	 */
	private static final long serialVersionUID = 3183728419601073999L;

	// 数据示例
	private HashMap<String, Double> seriesData;

	public HashMap<String, Double> getSeriesData() {
		return seriesData;
	}

	public void setSeriesData(HashMap<String, Double> seriesData) {
		this.seriesData = seriesData;
	}

}
