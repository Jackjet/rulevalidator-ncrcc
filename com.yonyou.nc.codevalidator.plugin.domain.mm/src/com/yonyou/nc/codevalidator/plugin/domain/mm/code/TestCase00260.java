package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * ��EJB�ĺ�̨ʵ�����⣬�����ĺ�̨��ķ����в��������쳣������ֱ��wrappס�쳣
 * 
 * @since 6.0
 * @version 2013-10-30 ����4:51:42
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "��EJB�ĺ�̨ʵ�����⣬�����ĺ�̨��ķ����в��������쳣������ֱ��wrappס�쳣  ", relatedIssueId = "260", coder = "zhongcha", solution = "�޸�ʹ�����в��������쳣������ֱ��wrappס�쳣��")
public class TestCase00260 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
        return javaResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        Map<String, List<String>> errorClz2Method = new HashMap<String, List<String>>();

        List<String> upmImplClassList = new ArrayList<String>();
        UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
        upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
        // ��ȡ�����upm��ʵ�����б�
        for (UpmResource upmResource : upmResources) {
            UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
            List<UpmComponentVO> pubComponentVoList = upmModuleVo.getPubComponentVoList();
            for (UpmComponentVO upmComponentVO : pubComponentVoList) {
                upmImplClassList.add(upmComponentVO.getImplementationName());
            }
        }

        try {
            for (JavaClassResource javaClassResource : resources) {
                // ȡ��java�ļ�������ȥ����׺.java��
                String fileName = javaClassResource.getJavaCodeClassName();
                // ����java�ļ�������׺ΪImpl����ִ�к���У��
                if (MMValueCheck.isNotEmpty(upmImplClassList) && upmImplClassList.contains(fileName)) {
                    continue;
                }
                ScanRule scanRule = new ScanRule();
                // ɨ���java�ļ��ж��Ƿ�����жϵ���״̬�Ĺ���
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (!scanRule.getErrorMethod().isEmpty()) {
                    errorClz2Method.put(fileName, scanRule.getErrorMethod());
                }
            }
            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(errorClz2Method)) {
                for (String clz : errorClz2Method.keySet()) {
                    errorContxt.append("����Ϊ[ " + clz + " ]�ķ���" + errorClz2Method.get(clz)
                            + "���������쳣������Ӧ��ֱ��wrappס�쳣  ��\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorContxt)) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        errorContxt.toString());
            }
        }
        catch (FileNotFoundException e) {
            Logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * ɨ��java�ļ��Ĺ���
     * 
     * @since 6.0
     * @version 2013-10-30 ����2:02:23
     * @author zhongcha
     */
    private static class ScanRule extends AbstractJavaRule {
        private List<String> errorMethod = new ArrayList<String>();

        public List<String> getErrorMethod() {
            return this.errorMethod;
        }

        @Override
        public Object visit(ASTNameList node, Object data) {
            if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTMethodDeclaration.class))) {
                ASTMethodDeclarator methodDeclarator =
                        node.getFirstParentOfType(ASTMethodDeclaration.class).getFirstChildOfType(
                                ASTMethodDeclarator.class);
                if (MMValueCheck.isNotEmpty(methodDeclarator) && MMValueCheck.isNotEmpty(methodDeclarator.getImage())) {
                    // ��¼���쳣�ķ���
                    this.errorMethod.add(methodDeclarator.getImage().trim());
                }
            }

            return super.visit(node, data);
        }
    }

}
