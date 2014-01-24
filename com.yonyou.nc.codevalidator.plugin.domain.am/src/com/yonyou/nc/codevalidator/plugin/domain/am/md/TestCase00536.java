package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
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
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "������Ԫ����(ʵ��)�����Զ����������û�����������ע���б���ע��",
		relatedIssueId = "536", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang",
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00536 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// ��ȡԪ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			// ��ȡԪ��������ʵ��
			List<IEntity> entitys = metadataFile.getAllEntities();
			StringBuilder noteBuilder = new StringBuilder();
			for (IEntity entity : entitys) { // ����ʵ��
				boolean containDef = false;
				List<IAttribute> attributes = entity.getAttributes(); // ���IAttribute�ӿ������ж��Զ�����ķ��������ֱ���ж��Ƿ����Զ�����
				// ��Ŀǰ��ʱ������
				for (IAttribute iaa : attributes) {
					if (iaa.getType().getDisplayName().contains("�Զ�����")) {
						containDef = true;
						break;
					}
				}
				if (containDef) { // �����Զ�����
					String id = entity.getId();
					// ��ѯ��� ref��
					String sql = String.format("select b.pk_userdefrule,a.name,a.code  "
							+ "from bd_userdefrule a,bd_userdefruleref b "
							+ "where a.pk_userdefrule = b.pk_userdefrule and " + " b.refclass = '%s'", id);
					DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
					if (ds.isEmpty()) { // �û��Զ���������û��ע��
						noteBuilder.append("ʵ�壺").append(entity.getDisplayName()).append(" ȱ���û��Զ�����ע��!\n");
					}
				}
			}
			if (StringUtils.isNotBlank(noteBuilder.toString())) {
				result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
			}
		}
		return result;
	}

}
