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

@RuleDefinition(catalog = CatalogEnum.METADATA, coder = "songkj", description = "��������ͬ�����ֶΣ���ͬҵ���������", relatedIssueId = "756", subCatalog = SubCatalogEnum.MD_FUNCCHECK, repairLevel = RepairLevel.MUSTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
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
                            result.addResultElement(metadataResource.getResourcePath(), "���齫" + entity.getFullName()
                                    + "ʵ����" + name + "����ʾ������\"" + currentDisplayName + "\"��Ϊ\"" + displayNameArray[0]
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
            "����", "��������"
        });
        this.nameToDisplayNameMap.put("cmaterialid", new String[] {
            "����", "��������", "��������"
        });
        this.nameToDisplayNameMap.put("cmaterialvid", new String[] {
            "���ϰ汾", "�������ϰ汾", "�������ϰ汾"
        });
        this.nameToDisplayNameMap.put("fbillstatus", new String[] {
            "����״̬"
        });
        this.nameToDisplayNameMap.put("vbatchcode", new String[] {
            "�������κ�"
        });
        this.nameToDisplayNameMap.put("vrowno", new String[] {
            "�к�", "���", "�����к�", "�����к�"
        });
        this.nameToDisplayNameMap.put("nnum", new String[] {
            "������"
        });
        this.nameToDisplayNameMap.put("nastnum", new String[] {
            "����"
        });
        this.nameToDisplayNameMap.put("cunitid", new String[] {
            "����λ", "��������λ", "��������λ"
        });
        this.nameToDisplayNameMap.put("castunitid", new String[] {
            "��λ", "���λ", "���λ"
        });
        this.nameToDisplayNameMap.put("cmeasureid", new String[] {
            "����λ", "��������λ", "��������λ"
        });
        this.nameToDisplayNameMap.put("cassmeasureid", new String[] {
            "��λ", "���λ", "���λ"
        });
        this.nameToDisplayNameMap.put("vchangerate", new String[] {
            "������", "�������", "�������"
        });
        this.nameToDisplayNameMap.put("vnote", new String[] {
            "��ע", "���ע", "���ע"
        });
        this.nameToDisplayNameMap.put("vfirsttype", new String[] {
            "Դͷ��������"
        });
        this.nameToDisplayNameMap.put("vfirsttrantypeid", new String[] {
            "Դͷ��������"
        });
        this.nameToDisplayNameMap.put("vfirsttrantypecode", new String[] {
            "Դͷ�������ͱ���"
        });
        this.nameToDisplayNameMap.put("vfirstid", new String[] {
            "Դͷ����ID"
        });
        this.nameToDisplayNameMap.put("vfirstcode", new String[] {
            "Դͷ���ݱ���"
        });
        this.nameToDisplayNameMap.put("vfirstbid", new String[] {
            "Դͷ������ID"
        });
        this.nameToDisplayNameMap.put("vfirstrowno", new String[] {
            "Դͷ�����к�"
        });
        this.nameToDisplayNameMap.put("vsrctype", new String[] {
            "��Դ��������"
        });
        this.nameToDisplayNameMap.put("vsrctrantypeid", new String[] {
            "��Դ��������"
        });
        this.nameToDisplayNameMap.put("vsrctrantypecode", new String[] {
            "��Դ�������ͱ���"
        });
        this.nameToDisplayNameMap.put("vsrcid", new String[] {
            "��Դ����ID"
        });
        this.nameToDisplayNameMap.put("vsrccode", new String[] {
            "��Դ���ݱ���"
        });
        this.nameToDisplayNameMap.put("vsrcbid", new String[] {
            "��Դ������ID"
        });
        this.nameToDisplayNameMap.put("vsrcrowno", new String[] {
            "��Դ�����к�"
        });
        this.nameToDisplayNameMap.put("billmaker", new String[] {
            "�Ƶ���"
        });
        this.nameToDisplayNameMap.put("dmakedate", new String[] {
            "�Ƶ�����"
        });
        this.nameToDisplayNameMap.put("creator", new String[] {
            "������"
        });
        this.nameToDisplayNameMap.put("creationtime", new String[] {
            "����ʱ��"
        });
        this.nameToDisplayNameMap.put("modifier", new String[] {
            "����޸���"
        });
        this.nameToDisplayNameMap.put("modifiedtime", new String[] {
            "����޸�ʱ��"
        });
        this.nameToDisplayNameMap.put("vfree1", new String[] {
            "���ɸ�������1"
        });
        this.nameToDisplayNameMap.put("vfree2", new String[] {
            "���ɸ�������2"
        });
        this.nameToDisplayNameMap.put("vfree3", new String[] {
            "���ɸ�������3"
        });
        this.nameToDisplayNameMap.put("vfree4", new String[] {
            "���ɸ�������4"
        });
        this.nameToDisplayNameMap.put("vfree5", new String[] {
            "���ɸ�������5"
        });
        this.nameToDisplayNameMap.put("vfree6", new String[] {
            "���ɸ�������6"
        });
        this.nameToDisplayNameMap.put("vfree7", new String[] {
            "���ɸ�������7"
        });
        this.nameToDisplayNameMap.put("vfree8", new String[] {
            "���ɸ�������8"
        });
        this.nameToDisplayNameMap.put("vfree9", new String[] {
            "���ɸ�������9"
        });
        this.nameToDisplayNameMap.put("vfree10", new String[] {
            "���ɸ�������10"
        });
        this.nameToDisplayNameMap.put("cvendorid", new String[] {
            "��Ӧ��"
        });
        this.nameToDisplayNameMap.put("cproductorid", new String[] {
            "��������"
        });
        this.nameToDisplayNameMap.put("cprojectid", new String[] {
            "��Ŀ"
        });
        this.nameToDisplayNameMap.put("ccustomerid", new String[] {
            "�ͻ�"
        });
        this.nameToDisplayNameMap.put("vdef1", new String[] {
            "�Զ�����1"
        });
        this.nameToDisplayNameMap.put("vdef2", new String[] {
            "�Զ�����2"
        });
        this.nameToDisplayNameMap.put("vdef3", new String[] {
            "�Զ�����3"
        });
        this.nameToDisplayNameMap.put("vdef4", new String[] {
            "�Զ�����4"
        });
        this.nameToDisplayNameMap.put("vdef5", new String[] {
            "�Զ�����5"
        });
        this.nameToDisplayNameMap.put("vdef6", new String[] {
            "�Զ�����6"
        });
        this.nameToDisplayNameMap.put("vdef7", new String[] {
            "�Զ�����7"
        });
        this.nameToDisplayNameMap.put("vdef8", new String[] {
            "�Զ�����8"
        });
        this.nameToDisplayNameMap.put("vdef9", new String[] {
            "�Զ�����9"
        });
        this.nameToDisplayNameMap.put("vdef10", new String[] {
            "�Զ�����10"
        });
        this.nameToDisplayNameMap.put("vdef11", new String[] {
            "�Զ�����11"
        });
        this.nameToDisplayNameMap.put("vdef12", new String[] {
            "�Զ�����12"
        });
        this.nameToDisplayNameMap.put("vdef13", new String[] {
            "�Զ�����13"
        });
        this.nameToDisplayNameMap.put("vdef14", new String[] {
            "�Զ�����14"
        });
        this.nameToDisplayNameMap.put("vdef15", new String[] {
            "�Զ�����15"
        });
        this.nameToDisplayNameMap.put("vdef16", new String[] {
            "�Զ�����16"
        });
        this.nameToDisplayNameMap.put("vdef17", new String[] {
            "�Զ�����17"
        });
        this.nameToDisplayNameMap.put("vdef18", new String[] {
            "�Զ�����18"
        });
        this.nameToDisplayNameMap.put("vdef19", new String[] {
            "�Զ�����19"
        });
        this.nameToDisplayNameMap.put("vdef20", new String[] {
            "�Զ�����20"
        });
    }
}
