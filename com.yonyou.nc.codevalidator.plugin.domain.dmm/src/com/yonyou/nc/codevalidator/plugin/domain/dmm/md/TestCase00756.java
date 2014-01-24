package com.yonyou.nc.codevalidator.plugin.domain.dmm.md;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@RuleDefinition(catalog = CatalogEnum.METADATA, coder = "songkj", description = "部门内相同名称字段，不同业务含义检测规则", relatedIssueId = "756", subCatalog = SubCatalogEnum.MD_FUNCCHECK, repairLevel = RepairLevel.MUSTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00756 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

        for (MetadataResource metadataResource : resources) {
            IMetadataFile metadataFile = metadataResource.getMetadataFile();
            List<IEntity> allEntities = metadataFile.getAllEntities();
            for (IEntity entity : allEntities) {
                List<IAttribute> attributeList = entity.getAttributes();
                for (IAttribute attribute : attributeList) {
                    String name = attribute.getName();
                    String currentDisplayName = attribute.getDisplayName();
                    if (this.nameToDisplayNameMap.containsKey(name)) {
                        boolean isInStandardNames = false;
                        String[] displayNameArray = this.nameToDisplayNameMap.get(name);
                        for (String displayName : displayNameArray) {
                            if (currentDisplayName.equals(displayName)) {
                                isInStandardNames = true;
                                break;
                            }
                        }
                        if (!isInStandardNames) {
                            result.addResultElement(metadataResource.getResourcePath(), "建议将" + entity.getFullName()
                                    + "实体中" + name + "的显示名称由\"" + currentDisplayName + "\"改为\"" + displayNameArray[0]
                                    + "\"");
                        }
                    }
                    // else {
                    // this.nameToDisplayNameMap.put(name, new String[] {
                    // currentDisplayName
                    // });
                    // }
                }
            }
        }
        return result;

    }

    private Map<String, String[]> nameToDisplayNameMap = new HashMap<String, String[]>();

    public TestCase00756() {
        this.nameToDisplayNameMap.put("pk_group", new String[] {
            "集团", "所属集团"
        });
        this.nameToDisplayNameMap.put("cmaterialid", new String[] {
            "物料", "子项物料", "父项物料"
        });
        this.nameToDisplayNameMap.put("cmaterialvid", new String[] {
            "物料版本", "子项物料版本", "父项物料版本"
        });
        this.nameToDisplayNameMap.put("fbillstatus", new String[] {
            "单据状态"
        });
        this.nameToDisplayNameMap.put("vbatchcode", new String[] {
            "生产批次号"
        });
        this.nameToDisplayNameMap.put("vrowno", new String[] {
            "行号", "序号", "子项行号", "父项行号"
        });
        this.nameToDisplayNameMap.put("nnum", new String[] {
            "主数量"
        });
        this.nameToDisplayNameMap.put("nastnum", new String[] {
            "数量"
        });
        this.nameToDisplayNameMap.put("cunitid", new String[] {
            "主单位", "子项主单位", "父项主单位"
        });
        this.nameToDisplayNameMap.put("castunitid", new String[] {
            "单位", "子项单位", "父项单位"
        });
        this.nameToDisplayNameMap.put("cmeasureid", new String[] {
            "主单位", "子项主单位", "父项主单位"
        });
        this.nameToDisplayNameMap.put("cassmeasureid", new String[] {
            "单位", "子项单位", "父项单位"
        });
        this.nameToDisplayNameMap.put("vchangerate", new String[] {
            "换算率", "子项换算率", "父项换算率"
        });
        this.nameToDisplayNameMap.put("vnote", new String[] {
            "备注", "子项备注", "父项备注"
        });
        this.nameToDisplayNameMap.put("vfirsttype", new String[] {
            "源头单据类型"
        });
        this.nameToDisplayNameMap.put("vfirsttrantypeid", new String[] {
            "源头交易类型"
        });
        this.nameToDisplayNameMap.put("vfirsttrantypecode", new String[] {
            "源头交易类型编码"
        });
        this.nameToDisplayNameMap.put("vfirstid", new String[] {
            "源头单据ID"
        });
        this.nameToDisplayNameMap.put("vfirstcode", new String[] {
            "源头单据编码"
        });
        this.nameToDisplayNameMap.put("vfirstbid", new String[] {
            "源头单据行ID"
        });
        this.nameToDisplayNameMap.put("vfirstrowno", new String[] {
            "源头单据行号"
        });
        this.nameToDisplayNameMap.put("vsrctype", new String[] {
            "来源单据类型"
        });
        this.nameToDisplayNameMap.put("vsrctrantypeid", new String[] {
            "来源交易类型"
        });
        this.nameToDisplayNameMap.put("vsrctrantypecode", new String[] {
            "来源交易类型编码"
        });
        this.nameToDisplayNameMap.put("vsrcid", new String[] {
            "来源单据ID"
        });
        this.nameToDisplayNameMap.put("vsrccode", new String[] {
            "来源单据编码"
        });
        this.nameToDisplayNameMap.put("vsrcbid", new String[] {
            "来源单据行ID"
        });
        this.nameToDisplayNameMap.put("vsrcrowno", new String[] {
            "来源单据行号"
        });
        this.nameToDisplayNameMap.put("billmaker", new String[] {
            "制单人"
        });
        this.nameToDisplayNameMap.put("dmakedate", new String[] {
            "制单日期"
        });
        this.nameToDisplayNameMap.put("creator", new String[] {
            "创建人"
        });
        this.nameToDisplayNameMap.put("creationtime", new String[] {
            "创建时间"
        });
        this.nameToDisplayNameMap.put("modifier", new String[] {
            "最后修改人"
        });
        this.nameToDisplayNameMap.put("modifiedtime", new String[] {
            "最后修改时间"
        });
        this.nameToDisplayNameMap.put("vfree1", new String[] {
            "自由辅助属性1"
        });
        this.nameToDisplayNameMap.put("vfree2", new String[] {
            "自由辅助属性2"
        });
        this.nameToDisplayNameMap.put("vfree3", new String[] {
            "自由辅助属性3"
        });
        this.nameToDisplayNameMap.put("vfree4", new String[] {
            "自由辅助属性4"
        });
        this.nameToDisplayNameMap.put("vfree5", new String[] {
            "自由辅助属性5"
        });
        this.nameToDisplayNameMap.put("vfree6", new String[] {
            "自由辅助属性6"
        });
        this.nameToDisplayNameMap.put("vfree7", new String[] {
            "自由辅助属性7"
        });
        this.nameToDisplayNameMap.put("vfree8", new String[] {
            "自由辅助属性8"
        });
        this.nameToDisplayNameMap.put("vfree9", new String[] {
            "自由辅助属性9"
        });
        this.nameToDisplayNameMap.put("vfree10", new String[] {
            "自由辅助属性10"
        });
        this.nameToDisplayNameMap.put("cvendorid", new String[] {
            "供应商"
        });
        this.nameToDisplayNameMap.put("cproductorid", new String[] {
            "生产厂商"
        });
        this.nameToDisplayNameMap.put("cprojectid", new String[] {
            "项目"
        });
        this.nameToDisplayNameMap.put("ccustomerid", new String[] {
            "客户"
        });
        this.nameToDisplayNameMap.put("vdef1", new String[] {
            "自定义项1"
        });
        this.nameToDisplayNameMap.put("vdef2", new String[] {
            "自定义项2"
        });
        this.nameToDisplayNameMap.put("vdef3", new String[] {
            "自定义项3"
        });
        this.nameToDisplayNameMap.put("vdef4", new String[] {
            "自定义项4"
        });
        this.nameToDisplayNameMap.put("vdef5", new String[] {
            "自定义项5"
        });
        this.nameToDisplayNameMap.put("vdef6", new String[] {
            "自定义项6"
        });
        this.nameToDisplayNameMap.put("vdef7", new String[] {
            "自定义项7"
        });
        this.nameToDisplayNameMap.put("vdef8", new String[] {
            "自定义项8"
        });
        this.nameToDisplayNameMap.put("vdef9", new String[] {
            "自定义项9"
        });
        this.nameToDisplayNameMap.put("vdef10", new String[] {
            "自定义项10"
        });
        this.nameToDisplayNameMap.put("vdef11", new String[] {
            "自定义项11"
        });
        this.nameToDisplayNameMap.put("vdef12", new String[] {
            "自定义项12"
        });
        this.nameToDisplayNameMap.put("vdef13", new String[] {
            "自定义项13"
        });
        this.nameToDisplayNameMap.put("vdef14", new String[] {
            "自定义项14"
        });
        this.nameToDisplayNameMap.put("vdef15", new String[] {
            "自定义项15"
        });
        this.nameToDisplayNameMap.put("vdef16", new String[] {
            "自定义项16"
        });
        this.nameToDisplayNameMap.put("vdef17", new String[] {
            "自定义项17"
        });
        this.nameToDisplayNameMap.put("vdef18", new String[] {
            "自定义项18"
        });
        this.nameToDisplayNameMap.put("vdef19", new String[] {
            "自定义项19"
        });
        this.nameToDisplayNameMap.put("vdef20", new String[] {
            "自定义项20"
        });
    }
}
