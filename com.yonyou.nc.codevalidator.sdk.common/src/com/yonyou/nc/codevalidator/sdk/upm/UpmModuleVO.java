package com.yonyou.nc.codevalidator.sdk.upm;

import java.util.List;

/**
 * upm模块级别的vo
 * @author mazhqa
 * @since V1.0
 */
public class UpmModuleVO {

	private String moduleName;

	private List<UpmComponentVO> pubComponentVoList;

	private List<UpmComponentVO> priComponentVoList;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<UpmComponentVO> getPubComponentVoList() {
		return pubComponentVoList;
	}

	public void setPubComponentVoList(List<UpmComponentVO> pubComponentVoList) {
		this.pubComponentVoList = pubComponentVoList;
	}

	public List<UpmComponentVO> getPriComponentVoList() {
		return priComponentVoList;
	}

	public void setPriComponentVoList(List<UpmComponentVO> priComponentVoList) {
		this.priComponentVoList = priComponentVoList;
	}

}
