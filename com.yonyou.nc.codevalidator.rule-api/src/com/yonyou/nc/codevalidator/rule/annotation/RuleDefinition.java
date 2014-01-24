package com.yonyou.nc.codevalidator.rule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 规则定义描述
 * 
 * @author luoweid
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RuleDefinition {

    /**
     * 规则应用范围
     * 
     * @return
     */
    ScopeEnum scope() default ScopeEnum.ALL;

    /**
     * 条目(规则类型)
     * 
     * @return
     */
    CatalogEnum catalog();

    /**
     * 条目分类(规则性质)
     * 
     * @return
     */
    SubCatalogEnum subCatalog();

    /**
     * 详细条目
     * 
     * @return
     */
    String description();

    /**
     * 确认角色
     * 
     * @return
     */
    CheckRoleEnum checkRole() default CheckRoleEnum.MUSTEXIST;

    /**
     * 严重性等级
     * 
     * @return
     */
    RepairLevel repairLevel() default RepairLevel.MUSTREPAIR;

    /**
     * 代码负责人：开发者
     * 
     * @return
     */
    String coder();

    /**
     * 特殊参数
     * 
     * @return
     */
    String[] specialParamDefine() default "";

    /**
     * 备注
     * 
     * @return
     */
    String memo() default "";

    /**
     * 问题解决办法
     * 
     * @return
     */
    String solution() default "";

    /**
     * 关联规则收集系统的问题ID, 只用输入对应的id即可
     * <p>
     * 比如http://ncrcc.yonyou.com/browse/NCRCC-327中只需要写id: 327
     * 
     * @return
     */
    String relatedIssueId();

    /**
     * 此规则运行的最低阶段
     * 
     * @return
     */
    ExecutePeriod executePeriod() default ExecutePeriod.DEPLOY;

    /**
     * 此规则执行的层级，决定该规则在整个运行过程中执行几次
     * 
     * @return
     */
    ExecuteLayer executeLayer() default ExecuteLayer.BUSICOMP;
}
