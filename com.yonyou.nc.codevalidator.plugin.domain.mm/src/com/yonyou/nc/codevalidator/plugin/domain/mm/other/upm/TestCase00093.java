package com.yonyou.nc.codevalidator.plugin.domain.mm.other.upm;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.code.JavaCodeFeature;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * 检查UPM文件中定义的接口是否在public层，实现类是否在private层
 * 
 * @author qiaoyang
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, executeLayer = ExecuteLayer.BUSICOMP,
		catalog = CatalogEnum.OTHERCONFIGFILE, description = "检查UPM文件中定义的接口是否在public层，实现类是否在private层",
		memo = "如果没有接口定义，不进实现范围private检查", solution = "", subCatalog = SubCatalogEnum.OCF_UPM, coder = "qiaoyang",
		relatedIssueId = "93")
public class TestCase00093 extends AbstractUpmQueryRuleDefinition {
	@Override
	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	@Override
	protected IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext, List<UpmResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
		BusinessComponent businessComponent = ruleExecContext.getBusinessComponent();
		for (UpmResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			try {
				UpmModuleVO upmModuleVO = resource.getUpmModuleVo();
				List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
				if (pubComponentVoList != null) {
					for (UpmComponentVO upmComponentVO : pubComponentVoList) {
						try {
							String interfaceName = upmComponentVO.getInterfaceName();
							if (interfaceName != null) {
								JavaCodeFeature interfaceFeature = classLoaderUtils.getCodeFeature(businessComponent,
										interfaceName);
								if (interfaceFeature.getResPrivilege() != JavaResPrivilege.PUBLIC) {
									noteBuilder.append("接口" + interfaceName + "应该在public目录下\n");
								}
								String implementationName = upmComponentVO.getImplementationName();
								JavaCodeFeature implFeature = classLoaderUtils.getCodeFeature(businessComponent,
										implementationName);
								if (implFeature.getResPrivilege() != JavaResPrivilege.PRIVATE) {
									noteBuilder.append("实现类" + implementationName + "应该在private目录下\n");
								}
							}
						} catch (RuleClassLoadException e) {
							noteBuilder.append(String.format("\n类型未加载到: %s", e.getMessage()));
						}
					}
				}
			} catch (ResourceParserException e1) {
				noteBuilder.append(String.format("Upm文件操作异常，具体信息: %s\n", e1.getMessage()));
			} finally {
				if (noteBuilder.toString().trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
