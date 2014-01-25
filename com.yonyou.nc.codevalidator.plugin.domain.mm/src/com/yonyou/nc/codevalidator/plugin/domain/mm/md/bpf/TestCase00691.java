package com.yonyou.nc.codevalidator.plugin.domain.mm.md.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.bpf.IBusiActivity;
import com.yonyou.nc.codevalidator.resparser.bpf.IBusiOperator;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetaProcessResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ��Ҫ��¼ҵ����־bpf�ļ��У�����ʵ�岻��Ϊ��
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, subCatalog = SubCatalogEnum.MD_BASESETTING,
description = "��Ҫ��¼ҵ����־bpf�ļ��У�����ʵ�岻��Ϊ��", solution = "", coder = "qiaoyang",
specialParamDefine = "",relatedIssueId = "691")
public class TestCase00691 extends AbstractMetaProcessResourceRuleDefinition{

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource resource : resources) {
				StringBuilder noteBuilder = new StringBuilder();
				IMetaProcessFile metadataFile = resource.getMetaProcessFile();
				// ����ҵ��
				List<IBusiActivity> busiactivitys = metadataFile.getBusiActivities();
				if (busiactivitys != null) {
					for (IBusiActivity busiactivity : busiactivitys) {
						if (busiactivity.getOwnType() == null) {
							noteBuilder.append("ҵ��" + busiactivity.getDisplayName() + "û������ʵ��\n");
						}
					}
				} else {
					noteBuilder.append("û����Ӧ��ҵ��\n");
				}
				//����ҵ�����
				List<IBusiOperator> busiOperators = metadataFile.getBusiOperators();
				if (busiOperators != null) {
					for (IBusiOperator busiOperator : busiOperators) {
						if (busiOperator.getOwnType() == null) {
							noteBuilder.append("ҵ�����" + busiOperator.getDisplayName() + "û������ʵ��\n");
						}
					}
				} else {
					noteBuilder.append("û����Ӧ��ҵ�����\n");
				}
				// ���нӿ�
				List<IOpInterface> opInterfaces = metadataFile.getOpInterfaces();
				if (opInterfaces != null) {
					for (IOpInterface opInterface : opInterfaces) {
						if (opInterface.getOwnType() == null) {
							noteBuilder.append("�ӿ�" + opInterface.getDisplayName() + "û������ʵ��\n");
						}
					}
				} else {
					noteBuilder.append("û����Ӧ�Ľӿ�\n");
				}
				if(noteBuilder.length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}
}
