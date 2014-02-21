package com.web.rule.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pub.fw.controller.BaseController;
import com.web.pub.fw.exception.PubBaseException;
import com.web.rule.page.paravo.ReportDetailQryPara;
import com.web.rule.service.IRuleQryService;
import com.web.rule.view.RusStaticView;
import com.web.rule.view.charts.BarChartView;
import com.web.rule.view.charts.ChartViewConverter;
import com.web.rule.view.charts.PieChartView;

/**
 * 规则执行报表查询controller
 * 
 * @author:sunshine
 * @date:2014-1-16 下午4:46:53
 */
@Controller
@RequestMapping("rule")
public class RuleReportContrller extends BaseController {

	private static final Log logger = LogFactory
			.getLog(RuleReportContrller.class);

	@Resource
	private IRuleQryService ruleService;

	/**
	 * 统计规则执行结果信息
	 * 
	 * @return
	 * @author:sunshine
	 * @date:2014-1-7 上午9:08:17
	 */
	@RequestMapping(value = "rulesatic.json", method = RequestMethod.POST)
	public @ResponseBody
	BarChartView queryRuleStatic(ModelMap modelMap,
			@ModelAttribute String recordID) {
		BarChartView barCharView = null;
		ReportDetailQryPara qryPara = new ReportDetailQryPara();
		qryPara.setRecordID(recordID);
		try {
			List<RusStaticView> ruleStaticList = ruleService
					.qryRuleStaticInfo(qryPara);
			barCharView = ChartViewConverter.conert2BarView(ruleStaticList);
		} catch (PubBaseException e) {
			logger.error(e);
		}
		return barCharView;
	}

	/**
	 * 根据条件查询每次执行结果的饼图
	 * 
	 * @param modelMap
	 * @param qryPara
	 * @return
	 * @author:sunshine
	 * @date:2014-1-16 下午4:49:20
	 */
	@RequestMapping(value = "rulesatic_pie.json", method = RequestMethod.POST)
	public @ResponseBody
	PieChartView queryRuleStatic(ModelMap modelMap,
			@ModelAttribute ReportDetailQryPara qryPara) {
		PieChartView pieChartView = null;
		try {
			List<RusStaticView> ruleStaticList = ruleService
					.qryRuleStaticInfo(qryPara);
			pieChartView = ChartViewConverter.conert2PieView(ruleStaticList);
		} catch (PubBaseException e) {
			logger.error(e);
		}
		return pieChartView;
	}
}
