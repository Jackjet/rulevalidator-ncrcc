package com.yonyou.nc.codevalidator.adapter;

import java.sql.SQLException;

import com.yonyou.nc.codevalidator.crossdb.CrossDBStatement;

public class GbaseAdapter extends BaseAdapter {

	public String getDriverClass() {
		return "com.gbase.jdbc.Driver";
	}

	public void supportHugeData(CrossDBStatement stmt) throws SQLException {
		// TODO Auto-generated method stub

	}

}
