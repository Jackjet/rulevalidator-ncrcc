package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * 条目(规则类型)： METADATA("元数据规则"), UIFACTORY("UI工厂配置规则"),
 * THIRDPARTYJAR("第三方jar包规则"), LANG("多语规则"), CREATESCRIPT("建库脚本规则"),
 * PRESCRIPT("预制脚本规则"), OTHERCONFIGFILE("其他配置文件规则"), WEBDEVELOP("web开发相关规则");
 * 
 * @author luoweid
 * 
 */
public enum CatalogEnum {

	JAVACODE("代码规则"), METADATA("元数据规则"), UIFACTORY("UI工厂配置规则"), THIRDPARTYJAR("第三方jar包规则"), LANG("多语规则"), CREATESCRIPT(
			"建库脚本规则"), PRESCRIPT("预制脚本规则"), OTHERCONFIGFILE("其他配置文件规则"), WEBDEVELOP("web开发相关规则");

	private String name;

	private CatalogEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
