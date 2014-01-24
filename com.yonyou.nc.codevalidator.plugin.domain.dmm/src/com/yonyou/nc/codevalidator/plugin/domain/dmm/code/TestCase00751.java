package com.yonyou.nc.codevalidator.plugin.domain.dmm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimitiveType;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTRelationalExpression;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, coder = "songkj", description = "�����Ƚ�ʱ��������MMNumberUtil�ķ��������Լ��ٿ�ָ���쳣", solution = "���齫NCԴ����������ʹ�������Ƚ�������ĵط��滻Ϊ���������NC�ؼ�", relatedIssueId = "751", subCatalog = SubCatalogEnum.JC_CODECRITERION, repairLevel = RepairLevel.SUGGESTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00751 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        return javaResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        for (JavaClassResource resource : resources) {
            RuleContext ruleContext;
            try {
                ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(), new CompareCheckRule());
                List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
                String errorDetail = net.sourceforge.pmd.SourceCodeProcessor.generateErrorDetail(ruleViolationList);
                if (errorDetail != null && errorDetail.trim().length() > 0) {
                    result.addResultElement(resource.getResourcePath(), errorDetail);
                }
            }
            catch (FileNotFoundException e) {
                throw new ResourceParserException(e);
            }
        }
        return result;
    }

    public static class CompareCheckRule extends AbstractJavaRule {

        private boolean findMethod = false;

        private boolean findClass = false;

        private List<String> localDoubleParams = new ArrayList<String>();// �����洢�ֲ���double��Double����

        private List<String> globalDoubelParams = new ArrayList<String>();// �����洢ȫ�ֵ�double��Double����

        @Override
        public Object visit(ASTRelationalExpression node, Object data) {
            RuleContext ruleContext = (RuleContext) data;
            // List<ASTLiteral> calculateNames = node.findDescendantsOfType(ASTLiteral.class);
            List<ASTPrimarySuffix> primarySuffixList = node.findDescendantsOfType(ASTPrimarySuffix.class);
            List<ASTName> nameList = node.findDescendantsOfType(ASTName.class);
            if (!primarySuffixList.isEmpty()) {
                for (ASTPrimarySuffix primarySuffix : primarySuffixList) {
                    String calculateParamName = primarySuffix.getImage();
                    if (null != calculateParamName) {
                        if (calculateParamName.contains(".doubleValue")) {
                            calculateParamName =
                                    calculateParamName.substring(0, calculateParamName.indexOf(".doubleValue"));
                        }
                        if (this.localDoubleParams.contains(calculateParamName)) {
                            String detailMessage = String.format("���ʽ��double���ͱ�����%s!����Ƚ�����", primarySuffix.getImage());
                            ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
                        }
                        if (this.globalDoubelParams.contains(calculateParamName)) {
                            String detailMessage = String.format("���ʽ��double���ͱ�����%s!����Ƚ�����", primarySuffix.getImage());
                            ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
                        }
                    }
                }
            }
            if (!nameList.isEmpty()) {
                for (ASTName name : nameList) {
                    String calculateParamName = name.getImage();
                    if (null != calculateParamName) {
                        if (calculateParamName.contains(".doubleValue")) {
                            calculateParamName =
                                    calculateParamName.substring(0, calculateParamName.indexOf(".doubleValue"));
                        }
                        if (this.localDoubleParams.contains(calculateParamName)) {
                            String detailMessage = String.format("���ʽ��double���ͱ�����%s!����Ƚ�����", name.getImage());
                            ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
                        }
                        if (this.globalDoubelParams.contains(calculateParamName)) {
                            String detailMessage = String.format("���ʽ��double���ͱ�����%s!����Ƚ�����", name.getImage());
                            ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
                        }
                    }
                }
            }

            // if (astLiteralList.size() == 1) {
            // String image = astLiteralList.get(0).getImage();
            // // ����һ���Ƚ϶���Ϊdouble����
            // if (image.contains(".")) {
            // ruleContext.addRuleViolation(ruleContext, this, node, "�����Ƚ�ʱ��������MMNumberUtil�еĵķ���,�Ӷ����ٿ���Ǳ�ڵĿ�ָ���쳣",
            // null);
            // }
            // }
            // // �����Ƚ϶����Ǳ��ʽ
            // else if (astLiteralList.size() == 0) {
            // ASTForStatement firstParentOfType = node.getFirstParentOfType(ASTForStatement.class);
            // // forѭ��������
            // if (firstParentOfType == null) {
            // ruleContext.addRuleViolation(ruleContext, this, node, "�����Ƚ�ʱ��������MMNumberUtil�еĵķ���,�Ӷ����ٿ���Ǳ�ڵĿ�ָ���쳣",
            // null);
            // }
            // }
            return data;
        }

        /**
         * �ڷ�����Ѱ��������ʽ���ѷ����е�double��Double��������list��
         */
        @Override
        public Object visit(ASTMethodDeclaration node, Object data) {// ��ⷽ�������к���double����
            List<ASTRelationalExpression> allRelationExpressionList =
                    node.findDescendantsOfType(ASTRelationalExpression.class);
            if (!allRelationExpressionList.isEmpty()) {
                this.initMethod();
                List<ASTFormalParameter> methodParameters = node.findDescendantsOfType(ASTFormalParameter.class);
                for (ASTFormalParameter astFormalParameter : methodParameters) {
                    Node jjtGetChild = astFormalParameter.jjtGetChild(0);
                    ASTType astType = null;
                    if (jjtGetChild instanceof ASTType) {
                        astType = (ASTType) jjtGetChild;
                    }
                    if (null != astType) {
                        if (astType.jjtGetChild(0) instanceof ASTPrimitiveType) {
                            ASTPrimitiveType primitiveType = (ASTPrimitiveType) astType.jjtGetChild(0);
                            if ("double".equals(primitiveType.getImage())) {
                                List<ASTVariableDeclaratorId> variableIds =
                                        astFormalParameter.findDescendantsOfType(ASTVariableDeclaratorId.class);
                                for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
                                    this.localDoubleParams.add(astVariableDeclaratorId.getImage());
                                }
                            }
                        }
                        if (astType.jjtGetChild(0) instanceof ASTReferenceType) {
                            List<ASTClassOrInterfaceType> astClassOrInterfaceTypes =
                                    astType.findDescendantsOfType(ASTClassOrInterfaceType.class);
                            if (!astClassOrInterfaceTypes.isEmpty()) {
                                for (ASTClassOrInterfaceType classOrInterfaceType : astClassOrInterfaceTypes) {
                                    if ("Double".equals(classOrInterfaceType.getImage())) {
                                        List<ASTVariableDeclaratorId> variableIds =
                                                astFormalParameter.findDescendantsOfType(ASTVariableDeclaratorId.class);
                                        for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
                                            this.localDoubleParams.add(astVariableDeclaratorId.getImage());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Object result = super.visit(node, data);
                this.cleanMethod();
                return result;
            }
            return data;
        }

        /**
         * ���ֲ���double��Double��������localDoubleParams��
         */
        @Override
        public Object visit(ASTLocalVariableDeclaration node, Object data) {// ��ⷽ���ж���ֲ���double����
            if (this.findMethod) {
                Node jjtGetChild = node.jjtGetChild(0);
                ASTType astType = null;
                if (jjtGetChild instanceof ASTType) {
                    astType = (ASTType) jjtGetChild;
                }
                if (null != astType) {
                    if (astType.jjtGetChild(0) instanceof ASTPrimitiveType) {
                        ASTPrimitiveType primitiveType = (ASTPrimitiveType) astType.jjtGetChild(0);
                        if ("double".equals(primitiveType.getImage())) {
                            List<ASTVariableDeclaratorId> findDescendantsOfType =
                                    node.findDescendantsOfType(ASTVariableDeclaratorId.class);
                            if (!findDescendantsOfType.isEmpty()) {
                                for (ASTVariableDeclaratorId astVariableDeclaratorId : findDescendantsOfType) {
                                    this.localDoubleParams.add(astVariableDeclaratorId.getImage());
                                }
                            }
                        }
                    }
                    if (astType.jjtGetChild(0) instanceof ASTReferenceType) {
                        List<ASTClassOrInterfaceType> astClassOrInterfaceTypes =
                                astType.findDescendantsOfType(ASTClassOrInterfaceType.class);
                        if (!astClassOrInterfaceTypes.isEmpty()) {
                            for (ASTClassOrInterfaceType classOrInterfaceType : astClassOrInterfaceTypes) {
                                if ("Double".equals(classOrInterfaceType.getImage())) {
                                    List<ASTVariableDeclaratorId> variableIds =
                                            node.findDescendantsOfType(ASTVariableDeclaratorId.class);
                                    for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
                                        this.localDoubleParams.add(astVariableDeclaratorId.getImage());
                                    }
                                }
                            }
                        }
                    }
                }
                return super.visit(node, data);
            }
            return data;
        }

        /**
         * ��ȫ�ֵ�double��Double����globalDoubelParams��
         */
        @Override
        public Object visit(ASTFieldDeclaration node, Object data) {// ���double���͵ĳ�Ա����
            if (this.findClass) {
                List<ASTPrimitiveType> findPrimitiveTypes = node.findDescendantsOfType(ASTPrimitiveType.class);
                List<ASTClassOrInterfaceType> findReferenceTypes =
                        node.findDescendantsOfType(ASTClassOrInterfaceType.class);
                if (!findPrimitiveTypes.isEmpty()) {
                    for (ASTPrimitiveType astPrimitiveType : findPrimitiveTypes) {
                        if ("double".equals(astPrimitiveType.getImage())) {
                            List<ASTVariableDeclaratorId> findDescendantsOfType =
                                    node.findDescendantsOfType(ASTVariableDeclaratorId.class);
                            if (!findDescendantsOfType.isEmpty()) {
                                for (ASTVariableDeclaratorId astVariableDeclaratorId : findDescendantsOfType) {
                                    this.globalDoubelParams.add(astVariableDeclaratorId.getImage());
                                }
                            }
                        }
                    }
                    return super.visit(node, data);
                }
                if (!findReferenceTypes.isEmpty()) {
                    for (ASTClassOrInterfaceType astClassOrInterfaceType : findReferenceTypes) {
                        if ("Double".equals(astClassOrInterfaceType.getImage())) {
                            List<ASTVariableDeclaratorId> findDescendantsOfType =
                                    node.findDescendantsOfType(ASTVariableDeclaratorId.class);
                            if (!findDescendantsOfType.isEmpty()) {
                                for (ASTVariableDeclaratorId astVariableDeclaratorId : findDescendantsOfType) {
                                    this.globalDoubelParams.add(astVariableDeclaratorId.getImage());
                                }
                            }
                        }
                    }
                    return super.visit(node, data);
                }
            }
            return data;
        }

        @Override
        public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {// ��ʼ������
            node.hasDescendantOfType(ASTMethodDeclaration.class);
            if (node.hasDescendantOfType(ASTMethodDeclaration.class)) {
                this.initClass();
                Object result = super.visit(node, data);
                this.cleanClass();
                return result;
            }
            return data;
        }

        private void initMethod() {// �������ʳ�ʼ��
            this.findMethod = true;
        }

        private void initClass() {// ����ʳ�ʼ��
            this.findClass = true;
        }

        private void cleanMethod() {// ���ʷ��������������
            this.findMethod = false;
            this.localDoubleParams.clear();
        }

        private void cleanClass() {// ����������������
            this.findClass = false;
            this.globalDoubelParams.clear();
        }
    }

}
