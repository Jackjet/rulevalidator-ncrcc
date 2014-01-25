package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util;

/**
 * sql��乹����������ƴдsql�����ٲ���Ҫ�Ĵ���
 * 
 * @since 1.0
 * @version 2013
 * @author wangfra
 */
public class SQLBuilder {

    /**
     * ���ƴдsql��StringBuffer
     */
    private StringBuffer buffer = new StringBuffer();

    /**
     * ��һ���ַ���ƴд��sql���
     * 
     * @param str �ַ���
     */
    public void append(String str) {
        this.buffer.append(str);
    }

    /**
     * ����String����ֵ����in����
     * 
     * @param name sql�ֶ���
     * @param values String����ֵ
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
     * �����ַ���ֵ���조���ڡ�����
     * 
     * @param name sql�ֶ���
     * @param value Stringֵ ����Ϊ�ա���Ϊ��֪���Ƿ�Ҫ���~
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
