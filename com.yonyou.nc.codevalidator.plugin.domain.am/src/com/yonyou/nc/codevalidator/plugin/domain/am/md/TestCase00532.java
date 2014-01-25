package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
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
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA,
		description = "��Ŀʵ�巽��ʵ��������Ϣ��д�ӿڵģ��˽ӿڵ�ʵ�������Ϊnc.impl.pm.common.PMFlowBizImpl", relatedIssueId = "532",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", scope = ScopeEnum.AM,
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00532 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		// �����
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// ��ȡԪ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				IEntity mainentity = metadataFile.getMainEntity();
				String mainsql = "select a.DISPLAYNAME,b.BIZITFIMPCLASSNAME " + "from md_class a,md_bizitfmap b  "
						+ "where a.DEFAULTTABLENAME like 'pm_%' " + "and a.id = b.classid "
						+ "and b.BIZINTERFACEID =  '" + mainentity.getId() + "'" + "and a.id = '" + mainentity.getId()
						+ "'";
				DataSet ds = SQLQueryExecuteUtils.executeQuery(mainsql, ruleExecContext.getRuntimeContext());
				List<DataRow> maindatarow = ds.getRows();
				if (!ds.isEmpty() && !maindatarow.isEmpty()) {
					for (DataRow dr : maindatarow) { // �������ԣ�Ѱ���Զ����� ������ʵ�����ʵ��
						String bitItfClassName = (String) dr.getValue("BIZITFIMPCLASSNAME");
						if (StringUtils.isNcStringNotEmpty(bitItfClassName)
								&& !bitItfClassName.equals("nc.impl.pm.common.PMFlowBizImpl")) { // ��ʵ��
							String displayName = (String) dr.getValue("DISPLAYNAME");
							result.addResultElement(resource.getResourcePath(), displayName
									+ " ʵ�ֵ�������Ϣ��д�ӿڵ�ʵ���಻��PMFlowBizImpl�� ���飡");
						}
					}
				}
			}
		}
		return result;
	}

}
