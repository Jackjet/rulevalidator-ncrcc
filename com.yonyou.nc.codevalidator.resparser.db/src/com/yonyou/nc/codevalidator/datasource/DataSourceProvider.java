package com.yonyou.nc.codevalidator.datasource;

import javax.sql.DataSource;

public interface DataSourceProvider {
	DataSource getDataSource(String name);
	void clean();
}
