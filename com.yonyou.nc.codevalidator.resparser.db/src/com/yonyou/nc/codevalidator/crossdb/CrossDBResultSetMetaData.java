package com.yonyou.nc.codevalidator.crossdb;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * @nopublish
 * 
 */
public class CrossDBResultSetMetaData extends CrossDBObject implements Serializable, ResultSetMetaData {
    /**
     * <code>serialVersionUID</code> 的注释
     */
    private static final long serialVersionUID = 8584900593145673266L;

    int[] columnTypes = null;

    int[] isnullable = null;

    String[] colTypeNames = null;

    List<String> colLabels = new ArrayList<String>();

    List<String> nameList = new ArrayList<String>();

    private int[] Scale;

    private int[] Precision;

    public CrossDBResultSetMetaData(ResultSetMetaData r) {
        super();
        try {
            int columnCount = r.getColumnCount();
            columnTypes = new int[columnCount];
            isnullable = new int[columnCount];
            Scale = new int[columnCount];
            Precision = new int[columnCount];
            width = new int[columnCount];
            colTypeNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                isnullable[i] = r.isNullable(i + 1);
                columnTypes[i] = r.getColumnType(i + 1);
                colTypeNames[i] = r.getColumnTypeName(i + 1);
                Scale[i] = r.getScale(i + 1);
                try {
                    /**
                     * ORACLE 上发现会出现 java.lang.NumberFormatException:
                     * 4294967295， 可以忽略这个错误
                     */
                    Precision[i] = r.getPrecision(i + 1);
                    width[i] = r.getColumnDisplaySize(i + 1);
                } catch (NumberFormatException nfe) {
                }
                String name = (r.getColumnName(i + 1) + "").toUpperCase();
//                if (GU_CONVERT)
//                    name = noucFromUnicode(name);
                nameList.add(name);
                String columnLabel = r.getColumnLabel(i + 1);
                if (columnLabel == null) {
                    columnLabel = name;
                }
                colLabels.add(columnLabel);

            }
        } catch (Throwable e) {
          	Logger.error(e.getMessage(), e);
            columnTypes = null;
        }
    }

    /**
     * getCatalogName method comment.
     */
    public String getCatalogName(int column) throws java.sql.SQLException {
        return null;
    }

    public String getColumnClassName(int column) throws java.sql.SQLException {
        return null;
    }

    /**
     * getColumnCount method comment.
     */
    public int getColumnCount() throws java.sql.SQLException {
        if (columnTypes == null)
            throw new java.sql.SQLException("The number of columns is null!!没有行数");
        return columnTypes.length;
    }

    /**
     * getColumnDisplaySize method comment.
     */
    public int getColumnDisplaySize(int column) throws java.sql.SQLException {
        column--;
        if (columnTypes == null || column < 0 || columnTypes.length <= column)
            throw new java.sql.SQLException("column < 1 or column >= column length" + (column + 1));
        return width[column];
    }

    /**
     * getColumnLabel method comment.
     */
    public String getColumnLabel(int column) throws java.sql.SQLException {
        column--;
        if (columnTypes == null || column < 0 || columnTypes.length <= column)
            throw new java.sql.SQLException("column < 1 or column >= column length" + (column + 1));
        return (String) colLabels.get(column);
    }

    /**
     * getColumnName method comment.
     */
    public String getColumnName(int column) throws java.sql.SQLException {
        column--;
        if (columnTypes == null || column < 0 || columnTypes.length <= column)
            throw new java.sql.SQLException("column < 1 or column >= column length" + (column + 1));
        return (String) nameList.get(column);
    }

    /**
     * getColumnType method comment.
     */
    public int getColumnType(int column) throws java.sql.SQLException {
        column--;
        if (columnTypes == null || column < 0 || columnTypes.length <= column)
            throw new java.sql.SQLException("column < 1 or column >= column length" + (column + 1));
        return columnTypes[column];
    }

    /**
     * getColumnTypeName method comment.
     */
    public String getColumnTypeName(int column) throws java.sql.SQLException {
        column--;
        return colTypeNames[column];
    }

    /**
     * This method was created by a SmartGuide.
     * 
     * @param str
     *            java.lang.String
     */
    public int getNameIndex(String str) {
        return nameList.indexOf(str);

    }

    /**
     * getPrecision method comment.
     */
    public int getPrecision(int column) throws java.sql.SQLException {
        return Precision[column - 1];
    }

    /**
     * getScale method comment.
     */
    public int getScale(int column) throws java.sql.SQLException {
        return Scale[column - 1];
    }

    /**
     * getSchemaName method comment.
     */
    public String getSchemaName(int column) throws java.sql.SQLException {
        return null;
    }

    /**
     * getTableName method comment.
     */
    public String getTableName(int column) throws java.sql.SQLException {
        return null;
    }

    /**
     * isAutoIncrement method comment.
     */
    public boolean isAutoIncrement(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * isCaseSensitive method comment.
     */
    public boolean isCaseSensitive(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * isCurrency method comment.
     */
    public boolean isCurrency(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * isDefinitelyWritable method comment.
     */
    public boolean isDefinitelyWritable(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * isNullable method comment.
     */
    public int isNullable(int column) throws java.sql.SQLException {
        return 0;
    }

    /**
     * isReadOnly method comment.
     */
    public boolean isReadOnly(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * isSearchable method comment.
     */
    public boolean isSearchable(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * isSigned method comment.
     */
    public boolean isSigned(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * isWritable method comment.
     */
    public boolean isWritable(int column) throws java.sql.SQLException {
        return false;
    }

    /**
     * Returns a String that represents the value of this object.
     */
    public String toString() {
        // insert code to print the receiver here
        // this implementation simply forwards the message to super. You may
        // replace or supplement this.
        return super.toString();
    }

    private int[] width;

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {

        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
    	throw new UnsupportedOperationException();
    }
}