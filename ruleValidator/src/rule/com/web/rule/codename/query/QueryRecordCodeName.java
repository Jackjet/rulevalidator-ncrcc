package com.web.rule.codename.query;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.web.pub.codename.iquery.IQueryCodeName;
import com.web.pub.comp.combobox.ComboBoxItem;
import com.web.pub.comp.combobox.ComboBoxModel;
import com.web.pub.comp.combobox.IComboBoxModel;
import com.web.pub.fw.exception.PubBaseException;
import com.web.rule.service.IRuleQryService;
import com.web.rule.view.ExecuteRecordView;

/**
 * 设置订单状态
 * 
 * @author,sunshine
 * @date,2013-2-5 下午1,44,33
 * 
 */
public class QueryRecordCodeName implements IQueryCodeName {

	public QueryRecordCodeName(IRuleQryService ruleService) {
		this.ruleService = ruleService;
	}

	private final IRuleQryService ruleService;

	@Override
	public IComboBoxModel getCodeNameModel(String param)
			throws PubBaseException {
		List<ExecuteRecordView> recordList = ruleService.qryExecuteRecord(null);
		HashMap<String, ComboBoxItem> ruleCheckMap = new HashMap<String, ComboBoxItem>();
		IComboBoxModel model = new ComboBoxModel();
		if (recordList != null) {
			for (ExecuteRecordView executeRecordView : recordList) {
				ComboBoxItem item = new ComboBoxItem(
						executeRecordView.getRecordId(), executeRecordView
								.getExeBeginTime().toString());
				model.addItem(item);
				ruleCheckMap.put(item.getCode(), item);

			}
		}
		// 如果获取某些特定状态的订单状态信息
		if (!StringUtils.isEmpty(param)) {
			String[] orders = param.split(",");
			model = new ComboBoxModel();
			for (int i = 0; i < orders.length; i++) {
				ComboBoxItem orderItem = ruleCheckMap.get(orders[i]);
				if (orderItem != null) {
					model.addItem(orderItem);
				}
			}
			return model;
		}
		return model;
	}
}
