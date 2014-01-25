package com.yonyou.nc.codevalidator.plugin.domain.dmm.md;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.METADATA, coder = "songkj", description = "元数据组件名称空间属性必须有值检测规则", solution = "获取到元数据命名空间，如果为空，则提示错误", relatedIssueId = "758", subCatalog = SubCatalogEnum.MD_BASESETTING, repairLevel = RepairLevel.MUSTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00758 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

        for (MetadataResource metadataResource : resources) {
            IMetadataFile metadataFile = metadataResource.getMetadataFile();
            String namespace = metadataFile.getNamespace();
            if (null == namespace || namespace.trim().length() == 0) {
                result.addResultElement(metadataResource.getResourcePath(), "元数据组件名称空间属性不能为空！");
            }
        }
        return result;
    }
}
