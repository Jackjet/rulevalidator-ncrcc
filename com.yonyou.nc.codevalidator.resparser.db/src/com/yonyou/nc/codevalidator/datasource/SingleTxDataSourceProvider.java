package com.yonyou.nc.codevalidator.datasource;

import java.util.HashMap;

import javax.sql.DataSource;

public class SingleTxDataSourceProvider implements DataSourceProvider {

	private HashMap<String, DataSource> map = new HashMap<String, DataSource>();

	public DataSource getDataSource(String name) {
		synchronized (this) {
			if (map.containsKey(name)) {
				return map.get(name);
			} else {
				SingleTxwareDataSource ds = new SingleTxwareDataSource(name);
				map.put(name, ds);
				return ds;
			}
		}
	}
	
	public synchronized void clean() {
		map.clear();
	}
}
