package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 贺扬
 * Date: 2005-6-7
 * Time: 14:28:49
 * 列值处理器，返回一个阿ArrayList对象，结果集中有多行数据，该对象对应与结果集中某一列的值，该处理器通过结果集列的序号或名称来确定列
 */
public class ColumnListProcessor extends BaseProcessor {
    /**
	 * <code>serialVersionUID</code> 的注释
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

