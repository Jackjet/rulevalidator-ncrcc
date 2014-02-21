package com.web.account.uiview;

import java.io.Serializable;

public class MessageUIView implements Serializable {

	/** serialVersionUID **/
	private static final long serialVersionUID = 1L;
	// 信息提示的标识
	private String msgFlag;

	private final String userNameNull = "请输入用户名";

	private final String userPwdNull = "请输入密码";

	private final String verificationCodeNull = "请输入验证码";

	private final String verificationCodeError = "验证码错误，请重新输入";

	private final String loginError = "登陆失败，用户名密码或密码错误。";

	private String errorMsg;

	public String getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getUserNameNull() {
		return userNameNull;
	}

	public String getUserPwdNull() {
		return userPwdNull;
	}

	public String getVerificationCodeNull() {
		return verificationCodeNull;
	}

	public String getVerificationCodeError() {
		return verificationCodeError;
	}

	public String getLoginError() {
		return loginError;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
