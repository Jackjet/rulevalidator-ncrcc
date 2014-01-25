package com.yonyou.nc.codevalidator.plugin.domain.mm.code.vo;

import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.code.util.MmJavaCodeQueryUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * �ۺ�VO����̳�AbstractBill
 * 
 * @author qiaoyanga
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, description = "�ۺ�VO����̳�AbstractBill", memo = "", solution = "�ۺ�VO����̳�nc.vo.pubapp.pattern.model.entity.bill.AbstractBill", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "155", coder = "lijbe")
public class TestCase00155 extends AbstractRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

        MetadataResourceQuery metadataResourceQuery = new MetadataResourceQuery();
        metadataResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<MetadataResource> resources = ResourceManagerFacade.getResource(metadataResourceQuery);
        String parentName = "nc.vo.pubapp.pattern.model.entity.bill.AbstractBill";
        for (MetadataResource resource : resources) {
            if (!resource.getResourceFileName().endsWith(".bmf")) {
                continue;
            }
            StringBuilder noteBuilder = new StringBuilder();
            IMetadataFile metadataFile = resource.getMetadataFile();

            if (MMValueCheck.isEmpty(metadataFile.getMainEntity())) {
                continue;
            }
            String metaName = metadataFile.getMainEntity().getFullName();

            String[] nameSpaceAndName = metaName.split("\\.");

            String aggVOName =
                    MmJavaCodeQueryUtil.queryAggVOClassName(nameSpaceAndName[0], nameSpaceAndName[1], ruleExecContext);
            if (MMValueCheck.isNotEmpty(aggVOName)) {
                IClassLoaderUtils iClassLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
                boolean b = false;
                try {
                    b =
                            iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                    .getProjectName(), aggVOName, parentName);
                }
                catch (RuleClassLoadException e) {
                    noteBuilder.append(e.getMessage() + "\n");
                    continue;
                }
                if (!b) {
                    noteBuilder.append(String.format("��%s��û�м̳�nc.vo.pubapp.pattern.model.entity.bill.AbstractBill.\n",
                            aggVOName));
                }
            }
            if (noteBuilder.toString().length() > 0) {
                result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
