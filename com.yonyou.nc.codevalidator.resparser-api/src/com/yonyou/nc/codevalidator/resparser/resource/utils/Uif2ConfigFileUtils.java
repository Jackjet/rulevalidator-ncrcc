package com.yonyou.nc.codevalidator.resparser.resource.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptResource;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * UI工厂2相关配置文件的工具类
 * @author mazhqa
 * @since V1.0
 */
public final class Uif2ConfigFileUtils {

	private Uif2ConfigFileUtils() {
	}

	/**
	 * 根据功能节点号，获取ui工厂中的配置文件
	 * 
	 * @param funcodes
	 * @return map<功能节点,配置文件路径>
	 * @throws RuleBaseException
	 */
	public static Map<String, String> getClientConfigFileByFuncode(String[] funcodes, RuntimeContext runtimeContext) throws RuleBaseException {
		if (funcodes == null || funcodes.length == 0) {
			throw new RuleBaseException("功能节点未设置，请设置功能节点后再进行查询！");
		}
		StringBuilder funcodeStr = new StringBuilder();
		for (int i = 0; i < funcodes.length; i++) {
			if (i == funcodes.length - 1) {
				funcodeStr.append("'" + funcodes[i] + "'");
				break;
			}
			funcodeStr.append("'" + funcodes[i] + "',");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select f.funcode,p.paramvalue ");
		sb.append("from sm_funcregister f ");
		sb.append("inner join sm_paramregister p on p.parentid = f.cfunid ");
		sb.append("where f.funcode in (" + funcodeStr.toString() + ") ");
		sb.append(" and p.paramname = 'BeanConfigFilePath'");

		ScriptResourceQuery scriptResourceQuery = new ScriptResourceQuery(sb.toString());
		scriptResourceQuery.setRuntimeContext(runtimeContext);
		List<ScriptResource> scriptResources = ResourceManagerFacade.getResource(scriptResourceQuery);
		Map<String, String> result = new HashMap<String, String>();
		for (ScriptResource scriptResource : scriptResources) {
			Map<String, Object> itemRow = scriptResource.getResourceObjectMap();
			result.put((String) itemRow.get("funcode"), (String) itemRow.get("paramvalue"));
		}
		return result;
	}

	/**
	 * 根据单个功能节点号获取对应的配置文件路径
	 * @param funcode
	 * @return
	 * @throws RuleBaseException
	 */
	public static String getClientConfigFileByFuncode(String funcode, RuntimeContext runtimeContext) throws RuleBaseException {
		return getClientConfigFileByFuncode(new String[] { funcode }, runtimeContext).get(funcode);
	}

}
