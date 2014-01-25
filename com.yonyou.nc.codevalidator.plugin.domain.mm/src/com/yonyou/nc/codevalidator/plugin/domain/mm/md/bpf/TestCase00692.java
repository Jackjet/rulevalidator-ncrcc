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
 * 需要记录业务日志bpf文件中，接口必须存在于对应的aop文件，并且对应接口在代码中存在
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.METADATA, subCatalog = SubCatalogEnum.MD_BASESETTING, description = "需要记录业务日志bpf文件中，接口必须存在于对应的aop文件，并且对应接口在代码中存在", solution = "", coder = "qiaoyang", specialParamDefine = "", relatedIssueId = "692")
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
                    noteBuilder.append("未定义aop文件\n");
                    continue;
                }

                IMetaProcessFile metadataFile = resource.getMetaProcessFile();
                List<IOpInterface> interfaces = metadataFile.getOpInterfaces();
                if (interfaces != null && interfaces.size() > 0) {
                    List<String> interfClassNames = new ArrayList<String>();
                    for (IOpInterface interf : interfaces) {
                        // 得到接口名
                        String interfName = interf.getName();
                        // 接口类名
                        String interfClassName = interf.getInterfaceName();
                        interfClassNames.add(interfClassName);
                        // 解析aop文件，判断是否存在此接口，并且接口是否在代码中存在
                        List<AopAspectVO> aopAspectVOs = aopModuleVO.getAopAspectVoList();
                        if (aopAspectVOs == null) {
                            noteBuilder.append(interfName + "未在aop文件中定义\n");
                        }
                        boolean contInAop = false;
                        for (AopAspectVO aopAspectVO : aopAspectVOs) {
                            if (interfClassName.equals(aopAspectVO.getComponentInterface())) {
                                contInAop = true;
                                break;
                            }
                        }
                        if (!contInAop) {
                            noteBuilder.append(interfName + "未在aop文件中定义\n");
                        }
                    }
                    IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
                    for (String interfClassName : interfClassNames) {
                        try {
                            classLoaderUtils.loadClass(ruleExecContext.getBusinessComponent().getProjectName(),
                                    interfClassName);
                        }
                        catch (RuleClassLoadException e) {
                            noteBuilder.append("代码中为找到接口：" + interfClassName + "\n");
                        }
                    }
                }
                else {
                    noteBuilder.append("未定义接口\n");
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
