package com.web.pub.fw.filter;

/**
 * 电子销售门户常量类
 * 
 * @author:sunshine
 * @date:2012-4-12 上午10:04:38
 */
public class FwConstants {

	public static String RANDOMCODEID = "RANDOMCODEID";

	// 用户对象
	public static String SESSION_USERUIVIEW = "userUIVIew";

	// 单点登录
	public static String SESSION_IS_SINGLE_LOGIN = "isSingleLogin";

	// 是否忽略验证码
	public static String SESSION_IS_IGNORE_VERIFICATION_CODE = "isIgnoreVerificationCode";

	/**
	 * 是否启用严格的信用额度检查（是：信用检查未通过时，不能提交订单；否：信用检查未通过时，仍然可以提交订单）
	 */
	public static String SESSION_IS_CREDIT_CHECK_STRICT = "isCreditCheckStrict";

	/**
	 * 是否启用信用额度检查（是：进行信用额度检查；否：不经行信用额度检查）
	 */
	public static String SESSION_IS_CREDIT_CHECK = "isCreditCheck";

	// 门户首页展销商品-新品、特价、幻灯图取前多少个
	public static int NEW_SIZE = 8;
	public static int SPECIALPRICE_SIZE = 8;
	public static int ADVERTISING_SIZE = 6;

	/**
	 * 一次最多接收500个商品id查询lucene索引
	 */
	public static final int LUCENE_MAXNUMBER = 500;

}
