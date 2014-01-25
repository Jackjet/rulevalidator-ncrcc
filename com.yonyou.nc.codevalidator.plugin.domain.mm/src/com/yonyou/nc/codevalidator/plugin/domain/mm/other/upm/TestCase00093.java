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
 * ���UPM�ļ��ж���Ľӿ��Ƿ���public�㣬ʵ�����Ƿ���private��
 * 
 * @author qiaoyang
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, executeLayer = ExecuteLayer.BUSICOMP,
		catalog = CatalogEnum.OTHERCONFIGFILE, description = "���UPM�ļ��ж���Ľӿ��Ƿ���public�㣬ʵ�����Ƿ���private��",
		memo = "���û�нӿڶ��壬����ʵ�ַ�Χprivate���", solution = "", subCatalog = SubCatalogEnum.OCF_UPM, coder = "qiaoyang",
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
									noteBuilder.append("�ӿ�" + interfaceName + "Ӧ����publicĿ¼��\n");
								}
								String implementationName = upmComponentVO.getImplementationName();
								JavaCodeFeature implFeature = classLoaderUtils.getCodeFeature(businessComponent,
										implementationName);
								if (implFeature.getResPrivilege() != JavaResPrivilege.PRIVATE) {
									noteBuilder.append("ʵ����" + implementationName + "Ӧ����privateĿ¼��\n");
								}
							}
						} catch (RuleClassLoadException e) {
							noteBuilder.append(String.format("\n����δ���ص�: %s", e.getMessage()));
						}
					}
				}
			} catch (ResourceParserException e1) {
				noteBuilder.append(String.format("Upm�ļ������쳣��������Ϣ: %s\n", e1.getMessage()));
			} finally {
				if (noteBuilder.toString().trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
