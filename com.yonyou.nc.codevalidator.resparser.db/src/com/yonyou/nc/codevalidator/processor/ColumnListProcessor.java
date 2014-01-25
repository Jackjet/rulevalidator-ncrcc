package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ����
 * Date: 2005-6-7
 * Time: 14:28:49
 * ��ֵ������������һ����ArrayList���󣬽�������ж������ݣ��ö����Ӧ��������ĳһ�е�ֵ���ô�����ͨ��������е���Ż�������ȷ����
 */
public class ColumnListProcessor extends BaseProcessor {
    /**
	 * <code>serialVersionUID</code> ��ע��
	 */
	private static final long serialVersionUID = -851727907824262100L;


	private int columnIndex = 1;


    private String columnName = null;


    public ColumnListProcessor() {
        super();
    }


    public ColumnListProcessor(int columnIndex) {
        this.columnIndex = columnIndex;
    }


    public ColumnListProcessor(String columnName) {
        this.columnName = columnName;
    }

    public Object processResultSet(ResultSet rs) throws SQLException {
        List<Object> result = new ArrayList<Object>();
        while (rs.next()) {
            if (this.columnName == null) {
                result.add(rs.getObject(this.columnIndex));
            } else {
                result.add(rs.getObject(this.columnName));
            }
        }
        return result;
    }
 }

