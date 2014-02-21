package com.web.pub.page.paravo;

import java.io.Serializable;

import com.web.pub.page.handler.PubQryPara;

public class ResourQryParaVO extends PubQryPara implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-24 ÉÏÎç11:27:04
	 */
	private static final long serialVersionUID = -4097473392688127651L;

	private String pkRes;
	private String resCode;
	private String resName;

	public String getPkRes() {
		return pkRes;
	}

	public void setPkRes(String pkRes) {
		this.pkRes = pkRes;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

}
