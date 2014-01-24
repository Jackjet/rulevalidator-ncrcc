package com.yonyou.nc.codevalidator.plugin.domain.mm.md.filed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMetaDataTypeConstants;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * Ԫ������������֧�ֶ�汾�ĵ�������ʹ��OID��VID�����ֶν��д洢,һ������֯�����ϣ�����
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */

@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "Ԫ������������֧�ֶ�汾�ĵ�������ʹ��OID��VID�����ֶν��д洢",solution="Ԫ������������֧�ֶ�汾�ĵ�������ʹ��OID��VID�����ֶν��д洢,����������OID����Ҫ������VID", relatedIssueId = "98", subCatalog = SubCatalogEnum.MD_BASESETTING,coder = "lijbe")
public class TestCase00098 extends AbstractMetadataResourceRuleDefinition {

	/**
	 * �ƻ���֯
	 */
	private static String[] PLANORG_OID_VID = new String[] {
			MmMetaDataTypeConstants.DT_PLANORG,
			MmMetaDataTypeConstants.DT_PLANORG_V };

	/**
	 * ����
	 */
	private static String[] FACTORY_OID_VID = new String[] {
			MmMetaDataTypeConstants.DT_ORG, MmMetaDataTypeConstants.DT_ORG_V };

	/**
	 * ����
	 */
	private static String[] MATERIAL = new String[] {
			MmMetaDataTypeConstants.DT_MATERIAL,
			MmMetaDataTypeConstants.DT_MATERIAL_V };

	/**
	 * ����
	 */
	private static String[] DEPT = new String[] {
			MmMetaDataTypeConstants.DT_DEPT, MmMetaDataTypeConstants.DT_DEPT_V };

	/**
	 * ����2������ĳ��ȵ�һ��
	 */
	private static String[][] REF_TYPE_IDS = new String[][] { PLANORG_OID_VID,
			FACTORY_OID_VID, MATERIAL, DEPT };

	private static String[] ITEM_NAMES = new String[] { "�ƻ���֯", "����", "����",
			"����" };

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource metadataResource : resources) {
			IMetadataFile metadataFile = metadataResource.getMetadataFile();
			List<IEntity> entities = metadataFile.getAllEntities();
			for (IEntity iEntity : entities) {
				String info = this.getUnlawfulField(iEntity);
				if (info.length() > 0) {
					result.addResultElement(metadataResource.getResourcePath(),
							info);
				}
			}
		}
		return result;
	}

	/**
	 * �õ����Ϸ�����Ϣ
	 * @param entity
	 * @return
	 */
	private String getUnlawfulField(IEntity entity) {
		List<IAttribute> attrList = entity.getAttributes();
		StringBuilder strBuilder = new StringBuilder();
		List<String> items = new ArrayList<String>();
		int i = 0;
		for (String[] refType : TestCase00098.REF_TYPE_IDS) {
			if (!this.isOidAndVid(attrList, refType)) {
				items.add(TestCase00098.ITEM_NAMES[i]);
			}
			i++;
		}
		if(items.size() > 0){
			strBuilder.append(String.format(
					"Ԫ���ݡ� %s ����Ӧ�ֶ��б�:%s ,����ʹ��OID��VID�����ֶν��д洢,���޸�!\n",
					entity.getFullName(), items));
		}
		
		return strBuilder.toString();
	}

	/**
	 * �ж�Ԫ����ʵ���Ƿ�ʵ����OID��VID�ֶ�
	 * @param attrList
	 * @return
	 */
	private boolean isOidAndVid(List<IAttribute> attrList,
			String[] oidAndVidTypeIds) {
		boolean flag = true;
		String typeId = "";
		List<String> oidAndVids = new ArrayList<String>();
		for (IAttribute iAttribute : attrList) {
			if (MDRuleConstants.TYPE_STYLE_REF
					.equals(iAttribute.getTypeStyle())) {
				typeId = iAttribute.getType().getTypeId();
				if (Arrays.asList(oidAndVidTypeIds).contains(typeId)
						&& !oidAndVids.contains(typeId)) {
					oidAndVids.add(typeId);
				}
			}
		}

		//OID��VID��ֻ��һ�����򲻷��Ϲ淶��
		if (oidAndVids.size() == 1) {
			flag = false;
		}
		return flag;
	}
}
