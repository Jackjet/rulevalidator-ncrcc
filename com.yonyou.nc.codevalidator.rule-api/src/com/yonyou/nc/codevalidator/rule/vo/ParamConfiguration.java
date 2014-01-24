package com.yonyou.nc.codevalidator.rule.vo;

/**
 * ����ÿ�����������ö���
 * @author mazhqa
 * @since V2.0
 */
public final class ParamConfiguration {

	public static final String DEFAULT_EDITOR_SPLITTER = "=";

	private final String paramName;
	private String paramValue;
	private String editorType = "com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.TextCellEditorDescriptor";

	public ParamConfiguration(String paramName) {
		this.paramName = paramName;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamName() {
		return paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public String getEditorType() {
		return editorType;
	}

	public void setEditorType(String editorType) {
		this.editorType = editorType;
	}

	@Override
	public String toString() {
		return String.format("%s%s%s", paramName, DEFAULT_EDITOR_SPLITTER, paramValue == null ? "" : paramValue);
	}

	/**
	 * ������ֵ�л�ȡ�������ƺ����õĲ���ֵ
	 * @param paramConfigurationString
	 * @return
	 */
	public static ParamConfiguration fromString(String paramConfigurationString) {
		boolean containsDot = paramConfigurationString.contains(DEFAULT_EDITOR_SPLITTER);
		if (containsDot) {
			String[] splitString = paramConfigurationString.split(DEFAULT_EDITOR_SPLITTER);
			ParamConfiguration result = new ParamConfiguration(splitString[0].trim());
			result.paramValue = splitString.length > 1 ? splitString[1].trim() : "";
			return result;
		} else {
			return new ParamConfiguration(paramConfigurationString);
		}
	}
	
	/**
	 * �Ӳ��������ַ����л�ȡ��������ƺͱ༭������
	 * @param paramConfigurationString
	 * @return
	 */
	public static ParamConfiguration parseConfig(String paramConfigurationString) {
		boolean containsDot = paramConfigurationString.contains(DEFAULT_EDITOR_SPLITTER);
		if (containsDot) {
			String[] splitString = paramConfigurationString.split(DEFAULT_EDITOR_SPLITTER);
			ParamConfiguration result = new ParamConfiguration(splitString[0].trim());
			result.editorType = splitString.length > 1 ? splitString[1].trim() : "";
			return result;
		} else {
			return new ParamConfiguration(paramConfigurationString);
		}
	}

}
