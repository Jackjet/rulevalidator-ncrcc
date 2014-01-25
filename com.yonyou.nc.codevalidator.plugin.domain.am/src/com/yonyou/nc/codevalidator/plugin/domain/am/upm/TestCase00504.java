package com.yonyou.nc.codevalidator.plugin.domain.am.upm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, description = "检查一个模块内服务接口是否有重名", relatedIssueId = "504",
		subCatalog = SubCatalogEnum.OCF_UPM, coder = "zhangnane")
public class TestCase00504 extends AbstractUpmQueryRuleDefinition {

	@Override
	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
		BusinessComponent businessComponent = ruleExecContext.getBusinessComponent();
		// 按模块分组map key--module_name value--map
		Map<String, Map<String, List<String>>> moduleGroup = new HashMap<String, Map<String, List<String>>>();
		StringBuilder noteBuilder = new StringBuilder();
		// 循环遍历范围内的upm文件
		for (UpmResource resource : resources) {
			try {
				UpmModuleVO upmModuleVO = resource.getUpmModuleVo();
				String modulename = upmModuleVO.getModuleName();
				Map<String, List<String>> funcNameToFunctioninfo = null;
				if (moduleGroup.containsKey(modulename)) {
					funcNameToFunctioninfo = moduleGroup.get(modulename);
				} else {
					funcNameToFunctioninfo = new HashMap<String, List<String>>();
					moduleGroup.put(modulename, funcNameToFunctioninfo);
				}
				List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
				if (pubComponentVoList != null) {
					// 循环每个Upm文件内的接口
					for (UpmComponentVO upmComponentVO : pubComponentVoList) {
						try {
							String interfaceName = upmComponentVO.getInterfaceName();
							if (interfaceName != null) {
								@SuppressWarnings("rawtypes")
								Class interfaceinfo = classLoaderUtils.loadClass(businessComponent.getProjectName(),
										interfaceName);
								Method methods[] = interfaceinfo.getMethods();
								// 循环接口内的方法
								for (Method method : methods) {
									String methodkey = method.getName() + "(";
									for (int i = 0; i < method.getParameterTypes().length; i++) {
										if (i != method.getParameterTypes().length - 1) {
											methodkey = methodkey + method.getParameterTypes()[i].getName() + ",";
										} else {
											methodkey = methodkey + method.getParameterTypes()[i].getName() + ")";
										}

									}
									if (funcNameToFunctioninfo.containsKey(methodkey)) {
										funcNameToFunctioninfo.get(methodkey).add(
												"\nupm位置：" + resource.getResourcePath() + ",接口名称：" + interfaceName);
									} else {
										List<String> functioninfo = new ArrayList<String>();
										functioninfo.add("\nupm位置：" + resource.getResourcePath() + ",接口名称："
												+ interfaceName);
										funcNameToFunctioninfo.put(methodkey, functioninfo);

									}
								}

							}
						} catch (RuleClassLoadException e) {
							noteBuilder.append(String.format("\n类型未加载到: %s", e.getMessage()));
						}
					}
				}
			} catch (ResourceParserException e1) {
				noteBuilder.append(String.format("Upm文件操作异常，具体信息: %s", e1.getMessage()));
			}
		}
		
		for (Map.Entry<String, Map<String, List<String>>> moduletemp : moduleGroup.entrySet()) {
			for (Map.Entry<String, List<String>> moduletempEntry : moduletemp.getValue().entrySet()) {
				String moduleName = moduletemp.getKey();
				List<String> list = moduletempEntry.getValue();
				if (list.size() > 1) {
					noteBuilder.append(String.format("\n模块名：%s 方法名重复：%s ", moduleName, moduletempEntry.getKey()));
					for (String l : list) {
						noteBuilder.append(l);
					}
				}
				
			}
		}
		return StringUtils.isBlank(noteBuilder.toString()) ? new SuccessRuleExecuteResult(getIdentifier())
				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
	}

}
