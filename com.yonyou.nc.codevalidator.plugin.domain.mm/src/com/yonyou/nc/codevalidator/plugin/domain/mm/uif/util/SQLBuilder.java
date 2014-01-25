package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util;

/**
 * sql语句构造器。方便拼写sql，减少不必要的错误
 * 
 * @since 1.0
 * @version 2013
 * @author wangfra
 */
public class SQLBuilder {

    /**
     * 存放拼写sql的StringBuffer
     */
    private StringBuffer buffer = new StringBuffer();

    /**
     * 将一个字符串拼写入sql语句
     * 
     * @param str 字符串
     */
    public void append(String str) {
        this.buffer.append(str);
    }

    /**
     * 对于String数组值构造in条件
     * 
     * @param name sql字段名
     * @param values String数组值
     */
    public void append(String name, String[] values) {
        int length = values.length;
        if (length == 1) {
            this.append(name, values[0]);
            return;
        }
        this.buffer.append(name);
        this.buffer.append(" in (");
        for (int i = 0; i < length; i++) {
            this.buffer.append("'");
            this.buffer.append(values[i]);
            this.buffer.append("'");
            this.buffer.append(",");
        }
        length = this.buffer.length();
        this.buffer.deleteCharAt(length - 1);
        this.buffer.append(") ");
    }

    /**
     * 对于字符串值构造“等于”条件
     * 
     * @param name sql字段名
     * @param value String值 不能为空。因为不知道是否要添加~
     */
    public void append(String name, String value) {
        this.buffer.append(name);
        this.buffer.append("='");
        this.buffer.append(value);
        this.buffer.append("' ");
    }

    @Override
    public String toString() {
        return this.buffer.toString();
    }
}
