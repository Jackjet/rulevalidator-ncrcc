package com.yonyou.nc.codevalidator.plugin.domain.mm.code.action;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNullLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IEntityTargetConnection;
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
 * 单据复制时必须清除掉子表的主键、子表中的主表主键、单据来源、执行量等信息（目前该TestCase只实现有没有清空子表主键和子表中主表主键的判断）；
 * 
 * @since 6.0
 * @version 2013-11-5 上午9:07:20
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "单据复制时必须清除掉子表的主键、子表中的主表主键、单据来源、执行量等信息（目前该TestCase只实现有没有清空子表主键和子表中主表主键的判断）。  ", relatedIssueId = "215", coder = "zhongcha", solution = "在复制按钮处理类中将子表的主键、子表中的主表主键、单据来源、执行量等信息设为空。")
public class TestCase00215 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // 用于存放错误信息
        Map<String, MapList<String, String>> errorMap = new HashMap<String, MapList<String, String>>();
        // 该数据结构是：聚合vo对应元数据中除主实体外的实体，实体对应需要检查的字段。
        Map<String, MapList<String, String>> aggvo2EntityVOs2fieldNames =
                new HashMap<String, MapList<String, String>>();
        // 实体全路径vo对应实体的名称（用于提示错误信息）
        Map<String, String> entityVO2EntityName = new HashMap<String, String>();
        // 记录下没有实现IBDObject接口或者id值没有映射值的实体（用于提示错误信息）
        Set<String> UnimplIBDObjectEnitys = new HashSet<String>();
        for (MetadataResource metadataResource : resources) {
            // 获得元数据资源文件
            IMetadataFile metaDataFile = metadataResource.getMetadataFile();
            IEntity mainEntity = metaDataFile.getMainEntity();
            List<IEntity> itemEntitys = metaDataFile.getAllEntities();
            Map<IEntity, IEntity> itemEnty2parentEnty = new HashMap<IEntity, IEntity>();
            for (IEntity entity : itemEntitys) {
                for (IEntityTargetConnection tgCon : entity.getAggreConnections()) {
                    itemEnty2parentEnty.put(tgCon.getTargetEntity(), entity);
                }
            }

            if (MMValueCheck.isNotEmpty(mainEntity)) {
                // 元数据文件中读取的聚合vo
                String aggvoName = mainEntity.getAccessor().getAccessorWrapperClassName();
                // 用于记录每个实体需要检查的字段
                MapList<String, String> entityVO2fieldNames = new MapList<String, String>();
                if (MMValueCheck.isNotEmpty(aggvoName)) {
                    for (Entry<IEntity, IEntity> entry : itemEnty2parentEnty.entrySet()) {
                        IEntity itemEntity = entry.getKey();
                        IEntity ParentEntity = entry.getValue();
                        entityVO2EntityName.put(itemEntity.getFullClassName(), itemEntity.getDisplayName() + "("
                                + itemEntity.getFullName() + ")");
                        String itemPk = this.getPKFieldName(itemEntity);
                        String paretnPk = this.getPKFieldName(ParentEntity);

                        if (MMValueCheck.isNotEmpty(itemPk)) {
                            // 获得子实体主键
                            entityVO2fieldNames.put(itemEntity.getFullClassName(), itemPk);
                        }
                        else {
                            UnimplIBDObjectEnitys.add(itemEntity.getDisplayName() + "(" + itemEntity.getFullName()
                                    + ")");
                        }

                        if (MMValueCheck.isNotEmpty(paretnPk)) {
                            // 获得父实体主键
                            entityVO2fieldNames.put(itemEntity.getFullClassName(), paretnPk);
                        }
                        else {
                            UnimplIBDObjectEnitys.add(ParentEntity.getDisplayName() + "(" + ParentEntity.getFullName()
                                    + ")");
                        }
                    }
                    aggvo2EntityVOs2fieldNames.put(aggvoName, entityVO2fieldNames);
                }
            }
        }
        List<JavaClassResource> javaClzResources = this.getClientJavaClassResource(ruleExecContext);
        // 用于记录是否存在合法的按钮处理类
        boolean isExistClz = false;
        for (JavaClassResource javaClassResource : javaClzResources) {
            // 取得java文件的名（去掉后缀.java）
            String fileName =
                    javaClassResource.getResourceFileName().substring(0,
                            javaClassResource.getResourceFileName().length() - 5);
            if (!fileName.endsWith("CopyActionProcessor")) {
                continue;
            }
            ScanRule scanRule = new ScanRule(aggvo2EntityVOs2fieldNames);
            // 扫描该java文件判定是否存在判断单据状态的规则
            try {
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (scanRule.isCorrectClz && !isExistClz) {
                    isExistClz = true;
                }
                if (scanRule.isCorrectClz) {
                    for (String entityvo : scanRule.entityVO2CheckFieldNames.keySet()) {
                        // 相同实体中所有需要检查的字段减去该实体在按钮处理类中已经做置空处理的字段，剩余的字段就是未做置空操作的字段
                        if (MMValueCheck.isNotEmpty(scanRule.entityVO2AllFieldNames.get(entityvo))) {
                            scanRule.entityVO2CheckFieldNames.get(entityvo).removeAll(
                                    scanRule.entityVO2AllFieldNames.get(entityvo));
                        }
                        if (MMValueCheck.isNotEmpty(scanRule.entityVO2CheckFieldNames.get(entityvo))) {
                            MapList<String, String> entity2RemainfieldNms = new MapList<String, String>();
                            entity2RemainfieldNms.putAll(entityVO2EntityName.get(entityvo),
                                    scanRule.entityVO2CheckFieldNames.get(entityvo));
                            errorMap.put(fileName, entity2RemainfieldNms);
                        }
                    }
                }
            }
            catch (FileNotFoundException e) {
                Logger.error(e.getMessage(), e);
            }
        }
        StringBuilder errorContxt = new StringBuilder();
        if (MMValueCheck.isNotEmpty(UnimplIBDObjectEnitys)) {
            errorContxt.append("该组件的元数据中实体名为" + UnimplIBDObjectEnitys + "的实体没有实现IBDObject接口（或者接口中id没有映射值）。\n");
        }
        if (!isExistClz) {
            errorContxt.append("该组件中不存在以CopyActionProcessor为后缀的复制按钮处理类。\n");
        }
        if (MMValueCheck.isNotEmpty(errorMap)) {
            for (String filename : errorMap.keySet()) {
                for (String entity : errorMap.get(filename).keySet()) {
                    errorContxt.append("在复制按钮处理类[ " + filename + " ]中实体名为[ " + entity + " ]对应的表中字段名"
                            + errorMap.get(filename).get(entity) + "的字段没做置空处理。\n");
                }
            }
        }
        if (MMValueCheck.isNotEmpty(errorContxt)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    errorContxt.toString());
        }

        return result;
    }

    private String getPKFieldName(IEntity entity) {
        Map<String, IAttribute> ibdoMap = entity.getBusiInterfaceAttributes(MDRuleConstants.IBDOBJECT_INTERFACE_NAME);
        if (MMValueCheck.isNotEmpty(ibdoMap) && MMValueCheck.isNotEmpty(ibdoMap.get("id"))
                && MMValueCheck.isNotEmpty(ibdoMap.get("id").getFieldName())) {
            return ibdoMap.get("id").getFieldName();
        }
        return null;
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

        public ScanRule(Map<String, MapList<String, String>> aggvo2EntityVOs2fieldNames) {
            this.aggvo2EntityVOs2fieldNames = aggvo2EntityVOs2fieldNames;
        }

        private Map<String, MapList<String, String>> aggvo2EntityVOs2fieldNames =
                new HashMap<String, MapList<String, String>>();

        // 记录该复制按钮处理类中声明的所有变量和其对应的类型
        private Map<String, String> variable2EntityVO = new HashMap<String, String>();

        // 记录该复制按钮处理类中做置空操作的所有实体即其对应的字段
        private MapList<String, String> entityVO2AllFieldNames = new MapList<String, String>();

        // 记录该复制按钮处理类中需要检查的实体即其需检查的字段
        private MapList<String, String> entityVO2CheckFieldNames = new MapList<String, String>();

        /**
         * 记录当前类是否实现了接口nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor
         * 其值为false时，其他判定不能继续
         */
        private boolean isCorrectClz = false;

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

        @Override
        public Object visit(ASTVariableDeclaratorId node, Object data) {
            if (MMValueCheck.isNotEmpty(node.getImage())) {
                if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTVariableDeclarator.class))
                        && MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTVariableDeclarator.class)
                                .getFirstParentOfType(ASTLocalVariableDeclaration.class))) {
                    // 获得含有指定方法的整个表达式
                    ASTLocalVariableDeclaration varDeclaration =
                            node.getFirstParentOfType(ASTVariableDeclarator.class).getFirstParentOfType(
                                    ASTLocalVariableDeclaration.class);
                    if (varDeclaration.hasDescendantOfType(ASTReferenceType.class)) {
                        ASTClassOrInterfaceType classType =
                                varDeclaration.findDescendantsOfType(ASTReferenceType.class).get(0)
                                        .getFirstChildOfType(ASTClassOrInterfaceType.class);
                        if (MMValueCheck.isNotEmpty(classType) && MMValueCheck.isNotEmpty(classType.getImage())) {
                            String entityvo = classType.getImage().trim();
                            if (!entityvo.contains(".")) {
                                entityvo =
                                        this.importClassMap.containsKey(entityvo) ? this.importClassMap.get(entityvo)
                                                : String.format("%s.%s", this.currentPackageName, entityvo);
                            }
                            this.variable2EntityVO.put(node.getImage().trim(), entityvo);
                        }
                    }

                }
            }
            return super.visit(node, data);
        }

        /**
         * 判断该被扫描的类中是否调用了指定的方法，调用了，根据情况处理相应的值
         */
        @Override
        public Object visit(ASTName node, Object data) {
            if (MMValueCheck.isNotEmpty(this.variable2EntityVO) && MMValueCheck.isNotEmpty(node.getImage())) {
                String image = node.getImage().trim();
                for (String variable : this.variable2EntityVO.keySet()) {
                    String prefix = variable + ".set";
                    if (image.startsWith(prefix)) {
                        if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class))
                                && MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTPrimaryPrefix.class)
                                        .getFirstParentOfType(ASTPrimaryExpression.class))) {
                            // 获得含有指定方法的整个表达式
                            ASTPrimaryExpression priExpression =
                                    node.getFirstParentOfType(ASTPrimaryPrefix.class).getFirstParentOfType(
                                            ASTPrimaryExpression.class);
                            if (priExpression.hasDescendantOfType(ASTArguments.class)) {
                                if (priExpression.findDescendantsOfType(ASTArguments.class).get(0)
                                        .hasDescendantOfType(ASTNullLiteral.class)) {
                                    this.entityVO2AllFieldNames.put(this.variable2EntityVO.get(variable), image
                                            .substring(prefix.length()).toLowerCase());
                                }
                            }
                        }
                    }
                }
            }

            return super.visit(node, data);
        }

        /**
         * 根据该被扫描类实现的接口中的AggVo的类型确定该类中需要的做置空操作的实体及其字段
         */
        @Override
        public Object visit(ASTClassOrInterfaceType node, Object data) {
            if (this.isCorrectClz && MMValueCheck.isNotEmpty(node.getImage())) {
                String image = node.getImage().trim();
                if (!image.contains(".")) {
                    image =
                            this.importClassMap.containsKey(image) ? this.importClassMap.get(image) : String.format(
                                    "%s.%s", this.currentPackageName, image);
                }
                for (String aggvo : this.aggvo2EntityVOs2fieldNames.keySet()) {
                    if (image.equals(aggvo)) {
                        this.entityVO2CheckFieldNames = this.aggvo2EntityVOs2fieldNames.get(aggvo);
                    }
                }
            }

            return super.visit(node, data);
        }
    }

}
