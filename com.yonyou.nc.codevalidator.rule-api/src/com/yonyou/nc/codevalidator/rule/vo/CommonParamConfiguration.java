package com.yonyou.nc.codevalidator.rule.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

/**
 * 通用的参数配置
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
	 * 增加参数配置项，当该参数配置已经存在时，不进行配置项的增加，因此可以以优先级从高到低的方式添加参数
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
	 * 在设置通用参数值之前，需要保证参数存在，建议在执行设置值之前先执行方法@see
	 * CommonParamConfiguration.addParamConfig
	 * 
	 * @param paramName
	 * @param paramValue
	 */
	public void setParamValue(String paramName, String paramValue) {
		if (!paramToConfigMap.containsKey(paramName)) {
			// Logg.error(String.format("设置参数时发生错误, 参数:%s不存在!", paramName));
			// 不进行设置
			return;
		}
		ParamConfiguration paramConfiguration = paramToConfigMap.get(paramName);
		paramConfiguration.setParamValue(paramValue);
	}

	public void removeUnusedParam(String paramName) {
		if (!paramToConfigMap.containsKey(paramName)) {
			throw new RuleBaseRuntimeException(String.format("移除参数:%s不存在!", paramName));
		}
		ParamConfiguration paramConfiguration = paramToConfigMap.get(paramName);
		paramConfigurationList.remove(paramConfiguration);
		paramToConfigMap.remove(paramName);
	}

	/**
	 * 移除其中的所有参数，仅当移除所有的配置规则后调用
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
