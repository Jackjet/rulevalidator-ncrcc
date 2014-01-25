package com.yonyou.nc.codevalidator.rule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ����������
 * 
 * @author luoweid
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RuleDefinition {

    /**
     * ����Ӧ�÷�Χ
     * 
     * @return
     */
    ScopeEnum scope() default ScopeEnum.ALL;

    /**
     * ��Ŀ(��������)
     * 
     * @return
     */
    CatalogEnum catalog();

    /**
     * ��Ŀ����(��������)
     * 
     * @return
     */
    SubCatalogEnum subCatalog();

    /**
     * ��ϸ��Ŀ
     * 
     * @return
     */
    String description();

    /**
     * ȷ�Ͻ�ɫ
     * 
     * @return
     */
    CheckRoleEnum checkRole() default CheckRoleEnum.MUSTEXIST;

    /**
     * �����Եȼ�
     * 
     * @return
     */
    RepairLevel repairLevel() default RepairLevel.MUSTREPAIR;

    /**
     * ���븺���ˣ�������
     * 
     * @return
     */
    String coder();

    /**
     * �������
     * 
     * @return
     */
    String[] specialParamDefine() default "";

    /**
     * ��ע
     * 
     * @return
     */
    String memo() default "";

    /**
     * �������취
     * 
     * @return
     */
    String solution() default "";

    /**
     * ���������ռ�ϵͳ������ID, ֻ�������Ӧ��id����
     * <p>
     * ����http://ncrcc.yonyou.com/browse/NCRCC-327��ֻ��Ҫдid: 327
     * 
     * @return
     */
    String relatedIssueId();

    /**
     * �˹������е���ͽ׶�
     * 
     * @return
     */
    ExecutePeriod executePeriod() default ExecutePeriod.DEPLOY;

    /**
     * �˹���ִ�еĲ㼶�������ù������������й�����ִ�м���
     * 
     * @return
     */
    ExecuteLayer executeLayer() default ExecuteLayer.BUSICOMP;
}
