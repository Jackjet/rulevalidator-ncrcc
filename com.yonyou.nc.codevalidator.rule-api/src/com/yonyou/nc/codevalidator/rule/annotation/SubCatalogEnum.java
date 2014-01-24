package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * 条目分类(规则性质)：
 * 0.代码规则：JC_CODECRITERION("代码规范"),JC_CODEDESIGN("代码设计（反模式）"),JC_PERFORMISSUES
 * ("性能问题"),JC_CONCURRENISSUES("并发问题"),JC_PREBUILDCHECK("构造前检查")
 * 1.元数据规则：MD_BASESETTING("基础设置"), MD_FUNCCHECK("元数据功能检查"), MD_EDITOR("元数据编辑器"),
 * MD_PUBLISH("元数据发布"), MD_CODEORSCRIPT("元数据代码&脚本生成")；
 * 2.UI工厂配置规则：UF_MODELAPIORTEMPLATE("模型API&模板"), UF_LAYOUT("页面布局"),
 * UF_BTNOREVENT("按钮&事件"), UF_CONFIGFILE("配置文件")；
 * 3.第三方jar包规则：JAR_VERSION("jar包版本"), JAR_FUNC("jar包功能"), JAR_DEPEND("jar包依赖")；
 * 4.多语规则：LANG_METADATA("元数据"), LANG_BILLTEMP("单据模板"), LANG_QUERYTEMP("查询模板"),
 * LANG_PRINTTEMP("打印模板"), LANG_CODE("代码多语", CatalogEnum.LANG),
 * LANG_CONFIG("配置多语"), LANG_COMMON("通用(包含所有)")；
 * 5.建库脚本规则：CS_CONTENTCHECK("脚本内容检查(注册类等编译前检查)"),
 * CS_UNIFORM("一致性检查(与元数据，java类匹配)")；
 * 6.预制脚本规则：PS_CONTENTCHECK("脚本内容检查（注册类等编译前检查）"),
 * PS_UNIFORM("一致性检查（与元数据，java类匹配）")； 7.其他配置文件规则：OCF_UPM("upm"), OCF_LOG("日志"),
 * OCF_EXCHANGEPLAT("外部交换平台"), OCF_FORMULA("公式解析"),
 * 8.web开发相关规则:WEB_FRAME("Web技术框架"), WEB_JSP("JSP页面"), WEB_JS("JS脚本"),
 * WEB_CSS("CSS相关");
 * 
 * @author luoweid
 * 
 */
public enum SubCatalogEnum {
	// 代码规则
	JC_CODECRITERION("代码规范", CatalogEnum.JAVACODE), JC_CODEDESIGN("代码设计（反模式）", CatalogEnum.JAVACODE), JC_PERFORMISSUES(
			"性能问题", CatalogEnum.JAVACODE), JC_CONCURRENISSUES("并发问题", CatalogEnum.JAVACODE), JC_PREBUILDCHECK("构造前检查",
			CatalogEnum.JAVACODE),
	// 元数据规则
	MD_BASESETTING("基础设置", CatalogEnum.METADATA), MD_FUNCCHECK("元数据功能检查", CatalogEnum.METADATA), MD_EDITOR("元数据编辑器",
			CatalogEnum.METADATA), MD_PUBLISH("元数据发布", CatalogEnum.METADATA), MD_CODEORSCRIPT("元数据代码&脚本生成",
			CatalogEnum.METADATA),
	// UI工厂配置规则
	UF_MODELAPIORTEMPLATE("模型API&模板", CatalogEnum.UIFACTORY), UF_LAYOUT("页面布局", CatalogEnum.UIFACTORY), UF_BTNOREVENT(
			"按钮&事件", CatalogEnum.UIFACTORY), UF_CONFIGFILE("配置文件", CatalogEnum.UIFACTORY),
	// 第三方jar包规则
	JAR_VERSION("jar包版本", CatalogEnum.THIRDPARTYJAR), JAR_FUNC("jar包功能", CatalogEnum.THIRDPARTYJAR), JAR_DEPEND(
			"jar包依赖", CatalogEnum.THIRDPARTYJAR),
	// 多语规则
	LANG_METADATA("元数据", CatalogEnum.LANG), LANG_BILLTEMP("单据模板", CatalogEnum.LANG), LANG_QUERYTEMP("查询模板",
			CatalogEnum.LANG), LANG_PRINTTEMP("打印模板", CatalogEnum.LANG), LANG_CODE("代码多语", CatalogEnum.LANG), LANG_CONFIG(
			"配置多语", CatalogEnum.LANG), LANG_COMMON("通用(包含所有)", CatalogEnum.LANG),
	// 建库脚本规则
	CS_CONTENTCHECK("脚本内容检查(注册类等编译前检查)", CatalogEnum.CREATESCRIPT), CS_UNIFORM("一致性检查(与元数据，java类匹配)",
			CatalogEnum.CREATESCRIPT),
	// 预制脚本规则
	PS_CONTENTCHECK("脚本内容检查（注册类等编译前检查）", CatalogEnum.PRESCRIPT), PS_UNIFORM("一致性检查（与元数据，java类匹配）",
			CatalogEnum.PRESCRIPT),
	// 其他配置文件规则
	OCF_UPM("upm", CatalogEnum.OTHERCONFIGFILE), OCF_LOG("日志", CatalogEnum.OTHERCONFIGFILE), OCF_EXCHANGEPLAT("外部交换平台",
			CatalogEnum.OTHERCONFIGFILE), OCF_FORMULA("公式解析", CatalogEnum.OTHERCONFIGFILE), OCF_AOP("aop", CatalogEnum.OTHERCONFIGFILE),
	// web开发相关规则
	WEB_FRAME("Web技术框架", CatalogEnum.WEBDEVELOP), WEB_JSP("JSP页面", CatalogEnum.WEBDEVELOP), WEB_JS("JS脚本",
			CatalogEnum.WEBDEVELOP), WEB_CSS("CSS相关", CatalogEnum.WEBDEVELOP);

