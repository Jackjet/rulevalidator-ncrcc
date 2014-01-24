package com.yonyou.nc.codevalidator.plugin.domain.platform.md;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
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

@RuleDefinition(catalog = CatalogEnum.METADATA, executePeriod = ExecutePeriod.DEPLOY, executeLayer = ExecuteLayer.BUSICOMP, description = "�������е�metadatatypename�ֶ���Ԫ����ʵ�������Ƿ���ͬ", relatedIssueId = "462", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "yuanchh", memo = "�����е�metadatatypename����Ԫ����ʵ������ͬ", solution = "�޸Ĳ����е�metadatatypename�ֶ���Ԫ����ʵ������ͬ")
public class TestCase00462 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		// ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		StringBuilder noteBuilder = new StringBuilder();
		// ��ȡ���е�Ԫ���ݿռ���.ʵ����
		List<String> fields = new ArrayList<String>();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource metadataResource : resources) {
				List<IEntity> entities = metadataResource.getMetadataFile()
						.getAllEntities();
				for (IEntity entity : entities) {
					if (!StringUtils.isBlank(entity.getFullName())) {
						fields.add(entity.getFullName());
					}
				}
			}
			// ��ѯ�����еĲ������ݡ�
			String sql = String
					.format("select pk_refinfo,modulename,metadatatypename from bd_refinfo where modulename='%s'",
							ruleExecContext.getBusinessComponent().getModule());
			DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(),
					ruleExecContext.getRuntimeContext());
			if (!dataSet.isEmpty()) {
				for (DataRow row : dataSet.getRows()) {
					String modulename = (String) row.getValue("modulename");
					String pk_refinfo = (String) row.getValue("pk_refinfo");
					if (StringUtils.isBlank(modulename)) {
						noteBuilder.append("PK_REFINFO=" + pk_refinfo
								+ "��modulenameΪ��,�����Ϲ���!\n");
						continue;
					}
					String metadatatypename = (String) row
							.getValue("metadatatypename");
					if (StringUtils.isBlank(metadatatypename)) {
						noteBuilder.append("����PK_REFINFO=" + pk_refinfo
								+ "��metadatatypenameΪ��,�����Ϲ���!\n");
						continue;
					}
					String fullname = modulename + "." + metadatatypename;
					if (fields.contains(fullname)) {
						continue;
					} else {
						noteBuilder
								.append("����PK_REFINFO=" + pk_refinfo
										+ "�Ŀռ���.ʵ������" + fullname
										+ "����Ԫ�����в�����,�����Ϲ���!\n");
					}
				}
			}
		}
		return noteBuilder.toString().trim().length() > 0 ? new ErrorRuleExecuteResult(
				getIdentifier(), noteBuilder.toString())
				: new SuccessRuleExecuteResult(getIdentifier());
	}
}
