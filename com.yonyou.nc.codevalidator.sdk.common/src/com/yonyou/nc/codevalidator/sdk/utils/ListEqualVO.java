package com.yonyou.nc.codevalidator.sdk.utils;

import java.util.List;

/**
 * �б�ȽϽ������ <li>�����ͬ��isEqual����true�������ȫ������sameObject�� <li>
 * �����ͬ��isEqual����false������һ������list�д��ڶ��ڶ��������в����ڵĶ������notExistDest�У�
 * ���ڶ�������list�д��ڶ���һ�������в����ڵ�uixiang����notExistSrc��
 * 
 * @author zhenglq
 * 
 * @param <T>
 */
public class ListEqualVO<T> {

	// �Ƿ����
	private Boolean isEqual;

	// Դ�����в�����Ŀ�����
	private List<T> notExistSrc;

	// Ŀ������в�����Դ����
	private List<T> notExistDest;

	private List<T> sameObj;

	public Boolean getIsEqual() {
		return isEqual;
	}

	public void setIsEqual(Boolean isEqual) {
		this.isEqual = isEqual;
	}

	public List<T> getNotExistSrc() {
		return notExistSrc;
	}

	public void setNotExistSrc(List<T> notExistSrc) {
		this.notExistSrc = notExistSrc;
	}

	public List<T> getNotExistDest() {
		return notExistDest;
	}

	public void setNotExistDest(List<T> notExistDest) {
		this.notExistDest = notExistDest;
	}

	public List<T> getSameObj() {
		return sameObj;
	}

	public void setSameObj(List<T> sameObj) {
		this.sameObj = sameObj;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("the result is " + isEqual + "\t\n");
		sb.append("the same object is " + sameObj.toString());
		sb.append("the src object is " + notExistSrc.toString());
		sb.append("the dest object is " + notExistDest.toString());
		return sb.toString();
	}
}
