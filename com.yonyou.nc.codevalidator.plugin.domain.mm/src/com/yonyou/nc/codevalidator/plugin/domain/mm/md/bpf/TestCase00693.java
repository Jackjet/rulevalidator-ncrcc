package com.yonyou.nc.codevalidator.plugin.domain.mm.md.bpf;

import java.lang.reflect.Method;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;
import com.yonyou.nc.codevalidator.resparser.bpf.IOperation;
import com.yonyou.nc.codevalidator.resparser.bpf.IParameter;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetaProcessResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IType;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;

/**
 * 业务日志bpf文件中，接口方法名称和参数类型必须和代码中接口参数相同
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.METADATA, subCatalog = SubCatalogEnum.MD_BASESETTING,
description = "业务日志bpf文件中，接口方法名称和参数类型必须和代码中接口参数相同", solution = "", coder = "qiaoyang",
specialParamDefine = "",relatedIssueId = "693")
public class TestCase00693 extends AbstractMetaProcessResourceRuleDefinition{

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource resource : resources) {
				StringBuilder noteBuilder = new StringBuilder();
				IMetaProcessFile metadataFile = resource.getMetaProcessFile();
				List<IOpInterface> interfaces = metadataFile.getOpInterfaces();
				if (interfaces != null && interfaces.size() > 0) {
					for (IOpInterface interf : interfaces) {
						// 得到实现类名
						String implInterf = interf.getImplementationClassName();
						// 得到实现类
						IClassLoaderUtils loder =  ClassLoaderUtilsFactory.getClassLoaderUtils();
						Class<?> cls = loder.loadClass(ruleExecContext.getBusinessComponent().getProjectName(), implInterf);
						if (cls == null) {
							noteBuilder.append(interf.getDisplayName() + "中未定义实现类\n");
						}
						
						// 将方法转成list
						List<IOperation> operations = interf.getOperations();
						if (operations == null || operations.size() == 0) {
							noteBuilder.append(interf.getDisplayName() + "中未定义方法\n");
						} else {
							for (IOperation operation : operations) {
								String operationName = operation.getName();
								IType type = operation.getReturnType();
								List<IParameter> parameters = operation.getParameters();
								// 此处查询代码中是否包含此方法，且返回值是否是type类型的
								// 得到所有方法
								Method[] mthods = cls.getMethods();
								boolean isImpl = false;
								for (Method mthod : mthods) {
									if(operationName.equals(mthod.getName()) && type.getClass().equals(mthod.getReturnType().getClass()) && parameters.size() == mthod.getParameterTypes().length) {
										isImpl = true;
										break;
									}
								}
								if (!isImpl) {
									noteBuilder.append(interf.getDisplayName() + "方法" + operationName + "未做实现类\n");
								}
							}
						}
					}
//				} else {
//					noteBuilder.append("未定义接口\n");
				}
				if(noteBuilder.length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
