package com.yonyou.nc.codevalidator.plugin.domain.mm.md.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.bpf.IBusiActivity;
import com.yonyou.nc.codevalidator.resparser.bpf.IBusiOperator;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetaProcessResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 需要记录业务日志bpf文件中，所属实体不能为空
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, subCatalog = SubCatalogEnum.MD_BASESETTING,
description = "需要记录业务日志bpf文件中，所属实体不能为空", solution = "", coder = "qiaoyang",
specialParamDefine = "",relatedIssueId = "691")
public class TestCase00691 extends AbstractMetaProcessResourceRuleDefinition{

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource resource : resources) {
				StringBuilder noteBuilder = new StringBuilder();
				IMetaProcessFile metadataFile = resource.getMetaProcessFile();
				// 所有业务活动
				List<IBusiActivity> busiactivitys = metadataFile.getBusiActivities();
				if (busiactivitys != null) {
					for (IBusiActivity busiactivity : busiactivitys) {
						if (busiactivity.getOwnType() == null) {
							noteBuilder.append("业务活动" + busiactivity.getDisplayName() + "没有所属实体\n");
						}
					}
				} else {
					noteBuilder.append("没有相应的业务活动\n");
				}
				//所有业务操作
				List<IBusiOperator> busiOperators = metadataFile.getBusiOperators();
				if (busiOperators != null) {
					for (IBusiOperator busiOperator : busiOperators) {
						if (busiOperator.getOwnType() == null) {
							noteBuilder.append("业务操作" + busiOperator.getDisplayName() + "没有所属实体\n");
						}
					}
				} else {
					noteBuilder.append("没有相应的业务操作\n");
				}
				// 所有接口
				List<IOpInterface> opInterfaces = metadataFile.getOpInterfaces();
				if (opInterfaces != null) {
					for (IOpInterface opInterface : opInterfaces) {
						if (opInterface.getOwnType() == null) {
							noteBuilder.append("接口" + opInterface.getDisplayName() + "没有所属实体\n");
						}
					}
				} else {
					noteBuilder.append("没有相应的接口\n");
				}
				if(noteBuilder.length() > 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}
}
