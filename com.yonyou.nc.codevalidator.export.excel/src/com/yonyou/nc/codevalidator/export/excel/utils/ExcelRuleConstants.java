package com.yonyou.nc.codevalidator.export.excel.utils;

public final class ExcelRuleConstants {
	// public static final String XML_BEAN_FACTORY = "IXMLBeanFactory";
	// public static final String XML_ANALYSER = "IXmlAnalyser";
	//
	// public static final String PUBIMPL_CLASS_PATH = "PubImpl路径";
	// public static final String IMPL_CLASS_PATH = "Impl路径";
	// public static final String BS_CLASS_PATH = "BS类路径";
	// public static final String ITF_CLASS_PATH = "Itf类路径";
	// public static final String PUBITF_CLASS_PATH = "PubItf类路径";
	// public static final String VO_CLASS_PATH = "VO类路径";
	// public static final String CLIENT_CLASS_PATH = "Client路径";

	public static final String DOMAIN_NAME = "领域简称";
	public static final String DOMAIN_CODE = "领域编码";
	public static final String BILLTYPE_PREFIX = "领域单据类型前缀";
	public static final String OID_MARK = "OidMark";
	public static final String CURRENT_FUNCODE = "当前功能节点编号";
	public static final String CURRENT_BILLTYPE = "当前单据类型";
	public static final String METADATA_NAME = "元数据名称";
	public static final String CURRENT_COMPONENT = "当前组件名";
	public static final String FILE_CONFIG = "配置文件";

	public static final String TEST_TYPE = "验证方式";
	public static final String TESTCASE_COL_NAME = "自动化测试处理类";
	public static final String SPECIAL_PARAM_COL_NAME = "特殊参数";
	public static final String RESULT_COL_NAME = "运行结果";
	public static final String ISCHECK = "是否检查";
//	public static final String CALALOG = "条目";

	public static final String ISPASS = "是否通过";
	
	public static final String EXECUTE_LEVEL = "执行级别";
	public static final String SERIAL_NO = "序号";
	public static final String CATELOG = "分类";
	public static final String SUB_CATELOG = "详细分类";
	public static final String DESCRIPTION = "详细介绍";
	public static final String TEST_CLASSNAME = "自动化测试处理类";
	public static final String SPECIAL_PARAMETER = "特殊参数";
	public static final String EXECUTE_RESULT = "运行结果";
	public static final String REMARK = "备注信息";

	// 验证方式是否需要？
	public static final String[] EXCELCOLSSTR = { EXECUTE_LEVEL, SERIAL_NO, CATELOG, SUB_CATELOG, DESCRIPTION, TEST_CLASSNAME, SPECIAL_PARAM_COL_NAME, ISPASS, EXECUTE_RESULT, REMARK };
//	public static final String[] EXCEL_CONFIG_COL_METHOD_NAME = { "SimpleIdentifier", "Catalog", "SubCatalog", "Description",
//			"CheckRole", "", "Coder", "", "RuleDefinitionIdentifier", "", "", "", "Memo" };
//	public static final String[] EXCEL_CONFIG_COL_ID = { "id", "catalog", "subCatalog", "description", "checkRole",
//			"coder", "className", "specialParamDefine", "memo" };
//	public static final String[] EXCEL_CONFIG_COL_NAME = { "序号", "条目", "条目分类", "详细条目", "确认角色", "负责人", "自动化测试处理类",
//			"特殊参数", "备注信息" };

}