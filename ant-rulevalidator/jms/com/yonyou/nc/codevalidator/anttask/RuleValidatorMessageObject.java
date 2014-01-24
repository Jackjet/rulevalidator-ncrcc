package com.yonyou.nc.codevalidator.anttask;

/**
 * 执行规则调用时采用的消息对象
 * 
 * @author mazhqa
 * @since V2.9
 */
public class RuleValidatorMessageObject {

	public static final String CODEPATH = "CODEPATH";
	public static final String PRODUCTCODE = "PRODUCTCODE";
	public static final String NCHOME = "NCHOME";
	public static final String DSNAME = "DSNAME";
	public static final String EXECUTEPERIOD = "EXECUTEPERIOD";

	private final String codePath;
	private final String ncHome;
	private final String dataSourceName;
	private final String executePeriod;
	private final String productCode;

	public RuleValidatorMessageObject(String codePath, String ncHome, String dataSourceName, String executePeriod,
			String productCode) {
		super();
		this.codePath = codePath;
		this.ncHome = ncHome;
		this.dataSourceName = dataSourceName;
		this.executePeriod = executePeriod;
		this.productCode = productCode;
	}

	String getCodePath() {
		return codePath;
	}

	String getNcHome() {
		return ncHome;
	}

	String getDataSourceName() {
		return dataSourceName;
	}

	String getExecutePeriod() {
		return executePeriod;
	}
	
	String getProductCode() {
		return productCode;
	}

}
