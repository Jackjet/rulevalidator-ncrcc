package com.yonyou.nc.codevalidator.resparser;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.resource.utils.Uif2ConfigFileUtils;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.FileLocatorUtils;

public class XmlResourceQuery extends AbstractResourceQuery {

	public final static String FUNCSPLITTOKEN = ",";

	// 当前功能节点编号
	private final String[] functionNodes;

	// 单据类型,默认为
	private final BusinessComponent businessComponent;
	private Map<String, String> clientConfigFiles;

	public XmlResourceQuery(String[] functionNodes, BusinessComponent businessComponent) {
		super();
		this.functionNodes = functionNodes;
		this.businessComponent = businessComponent;
	}

	public XmlResourceQuery(String queryString, BusinessComponent businessComponent) {
		super();
		this.queryString = queryString;
		this.functionNodes = queryString.split(FUNCSPLITTOKEN);
		this.businessComponent = businessComponent;
	}

	/**
	 * 得到client节点号到配置文件相对路径的映射
	 * @return
	 * @throws RuleBaseException
	 */
	public Map<String, String> getFuncNodeToConfigPathMap() throws RuleBaseException {
		if (clientConfigFiles == null) {
			this.clientConfigFiles = Uif2ConfigFileUtils.getClientConfigFileByFuncode(functionNodes, getRuntimeContext());
		}
		return clientConfigFiles;
	}

	/**
	 * 得到client节点号到配置文件绝对路径的映射
	 * @return
	 * @throws RuleBaseException
	 */
	public Map<String, String> getFuncNodeToAbsoluteConfigPathMap() throws RuleBaseException {
		Map<String, String> configFiles = getFuncNodeToConfigPathMap();
		Map<String, String> result = new HashMap<String, String>();

		for (Map.Entry<String, String> entry : configFiles.entrySet()) {
			String configFilePath = (FileLocatorUtils.getComponentClientPath(getBusinessComponent()) + entry.getValue())
					.replace("//", "/");
			result.put(entry.getKey(), configFilePath);
		}
		return result;
	}

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

}
