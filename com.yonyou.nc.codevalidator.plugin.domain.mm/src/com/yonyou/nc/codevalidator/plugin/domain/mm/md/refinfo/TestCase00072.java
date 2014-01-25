package com.yonyou.nc.codevalidator.plugin.domain.mm.md.refinfo;

import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.IReference;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * <li>Ԫ����ʵ������Ϊ��������ʵ��ʱ����Ҫ����ʵ����չ��ǩ���Լ���DOC��ǩ</li>
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA,
		description = "Ԫ����ʵ������Ϊ��������ʵ��ʱ����Ҫ����ʵ����չ��ǩ���Լ���DOC��ǩ", solution = "Ԫ����ʵ������Ϊ��������ʵ��ʱ����Ҫ����ʵ����չ��ǩ���Լ���DOC��ǩ",
		relatedIssueId = "72", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe")
public class TestCase00072 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		String extendTag = "";
		for (MetadataResource metadataResource : resources) {
			IMetadataFile metaDataFile = metadataResource.getMetadataFile();
			if (MMValueCheck.isEmpty(metaDataFile.getMainEntity())) {
				continue;
			}
			// ��ʵ����չ��ǩ
			extendTag = metaDataFile.getMainEntity().getExtendTag();

			// ��ʵ���ṩ�Ĳ��շ�ʽ�ļ���
			List<IReference> referenceList = metaDataFile.getMainEntity().getReferences();

			if (null != referenceList && referenceList.size() > 0 && !extendTag.contains(MmMDConstants.EXTEND_TAG_DOC)) {
				result.addResultElement(metadataResource.getResourcePath(), "Ԫ���ݡ�"
						+ metaDataFile.getMainEntity().getFullName() + "������Ϊ��������ʵ��ʱ����Ҫ����ʵ����չ��ǩ���Լ���DOC��ǩ\n");
			}
		}
		return result;
	}

}
