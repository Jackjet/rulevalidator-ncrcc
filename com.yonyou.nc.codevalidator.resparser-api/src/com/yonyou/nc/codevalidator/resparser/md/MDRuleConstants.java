package com.yonyou.nc.codevalidator.resparser.md;

public final class MDRuleConstants {

	private MDRuleConstants() {

	}

	public static final String IBDOBJECT_INTERFACE_NAME = "nc.vo.bd.meta.IBDObject";
	public static final String AUDITINFO_NAME = "nc.itf.pubapp.pub.bill.IAuditInfo";
	public static final String BILLNO_NAME = "nc.itf.pubapp.pub.bill.IBillNo";
	public static final String ROWNO_NAME = "nc.itf.pubapp.pub.bill.IRowNo";
	public static final String ORGINFO_NAME = "nc.itf.pubapp.pub.bill.IOrgInfo";
	public static final String BILLDATE_NAME = "nc.itf.pubapp.pub.bill.IBillDate";
	public static final String MAKETIME_NAME = "nc.itf.pubapp.pub.bill.IMakeTime";
	public static final String SINGLE_SUPPORT_BILLFLOW_NAME = "nc.itf.pubapp.pub.bill.ISingleSuportBillFlow";
	public static final String SRC_GROUP_SUPPORT_BILLFLOW_NAME = "nc.itf.pubapp.pub.bill.ISrcGroupSuportBillFlow";

	public static final String FLOWBIZ_INTERFACE_NAME = "nc.itf.uap.pf.metadata.IFlowBizItf";
	public static final String PF_BILLLOCK_NAME = "nc.vo.pub.pf.IPfBillLock";
	public static final String HEADBODY_QUERY_INTERFACE_NAME = "nc.itf.uap.pf.metadata.IHeadBodyQueryItf";
	public static final String BILLSTATUS_ENUM_NAME = "nc.vo.pub.pf.BillStatusEnum";
	
	public static final String TYPE_STYLE_ARRAY = "ARRAY";
	public static final String TYPE_STYLE_SINGLE = "SINGLE";
	public static final String TYPE_STYLE_LIST = "LIST";
	public static final String TYPE_STYLE_REF = "REF";

	public static final String ACCESS_STRATEGY_POJO = "nc.md.model.access.PojoAccessor";
	public static final String ACCESS_STRATEGY_NCBEAN = "nc.md.model.access.NCBeanAccessor";
	public static final String ACCESS_STRATEGY_BODYOFAGGVO = "nc.md.model.access.BodyOfAggVOAccessor";

}
