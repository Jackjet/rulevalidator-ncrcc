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
 * ���ݸ���ʱ����������ӱ���������ӱ��е�����������������Դ��ִ��������Ϣ��Ŀǰ��TestCaseֻʵ����û������ӱ��������ӱ��������������жϣ���
 * 
 * @since 6.0
 * @version 2013-11-5 ����9:07:20
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "���ݸ���ʱ����������ӱ���������ӱ��е�����������������Դ��ִ��������Ϣ��Ŀǰ��TestCaseֻʵ����û������ӱ��������ӱ��������������жϣ���  ", relatedIssueId = "215", coder = "zhongcha", solution = "�ڸ��ư�ť�������н��ӱ���������ӱ��е�����������������Դ��ִ��������Ϣ��Ϊ�ա�")
public class TestCase00215 extends AbstractMetadataResourceRuleDefinition {

    @Override
    protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // ���ڴ�Ŵ�����Ϣ
        Map<String, MapList<String, String>> errorMap = new HashMap<String, MapList<String, String>>();
        // �����ݽṹ�ǣ��ۺ�vo��ӦԪ�����г���ʵ�����ʵ�壬ʵ���Ӧ��Ҫ�����ֶΡ�
        Map<String, MapList<String, String>> aggvo2EntityVOs2fieldNames =
                new HashMap<String, MapList<String, String>>();
        // ʵ��ȫ·��vo��Ӧʵ������ƣ�������ʾ������Ϣ��
        Map<String, String> entityVO2EntityName = new HashMap<String, String>();
        // ��¼��û��ʵ��IBDObject�ӿڻ���idֵû��ӳ��ֵ��ʵ�壨������ʾ������Ϣ��
        Set<String> UnimplIBDObjectEnitys = new HashSet<String>();
        for (MetadataResource metadataResource : resources) {
            // ���Ԫ������Դ�ļ�
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
                // Ԫ�����ļ��ж�ȡ�ľۺ�vo
                String aggvoName = mainEntity.getAccessor().getAccessorWrapperClassName();
                // ���ڼ�¼ÿ��ʵ����Ҫ�����ֶ�
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
                            // �����ʵ������
                            entityVO2fieldNames.put(itemEntity.getFullClassName(), itemPk);
                        }
                        else {
                            UnimplIBDObjectEnitys.add(itemEntity.getDisplayName() + "(" + itemEntity.getFullName()
                                    + ")");
                        }

                        if (MMValueCheck.isNotEmpty(paretnPk)) {
                            // ��ø�ʵ������
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
        // ���ڼ�¼�Ƿ���ںϷ��İ�ť������
        boolean isExistClz = false;
        for (JavaClassResource javaClassResource : javaClzResources) {
            // ȡ��java�ļ�������ȥ����׺.java��
            String fileName =
                    javaClassResource.getResourceFileName().substring(0,
                            javaClassResource.getResourceFileName().length() - 5);
            if (!fileName.endsWith("CopyActionProcessor")) {
                continue;
            }
            ScanRule scanRule = new ScanRule(aggvo2EntityVOs2fieldNames);
            // ɨ���java�ļ��ж��Ƿ�����жϵ���״̬�Ĺ���
            try {
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (scanRule.isCorrectClz && !isExistClz) {
                    isExistClz = true;
                }
                if (scanRule.isCorrectClz) {
                    for (String entityvo : scanRule.entityVO2CheckFieldNames.keySet()) {
                        // ��ͬʵ����������Ҫ�����ֶμ�ȥ��ʵ���ڰ�ť���������Ѿ����ÿմ�����ֶΣ�ʣ����ֶξ���δ���ÿղ������ֶ�
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
            errorContxt.append("�������Ԫ������ʵ����Ϊ" + UnimplIBDObjectEnitys + "��ʵ��û��ʵ��IBDObject�ӿڣ����߽ӿ���idû��ӳ��ֵ����\n");
        }
        if (!isExistClz) {
            errorContxt.append("������в�������CopyActionProcessorΪ��׺�ĸ��ư�ť�����ࡣ\n");
        }
        if (MMValueCheck.isNotEmpty(errorMap)) {
            for (String filename : errorMap.keySet()) {
                for (String entity : errorMap.get(filename).keySet()) {
                    errorContxt.append("�ڸ��ư�ť������[ " + filename + " ]��ʵ����Ϊ[ " + entity + " ]��Ӧ�ı����ֶ���"
                            + errorMap.get(filename).get(entity) + "���ֶ�û���ÿմ���\n");
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

        public ScanRule(Map<String, MapList<String, String>> aggvo2EntityVOs2fieldNames) {
            this.aggvo2EntityVOs2fieldNames = aggvo2EntityVOs2fieldNames;
        }

        private Map<String, MapList<String, String>> aggvo2EntityVOs2fieldNames =
                new HashMap<String, MapList<String, String>>();

        // ��¼�ø��ư�ť�����������������б��������Ӧ������
        private Map<String, String> variable2EntityVO = new HashMap<String, String>();

        // ��¼�ø��ư�ť�����������ÿղ���������ʵ�弴���Ӧ���ֶ�
        private MapList<String, String> entityVO2AllFieldNames = new MapList<String, String>();

        // ��¼�ø��ư�ť����������Ҫ����ʵ�弴��������ֶ�
        private MapList<String, String> entityVO2CheckFieldNames = new MapList<String, String>();

        /**
         * ��¼��ǰ���Ƿ�ʵ���˽ӿ�nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor
         * ��ֵΪfalseʱ�������ж����ܼ���
         */
        private boolean isCorrectClz = false;

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

        @Override
        public Object visit(ASTVariableDeclaratorId node, Object data) {
            if (MMValueCheck.isNotEmpty(node.getImage())) {
                if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTVariableDeclarator.class))
                        && MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTVariableDeclarator.class)
                                .getFirstParentOfType(ASTLocalVariableDeclaration.class))) {
                    // ��ú���ָ���������������ʽ
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
         * �жϸñ�ɨ��������Ƿ������ָ���ķ����������ˣ��������������Ӧ��ֵ
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
                            // ��ú���ָ���������������ʽ
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
         * ���ݸñ�ɨ����ʵ�ֵĽӿ��е�AggVo������ȷ����������Ҫ�����ÿղ�����ʵ�弰���ֶ�
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
