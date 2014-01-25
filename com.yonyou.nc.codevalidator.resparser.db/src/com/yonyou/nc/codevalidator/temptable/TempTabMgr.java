package com.yonyou.nc.codevalidator.temptable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * @nopublish
 * @author hey
 * 
 */
public class TempTabMgr extends ThreadLocal<Map<Connection, Set<String>>> {

	public static TempTabMgr getInstance() {
		return instance;
	}

	private TempTabMgr()

	{
	}

	protected Map<Connection, Set<String>> initialValue() {
		return new HashMap<Connection, Set<String>>();
	}

	public void addTempTable(Connection conn, String tableName) {
		Map<Connection, Set<String>> map = get();
		Set<String> tableSet = (Set<String>) map.get(conn);
		if (tableSet == null) {
			tableSet = new HashSet<String>(8);
			map.put(conn, tableSet);
		}
		tableSet.add(tableName);
	}

	private String[] getTempTables(Connection conn) {
		Map<Connection, Set<String>> map = get();
		Set<String> tableSet = (Set<String>) map.get(conn);
		if (tableSet == null)
			return new String[0];
		else
			return (String[]) tableSet.toArray(new String[0]);
	}

	private void clearTempTables(Connection conn) {
		Map<Connection, Set<String>> map = get();
		Set<String> tableSet = (Set<String>) map.get(conn);
		if (tableSet != null)
			tableSet.clear();
	}

	public void dropTempTables(Connection conn) {
		String tables[] = getTempTables(conn);
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			for (int i = 0; i < tables.length; i++)
				try {
					stmt.execute("drop table " + tables[i]);
				} catch (SQLException e) {
					Logger.error(e.getMessage(), e);
				}

		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.error(e.getMessage(), e);
				}
		}
		clearTempTables(conn);
		return;
	}

	private static TempTabMgr instance = new TempTabMgr();
}
