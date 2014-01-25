package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
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
 * 表头表体VO必须继承自SuperVO
 * 
 * @author qiaoyanga
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, description = "表头表体VO必须继承自SuperVO", memo = "", solution = "", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "156", coder = "qiaoyanga")
public class TestCase00156 extends AbstractRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

        MetadataResourceQuery metadataResourceQuery = new MetadataResourceQuery();
        metadataResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        metadataResourceQuery.setMetaResType(MetaResType.BMF);
        List<MetadataResource> resources = ResourceManagerFacade.getResource(metadataResourceQuery);
        for (MetadataResource resource : resources) {
            StringBuilder noteBuilder = new StringBuilder();
            IMetadataFile metadataFile = resource.getMetadataFile();
            List<IEntity> allEntities = metadataFile.getAllEntities();
            for (IEntity entity : allEntities) {
                String entityName = entity.getFullClassName();
                String itfName = "nc.vo.pub.SuperVO";
                IClassLoaderUtils iClassLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
                boolean b = false;
                try {
                    b =
                            iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                    .getProjectName(), entityName, itfName);
                }
                catch (RuleClassLoadException e) {
                    noteBuilder.append(e.getMessage() + "\n");
                    continue;
                }
                if (!b) {
                    noteBuilder.append(String.format("%s 没有继承nc.vo.pub.SuperVO\n", entityName));
                }
            }
            if (noteBuilder.toString().length() > 0) {
                result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
