package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 自定义项引用预置数据是否注册
 * 
 * @author qiaoyanga
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "自定义项引用预置数据是否注册", memo = "", solution = "", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, relatedIssueId = "152", coder = "qiaoyanga")
public class TestCase00152 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        MetadataResourceQuery metadataResourceQuery = new MetadataResourceQuery();
        metadataResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<MetadataResource> resources = ResourceManagerFacade.getResource(metadataResourceQuery);
        for (MetadataResource resource : resources) {
            // 过滤掉非bmf类型的文件
            if (!resource.getResourceFileName().endsWith(".bmf")) {
                continue;
            }
            StringBuilder noteBuilder = new StringBuilder();
            IMetadataFile metadataFile = resource.getMetadataFile();
            List<IEntity> allEntities = metadataFile.getAllEntities();
            StringBuilder insqlrefclass = new StringBuilder();
            for (IEntity entity : allEntities) {
                insqlrefclass.append(", ");
                insqlrefclass.append("'" + entity.getId() + "'");
            }
            StringBuilder sqlBuilder = new StringBuilder();
            if (insqlrefclass.length() <= 0) {
                continue;
            }
            sqlBuilder.append("select pk_userdefruleref,refclass from bd_userdefruleref where isnull(dr,0)=0 and ");
            sqlBuilder.append("refclass in (" + insqlrefclass.substring(1, insqlrefclass.length()) + ")");
            DataSet ds = this.executeQuery(ruleExecContext, sqlBuilder.toString());
            if (ds.isEmpty()) {
                noteBuilder.append("节点所有实体没有定义自定义项属性组。\n");
                result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
                continue;
            }
            List<String> refclassList = new ArrayList<String>();
            for (DataRow row : ds.getRows()) {
                refclassList.add((String) row.getValue("refclass"));
            }
            for (IEntity entry : allEntities) {
                if (!refclassList.contains(entry.getId())) {
                    noteBuilder.append(entry.getDisplayName() + ",");
                }
            }
            if (noteBuilder.length() != 0) {
                noteBuilder.append("实体没有定义自定义项属性组！\n");
                result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }

}
