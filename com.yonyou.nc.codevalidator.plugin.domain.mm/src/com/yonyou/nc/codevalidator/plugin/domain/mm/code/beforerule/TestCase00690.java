package com.yonyou.nc.codevalidator.plugin.domain.mm.code.beforerule;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
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
 * Ԫ������ʵ��IRowNo�ӿڵ�ʵ�壬insertBP�б��������к�У�����
 * 
 * @author qiaoyanga
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "Ԫ����ʵ��Irowno�ӿڵ�ʵ�壬�������к�У��rule", relatedIssueId = "690", coder = "qiaoyanga", solution = "")
public class TestCase00690 extends AbstractJavaQueryRuleDefinition {

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
        MapList<String, JavaClassResource> javaClazzResMapList = new MapList<String, JavaClassResource>();
        /*
         * �ҳ�һ������������InsertBP��β��java�ļ���������java�ļ�����������̵�BP��ΪĿ���ļ���
         * ������ɢ������������DmoPushInsertBP��DmoTranstypeInsertBP���������Ŀ���ļ���DmoInsertBP
         * ����ͨ������������Ҳ����˵�
         */
        for (final JavaClassResource javaClassResource : resources) {

            String className = javaClassResource.getJavaCodeClassName();

            if (className.endsWith("InsertBP")) {
                String key = className.substring(0, className.indexOf("bp") + 2);
                javaClazzResMapList.put(key, javaClassResource);
            }
        }
        if (javaClazzResMapList.size() < 1) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "δ�ҵ���InsertBP��β��BP�ļ�");
            return result;
        }
        Set<String> keys = javaClazzResMapList.keySet();
        for (String key : keys) {

            List<JavaClassResource> javaClassResList = javaClazzResMapList.get(key);
            if (javaClassResList.size() == 1) {
                // ���
                this.check(javaClassResList.get(0), ruleExecContext, result);
            }
            else {
                this.check(this.findOptimalClassResource(javaClassResList), ruleExecContext, result);
            }
        }

        return result;
    }

    /**
     * �ҵ����ŵ�Ŀ���ļ�
     * 
     * @param javaClassResList
     * @return
     */
    private JavaClassResource findOptimalClassResource(List<JavaClassResource> javaClassResList) {
        List<String> clazzNameList = new ArrayList<String>();
        Map<String, JavaClassResource> resourceMap = new HashMap<String, JavaClassResource>();
        for (JavaClassResource javaClassResource : javaClassResList) {
            resourceMap.put(javaClassResource.getJavaCodeClassName(), javaClassResource);
            clazzNameList.add(javaClassResource.getJavaCodeClassName());
        }
        Collections.sort(clazzNameList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        });
        return resourceMap.get(clazzNameList.get(0));
    }

    private void check(JavaClassResource javaClassResource, IRuleExecuteContext ruleExecContext,
            ResourceRuleExecuteResult result) throws RuleBaseException {
        try {
            CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));

            Visitor visitor = new Visitor();
            visitor.visit(compilationUnit, null);
            if (!visitor.isImport()) {
                String aggVoName = visitor.getAggvoname();
                if (aggVoName != null) {
                    MetadataResourceQuery metadataResourceQuery = new MetadataResourceQuery();
                    metadataResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
                    metadataResourceQuery.setMetaResType(MetaResType.BMF);
                    List<MetadataResource> resources = ResourceManagerFacade.getResource(metadataResourceQuery);
                    MetadataResource mdResource = null;
                    for (MetadataResource metadataResource : resources) {
                        IMetadataFile metaDataFile = metadataResource.getMetadataFile();
                        String mdAggVOName = metaDataFile.getMainEntity().getAccessor().getAccessorWrapperClassName();
                        if (aggVoName.equals(mdAggVOName)) {
                            mdResource = metadataResource;
                            break;
                        }
                    }
                    if (mdResource != null) {
                        List<IEntity> allEntitys = mdResource.getMetadataFile().getAllEntities();
                        if (allEntitys != null && allEntitys.size() > 0) {
                            for (IEntity entity : allEntitys) {
                                List<IBusiInterface> intfaces = entity.getBusiInterfaces();
                                if (intfaces != null && intfaces.size() > 0) {
                                    for (IBusiInterface intface : intfaces) {
                                        if (intface.getFullClassName().equals("nc.itf.pubapp.pub.bill.IRowNo")) {
                                            result.addResultElement(javaClassResource.getJavaCodeClassName(),
                                                    "Ԫ����ʵ�塮" + entity.getDisplayName() + "����ʵ��IRowNo�ӿڣ���"
                                                            + javaClassResource.getJavaCodeClassName() + "��δ�����кż�����");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        catch (ParseException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }
    }

    private static class Visitor extends VoidVisitorAdapter<Void> {
        List<String> importnames = new ArrayList<String>();

        String aggvoname = null;

        boolean isImport = false;

        @Override
        public void visit(ImportDeclaration n, Void arg) {
            String importClazz = n.getName().toString();
            this.importnames.add(importClazz);
            if ("nc.mmbd.pub.rule.MMRowNoCheckRule".equals(importClazz)) {
                this.isImport = true;
            }
        }

        @Override
        public void visit(MethodDeclaration n, Void v) {
            if (this.aggvoname != null) {
                return;
            }
            List<Parameter> params = n.getParameters();
            String str = params.get(0).getType().toString();
            for (String importname : this.importnames) {
                if (importname.contains(str.substring(0, str.length() - 2))) {
                    this.aggvoname = importname;
                    break;
                }
            }
        }

        public String getAggvoname() {
            return this.aggvoname;
        }

        public boolean isImport() {
            return this.isImport;
        }
    }
}
