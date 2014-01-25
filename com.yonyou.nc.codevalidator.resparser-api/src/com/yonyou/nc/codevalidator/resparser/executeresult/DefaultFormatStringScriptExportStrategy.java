package com.yonyou.nc.codevalidator.resparser.executeresult;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;

/**
 * �ø�ʽ���ַ�����ʽ�Ľű���������
 * <p>
 * ���е�parameterΪ��Ӧ��sql��ѯ�ֶΣ������Ǳ�����������ڶ����ͬ���Ƶ��ֶΣ�����˳��
 * �����������field����ô��һ��Ϊfield�ڶ���Ϊ1_field����������
 * 
 * @author mazhqa
 * @since V2.5
 */
public class DefaultFormatStringScriptExportStrategy extends AbstractScriptExportStrategy {

	private final String formatString;
	private final String[] parameters;

	/**
	 * @param formatString
	 *            - �ο�String.formatʵ�֣���%s��Ϊռλ��
	 * @param parameters
	 *            - �������ƣ������ǴӶ�Ӧ��dataRow�л�ã�
	 */
	public DefaultFormatStringScriptExportStrategy(String formatString, String... parameters) {
		this.formatString = formatString;
		this.parameters = parameters;
	}

	/**
	 * @param formatString
	 *            - �ο�String.formatʵ��
	 * @param parameters
	 *            - �������ƣ������ǴӶ�Ӧ��dataRow�л�ã�
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
