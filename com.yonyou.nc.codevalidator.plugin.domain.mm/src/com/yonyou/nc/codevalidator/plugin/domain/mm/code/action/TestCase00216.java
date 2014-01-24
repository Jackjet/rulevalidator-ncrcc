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
 * 单据复制时必须设置默认值，并把单据状态置成自由态
 * 
 * @since 6.0
 * @version 2013-11-5 上午9:07:20
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "单据复制时必须设置默认值，并把单据状态置成自由态 ；  ", relatedIssueId = "216", coder = "zhongcha", solution = "请在该复制按钮处理类中把单据状态置成自由态。")
public class TestCase00216 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // 用于存放错误信息
        MapList<String, String> errorMapList = new MapList<String, String>();
        // key为元数据文件中读取的聚合vo，value为根据元数据实现的接口获取的复制过程中需处理的字段
        MapList<String, IAttribute> aggvo2Attributes = new MapList<String, IAttribute>();

        for (MetadataResource metadataResource : resources) {
            IMetadataFile metaDataFile = metadataResource.getMetadataFile();
            IEntity mainEntity = metaDataFile.getMainEntity();
            if (MMValueCheck.isNotEmpty(mainEntity)) {
                // 元数据文件中读取的聚合vo
                String aggvoName = mainEntity.getAccessor().getAccessorWrapperClassName();
                if (MMValueCheck.isNotEmpty(aggvoName)) {
                    Map<String, IAttribute> flowbizMap =
                            mainEntity.getBusiInterfaceAttributes(MDRuleConstants.FLOWBIZ_INTERFACE_NAME);
                    if (MMValueCheck.isNotEmpty(flowbizMap)) {
                        aggvo2Attributes.put(aggvoName, flowbizMap.get("审批状态"));
                    }
                }
            }
        }
        MapList<String, String> aggvo2fieldNames = this.getAggvo2FieldNames(aggvo2Attributes);
        List<JavaClassResource> javaClzResources = this.getClientJavaClassResource(ruleExecContext);
        boolean isExistClz = false;
        for (JavaClassResource javaClassResource : javaClzResources) {
            // 取得java文件的名（去掉后缀.java）
            String fileName =
                    javaClassResource.getResourceFileName().substring(0,
                            javaClassResource.getResourceFileName().length() - 5);
            if (!fileName.endsWith("CopyActionProcessor")) {
                continue;
            }
            ScanRule scanRule = new ScanRule(aggvo2fieldNames);
            // 扫描该java文件判定是否存在判断单据状态的规则
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
            errorContxt.append("该组件中不存在以CopyActionProcessor为后缀的复制按钮处理类。");
        }
        if (MMValueCheck.isNotEmpty(errorMapList)) {
            for (String key : errorMapList.keySet()) {
                errorContxt.append("在复制按钮处理类[ " + key + " ]中的单据状态(即字段名" + errorMapList.get(key) + "）没有设为自由态）。\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorContxt)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    errorContxt.toString());
        }

        return result;
    }

    /**
     * 获取前台.java的资源文件
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
     * 得到key为aggvo,value为需清空的字段名称（即在元数据实现的接口中做了映射）
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
     * 扫描java文件的规则（假定用户不会重写java.lang包中的类）
     * 根据该被扫描类实现的接口中的AggVO的类型确定该类中需要的做置空操作的字段
     * 然后扫描过程中判断是否有做置空操作，以下两种情况都认为是正确的：
     * （1）例如：headvo.setPk_wr(null);headvo.setCreator(null);
     * 该规则中判断存在.setPk_wr(null)和.setCreator(null)就是正确的。
     * （2）例如： headvo.setPrimaryKey(null); headvo.setAttributeValue(WrVO.CREATOR, null);
     * 该规则中判断存在.setPrimaryKey(null)和.setAttributeValue(.CREATOR, null)就是正确的。
     * 
     * @since 6.0
     * @version 2013-11-7 上午9:50:32
     * @author zhongcha
     */
    private static class ScanRule extends AbstractJavaRule {
        private String currentPackageName;

        private Map<String, String> importClassMap = new HashMap<String, String>();

        public ScanRule(MapList<String, String> aggvo2fieldNames) {
            this.aggvo2fieldNames = aggvo2fieldNames;
        }

        /**
         * 聚合vo对应需清空的字段的MapList
         */
        MapList<String, String> aggvo2fieldNames;

        /**
         * 用于情况（1）的判断：如.setPk_wr-----pk_wr
         */
        private Map<String, String> fieldMethod2Name = new HashMap<String, String>();

        /**
         * 用于情况（2）的判断：如.CREATOR------creator(其中主键特殊：.setPrimaryKey-----pk_wr()）
         */
        private Map<String, String> fieldUpCase2DownCase = new HashMap<String, String>();

        /**
         * 记录结果，value=2代表正确（做了置空处理），value等于1代表有做处理但不是置空处理，value=0代表不正确（即不存在）
         */
        private Map<String, Integer> fieldName2Result = new HashMap<String, Integer>();

        public Map<String, Integer> getFieldName2Result() {
            return this.fieldName2Result;
        }

        /**
         * 记录当前类是否实现了接口nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor
         * 其值为false时，其他判定不能继续
         */
        private boolean isCorrectClz = false;

        /**
         * 根据字段构造情况（1）中所需的方法名
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
         * 根据字段构造情况（2）中所需的字段名
         * 
         * @param fieldName
         * @return
         */
        private String getFieldUpCase(String fieldName) {
            String fieldMethod = "." + fieldName.toUpperCase();
            return fieldMethod;
        }

        /**
         * 获得被扫描类的package的值
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
         * 得到被扫描类引入的类
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
         * 判断被扫描的类是否实现了接口nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor
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
         * 判断该被扫描的类中是否调用了指定的方法，调用了，根据情况处理相应的值
         */
        @Override
        public Object visit(ASTName node, Object data) {
            if (MMValueCheck.isNotEmpty(this.fieldMethod2Name) && MMValueCheck.isNotEmpty(node.getImage())) {
                String image = node.getImage().trim();
                for (String fieldMethod : this.fieldMethod2Name.keySet()) {
                    if (image.endsWith(fieldMethod)) {
                        // 能到这里说明有处理该字段，所以将结果值置为1
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
                        // 获得含有.setAttributeValue方法的整个表达式
                        ASTPrimaryExpression priExpression =
                                node.getFirstParentOfType(ASTPrimaryPrefix.class).getFirstParentOfType(
                                        ASTPrimaryExpression.class);
                        // 若方法存在参数，则判断第一个参数是否与要检查的字段对应，第二个参数是否为空
                        if (priExpression.hasDescendantOfType(ASTArguments.class)) {
                            // 获得方法参数
                            List<ASTExpression> expressions =
                                    priExpression.findDescendantsOfType(ASTArguments.class).get(0)
                                            .findDescendantsOfType(ASTExpression.class);
                            if (expressions.size() >= 2 && expressions.get(0).hasDescendantOfType(ASTName.class)) {
                                // 取得第一个参数
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
         * 根据该被扫描类实现的接口中的AggVo的类型确定该类中需要的做置空操作的方法
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
