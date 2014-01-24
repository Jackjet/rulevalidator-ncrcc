package com.yonyou.nc.codevalidator.rule.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

/**
 * ͨ�õĲ�������
 * 
 * @author mazhqa
 * 
 */
public class CommonParamConfiguration {

	private List<ParamConfiguration> paramConfigurationList = new ArrayList<ParamConfiguration>();
	private Map<String, ParamConfiguration> paramToConfigMap = new HashMap<String, ParamConfiguration>();

	public boolean isParamEmpty() {
		return paramConfigurationList.size() == 0;
	}

	/**
	 * ���Ӳ�����������ò��������Ѿ�����ʱ������������������ӣ���˿��������ȼ��Ӹߵ��͵ķ�ʽ��Ӳ���
	 * 
	 * @param originConfig
	 */
	public void addParamConfig(ParamConfiguration originConfig) {
		String paramName = originConfig.getParamName();
		if (!paramToConfigMap.containsKey(paramName)) {
			ParamConfiguration paramConfiguration = new ParamConfiguration(paramName);
			paramConfiguration.setEditorType(originConfig.getEditorType());
			paramConfiguration.setParamValue(originConfig.getParamValue());
			paramToConfigMap.put(paramName, paramConfiguration);
			paramConfigurationList.add(paramConfiguration);
		}
	}

	/**
	 * ������ͨ�ò���ֵ֮ǰ����Ҫ��֤�������ڣ�������ִ������ֵ֮ǰ��ִ�з���@see
	 * CommonParamConfiguration.addParamConfig
	 * 
	 * @param paramName
	 * @param paramValue
	 */
	public void setParamValue(String paramName, String paramValue) {
		if (!paramToConfigMap.containsKey(paramName)) {
			// Logg.error(String.format("���ò���ʱ��������, ����:%s������!", paramName));
			// ����������
			return;
		}
		ParamConfiguration paramConfiguration = paramToConfigMap.get(paramName);
		paramConfiguration.setParamValue(paramValue);
	}

	public void removeUnusedParam(String paramName) {
		if (!paramToConfigMap.containsKey(paramName)) {
			throw new RuleBaseRuntimeException(String.format("�Ƴ�����:%s������!", paramName));
		}
		ParamConfiguration paramConfiguration = paramToConfigMap.get(paramName);
		paramConfigurationList.remove(paramConfiguration);
		paramToConfigMap.remove(paramName);
	}

	/**
	 * �Ƴ����е����в����������Ƴ����е����ù�������
	 */
	public void removeAllParams() {
		paramConfigurationList.clear();
		paramToConfigMap.clear();
	}

	public List<ParamConfiguration> getParamConfigurationList() {
		return Collections.unmodifiableList(paramConfigurationList);
	}

	public Map<String, ParamConfiguration> getParamToConfigMap() {
		return Collections.unmodifiableMap(paramToConfigMap);
	}

	public List<String> getParamConfigurationNameList() {
		return Collections.unmodifiableList(new ArrayList<String>(paramToConfigMap.keySet()));
	}
}
