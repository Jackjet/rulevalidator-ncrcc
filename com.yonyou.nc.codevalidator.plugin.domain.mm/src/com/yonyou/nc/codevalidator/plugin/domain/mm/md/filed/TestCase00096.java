package com.yonyou.nc.codevalidator.plugin.domain.mm.md.filed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * pk_group、pk_org、pk_org_v等在主表中必须存在
 * @author qiaoyanga
 *
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "pk_group、pk_org、pk_org_v等在主表中必须存在", subCatalog = SubCatalogEnum.MD_BASESETTING, memo = "生产制造原有用例: nc.test.mm.autotest.bill.testcase.mdbased.business.TestCase000011", relatedIssueId = "96", coder = "qiaoyanga")
public class TestCase00096 extends AbstractMetadataResourceRuleDefinition {

    private static final List<String> CHECK_PROPERTY_LIST = Arrays.asList("pk_group", "pk_org", "pk_org_v");

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
                List<IAttribute> propAttributes = mainEntity.getAttributes();
                List<String> allFieldNames = new ArrayList<String>();
                for (IAttribute attribute : propAttributes) {
                    allFieldNames.add(attribute.getFieldName());
                }

                StringBuilder noteBuilder = new StringBuilder();
                for (String checkProperty : TestCase00096.CHECK_PROPERTY_LIST) {
                    if (!allFieldNames.contains(checkProperty)) {
                        noteBuilder.append(String.format("主实体: %s 中没有属性: %s \n", mainEntity.getDisplayName(),
                                checkProperty));
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
