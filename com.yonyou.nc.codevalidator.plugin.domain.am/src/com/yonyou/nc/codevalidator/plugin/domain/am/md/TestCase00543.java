package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.CollectionUtils;
import com.yonyou.nc.codevalidator.sdk.utils.ListEqualVO;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "��ⵥ��ģ��ʱ������ҳǩ������Ԫ������ʵ���������ӱ������Ƿ�һ��",
		relatedIssueId = "543", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "ZG")
public class TestCase00543 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			// ��ȡԪ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			// ��ʵ��
			if(metadataFile.containsMainEntity()) {
				IEntity mainentity = metadataFile.getMainEntity();
				if (containMultiEntity(metadataFile)) {
					String id = mainentity.getId();
					// �õ�Ԫ���ݶ�Ӧ�ĵ���ģ��
					String preSql = "select b.BILL_TEMPLETCAPTION ,b.NODECODE,b.PK_BILLTYPECODE,b.PK_BILLTEMPLET "
							+ " from  sm_funcregister a,pub_billtemplet b"
							+ " where  a.funcode = b.NODECODE and  a.mdid = '" + id + "'";
					DataSet dstemp = SQLQueryExecuteUtils.executeQuery(preSql, ruleExecContext.getRuntimeContext());
					// ��Ϊ��˵��һ���˵���ģ�� û������ģ�������²�У��
					if (!dstemp.isEmpty()) {
						List<String> arrayStyleAttributeNameList = new ArrayList<String>(); // �����ʵ������ʵ�������
						// Ԫ��������ʵ������list
						List<IAttribute> attributes = mainentity.getAttributes();
						
						//��ȡԪ��������
						String mateName = mainentity.getDisplayName();
						for (IAttribute attribute : attributes) {
							if (attribute.getTypeStyle().trim().equals(MDRuleConstants.TYPE_STYLE_ARRAY)) { // ΪArray��ʽ
								arrayStyleAttributeNameList.add(attribute.getName());// ////
							}
						}
						// ����ģ��������ŵ�tabcodelistt�У�����ģ������-���Ʒŵ�map��
						HashMap<String, String> billTemplateInfoMap = new HashMap<String, String>();
						List<DataRow> datatemplet = dstemp.getRows();
					
						for (DataRow dr : datatemplet) {
							billTemplateInfoMap.put((String) dr.getValue("PK_BILLTEMPLET"),
									(String) dr.getValue("BILL_TEMPLETCAPTION"));
						}
					
						
						for (Map.Entry<String, String> entry : billTemplateInfoMap.entrySet()) {
							String pk_billtemplet = entry.getKey();
							// ����ģ��ҳǩ��
							StringBuilder billTemplateTobillTemplate_tSQL = new StringBuilder();
							billTemplateTobillTemplate_tSQL.append(" select distinct c.tabname, c.tabcode ");
							billTemplateTobillTemplate_tSQL.append(" from pub_billtemplet b, pub_billtemplet_t c  ");
							billTemplateTobillTemplate_tSQL.append(" where b.PK_BILLTEMPLET = c.PK_BILLTEMPLET and c.pos = 1");
							billTemplateTobillTemplate_tSQL.append(" and b.PK_BILLTEMPLET = '"+pk_billtemplet+"'");
							DataSet dsbill_templet = SQLQueryExecuteUtils.executeQuery(
									billTemplateTobillTemplate_tSQL.toString(), ruleExecContext.getRuntimeContext());
							List<DataRow> datarowbilltemplet = dsbill_templet.getRows();
							List<String> tabcodelist = new ArrayList<String>();
							if (!datarowbilltemplet.isEmpty()) {
								for (DataRow dr : datarowbilltemplet) {
									tabcodelist.add((String) dr.getValue("TABCODE"));// ////
								}
								ListEqualVO<String> compareVO = CollectionUtils.isListEqual(tabcodelist, arrayStyleAttributeNameList);
								if (!compareVO.getIsEqual()) {
									List<String> notExistInTemplet = compareVO.getNotExistDest();
									for(String field : notExistInTemplet){
										noteBuilder.append("����ģ��: "+billTemplateInfoMap.get(pk_billtemplet)+" �д��ڶ���ӦԪ���ݣ�"+mateName+"�в����ڵ�ҳǩ�ֶΣ�"+field +"\n");
									}
									List<String> notExistInMata = compareVO.getNotExistSrc();
									for(String property : notExistInMata){
										noteBuilder.append("Ԫ���ݣ�"+mateName+" �е�array�ֶ��ڵ���ģ�壺"+billTemplateInfoMap.get(pk_billtemplet)+"�в���ҳǩ���ֶΣ�"+property+"\n");
									}
								}
							}
						}
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


