package com.yonyou.nc.codevalidator.resparser.executeresult;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;

/**
 * 抽象的脚本导出策略，建议自定义策略从该类中继承
 * @author mazhqa
 * @since V2.5
 */
public abstract class AbstractScriptExportStrategy implements IScriptExportStrategy {

	@Override
	public final String exportDataSet(DataSet dataSet) {
		StringBuilder result = new StringBuilder();
		result.append("数据库查询结果集结果:\n");
		List<DataRow> dataRows = dataSet.getRows();
		for (DataRow dataRow : dataRows) {
			result.append(processRow(dataRow)).append("\n");
		}
		return result.toString();
	}
	
	/**
	 * 递归处理每一行的结果
	 * @param dataRow
	 * @return
	 */
	protected abstract String processRow(DataRow dataRow);

}
