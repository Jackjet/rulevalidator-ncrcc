package com.yonyou.nc.codevalidator.dao;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.type.SQLTypeFactory;

/**
 * Created by IntelliJ IDEA. User: ���� Date: 2005-1-18 Time: 13:37:47 Ԥ�����ѯ��������
 */
public class SQLParameter implements Serializable {

	private List<Object> paramList = new ArrayList<Object>();

	static final long serialVersionUID = 1118941587227355811L;

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public SQLParameter() {
	}

	public SQLParameter copyOfMyself() {
		SQLParameter newParam = new SQLParameter();
		newParam.paramList.addAll(this.paramList);
		return newParam;
	}

	/**
	 * ����һ��Null��������
	 * 
	 * @param type
	 *            ���������Ͳο�java.sql.Types
	 */
	public void addNullParam(int type) {
		paramList.add(SQLTypeFactory.getNullType(type));
	}

	/**
	 * ����һ��Blob��������
	 * 
	 * @param blob
	 *            �������
	 */
	public void addBlobParam(Object blob) {
		if (blob == null)
			addNullParam(Types.BLOB);
		else
			paramList.add(SQLTypeFactory.getBlobType(blob));
	}

	/**
	 * ����һ��Blob��������
	 * 
	 * @param blob
	 *            �ֽ��������
	 */
	public void addBlobParam(byte[] bytes) {
		if (bytes == null)
			addNullParam(Types.BLOB);
		else
			paramList.add(SQLTypeFactory.getBlobType(bytes));
	}

	/**
	 * ����һ��Blob��������
	 * 
	 * @param stream
	 *            �ֽ���
	 * @param length
	 *            ����
	 */
	public void addBlobParam(InputStream stream, int length) {
		if (stream == null)
			addNullParam(Types.BLOB);
		else
			paramList.add(SQLTypeFactory.getBlobType(stream, length));
	}

	/**
	 * ����һ��Clob��������
	 * 
	 * @param clob
	 */
	public void addClobParam(String clob) {
		if (clob == null)
			addNullParam(Types.CLOB);
		else
			paramList.add(SQLTypeFactory.getClobType(clob));
	}

	/**
	 * ����һ��Clob��������
	 * 
	 * @param reader
	 *            �ַ���
	 * @param length
	 *            ����
	 */
	public void addClobParam(Reader reader, int length) {
		if (reader == null)
			addNullParam(Types.CLOB);
		else
			paramList.add(SQLTypeFactory.getClobType(reader, length));
	}

	/**
	 * ����һ��������������ע��ò�������Ϊ��
	 * 
	 * @param param
	 *            �������
	 */
	public void addParam(Object param) {
		if (param == null)
			throw new IllegalArgumentException("SQL Parameter object cannot be null, which can be replaced by NullType Object!!");
		paramList.add(param);

	}

	/**
	 * ����һ�����Ͳ���
	 * 
	 * @param param
	 */
	public void addParam(Integer param) {
		if (param == null) {
			addNullParam(Types.INTEGER);
		} else {
			paramList.add(param);
		}
	}

	/**
	 * ����һ���ַ������Ͳ���
	 * 
	 * @param param
	 */
	public void addParam(String param) {
		if (param == null) {
			addNullParam(Types.VARCHAR);
		} else if (param.equals("")) {
			addNullParam(Types.VARCHAR);
		} else {
			paramList.add(param);
		}
	}

	/**
	 * ����һ���������Ͳ���
	 * 
	 * @param param
	 */
	public void addParam(int param) {
		paramList.add(Integer.valueOf(param));
	}

	/**
	 * ����һ�����������Ͳ���
	 * 
	 * @param param
	 */
	public void addParam(long param) {
		paramList.add(Long.valueOf(param));
	}

	/**
	 * ����һ��˫�������Ͳ���
	 * 
	 * @param param
	 */
	public void addParam(double param) {
		paramList.add(Double.valueOf(param));
	}

	/**
	 * ����һ���������Ͳ���
	 * 
	 * 
	 * @param param
	 */
	public void addParam(boolean param) {
		paramList.add(Boolean.valueOf(param));
	}

	/**
	 * ����һ���������Ͳ���
	 * 
	 * @param param
	 */
	public void addParam(float param) {
		paramList.add(Float.valueOf(param));
	}

	/**
	 * ����һ�����������Ͳ���
	 * 
	 * @param param
	 */
	public void addParam(short param) {
		paramList.add(Short.valueOf(param));
	}

	/**
	 * ���������õ���������
	 * 
	 * @param index
	 *            ������˳������
	 * @return
	 */
	public Object get(int index) {
		return paramList.get(index);
	}

	/**
	 * �������滻���������ֲ��������ԭʼ��Ϣ�����������¹����������.
	 * 
	 * @param index
	 *            Ҫ�滻�����������0��ʼ����
	 * @param obj
	 */
	public void replace(int index, Object param) {
		if (param == null)
			throw new IllegalArgumentException("SQL Parameter object cannot be null, which can be replaced by NullType Object!!");
		paramList.remove(index);
		paramList.add(index, param);
	}

	/**
	 * �������滻���������ֲ��������ԭʼ��Ϣ�����������¹����������.
	 * 
	 * @param index
	 *            Ҫ�滻�����������0��ʼ����
	 * @param param
	 *            �ַ�������
	 */
	public void replace(int index, String param) {
		paramList.remove(index);
		if (param == null)
			paramList.add(SQLTypeFactory.getNullType(Types.VARCHAR));
		else
			paramList.add(index, param);

	}

	/**
	 * ������в���
	 */
	public void clearParams() {
		paramList.clear();
	}

	/**
	 * �õ������ĸ���
	 * 
	 * @return ���ز����ĸ���
	 */
	public int getCountParams() {
		return paramList.size();
	}

	/**
	 * �õ����в�������
	 * 
	 * @return ���ز����ļ���
	 */
	public List<Object> getParameters() {
		return paramList;
	}

	public String toString() {
		return "SQLParameter--" + paramList + "";
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final SQLParameter that = (SQLParameter) o;

		return !(paramList != null ? !paramList.equals(that.paramList)
				: that.paramList != null);

	}

	public int hashCode() {
		return (paramList != null ? paramList.hashCode() : 0);
	}

}