package com.web.pub.fw.constant;

/**
 * 系统所有jsp页面常量类。
 * 
 * controler中所有返回页面必须使用常量
 * 
 * @author:sunshine
 * @date:2014-1-7 下午4:49:48
 */
public interface JSPFileNames {
	// 系统页面
	public final static String ERROR = "/error/error";
	public final static String NOTFOUND = "/error/notfound";
	/* 订单相关详细信息页面 在新页面打开 */
	public final static String RULE_REPORTETAIL = "/rule/subpages/reportdetail";
	public final static String RULE_EXEC_DTETAIL = "/rule/subpages/ruleexecdetail";
	// 状态报表
	public final static String RULE_REPORT_STATUS = "/report/statusreport";

}