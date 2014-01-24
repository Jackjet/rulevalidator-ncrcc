package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: ����<br>
 * Date: 2005-1-14<br>
 * Time: 15:16:17<br>
 * HashMap������������һ��HashMap, �������ֻ��һ�����ݣ����н��������ÿһ�е���������ֵ��ӦHashMap��һ���ؼ��ֺ���Ӧ��ֵ
 *
 */
public class MapProcessor extends BaseProcessor  {
    /**
	 * <code>serialVersionUID</code> ��ע��
	 */
    
    private static final long serialVersionUID = 1401425123064791536L;

	public Object processResultSet(ResultSet rs) throws SQLException {
        return rs.next() ? ProcessorUtils.toMap(rs) : null;
    }
}

