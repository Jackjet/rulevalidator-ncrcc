package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
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
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.CollectionUtils;
import com.yonyou.nc.codevalidator.sdk.utils.ListEqualVO;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "���Բ�ѯģ���ϵ��Զ������Ƿ�Ͷ�ӦԪ�����ϵ��Զ��������", relatedIssueId = "856",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00856 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// ��ȡԪ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				// ��ʵ��
				IEntity mainentity = metadataFile.getMainEntity();
				String mainEntityId = mainentity.getId();
				String mainEntityDisplayName = mainentity.getDisplayName();
				// Ԫ�����ϵ������Զ�����

				// ��ѯģ��item��ѯ��� ֻҪpk��field_code ���ֶ�
				String sql = " select b.pk_templet,b.field_code " + " from pub_query_templet a,pub_query_condition b "
						+ "where a.id = b.pk_templet and a.metaclass = " + "'" + mainEntityId + "'"
						+ " and a.MODEL_NAME like '%" + mainEntityDisplayName + "%'" + " ";
				DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
				if (!ds.isEmpty()) { // �����˲�ѯģ��
					List<DataRow> maindatarow = ds.getRows();
					// �ò�ѯģ���ϵ������Զ�����list
					List<String> defFieldCodeInDbList = new ArrayList<String>();
					for (DataRow dr : maindatarow) { // �������ԣ�Ѱ���Զ����� ������ʵ�����ʵ��
						String fieldCode = (String) dr.getValue("field_code");
						if (fieldCode.startsWith("def")) { // ��ʵ��
							defFieldCodeInDbList.add(fieldCode);
						} else if (fieldCode.startsWith("bodyvos.def")) { // ��ʵ��
							String[] ss = fieldCode.split("\\.");
							defFieldCodeInDbList.add(ss[ss.length - 1]);
						}
					}

					// ��Ԫ�����Զ�����list
					List<String> attribNameInMetaList = new ArrayList<String>(); // ���Ԫ�����ϵ�����ʵ����Զ�����
					List<IEntity> allEntities = metadataFile.getAllEntities();
					for (IEntity ientity : allEntities) {
						List<IAttribute> entityatt = ientity.getAttributes();
						for (IAttribute ia : entityatt) {
							if (ia.getName().startsWith("def") || ia.getName().startsWith("bodyvos.def")) {
								attribNameInMetaList.add(ia.getName());
							}
						}
					}

					// �����Ϣ
					// TODO : NB�Ĵ���
					StringBuilder noteBuildershengyu = new StringBuilder();

					ListEqualVO<String> compareVO = CollectionUtils.isListEqual(defFieldCodeInDbList,
							attribNameInMetaList);

					if (!compareVO.getIsEqual()) { // ������������˵����ȱ��
						// ����
						for (String s : compareVO.getNotExistDest()) { // ����Ԫ���ݵ��Զ����������жϴ����Ƿ��ڲ�ѯģ����
							noteBuildershengyu.append(s + " ");

						}
						// ���noteBuildershengyu��Ϊ�գ�˵����ȱʧ��Ϣ
						if (noteBuildershengyu.toString().trim().length() > 0) {
							result.addResultElement("\n" + resource.getResourcePath(), mainEntityDisplayName
									+ "����Ӧ�Ĳ�ѯģ��ȱ����Ϣ����(ȱ����)��" + noteBuildershengyu.toString() + "\n\n");
						}
					}
				}
			}

		}
		return result;
	}

}
