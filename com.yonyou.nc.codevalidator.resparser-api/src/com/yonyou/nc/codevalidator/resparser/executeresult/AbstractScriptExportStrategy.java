package com.yonyou.nc.codevalidator.resparser.executeresult;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;

/**
 * ����Ľű��������ԣ������Զ�����ԴӸ����м̳�
 * @author mazhqa
 * @since V2.5
 */
public abstract class AbstractScriptExportStrategy implements IScriptExportStrategy {

	@Override
	public final String exportDataSet(DataSet dataSet) {
		StringBuilder result = new StringBuilder();
		result.append("���ݿ��ѯ��������:\n");
		List<DataRow> dataRows = dataSet.getRows();
		for (DataRow dataRow : dataRows) {
			result.append(processRow(dataRow)).append("\n");
		}
		return result.toString();
	}
	
	/**
	 * �ݹ鴦��ÿһ�еĽ��
	 * @param dataRow
	 * @return
	 */
	protected abstract String processRow(DataRow dataRow);

}
