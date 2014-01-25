package com.yonyou.nc.codevalidator.plugin.domain.mm.code.action;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ���ݸ���ʱ��������Ĭ��ֵ�����ѵ���״̬�ó�����̬
 * 
 * @since 6.0
 * @version 2013-11-5 ����9:07:20
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "���ݸ���ʱ��������Ĭ��ֵ�����ѵ���״̬�ó�����̬ ��  ", relatedIssueId = "216", coder = "zhongcha", solution = "���ڸø��ư�ť�������аѵ���״̬�ó�����̬��")
public class TestCase00216 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // ���ڴ�Ŵ�����Ϣ
        MapList<String, String> errorMapList = new MapList<String, String>();
        // keyΪԪ�����ļ��ж�ȡ�ľۺ�vo��valueΪ����Ԫ����ʵ�ֵĽӿڻ�ȡ�ĸ��ƹ������账����ֶ�
        MapList<String, IAttribute> aggvo2Attributes = new MapList<String, IAttribute>();

        for (MetadataResource metadataResource : resources) {
            IMetadataFile metaDataFile = metadataResource.getMetadataFile();
            IEntity mainEntity = metaDataFile.getMainEntity();
            if (MMValueCheck.isNotEmpty(mainEntity)) {
                // Ԫ�����ļ��ж�ȡ�ľۺ�vo
                String aggvoName = mainEntity.getAccessor().getAccessorWrapperClassName();
                if (MMValueCheck.isNotEmpty(aggvoName)) {
                    Map<String, IAttribute> flowbizMap =
                            mainEntity.getBusiInterfaceAttributes(MDRuleConstants.FLOWBIZ_INTERFACE_NAME);
                    if (MMValueCheck.isNotEmpty(flowbizMap)) {
                        aggvo2Attributes.put(aggvoName, flowbizMap.get("����״̬"));
                    }
                }
            }
        }
        MapList<String, String> aggvo2fieldNames = this.getAggvo2FieldNames(aggvo2Attributes);
        List<JavaClassResource> javaClzResources = this.getClientJavaClassResource(ruleExecContext);
        boolean isExistClz = false;
        for (JavaClassResource javaClassResource : javaClzResources) {
            // ȡ��java�ļ�������ȥ����׺.java��
            String fileName =
                    javaClassResource.getResourceFileName().substring(0,
                            javaClassResource.getResourceFileName().length() - 5);
            if (!fileName.endsWith("CopyActionProcessor")) {
                continue;
            }
            ScanRule scanRule = new ScanRule(aggvo2fieldNames);
            // ɨ���java�ļ��ж��Ƿ�����жϵ���״̬�Ĺ���
            try {
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (scanRule.isCorrectClz && !isExistClz) {
                    isExistClz = true;
                }
                if (MMValueCheck.isNotEmpty(scanRule.getFieldName2Result())) {
                    for (String key : scanRule.getFieldName2Result().keySet()) {
                        if (!scanRule.getFieldName2Result().get(key).equals(Integer.valueOf(1))) {
                            errorMapList.put(javaClassResource.getJavaCodeClassName(), key);
                        }
                    }
                }
            }
            catch (FileNotFoundException e) {
                Logger.error(e.getMessage(), e);
            }
        }
        StringBuilder errorContxt = new StringBuilder();
        if (!isExistClz) {
            errorContxt.append("������в�������CopyActionProcessorΪ��׺�ĸ��ư�ť�����ࡣ");
        }
        if (MMValueCheck.isNotEmpty(errorMapList)) {
            for (String key : errorMapList.keySet()) {
                errorContxt.append("�ڸ��ư�ť������[ " + key + " ]�еĵ���״̬(���ֶ���" + errorMapList.get(key) + "��û����Ϊ����̬����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorContxt)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    errorContxt.toString());
        }

        return result;
    }

    /**
     * ��ȡǰ̨.java����Դ�ļ�
     * 
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private List<JavaClassResource> getClientJavaClassResource(IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
        return ResourceManagerFacade.getResource(javaResourceQuery);

    }

    /**
     * �õ�keyΪaggvo,valueΪ����յ��ֶ����ƣ�����Ԫ����ʵ�ֵĽӿ�������ӳ�䣩
     * 
     * @param aggvo2Attributes
     * @return
     */
    private MapList<String, String> getAggvo2FieldNames(MapList<String, IAttribute> aggvo2Attributes) {
        MapList<String, String> aggvo2fieldNames = new MapList<String, String>();
        for (String key : aggvo2Attributes.keySet()) {
            for (IAttribute attribute : aggvo2Attributes.get(key)) {
                if (MMValueCheck.isNotEmpty(attribute)) {
                    aggvo2fieldNames.put(key, attribute.getFieldName());
                }
            }
        }
        return aggvo2fieldNames;
    }

    /**
     * ɨ��java�ļ��Ĺ��򣨼ٶ��û�������дjava.lang���е��ࣩ
     * ���ݸñ�ɨ����ʵ�ֵĽӿ��е�AggVO������ȷ����������Ҫ�����ÿղ������ֶ�
     * Ȼ��ɨ��������ж��Ƿ������ÿղ��������������������Ϊ����ȷ�ģ�
     * ��1�����磺headvo.setPk_wr(null);headvo.setCreator(null);
     * �ù������жϴ���.setPk_wr(null)��.setCreator(null)������ȷ�ġ�
     * ��2�����磺 headvo.setPrimaryKey(null); headvo.setAttributeValue(WrVO.CREATOR, null);
     * �ù������жϴ���.setPrimaryKey(null)��.setAttributeValue(.CREATOR, null)������ȷ�ġ�
     * 
     * @since 6.0
     * @version 2013-11-7 ����9:50:32
     * @author zhongcha
     */
    private static class ScanRule extends AbstractJavaRule {
        private String currentPackageName;

        private Map<String, String> importClassMap = new HashMap<String, String>();

        public ScanRule(MapList<String, String> aggvo2fieldNames) {
            this.aggvo2fieldNames = aggvo2fieldNames;
        }

        /**
         * �ۺ�vo��Ӧ����յ��ֶε�MapList
         */
        MapList<String, String> aggvo2fieldNames;

        /**
         * ���������1�����жϣ���.setPk_wr-----pk_wr
         */
        private Map<String, String> fieldMethod2Name = new HashMap<String, String>();

        /**
         * ���������2�����жϣ���.CREATOR------creator(�����������⣺.setPrimaryKey-----pk_wr()��
         */
        private Map<String, String> fieldUpCase2DownCase = new HashMap<String, String>();

        /**
         * ��¼�����value=2������ȷ�������ÿմ�����value����1�����������������ÿմ���value=0������ȷ���������ڣ�
         */
        private Map<String, Integer> fieldName2Result = new HashMap<String, Integer>();

        public Map<String, Integer> getFieldName2Result() {
            return this.fieldName2Result;
        }

        /**
         * ��¼��ǰ���Ƿ�ʵ���˽ӿ�nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor
         * ��ֵΪfalseʱ�������ж����ܼ���
         */
        private boolean isCorrectClz = false;

        /**
         * �����ֶι��������1��������ķ�����
         * 
         * @param fieldName
         * @return
         */
        private String getFieldMethod(String fieldName) {
            char firstChar = fieldName.toUpperCase().charAt(0);
            String fieldMethod = ".set" + firstChar + fieldName.substring(1);
            return fieldMethod;
        }

        /**
         * �����ֶι��������2����������ֶ���
         * 
         * @param fieldName
         * @return
         */
        private String getFieldUpCase(String fieldName) {
            String fieldMethod = "." + fieldName.toUpperCase();
            return fieldMethod;
        }

        /**
         * ��ñ�ɨ�����package��ֵ
         */
        @Override
        public Object visit(ASTPackageDeclaration node, Object data) {
            List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
            if (astNames.size() == 1) {
                ASTName astName = astNames.get(0);
                this.currentPackageName = astName.getImage();
            }
            else {
                RuleContext ruleContext = (RuleContext) data;
                ruleContext.addRuleViolation(ruleContext, this, node, "", null);
                return data;
            }
            return super.visit(node, data);
        }

        /**
         * �õ���ɨ�����������
         */
        @Override
        public Object visit(ASTImportDeclaration node, Object data) {
            List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
            if (!astNames.isEmpty()) {
                for (ASTName astName : astNames) {
                    String importClassName = astName.getImage();
                    this.importClassMap.put(importClassName.substring(importClassName.lastIndexOf(".") + 1),
                            importClassName);
                }
            }
            return super.visit(node, data);
        }

        /**
         * �жϱ�ɨ������Ƿ�ʵ���˽ӿ�nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor
         */
        @Override
        public Object visit(ASTImplementsList node, Object data) {
            List<ASTClassOrInterfaceType> interfaceList = node.findChildrenOfType(ASTClassOrInterfaceType.class);
            List<String> interfaceNames = new ArrayList<String>();
            for (ASTClassOrInterfaceType element : interfaceList) {
                String image = element.getImage().trim();
                if (!image.contains(".")) {
                    image =
                            this.importClassMap.containsKey(image) ? this.importClassMap.get(image) : String.format(
                                    "%s.%s", this.currentPackageName, image);
                }
                interfaceNames.add(image);
            }

            if (interfaceNames.contains("nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor")) {
                this.isCorrectClz = true;
            }
            return super.visit(node, data);
        }

        /**
         * �жϸñ�ɨ��������Ƿ������ָ���ķ����������ˣ��������������Ӧ��ֵ
         */
        @Override
        public Object visit(ASTName node, Object data) {
            if (MMValueCheck.isNotEmpty(this.fieldMethod2Name) && MMValueCheck.isNotEmpty(node.getImage())) {
                String image = node.getImage().trim();
                for (String fieldMethod : this.fieldMethod2Name.keySet()) {
                    if (image.endsWith(fieldMethod)) {
                        // �ܵ�����˵���д�����ֶΣ����Խ����ֵ��Ϊ1
                        this.fieldName2Result.put(this.fieldMethod2Name.get(fieldMethod), Integer.valueOf(1));
                    }
                }
            }

            if (MMValueCheck.isNotEmpty(this.fieldUpCase2DownCase) && MMValueCheck.isNotEmpty(node.getImage())) {
                String image = node.getImage().trim();
                if (image.endsWith(".setAttributeValue")) {
                    if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class))
                            && MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class)
                                    .getFirstParentOfType(ASTPrimaryExpression.class))) {
                        // ��ú���.setAttributeValue�������������ʽ
                        ASTPrimaryExpression priExpression =
                                node.getFirstParentOfType(ASTPrimaryPrefix.class).getFirstParentOfType(
                                        ASTPrimaryExpression.class);
                        // ���������ڲ��������жϵ�һ�������Ƿ���Ҫ�����ֶζ�Ӧ���ڶ��������Ƿ�Ϊ��
                        if (priExpression.hasDescendantOfType(ASTArguments.class)) {
                            // ��÷�������
                            List<ASTExpression> expressions =
                                    priExpression.findDescendantsOfType(ASTArguments.class).get(0)
                                            .findDescendantsOfType(ASTExpression.class);
                            if (expressions.size() >= 2 && expressions.get(0).hasDescendantOfType(ASTName.class)) {
                                // ȡ�õ�һ������
                                String itemname =
                                        expressions.get(0).findDescendantsOfType(ASTName.class).get(0).getImage();
                                for (String fieldUpCase : this.fieldUpCase2DownCase.keySet()) {
                                    if (itemname.endsWith(fieldUpCase)) {
                                        this.fieldName2Result.put(this.fieldUpCase2DownCase.get(fieldUpCase),
                                                Integer.valueOf(1));
                                        break;
                                    }
                                }
                            }

                        }
                    }
                }
            }
            return super.visit(node, data);
        }

        /**
         * ���ݸñ�ɨ����ʵ�ֵĽӿ��е�AggVo������ȷ����������Ҫ�����ÿղ����ķ���
         */
        @Override
        public Object visit(ASTClassOrInterfaceType node, Object data) {
            if (this.isCorrectClz && MMValueCheck.isEmpty(this.fieldMethod2Name)) {
                String image = node.getImage().trim();
                if (!image.contains(".")) {
                    image =
                            this.importClassMap.containsKey(image) ? this.importClassMap.get(image) : String.format(
                                    "%s.%s", this.currentPackageName, image);
                }
                for (String key : this.aggvo2fieldNames.keySet()) {
                    if (key.equals(image)) {
                        for (String fieldName : this.aggvo2fieldNames.get(key)) {
                            this.fieldUpCase2DownCase.put(this.getFieldUpCase(fieldName), fieldName);
                            this.fieldMethod2Name.put(this.getFieldMethod(fieldName), fieldName);
                            this.fieldName2Result.put(fieldName, Integer.valueOf(0));
                        }

                    }
                }
            }

            return super.visit(node, data);
        }
    }

}
