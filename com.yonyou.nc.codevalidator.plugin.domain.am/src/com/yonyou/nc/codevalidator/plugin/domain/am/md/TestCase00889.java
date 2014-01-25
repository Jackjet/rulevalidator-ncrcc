package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.resparser.resource.utils.TableColumn;
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
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "ȷ��Ԫ���ݡ����ݿ�����ֶ�һ���Լ�� ", relatedIssueId = "430",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang")
public class TestCase00889 extends AbstractMetadataResourceRuleDefinition {
	
	private static final List<String> IGNORED_FIELDS = Arrays.asList("DR", "TS");

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			IMetadataFile metadataFile = resource.getMetadataFile();
			// ��ʵ��
			if(metadataFile.containsMainEntity()) {
				// ����ʵ��
				List<IEntity> entitys = metadataFile.getAllEntities();
				IEntity mainEntity = metadataFile.getMainEntity();
				IAttribute mainAttribute = mainEntity.getKeyAttribute(); // ��ʵ�����������Ϊ������
				for (IEntity entity : entitys) {
					List<String> metadataFieldNameList = new ArrayList<String>();
					List<IAttribute> allAttributes = entity.getAttributes(); // Ԫ���ݵ������ֶ�
					for (IAttribute attribute : allAttributes) {
						metadataFieldNameList.add(attribute.getName().trim().toUpperCase());// Ԫ���ݵ������ֶ���
					}
					String tableName = entity.getTableName(); // ���ݿ����
					List<TableColumn> columns = SQLQueryExecuteUtils.getTableColumnInfo(tableName.toUpperCase(), ruleExecContext.getRuntimeContext());
					if (!columns.isEmpty()) { // ���ݿ��б�Ĵ���ʱ
						List<String> tableFields = new ArrayList<String>();
						
						for(TableColumn column : columns){
							tableFields.add(column.getColumnName().toUpperCase());
						}
						ListEqualVO<String> compareVO = CollectionUtils.isListEqual(tableFields, metadataFieldNameList);
						if(!compareVO.getIsEqual()){
							List<String> notExistInMeta = compareVO.getNotExistDest();
							for(String tableField : notExistInMeta){
								if(!IGNORED_FIELDS.contains(tableField)&&!tableField.equals(mainAttribute.getName().toUpperCase())){
									noteBuilder.append("���ݿ��"+tableName+" �е��ֶ� ��"+tableField +"����Ӧ��Ԫ�����е��ֶβ�ƥ�䣬���飡");
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
