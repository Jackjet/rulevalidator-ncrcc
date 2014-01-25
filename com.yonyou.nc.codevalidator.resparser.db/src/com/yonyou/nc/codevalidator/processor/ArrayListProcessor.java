package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * User: 贺扬<br>
 * Date: 2005-1-14<br>
 * Time: 13:45:20<br>
 * <p>
 * 数组集合处理器，返回一个ArrayList集合，集合中的每一个元素是一个数组，每个数组对应结果集中的一行数据，其中结果集中每一列对应数组的一个元素
 * </p>
 */
public class ArrayListProcessor extends BaseProcessor {
	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = -3631733378522079801L;

	public Object processResultSet(ResultSet rs) throws SQLException {
		List<Object[]> result = new ArrayList<Object[]>();
		while (rs.next()) {
			result.add(ProcessorUtils.toArray(rs));
		}
		return result;
	}

}
