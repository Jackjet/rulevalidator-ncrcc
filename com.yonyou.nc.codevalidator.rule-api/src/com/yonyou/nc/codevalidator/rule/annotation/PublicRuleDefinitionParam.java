package com.yonyou.nc.codevalidator.rule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 规则公共参数描述
 *
 * @author luoweid
 * @since V1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PublicRuleDefinitionParam {

	/**
	 * 配置的公共参数信息，可在此配置多个参数（数组）
	 * <p>
	 * 可配置参数的编辑类型（用逗号分隔），如果未设置，默认为字符串文本编辑器
	 * <p>
	 * 示例：可仅设置参数 名称，或同时设置参数名称和参数编辑器类型
	 * <li>paramName</li>
	 * <li>paramName=editorType</li>
	 * @return
	 */
	String[] params();
	
}
