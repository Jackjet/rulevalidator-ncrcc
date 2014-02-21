package com.web.rule.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pub.fw.constant.JSPFileNames;
import com.web.pub.fw.controller.BaseController;
import com.web.pub.fw.exception.PubBaseException;
import com.web.pub.fw.uiview.GridModel;
import com.web.pub.page.handler.DBDataPageSearcher;
import com.web.pub.page.handler.IPageAwareResult;
import com.web.rule.page.handler.RuleReportDetailDBQryHandler;
import com.web.rule.page.handler.RuleStaticQryHandler;
import com.web.rule.page.paravo.ReportDetailQryPara;
import com.web.rule.page.paravo.StaticQryPara;
import com.web.rule.service.IRuleQryService;
import com.web.rule.view.RuleExecuteResultView;

/**
 * 规则校验统计信息
 * 
 * @author:sunshine
 * @date:2013-12-27 下午3:32:25
 */
@Controller
@RequestMapping("rule")
@SuppressWarnings("unchecked")
public class RuleController extends BaseController {

	private static final Log logger = LogFactory.getLog(RuleController.class);

	@Resource
	private IRuleQryService ruleService;

	/**
	 * 执行记录
	 * 
	 * @return
	 * @author:sunshine
	 * @date:2014-1-2 上午11:09:19
	 */
	@RequestMapping(value = "resrecord.json", method = RequestMethod.POST)
	public @ResponseBody
	GridModel queryResultRecord(@ModelAttribute StaticQryPara qryPara) {
		GridModel gridModel = null;
		try {
			RuleStaticQryHandler qryHandler = new RuleStaticQryHandler();
			qryHandler.setRuleService(ruleService);
			IPageAwareResult result = DBDataPageSearcher
					.getInstance(qryHandler).getPageAwareResult(qryHandler,
							qryPara);
			if (result != null) {
				List<RuleExecuteResultView> pageList = result.toPage(qryPara
						.getPageInfo().getCurPageNo());
				gridModel = new GridModel(result.getTotalRecords(), pageList);
			}
		} catch (PubBaseException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return gridModel;
	}

	/**
	 * 删除规则执行记录
	 * 
	 * @param qryPara
	 * @return
	 * @author:sunshine
	 * @date:2014-1-7 下午3:38:38
	 */
	@RequestMapping(value = "delrecord.json", method = RequestMethod.POST)
	public @ResponseBody
	String delResultRecord(String recordID) {
		String delFlag = "N";
		try {
			// 删除规则执行记录
			ruleService.delExecRecord(recordID);
			delFlag = "Y";
		} catch (PubBaseException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return delFlag;
	}

	/**
	 * 执行记录明细
	 * 
	 * @param request
	 * @return
	 * @author:sunshine
	 * @throws Exception
	 * @date:2014-1-2 下午3:38:56
	 */
	@RequestMapping(value = "resrecorddetail.json", method = RequestMethod.POST)
	public @ResponseBody
	GridModel queryResultRecordDetail(
			@ModelAttribute ReportDetailQryPara qryPara) throws Exception {
		GridModel gridModel = null;
		try {
			RuleReportDetailDBQryHandler qryHandler = new RuleReportDetailDBQryHandler();
			qryHandler.setRuleService(ruleService);
			IPageAwareResult result = DBDataPageSearcher
					.getInstance(qryHandler).getPageAwareResult(qryHandler,
							qryPara);
			if (result != null) {
				List<RuleExecuteResultView> pageList = result.toPage(qryPara
						.getPageInfo().getCurPageNo());
				gridModel = new GridModel(result.getTotalRecords(), pageList);
			}
		} catch (PubBaseException e) {
			logger.error(e);
		}
		return gridModel;
	}

	/**
	 * 根据执行结果ID查询信息
	 * 
	 * @param resultID
	 * @return
	 * @throws Exception
	 * @author:sunshine
	 * @date:2014-1-8 下午4:53:52
	 */
	@RequestMapping(value = "ruleexedetail.htm", method = RequestMethod.GET)
	public String queryResultDetailByID(ModelMap modelMap,
			HttpServletRequest request) throws Exception {
		String forWard = JSPFileNames.RULE_EXEC_DTETAIL;
		String resultId = request.getParameter("resultId");
		RuleExecuteResultView resultView = ruleService
				.queryResultDetailByID(resultId);
		resultView.setResultDetail(resultView.getResultDetail().replace("\n",
				"<br/>"));
		modelMap.put("resultVO", resultView);
		return forWard;
	}
}
