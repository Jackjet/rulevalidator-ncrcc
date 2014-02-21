package com.web.pub.fw.factory;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-11 下午1:38:55
 */
public class UserContext implements Serializable {
	private static final long serialVersionUID = -7109144523264236997L;

	public UserContext() {
		// 创建之初，上次修改日期为系统当前日期
		this.lastDate = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String userPassword;

	/**
	 * 用户类型 0-集团管理员类型 1-普通用户类型 2-帐套管理员
	 * 
	 * @see nc.login.vo.INCUserTypeConstant 用户类型
	 */
	private Integer userType;

	/**
	 * 会话上次访问日期
	 */
	private long lastDate;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getUserType() {
		return userType;
	}

	public long getLastDate() {
		return lastDate;
	}

	public void setLastDate(long lastDate) {
		this.lastDate = lastDate;
	}

}
