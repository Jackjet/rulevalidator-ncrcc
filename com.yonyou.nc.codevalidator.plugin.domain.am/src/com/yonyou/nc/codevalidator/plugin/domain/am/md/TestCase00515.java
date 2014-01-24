package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.am.consts.MDRuleConstants;
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

/**
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "创建元数据实体时，添加常用的接口", memo = "单据或档案在元数据中需要实现自己的业务接口",
		relatedIssueId = "515", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "xiepch",
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00515 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			if(metadataFile.containsMainEntity()) {
				// 主实体
				IEntity mainEntity = metadataFile.getMainEntity();
				// 档案过滤
				if (metadataFile.isDoc()) {
					Map<String, IAttribute> map = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.BDObject_INTERFACE_NAME);
					if (map == null) {
						result.addResultElement(resource.getResourcePath(), "档案：" + mainEntity.getDisplayName()
								+ " 未实现IBDObject接口!");
					}
				} else {
					StringBuffer noteString = new StringBuffer();
					Map<String, IAttribute> mapFlow = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.FlowBizItf_INTERFACE_NAME);
					Map<String, IAttribute> mapHead = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.HeadBodyQueryItf_INTERFACE_NAME);
					Map<String, IAttribute> mapLOCK = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.PKLOCK_INTERFACE_NAME);
					Map<String, IAttribute> mapBd = mainEntity
							.getBusiInterfaceAttributes(MDRuleConstants.BDObject_INTERFACE_NAME);
					if (mapFlow == null) {
						noteString.append("流程信息获取、回写；");
					}
					if (mapHead == null) {
						noteString.append("单据主子VO查询；");
					}
					if (mapLOCK == null) {
						noteString.append("业务PK锁；");
					}
					if (mapBd == null) {
						noteString.append("IBDObject");
					}
					
					if (noteString.toString().length() > 0) {
						result.addResultElement(resource.getResourcePath(), "单据: " + mainEntity.getDisplayName()
								+ " 未实现接口 " + noteString.toString() + "\n");
					}
				}
			}
		}
		return result;
	}

}
