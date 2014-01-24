package com.yonyou.nc.codevalidator.plugin.domain.am.upm;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractUpmQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * upm����Ľӿں�ʵ���� ǰ�����пո� ����was���𲻳ɹ�
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, description = "upm����Ľӿں�ʵ���� ǰ�����пո� ����was���𲻳ɹ�",
		memo = "upm����Ľӿں�ʵ���� ǰ�����пո� ����was���𲻳ɹ�", solution = "", subCatalog = SubCatalogEnum.OCF_UPM, coder = "ZG",
		relatedIssueId = "662", executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00662 extends AbstractUpmQueryRuleDefinition {

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
		for (UpmResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			try {
				// �õ�ģ�� �൱��һ��upm�ļ�
				UpmModuleVO upmModuleVO = resource.getUpmModuleVo();
				// �õ�ģ�����������<�ӿڡ���ʵ����>��
				// UpmComponentVO Ϊһ�Խӿں�ʵ����
				List<UpmComponentVO> pubComponentVoList = upmModuleVO.getPubComponentVoList();
				if (pubComponentVoList != null) {
					for (UpmComponentVO upmComponentVO : pubComponentVoList) {
						String interfaceName = upmComponentVO.getInterfaceName();
						String implementationName = upmComponentVO.getImplementationName();
						if (interfaceName.length() != interfaceName.trim().length()) {
							noteBuilder.append("�ӿڣ�" + interfaceName + "��" + upmModuleVO.getModuleName()
									+ "-upm�ļ����пո����!\n");
						}
						if (implementationName.length() != implementationName.trim().length()) {
							noteBuilder.append("ʵ���ࣺ" + implementationName + "��" + upmModuleVO.getModuleName()
									+ "-upm�ļ����пո����!\n");
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
