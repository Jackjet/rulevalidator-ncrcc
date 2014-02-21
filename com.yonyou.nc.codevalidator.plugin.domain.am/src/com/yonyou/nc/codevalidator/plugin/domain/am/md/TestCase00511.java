package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

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
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "Ԫ�����е�ʵ�������е���ʾ���Ʊ���Ϊ���� ", relatedIssueId = "511",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00511 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		// �����
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// ��ȡԪ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			// ��ȡԪ��������ʵ��
			List<IEntity> entitys = metadataFile.getAllEntities();
			for (IEntity entity : entitys) { // ����ʵ��
				StringBuilder noteBuilder = new StringBuilder();
				if (!StringUtils.isAllChineseCharacter(entity.getDisplayName())) { // ʵ����ʾ���ƺ��ֵ��ж�
					noteBuilder.append(entity.getDisplayName() + "ʵ�����ʾ�����к��в������ĺ��ֵ������ַ���");
				}
				List<IAttribute> entityattlist = entity.getAttributes(); // ʵ�����Լ�
				for (IAttribute attribute : entityattlist) {
					String attdisplayname = attribute.getDisplayName(); // ʵ��������ʾ����
					if (!StringUtils.isAllChineseCharacter(attdisplayname)) { // ��������
						noteBuilder.append(entity.getDisplayName() + "ʵ���е�").append(attdisplayname)
								.append("�ֶ���ʾ����û�����ĺ��֣�");
					}
				}
				if (noteBuilder.length() != 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
