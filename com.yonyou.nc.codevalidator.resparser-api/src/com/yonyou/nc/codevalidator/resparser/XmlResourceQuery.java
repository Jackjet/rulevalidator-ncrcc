package com.yonyou.nc.codevalidator.resparser;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.resource.utils.Uif2ConfigFileUtils;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.FileLocatorUtils;

public class XmlResourceQuery extends AbstractResourceQuery {

	public final static String FUNCSPLITTOKEN = ",";

	// ��ǰ���ܽڵ���
	private final String[] functionNodes;

	// ��������,Ĭ��Ϊ
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
	 * �õ�client�ڵ�ŵ������ļ����·����ӳ��
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
	 * �õ�client�ڵ�ŵ������ļ�����·����ӳ��
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
