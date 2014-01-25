package com.yonyou.nc.codevalidator.sdk.datasource;

public class PropInfo {
	public PropInfo() {
		TransactionManagerProxyClass = "nc.bs.mw.tran.IerpTransactionManagerProxy";
		UserTransactionClass = "nc.bs.mw.tran.IerpUserTransaction";
		TransactionManagerClass = "nc.bs.mw.tran.IerpTransactionManager";
		SqlDebugSetClass = "nc.bs.mw.sql.UFSqlObject";
		XADataSourceClass = "nc.bs.mw.ejbsql.IerpXADataSource";
	}

	/*
	 * public PropInfo(String name, DomainInfo domain, DataSourceMeta metas[],
	 * boolean isEncode, InternalServiceArray internalService[], boolean
	 * hotDeploy) { TransactionManagerProxyClass =
	 * "nc.bs.mw.tran.IerpTransactionManagerProxy"; UserTransactionClass =
	 * "nc.bs.mw.tran.IerpUserTransaction"; TransactionManagerClass =
	 * "nc.bs.mw.tran.IerpTransactionManager"; SqlDebugSetClass =
	 * "nc.bs.mw.sql.UFSqlObject"; XADataSourceClass =
	 * "nc.bs.mw.ejbsql.IerpXADataSource"; this.domain = domain; this.isEncode =
	 * isEncode; dataSource = metas; internalServiceArray = internalService;
	 * enableHotDeploy = hotDeploy; }
	 */

	/*
	 * public InternalServiceArray[] getInternalService() { return
	 * internalServiceArray; }
	 * 
	 * public void setInternalServiceArray(InternalServiceArray
	 * internalService[]) { internalServiceArray = internalService; }
	 */

	public boolean isEncode() {
		return isEncode;
	}

	public void setEncode(boolean isEncode) {
		this.isEncode = isEncode;
	}

	public DataSourceMeta[] getDataSource() {
		DataSourceMeta metas[] = new DataSourceMeta[dataSource.length];
		for (int i = 0; i < metas.length; i++) {
			metas[i] = (DataSourceMeta) dataSource[i].clone();
			if (isEncode()) {
				// metas[i].setPassword(ToolUtils.getEncode().decode(metas[i].getPassword()));
				metas[i].setPassword(encode.decode(metas[i].getPassword()));
			}
		}
		return metas;
	}

	public void setDataSource(DataSourceMeta dataSource[]) {
		DataSourceMeta metas[] = new DataSourceMeta[dataSource.length];
		for (int i = 0; i < metas.length; i++) {
			metas[i] = (DataSourceMeta) dataSource[i].clone();
			if (isEncode()) {
				// metas[i].setPassword(ToolUtils.getEncode().encode(metas[i].getPassword()));
				metas[i].setPassword(encode.encode(metas[i].getPassword()));
			}
		}

		this.dataSource = metas;
	}

	public String getXADataSourceClass() {
		return XADataSourceClass;
	}

	public void setSqlDebugSetClass(String sqlDebugSetClass) {
		SqlDebugSetClass = sqlDebugSetClass;
	}

	public void setTransactionManagerClass(String transactionManagerClass) {
		TransactionManagerClass = transactionManagerClass;
	}

	public void setTransactionManagerProxyClass(
			String transactionManagerProxyClass) {
		TransactionManagerProxyClass = transactionManagerProxyClass;
	}

	public void setUserTransactionClass(String userTransactionClass) {
		UserTransactionClass = userTransactionClass;
	}

	public void setXADataSourceClass(String dataSourceClass) {
		XADataSourceClass = dataSourceClass;
	}

	public boolean isEnableHotDeploy() {
		return enableHotDeploy;
	}

	public void setEnableHotDeploy(boolean enableHotDeploy) {
		this.enableHotDeploy = enableHotDeploy;
	}

	/*
	 * public DomainInfo getDomain() { return domain; }
	 * 
	 * public void setDomain(DomainInfo domain) { this.domain = domain; }
	 * 
	 * public InternalServiceArray[] getInternalServiceArray() { return
	 * internalServiceArray; }
	 */

	public String getSqlDebugSetClass() {
		return SqlDebugSetClass;
	}

	public String getTransactionManagerClass() {
		return TransactionManagerClass;
	}

	public String getTransactionManagerProxyClass() {
		return TransactionManagerProxyClass;
	}

	public String getUserTransactionClass() {
		return UserTransactionClass;
	}

	private Encode encode = new Encode();
	private boolean enableHotDeploy;
	// private DomainInfo domain;
	private boolean isEncode;
	// private InternalServiceArray internalServiceArray[];
	private String TransactionManagerProxyClass;
	private String UserTransactionClass;
	private String TransactionManagerClass;
	private String SqlDebugSetClass;
	private String XADataSourceClass;
	private DataSourceMeta dataSource[];

}
