package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * User: ����<br>
 * Date: 2005-1-14<br>
 * Time: 13:45:20<br>
 * <p>
 * ���鼯�ϴ�����������һ��ArrayList���ϣ������е�ÿһ��Ԫ����һ�����飬ÿ�������Ӧ������е�һ�����ݣ����н������ÿһ�ж�Ӧ�����һ��Ԫ��
 * </p>
 */
public class ArrayListProcessor extends BaseProcessor {
	/**
	 * <code>serialVersionUID</code> ��ע��
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
