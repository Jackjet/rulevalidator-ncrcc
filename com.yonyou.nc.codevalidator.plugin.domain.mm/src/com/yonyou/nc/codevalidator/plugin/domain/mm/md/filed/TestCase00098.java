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
 * 元数据中引用了支持多版本的档案必须使用OID和VID两个字段进行存储,一般有组织，物料，部门
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */

@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "元数据中引用了支持多版本的档案必须使用OID和VID两个字段进行存储",solution="元数据中引用了支持多版本的档案必须使用OID和VID两个字段进行存储,比如物料有OID，就要有物料VID", relatedIssueId = "98", subCatalog = SubCatalogEnum.MD_BASESETTING,coder = "lijbe")
public class TestCase00098 extends AbstractMetadataResourceRuleDefinition {

	/**
	 * 计划组织
	 */
	private static String[] PLANORG_OID_VID = new String[] {
			MmMetaDataTypeConstants.DT_PLANORG,
			MmMetaDataTypeConstants.DT_PLANORG_V };

	/**
	 * 工厂
	 */
	private static String[] FACTORY_OID_VID = new String[] {
			MmMetaDataTypeConstants.DT_ORG, MmMetaDataTypeConstants.DT_ORG_V };

	/**
	 * 物料
	 */
	private static String[] MATERIAL = new String[] {
			MmMetaDataTypeConstants.DT_MATERIAL,
			MmMetaDataTypeConstants.DT_MATERIAL_V };

	/**
	 * 部门
	 */
	private static String[] DEPT = new String[] {
			MmMetaDataTypeConstants.DT_DEPT, MmMetaDataTypeConstants.DT_DEPT_V };

	/**
	 * 以下2个数组的长度得一致
	 */
	private static String[][] REF_TYPE_IDS = new String[][] { PLANORG_OID_VID,
			FACTORY_OID_VID, MATERIAL, DEPT };

	private static String[] ITEM_NAMES = new String[] { "计划组织", "工厂", "物料",
			"部门" };

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
	 * 得到不合法的信息
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
					"元数据【 %s 】对应字段列表:%s ,必须使用OID和VID两个字段进行存储,请修改!\n",
					entity.getFullName(), items));
		}
		
		return strBuilder.toString();
	}

	/**
	 * 判断元数据实体是否实现了OID和VID字段
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

		//OID和VID中只有一个，则不符合规范。
		if (oidAndVids.size() == 1) {
			flag = false;
		}
		return flag;
	}
}
