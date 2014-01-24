package com.yonyou.nc.codevalidator.plugin.domain.mm.md.filed;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
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

@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "主子实体的访问器要正确设置，主实体中对子实体的引用的访问策略要正确", subCatalog = SubCatalogEnum.MD_BASESETTING, memo = "生产制造原有用例: nc.test.mm.autotest.bill.testcase.mdbased.business.TestCase000019", relatedIssueId = "103", coder = "qiaoyang")
public class TestCase00103 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws ResourceParserException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        if (resources != null && resources.size() > 0) {
            for (MetadataResource resource : resources) {
                IMetadataFile metadataFile = resource.getMetadataFile();
                IEntity mainEntity = metadataFile.getMainEntity();
                if (mainEntity == null) {
                    continue;
                }
                List<IAttribute> propAttributeList = mainEntity.getAttributes();
                StringBuilder noteBuilder = new StringBuilder();
                for (IAttribute attribute : propAttributeList) {
                    String typeStyle = attribute.getTypeStyle();
                    if (MDRuleConstants.TYPE_STYLE_ARRAY.equals(typeStyle)) {
                        String accessStrategy = attribute.getAccessStrategy();
                        if (!MDRuleConstants.ACCESS_STRATEGY_BODYOFAGGVO.equals(accessStrategy)) {
                            noteBuilder.append(String.format("主实体: %s ARRAY 属性: %s 的访问策略应该为: %s\n",
                                    mainEntity.getDisplayName(), attribute.getName(),
                                    MDRuleConstants.ACCESS_STRATEGY_BODYOFAGGVO));
                        }
                    }
                }
                if (noteBuilder.toString().trim().length() > 0) {
                    result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
                }
            }
        }
        return result;
    }

}
