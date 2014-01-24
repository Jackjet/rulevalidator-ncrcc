package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * User: ����<br>
 * Date: 2005-1-14<br>
 * Time: 15:17:32<br>
 *HashMap���ϴ�����������һ��ArrayList���ϣ������е�ÿһ��Ԫ����һ��HashMap��ÿ��HashMap��Ӧ������е�һ������, ���н��������ÿһ�е���������ֵ��ӦHashMap��һ���ؼ��ֺ���Ӧ��ֵ
 */
public class MapListProcessor extends BaseProcessor {


    /**
	 * <code>serialVersionUID</code> ��ע��
	 */
	private static final long serialVersionUID = -8235754267454898488L;

	public Object processResultSet(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            results.add(ProcessorUtils.toMap(rs));
        }
        return results;
    }

}
