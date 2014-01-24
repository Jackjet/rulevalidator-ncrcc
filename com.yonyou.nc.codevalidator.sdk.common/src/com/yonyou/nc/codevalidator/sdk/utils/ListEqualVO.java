package com.yonyou.nc.codevalidator.sdk.utils;

import java.util.List;

/**
 * 列表比较结果对象， <li>如果相同，isEqual返回true，对象会全部放在sameObject中 <li>
 * 如果不同，isEqual返回false；将第一个参数list中存在而第二个参数中不存在的对象放在notExistDest中，
 * 将第二个参数list中存在而第一个参数中不存在的uixiang放在notExistSrc中
 * 
 * @author zhenglq
 * 
 * @param <T>
 */
public class ListEqualVO<T> {

	// 是否相等
	private Boolean isEqual;

	// 源对象中不存在目标对象
	private List<T> notExistSrc;

	// 目标对象中不存在源对象
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
