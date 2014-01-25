package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.am.consts.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ���Ի��������Ƿ������ȫ�����˱����ֶ�(��ͷ)
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "���Ի��������Ƿ������ȫ�����˱����ֶ�(��ͷ)", relatedIssueId = "517",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", scope = ScopeEnum.AM,
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00517 extends AbstractMetadataResourceRuleDefinition {

	private static final List<String> CHECK_FIELD_LIST = Arrays.asList("pk_group", "pk_org_v", "pk_org", "busi_type",
			"bill_status", "bill_type", "transi_type", "bill_code", "creator", "creationtime", "modifier",
			"modifiedtime", "billmaker", "billmaketime", "auditor", "audittime", "check_opinion");

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			IMetadataFile metadataFile = resource.getMetadataFile();
			IEntity mainEntity = metadataFile.getMainEntity();
			// ��ʵ��
			if (metadataFile.containsMainEntity()) {
				// ��ʵ��ӿڼ�
				List<IBusiInterface> busiItfs = mainEntity.getBusiInterfaces();
				boolean implemented = false;
				// TODO: ��չ��ǩ����������ж����Ƿ񵥾�
				for (IBusiInterface businInterface : busiItfs) {
					// �Ƿ���������Ϣ��д // ֻ���ж��Ƿ��ǵ��ݶ��� ʵ��������Ϣ��д�ľ���Ϊ�ǵ��ݣ���ʱ������
					if (businInterface.getFullClassName().equals(MDRuleConstants.FlowBizItf_INTERFACE_NAME)) {
						implemented = true;
						break;
					}
				}
				if (implemented) {
					List<IAttribute> attributeList = mainEntity.getAttributes(); // ʵ�����Լ�
					List<String> attributeNameList = new ArrayList<String>();
					for (IAttribute attribute : attributeList) {
						attributeNameList.add(attribute.getName().trim());
					}
					List<String> unincludeAttributeNameList = new ArrayList<String>();
					for (String checkAttribute : CHECK_FIELD_LIST) {
						if (!attributeNameList.contains(checkAttribute)) {
							unincludeAttributeNameList.add(checkAttribute);
						}
					}
					if (!unincludeAttributeNameList.isEmpty()) {
						String noteString = "ʵ�� " + mainEntity.getDisplayName() + " ȱʧ���ݱ����ֶΣ�"
								+ unincludeAttributeNameList.toString();
						result.addResultElement(resource.getResourcePath(), noteString);
					}
				}
			}
		}
		return result;
	}

}
