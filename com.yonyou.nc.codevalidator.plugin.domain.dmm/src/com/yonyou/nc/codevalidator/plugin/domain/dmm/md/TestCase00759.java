package com.yonyou.nc.codevalidator.plugin.domain.dmm.md;

import java.util.List;

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
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.METADATA, coder = "songkj", description = "Ԫ����ʵ������������ʽΪSINGLE������ΪUFID������", solution = "ȡ��Ԫ����ʵ������������ʽ���������ͣ�������Ƿֱ���SINGLE��UFID������ʾ����", relatedIssueId = "759", subCatalog = SubCatalogEnum.MD_BASESETTING, repairLevel = RepairLevel.MUSTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00759 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

        for (MetadataResource metadataResource : resources) {
            IMetadataFile metadataFile = metadataResource.getMetadataFile();
            List<IEntity> allEntities = metadataFile.getAllEntities();
            for (IEntity entity : allEntities) {
                IAttribute keyAttribute = entity.getKeyAttribute();
                String typeStyle = keyAttribute.getTypeStyle();
                String typeName = keyAttribute.getType().getName();
                if (null == typeStyle || !typeStyle.equals("SINGLE")) {
                    result.addResultElement(metadataResource.getResourcePath(), "����������ʽ����SINGLE��");
                }
                if (null == typeName || !typeName.equals("UFID")) {
                    result.addResultElement(metadataResource.getResourcePath(), "�������Ͳ���UFID��");
                }
            }
        }
        return result;
    }
}
