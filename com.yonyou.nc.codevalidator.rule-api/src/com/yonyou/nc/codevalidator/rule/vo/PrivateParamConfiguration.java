package com.yonyou.nc.codevalidator.rule.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

/**
 * 私有参数规则配置
 * @author mazhqa
 * @since V2.3
 */
public class PrivateParamConfiguration {

	private List<ParamConfiguration> paramConfigurationList = new ArrayList<ParamConfiguration>();
	private Map<String, ParamConfiguration> paramToConfigMap = new HashMap<String, ParamConfiguration>();

	private static final String SPLIT_STRING = ";";

	public void addParamName(ParamConfiguration originConfig) {
		String paramName = originConfig.getParamName();
		if (!paramToConfigMap.containsKey(paramName)) {
			ParamConfiguration paramConfiguration = new ParamConfiguration(paramName);
			paramConfiguration.setEditorType(originConfig.getEditorType());
			paramConfiguration.setParamValue(originConfig.getParamValue());

			paramToConfigMap.put(paramName, paramConfiguration);
			paramConfigurationList.add(paramConfiguration);
		} else {
			ParamConfiguration paramConfiguration = paramToConfigMap.get(paramName);
			paramConfiguration.setParamValue(originConfig.getParamValue());
		}
	}

	public void setParamValue(String paramName, String paramValue) {
		if (!paramToConfigMap.containsKey(paramName)) {
			throw new RuleBaseRuntimeException(String.format("参数:%s不存在!", paramName));
		}
		ParamConfiguration paramConfiguration = paramToConfigMap.get(paramValue);
		paramConfiguration.setParamValue(paramValue);
	}

	public List<ParamConfiguration> getParamConfigurationList() {
		return Collections.unmodifiableList(paramConfigurationList);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (ParamConfiguration privateParamConfiguration : paramConfigurationList) {
			result.append(privateParamConfiguration.toString()).append(SPLIT_STRING);
		}
		return result.toString();
	}

	public static PrivateParamConfiguration fromString(String configurationString) {
		PrivateParamConfiguration configuration = new PrivateParamConfiguration();
		if (configurationString.contains(SPLIT_STRING)) {
			String[] singleConfigStringArray = configurationString.split(SPLIT_STRING);
			for (String singleConfig : singleConfigStringArray) {
				if (singleConfig.trim().length() > 0) {
					ParamConfiguration paramConfiguration = ParamConfiguration.fromString(singleConfig);
					configuration.addParamConfiguration(paramConfiguration);
				}
			}
		}
		return configuration;
	}

	private void addParamConfiguration(ParamConfiguration paramConfiguration) {
		String paramName = paramConfiguration.getParamName();
		if (!paramToConfigMap.containsKey(paramName)) {
			paramToConfigMap.put(paramName, paramConfiguration);
			paramConfigurationList.add(paramConfiguration);
		} else {
			ParamConfiguration paramConfiguration2 = paramToConfigMap.get(paramName);
			paramConfiguration2.setParamValue(paramConfiguration.getParamValue());
		}
	}

}
