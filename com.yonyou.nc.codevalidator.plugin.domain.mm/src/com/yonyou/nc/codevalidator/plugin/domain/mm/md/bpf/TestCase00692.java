package com.yonyou.nc.codevalidator.plugin.domain.mm.md.bpf;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.AopResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpf.IOpInterface;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetaProcessResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.AopResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.aop.AopAspectVO;
import com.yonyou.nc.codevalidator.sdk.aop.AopModuleVO;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * ��Ҫ��¼ҵ����־bpf�ļ��У��ӿڱ�������ڶ�Ӧ��aop�ļ������Ҷ�Ӧ�ӿ��ڴ����д���
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.METADATA, subCatalog = SubCatalogEnum.MD_BASESETTING, description = "��Ҫ��¼ҵ����־bpf�ļ��У��ӿڱ�������ڶ�Ӧ��aop�ļ������Ҷ�Ӧ�ӿ��ڴ����д���", solution = "", coder = "qiaoyang", specialParamDefine = "", relatedIssueId = "692")
public class TestCase00692 extends AbstractMetaProcessResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        if (resources != null && resources.size() > 0) {
            for (MetadataResource resource : resources) {
                StringBuilder noteBuilder = new StringBuilder();
                String name = resource.getResourceFileName().split("\\.")[0];
                AopModuleVO aopModuleVO = this.getAopResource(ruleExecContext, name);
                if (aopModuleVO == null) {
                    noteBuilder.append("δ����aop�ļ�\n");
                    continue;
                }

                IMetaProcessFile metadataFile = resource.getMetaProcessFile();
                List<IOpInterface> interfaces = metadataFile.getOpInterfaces();
                if (interfaces != null && interfaces.size() > 0) {
                    List<String> interfClassNames = new ArrayList<String>();
                    for (IOpInterface interf : interfaces) {
                        // �õ��ӿ���
                        String interfName = interf.getName();
                        // �ӿ�����
                        String interfClassName = interf.getInterfaceName();
                        interfClassNames.add(interfClassName);
                        // ����aop�ļ����ж��Ƿ���ڴ˽ӿڣ����ҽӿ��Ƿ��ڴ����д���
                        List<AopAspectVO> aopAspectVOs = aopModuleVO.getAopAspectVoList();
                        if (aopAspectVOs == null) {
                            noteBuilder.append(interfName + "δ��aop�ļ��ж���\n");
                        }
                        boolean contInAop = false;
                        for (AopAspectVO aopAspectVO : aopAspectVOs) {
                            if (interfClassName.equals(aopAspectVO.getComponentInterface())) {
                                contInAop = true;
                                break;
                            }
                        }
                        if (!contInAop) {
                            noteBuilder.append(interfName + "δ��aop�ļ��ж���\n");
                        }
                    }
                    IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
                    for (String interfClassName : interfClassNames) {
                        try {
                            classLoaderUtils.loadClass(ruleExecContext.getBusinessComponent().getProjectName(),
                                    interfClassName);
                        }
                        catch (RuleClassLoadException e) {
                            noteBuilder.append("������Ϊ�ҵ��ӿڣ�" + interfClassName + "\n");
                        }
                    }
                }
                else {
                    noteBuilder.append("δ����ӿ�\n");
                }
                if (noteBuilder.length() > 0) {
                    result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
                }
            }
        }
        return result;
    }

    private AopModuleVO getAopResource(IRuleExecuteContext ruleExecContext, String name) throws RuleBaseException {
        AopResourceQuery query = new AopResourceQuery();
        query.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<AopResource> aopResources = ResourceManagerFacade.getResource(query);
        if (aopResources == null) {
            return null;
        }
        AopModuleVO aopModuleVO = null;
        for (AopResource aopResource : aopResources) {
            String aopname = aopResource.getResourceFileName().split(".")[0];
            if (name.equals(aopname)) {
                aopModuleVO = aopResource.getAopModuleVo();
                break;
            }
        }
        return aopModuleVO;
    }

}