	private String name;
	private CatalogEnum catalogEnum;

	private SubCatalogEnum(String name, CatalogEnum catalogEnum) {
		this.name = name;
		this.catalogEnum = catalogEnum;
	}

	public String getName() {
		return name;
	}

	public CatalogEnum getCatalogEnum() {
		return catalogEnum;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * 获取对应条目的条目分类
	 * 
	 * @param catalogEnum
	 * @return
	 */
	public static SubCatalogEnum[] getSubCatalogByCatalog(CatalogEnum catalogEnum) {
		SubCatalogEnum[] result = null;
		switch (catalogEnum) {
		case JAVACODE:
			result = new SubCatalogEnum[] { SubCatalogEnum.JC_CODECRITERION, SubCatalogEnum.JC_CODEDESIGN,
					SubCatalogEnum.JC_CONCURRENISSUES, SubCatalogEnum.JC_PERFORMISSUES, SubCatalogEnum.JC_PREBUILDCHECK };
			break;
		case METADATA:
			result = new SubCatalogEnum[] { SubCatalogEnum.MD_BASESETTING, SubCatalogEnum.MD_CODEORSCRIPT,
					SubCatalogEnum.MD_EDITOR, SubCatalogEnum.MD_FUNCCHECK, SubCatalogEnum.MD_PUBLISH };
			break;
		case UIFACTORY:
			result = new SubCatalogEnum[] { SubCatalogEnum.UF_BTNOREVENT, SubCatalogEnum.UF_CONFIGFILE,
					SubCatalogEnum.UF_LAYOUT, SubCatalogEnum.UF_MODELAPIORTEMPLATE };
			break;
		case THIRDPARTYJAR:
			result = new SubCatalogEnum[] { SubCatalogEnum.JAR_DEPEND, SubCatalogEnum.JAR_FUNC,
					SubCatalogEnum.JAR_VERSION };
			break;
		case LANG:
			result = new SubCatalogEnum[] { SubCatalogEnum.LANG_BILLTEMP, SubCatalogEnum.LANG_CODE,
					SubCatalogEnum.LANG_COMMON, SubCatalogEnum.LANG_CONFIG, SubCatalogEnum.LANG_METADATA,
					SubCatalogEnum.LANG_PRINTTEMP, SubCatalogEnum.LANG_QUERYTEMP };
			break;
		case CREATESCRIPT:
			result = new SubCatalogEnum[] { SubCatalogEnum.CS_CONTENTCHECK, SubCatalogEnum.CS_UNIFORM };
			break;
		case PRESCRIPT:
			result = new SubCatalogEnum[] { SubCatalogEnum.PS_CONTENTCHECK, SubCatalogEnum.PS_UNIFORM };
			break;
		case OTHERCONFIGFILE:
			result = new SubCatalogEnum[] { SubCatalogEnum.OCF_EXCHANGEPLAT, SubCatalogEnum.OCF_FORMULA,
					SubCatalogEnum.OCF_LOG, SubCatalogEnum.OCF_UPM };
			break;
		case WEBDEVELOP:
			result = new SubCatalogEnum[] { SubCatalogEnum.WEB_CSS, SubCatalogEnum.WEB_FRAME, SubCatalogEnum.WEB_JS,
					SubCatalogEnum.WEB_JSP };
			break;
		default:
			break;
		}
		return result;
	}

}
