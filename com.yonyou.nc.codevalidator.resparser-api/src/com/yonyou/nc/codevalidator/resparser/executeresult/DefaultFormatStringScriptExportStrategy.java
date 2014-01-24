package com.yonyou.nc.codevalidator.resparser.executeresult;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;

/**
 * 用格式化字符串形式的脚本导出策略
 * <p>
 * 其中的parameter为对应的sql查询字段，不考虑别名，如果存在多个相同名称的字段，按照顺序：
 * 比如存在两个field，那么第一个为field第二个为1_field，依次类推
 * 
 * @author mazhqa
 * @since V2.5
 */
public class DefaultFormatStringScriptExportStrategy extends AbstractScriptExportStrategy {

	private final String formatString;
	private final String[] parameters;

	/**
	 * @param formatString
	 *            - 参考String.format实现，用%s作为占位符
	 * @param parameters
	 *            - 参数名称，其中是从对应的dataRow中获得，
	 */
	public DefaultFormatStringScriptExportStrategy(String formatString, String... parameters) {
		this.formatString = formatString;
		this.parameters = parameters;
	}

	/**
	 * @param formatString
	 *            - 参考String.format实现
	 * @param parameters
	 *            - 参数名称，其中是从对应的dataRow中获得，
	 */
	public DefaultFormatStringScriptExportStrategy(String formatString, List<String> parameters) {
		this.formatString = formatString;
		this.parameters = parameters.toArray(new String[0]);
	}

	@Override
	protected String processRow(DataRow dataRow) {
		List<Object> objectParams = new ArrayList<Object>();
		for (String param : parameters) {
			String paramUppercase = param.toUpperCase();
			if (dataRow.hasColumn(paramUppercase)) {
				objectParams.add(dataRow.getValue(paramUppercase));
			} else {
				objectParams.add(param);
			}
		}
		return String.format(formatString, objectParams.toArray());
	}

}
