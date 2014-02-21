package com.web.rule.codename.query;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.web.pub.codename.iquery.IQueryCodeName;
import com.web.pub.comp.combobox.ComboBoxItem;
import com.web.pub.comp.combobox.ComboBoxModel;
import com.web.pub.comp.combobox.IComboBoxModel;
import com.web.pub.fw.exception.PubBaseException;

/**
 * 设置订单状态
 * 
 * @author,sunshine
 * @date,2013-2-5 下午1,44,33
 * 
 */
public class QueryOrderStatusCodeName implements IQueryCodeName {

	@Override
	public IComboBoxModel getCodeNameModel(String param)
			throws PubBaseException {

		HashMap<String, ComboBoxItem> orderStatusMap = new HashMap<String, ComboBoxItem>();
		IComboBoxModel model = new ComboBoxModel();
		ComboBoxItem item = new ComboBoxItem("执行失败", "执行失败");
		model.addItem(item);
		orderStatusMap.put(item.getCode(), item);
		item = new ComboBoxItem("执行成功", "执行成功");
		model.addItem(item);
		orderStatusMap.put(item.getCode(), item);
		item = new ComboBoxItem("忽略", "忽略");
		model.addItem(item);
		orderStatusMap.put(item.getCode(), item);
		item = new ComboBoxItem("执行异常", "执行异常");
		model.addItem(item);
		orderStatusMap.put(item.getCode(), item);
		// 如果获取某些特定状态的订单状态信息
		if (!StringUtils.isEmpty(param)) {
			String[] orders = param.split(",");
			model = new ComboBoxModel();
			for (int i = 0; i < orders.length; i++) {
				ComboBoxItem orderItem = orderStatusMap.get(orders[i]);
				if (orderItem != null) {
					model.addItem(orderItem);
				}
			}
			return model;
		}
		return model;
	}
}
