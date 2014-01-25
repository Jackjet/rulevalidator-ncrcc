package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: ����<br>
 * Date: 2005-1-14<br>
 * Time: 13:43:06<br>
 * ��������������������Զ��ر�ResultSet����������Ҫʵ��processResultSet����
 */
public abstract class BaseProcessor implements ResultSetProcessor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object handleResultSet(ResultSet rs) throws SQLException {
        if (rs == null)
            throw new IllegalArgumentException("resultset parameter can't be null");
        try {
            return processResultSet(rs);
        } catch (SQLException e) {
            throw new SQLException("the resultsetProcessor error!" + e.getMessage(), e.getSQLState());
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {

                }

        }

    }

    /**
     * ��������������Ҫ�Ķ���
     * 
     * @param rs
     *            �����
     * @return ��Ҫ�Ķ���
     * @throws SQLException�����������
     */
    public abstract Object processResultSet(ResultSet rs) throws SQLException;

}
