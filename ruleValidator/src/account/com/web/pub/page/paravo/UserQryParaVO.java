package com.web.pub.page.paravo;

import java.io.Serializable;

import com.web.pub.page.handler.PubQryPara;

public class UserQryParaVO extends PubQryPara implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-24 ÉÏÎç11:27:04
	 */
	private static final long serialVersionUID = -4097473392688127651L;

	private String pkUser;
	private String userCode;
	private String userName;

	public String getPkUser() {
		return pkUser;
	}

	public void setPkUser(String pkUser) {
		this.pkUser = pkUser;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
