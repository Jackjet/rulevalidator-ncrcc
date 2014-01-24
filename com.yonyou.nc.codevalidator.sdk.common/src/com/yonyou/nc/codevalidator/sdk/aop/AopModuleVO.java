package com.yonyou.nc.codevalidator.sdk.aop;

import java.util.ArrayList;
import java.util.List;

/**
 * ��Ӧaopģ��VO����
 * @author mazhqa
 * @since V2.1
 */
public class AopModuleVO {

	private int priority;
	
	private List<AopAspectVO> aopAspectVoList = new ArrayList<AopAspectVO>();

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<AopAspectVO> getAopAspectVoList() {
		return aopAspectVoList;
	}

	public void setAopAspectVoList(List<AopAspectVO> aopAspectVoList) {
		this.aopAspectVoList = aopAspectVoList;
	}

}
