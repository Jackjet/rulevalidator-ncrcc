package com.yonyou.nc.codevalidator.plugin.domain.mm.md;

/**
 * 流程生产制造自动化检查的一些常量
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public final class MmMDConstants {

    private MmMDConstants() {

    }

    public final static String PK_ORG = "pk_org";

    public final static String PK_ORG_V = "pk_org_v";

    public final static String PK_GROUP = "pk_group";

    /**
     * 物料OID
     */
    public final static String MATERIALOID = "cmateialid";

    /**
     * 物料VID
     */
    public final static String MATERIALOVD = "cmateialvid";

    /**
     * 单据号
     */
    public final static String BILLCODE = "单据号";

    /**
     * DOC标签
     */
    public final static String EXTEND_TAG_DOC = "DOC";

    /**
     * smart标签
     */
    public final static String EXTEND_TAG_SMART = "smart";

    /**
     * 行号字段
     */
    public final static String ROW_NO = "vrowno";

    /**
     * 元数据中不能包含的技术性名称集合
     */
    public final static String[] NONSTANDARD_NAMES = new String[] {
        "主键", "ID", "PK", "OID", "VID", "主表", "子表"
    };

    /**
     * 审计信息接口属性集合
     */
    public final static String[] AUDIT_INFOS = new String[] {
        "创建人", "创建时间", "最后修改人", "最后修改时间"
    };

    /**
     * 组织信息接口属性集合
     */
    public final static String[] ORG_INFOS = new String[] {
        "组织", "组织多版本"
    };

    public final static String PK_LOCK = "PK锁";

    /**
     * 流程信息回写接口必须对应的属性
     */
    public final static String[] FLOW_BUSI_ITFS = new String[] {
        "单据ID", "单据号", "所属组织", "制单人"
    };

    public final static String[] FLOW_BUSI_ITF_ALL_FIELDS = new String[] {
        "单据ID", "单据号", "所属组织", "业务类型", "制单人", "审批人", "审批状态", "审批批语", "审批时间", "交易类型", "单据类型", "交易类型pk", "来源单据类型",
        "来源单据id", "修订枚举", "单据版本pk"
    };

    /**
     * IRowNo接口属性映射
     */
    public final static String ROW_INFO = "行号";

}
