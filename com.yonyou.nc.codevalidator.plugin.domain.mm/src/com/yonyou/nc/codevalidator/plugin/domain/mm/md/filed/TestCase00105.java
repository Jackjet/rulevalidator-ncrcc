package com.yonyou.nc.codevalidator.plugin.domain.mm.md.filed;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
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

/**
 * IBDObject的接口实现和属性映射
 * @author qiaoyanga
 *
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "IBDObject的接口实现和属性映射", memo = "生产制造原有用例:nc.test.mm.autotest.bill.testcase.mdbased.itfmapping.TestCase000004", subCatalog = SubCatalogEnum.MD_BASESETTING, relatedIssueId = "105", coder = "qiaoyanga")
public class TestCase00105 extends AbstractMetadataResourceRuleDefinition {

    private static final List<String> CHECK_REF_NAME_LIST = Arrays.asList("id", "pk_org", "pk_group");

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws ResourceParserException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        if (resources != null && resources.size() > 0) {
            for (MetadataResource resource : resources) {
                // JGraph metaGraph =
                // XMLSerialize.getInstance().paserXmlToMDP(resource.getResourcePath(),
                // false);
                IMetadataFile metadataFile = resource.getMetadataFile();
                List<IEntity> allEntities = metadataFile.getAllEntities();
                StringBuilder noteBuilder = new StringBuilder();
                for (IEntity entity : allEntities) {
                    List<IBusiInterface> busiItfs = entity.getBusiInterfaces();
                    boolean implemented = false;
                    if (busiItfs != null && busiItfs.size() > 0) {
                        for (IBusiInterface businInterface : busiItfs) {
                            if (businInterface.getFullClassName().equals(MDRuleConstants.IBDOBJECT_INTERFACE_NAME)) {
                                implemented = true;
                                break;
                            }
                        }
                    }
                    if (!implemented) {
                        noteBuilder.append(String.format("实体: %s中未实现接口:%s\n", entity.getFullClassName(),
                                MDRuleConstants.IBDOBJECT_INTERFACE_NAME));
                    }
                    else {
                        Map<String, IAttribute> busiInterfaceAttributeMap =
                                entity.getBusiInterfaceAttributes(MDRuleConstants.IBDOBJECT_INTERFACE_NAME);
                        for (String checkRefName : TestCase00105.CHECK_REF_NAME_LIST) {
                            if (busiInterfaceAttributeMap.get(checkRefName) == null) {
                                noteBuilder.append(String.format("实体: %s 接口属性映射: %s 未配置!\n", entity.getFullClassName(),
                                        checkRefName));
                            }
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

    // private boolean containsBusiItfs(IEntity entity, Map<String, String>
    // idToNameMap) {
    // List<IBusiInterface> busiItfs = entity.getBusiInterfaces();
    // if (busiItfs != null && busiItfs.size() > 0) {
    // for (IBusiInterface businInterface : busiItfs) {
    // if
    // (businInterface.getFullClassName().equals(MDRuleConstants.IBDOBJECT_INTERFACE_NAME))
    // {
    // List<IBusiInterfaceAttribute> busiItAttrs =
    // businInterface.getBusiInterfaceAttributes();
    // for (IBusiInterfaceAttribute busiItfAttr : busiItAttrs) {
    // idToNameMap.put(busiItfAttr.getId(), busiItfAttr.getName());
    // }
    // return true;
    // }
    // }
    // }
    // return false;
    // }

}
