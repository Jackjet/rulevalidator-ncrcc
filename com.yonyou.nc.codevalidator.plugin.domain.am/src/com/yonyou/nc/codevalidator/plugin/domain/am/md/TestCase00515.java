package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.am.consts.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "����Ԫ����ʵ��ʱ����ӳ��õĽӿ�", memo = "���ݻ򵵰���Ԫ��������Ҫʵ���Լ���ҵ��ӿ�",
		relatedIssueId = "515", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "xiepch",
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00515 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// ��ȡԪ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			if(metadataFile.containsMainEntity()) {
				// ��ʵ��
				IEntity mainEntity = metadataFile.getMainEntity();
				// ��������
				if (metadataFile.isDoc()) {
					Map<String, IAttribute> map = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.BDObject_INTERFACE_NAME);
					if (map == null) {
						result.addResultElement(resource.getResourcePath(), "������" + mainEntity.getDisplayName()
								+ " δʵ��IBDObject�ӿ�!");
					}
				} else {
					StringBuffer noteString = new StringBuffer();
					Map<String, IAttribute> mapFlow = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.FlowBizItf_INTERFACE_NAME);
					Map<String, IAttribute> mapHead = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.HeadBodyQueryItf_INTERFACE_NAME);
					Map<String, IAttribute> mapLOCK = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.PKLOCK_INTERFACE_NAME);
					Map<String, IAttribute> mapBd = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.BDObject_INTERFACE_NAME);
					if (mapFlow == null) {
						noteString.append("������Ϣ��ȡ����д��");
					}
					if (mapHead == null) {
						noteString.append("��������VO��ѯ��");
					}
					if (mapLOCK == null) {
						noteString.append("ҵ��PK����");
					}
					if (mapBd == null) {
						noteString.append("IBDObject");
					}
					
					if (noteString.toString().length() > 0) {
						result.addResultElement(resource.getResourcePath(), "����: " + mainEntity.getDisplayName()
								+ " δʵ�ֽӿ� " + noteString.toString() + "\n");
					}
				}
			}
		}
		return result;
	}

}
