package com.web.account.uiview;

import java.io.Serializable;

import com.web.pub.fw.uiview.UIView;

public class UserUIView extends UIView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-23 下午3:18:14
	 */
	private static final long serialVersionUID = -8864259116697798413L;

	// /**
	// * 用户主键
	// */
	// private String cuserid;
	//
	// /**
	// * 用户编码
	// */
	// private String user_code;
	//
	// /**
	// * 用户名称
	// */
	// private String user_name;
	//
	// /**
	// * 用户密码
	// */
	// private String user_password;

	// 验证码
	private String verificationCode;

	private String pkUser;

	private String userCode;

	private String userName;

	private String pwd;

	private String cellPhone;

	private String email;

	private String sex;

	private String memo;

	private String createTs;

	private String certificate;

	private String isLock;

	private String login_type;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLogin_type() {
		return login_type;
	}

	public void setLogin_type(String login_type) {
		this.login_type = login_type;
	}

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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	// public String getCuserid() {
	// return cuserid;
	// }
	//
	// public void setCuserid(String cuserid) {
	// this.cuserid = cuserid;
	// }
	//
	// public String getUser_code() {
	// return user_code;
	// }
	//
	// public void setUser_code(String user_code) {
	// this.user_code = user_code;
	// }
	//
	// public String getUser_name() {
	// return user_name;
	// }
	//
	// public void setUser_name(String user_name) {
	// this.user_name = user_name;
	// }
	//
	// public String getUser_password() {
	// return user_password;
	// }
	//
	// public void setUser_password(String user_password) {
	// this.user_password = user_password;
	// }

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
