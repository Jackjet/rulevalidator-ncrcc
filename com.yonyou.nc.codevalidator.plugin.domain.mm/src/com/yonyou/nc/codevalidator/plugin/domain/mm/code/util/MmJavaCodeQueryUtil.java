package com.yonyou.nc.codevalidator.plugin.domain.mm.code.util;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 制造Java类查询工具类
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmJavaCodeQueryUtil {

	/**
	 * 根据名称空间和元数据的name查询AggVO
	 * 
	 * @param nameSpace
	 * @param mdName
	 * @param ruleExecContext
	 * @return
	 */
	public static String queryAggVOClassName(String nameSpace, String mdName,
			IRuleExecuteContext ruleExecContext) {
		if (MMValueCheck.isEmpty(nameSpace) || MMValueCheck.isEmpty(mdName)) {
			return null;
		}

		String sql = "select acc.paravalue,cls.name from md_component comp, md_class cls, md_accessorpara acc "
				+ "where cls.isprimary = 'Y' and comp.id = cls.componentid and cls.id = acc.id "
				+ "and comp.namespace = '"
				+ nameSpace
				+ "' and cls.name = '"
				+ mdName + "' ";

		DataSet dataSet = null;
		try {
			dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(),
					ruleExecContext.getRuntimeContext());
		} catch (RuleBaseException e) {

			Logger.error(e.getMessage());
		}

		if (MMValueCheck.isEmpty(dataSet)) {
			return null;
		}
		String aggVOName = null;
		for (DataRow dataRow : dataSet.getRows()) {
			aggVOName = (String) dataRow.getValue("paravalue");
		}

		return aggVOName;

	}

	/**
	 * 根据名称空间和元数据的name查询SuperVO
	 * 
	 * @param nameSpace
	 * @param mdName
	 * @param ruleExecContext
	 * @return
	 */
	public static String[] querySuperVOClassName(String nameSpace,
			String mdName, IRuleExecuteContext ruleExecContext) {

		if (MMValueCheck.isEmpty(nameSpace) || MMValueCheck.isEmpty(mdName)) {
			return null;
		}

		String sql = "select cls2.fullclassname from md_component comp inner join md_class cls "
				+ "on comp.id = cls.componentid inner join  md_class cls2 on  comp.id = cls2.componentid "
				+ "where cls.classtype = 201 and comp.namespace = '"
				+ nameSpace
				+ "' and cls.name = '"
				+ mdName
				+ "' and cls2.classtype = 201";

		DataSet dataSet = null;
		try {
			dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(),
					ruleExecContext.getRuntimeContext());
		} catch (RuleBaseException e) {

			Logger.error(e.getMessage());
		}

		if (MMValueCheck.isEmpty(dataSet)) {
			return null;
		}
		List<String> superVOClassNameList = new ArrayList<String>();
		for (DataRow dataRow : dataSet.getRows()) {
			superVOClassNameList
					.add((String) dataRow.getValue("fullclassname"));
		}

		return superVOClassNameList.toArray(new String[superVOClassNameList
				.size()]);
	}
}
