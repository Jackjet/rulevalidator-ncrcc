package com.yonyou.nc.codevalidator.resparser.md;

/**
 * 元数据-属性
 * @author mazhqa
 *
 */
public interface IAttribute {
	
	/**
	 * 访问策略，在MDRuleConstants类中，分成三种：<p>
	 * ACCESS_STRATEGY_POJO， ACCESS_STRATEGY_NCBEAN， ACCESS_STRATEGY_BODYOFAGGVO
	 * @return
	 */
	String getAccessStrategy();
	
	/**
	 * 类型样式，在MDRuleConstants类中，分成四种：<p>
	 * TYPE_STYLE_ARRAY, TYPE_STYLE_SINGLE, TYPE_STYLE_LIST, TYPE_STYLE_REF
	 * @return
	 */
	String getTypeStyle();
	
	/**
	 * 属性名称
	 * @return
	 */
	String getName();
	
	/**
	 * 字段名称
	 * @return
	 */
	String getFieldName();
	
	/**
	 * 字段类型
	 * @return
	 */
	String getFieldType();
	
	/**
	 * 字段长度（有些字段类型可能为空）
	 * @return
	 */
	String getLength();
	
	/**
	 * 字段显示名称
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * 得到属性的类型
	 * @return
	 */
	IType getType();
	
	/**
	 * 使用权
	 * @return
	 */
	boolean isAccessPower();
	
	/**
	 * 使用权组
	 * @return
	 */
	String getAccessPowerGroup();

	
	/**
	 * 获取属性参照名称，可为空
	 */
	String getRefModuleName();
	
	/**
	 * 是否动态属性
	 * @return
	 */
	boolean isDynamic();
}
