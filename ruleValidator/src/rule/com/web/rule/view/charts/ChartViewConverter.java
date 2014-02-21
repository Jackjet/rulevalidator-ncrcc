package com.web.rule.view.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.web.rule.view.RusStaticView;

/**
 * 图形视图转换工具类
 * 
 * @author:sunshine
 * @date:2014-1-15 下午1:36:56
 */
public class ChartViewConverter {

	/**
	 * 将统计结果转化为柱状图
	 * 
	 * @param resStatViewList
	 * @return
	 * @author:sunshine
	 * @date:2014-1-15 下午1:36:53
	 */
	public static BarChartView conert2BarView(
			List<RusStaticView> resStatViewList) {
		BarChartView barChartView = new BarChartView();
		if (resStatViewList != null) {
			// 驱动图表生成的数据内容数组
			HashMap<String, List<Double>> seriesData = new HashMap<String, List<Double>>();
			// X轴数据名称
			HashMap<String, String> axisDataMap = new HashMap<String, String>();
			// 统计结果按照规则执行主键分组
			HashMap<String, List<RusStaticView>> staticMap = new HashMap<String, List<RusStaticView>>();

			for (RusStaticView rusStaticView : resStatViewList) {
				// 统计结果按照规则执行主键分组
				List<RusStaticView> staticViewList = staticMap
						.get(rusStaticView.getRecordID());
				if (staticViewList == null) {
					staticViewList = new ArrayList<RusStaticView>();
				}
				staticViewList.add(rusStaticView);
				staticMap.put(rusStaticView.getRecordID(), staticViewList);
				if (!StringUtils.isEmpty(rusStaticView.getRecordDate())) {
					axisDataMap.put(rusStaticView.getRecordID(),
							rusStaticView.getRecordDate());
				}
			}

			Iterator<Entry<String, List<RusStaticView>>> staticIT = staticMap
					.entrySet().iterator();
			List<String> recordIDList = new ArrayList<String>();
			while (staticIT.hasNext()) {
				List<RusStaticView> staticList = staticIT.next().getValue();
				for (RusStaticView rusStaticView : staticList) {
					List<Double> doubleList = seriesData.get(rusStaticView
							.getResultStatu());
					if (doubleList == null) {
						doubleList = new ArrayList<Double>();
					}
					doubleList
							.add(Double.valueOf(rusStaticView.getStatusNum()));
					seriesData.put(rusStaticView.getResultStatu(), doubleList);

					// 整理X轴数据
					String recordID = rusStaticView.getRecordID();
					if (!recordIDList.contains(recordID)) {
						recordIDList.add(recordID);
					}
				}
			}

			// 图例名称
			barChartView.setTitleText("规则检查趋势图");
			// 图例名称备注
			barChartView.setTitleSubText("数据来自规则检查系统");

			// X轴数据名称
			String[] axisData = new String[recordIDList.size()];
			int i = 0;
			for (String recordID : recordIDList) {
				axisData[i++] = axisDataMap.get(recordID);
			}
			barChartView.setxAxisData(axisData);
			Iterator<Entry<String, List<Double>>> it = seriesData.entrySet()
					.iterator();

			// Y轴数据项
			HashMap<String, Double[]> seriesDatas = new HashMap<String, Double[]>();
			while (it.hasNext()) {
				Entry<String, List<Double>> entry = it.next();
				List<Double> doubleList = entry.getValue();
				seriesDatas.put(entry.getKey(),
						doubleList.toArray(new Double[doubleList.size()]));
			}
			barChartView.setSeriesData(seriesDatas);
		}
		return barChartView;
	}

	/**
	 * 将统计结果转换为饼图模型
	 * 
	 * @param ruleStaticList
	 * @return
	 * @author:sunshine
	 * @date:2014-1-17 下午2:28:49
	 */
	public static PieChartView conert2PieView(List<RusStaticView> ruleStaticList) {
		PieChartView pieChart = new PieChartView();
		if (ruleStaticList != null) {
			HashMap<String, Double> statuMap = new HashMap<String, Double>();
			Double totalRules = Double.valueOf(0);
			for (RusStaticView rusStaticView : ruleStaticList) {
				String statu = rusStaticView.getResultStatu();
				Double number = statuMap.get(statu);
				if (number == null) {
					number = Double.valueOf(0);
				}
				String statuNum = rusStaticView.getStatusNum();
				if (!StringUtils.isEmpty(statuNum)) {
					number += Double.valueOf(statuNum);
					totalRules += number;
				}
				statuMap.put(statu, number);
			}
			pieChart.setSeriesData(statuMap);
			pieChart.setTitleText("规则执行状态分布图");
			pieChart.setTitleSubText("执行规则总数：" + totalRules + "条<br/>执行时间："
					+ ruleStaticList.get(0).getRecordDate());
		}
		return pieChart;
	}
}
