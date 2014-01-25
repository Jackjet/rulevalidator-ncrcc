package com.yonyou.nc.codevalidator.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
public class UserVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8157478620433215097L;

	@Id
	private String pkUser;
	@Column(name = "user_code")
	private String userCode;
	@Column(name = "password")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "create_ts")
	private String createTs;
	@Column(name = "certificate")
	private String certificate;
	@Column(name = "is_lock")
	private String isLock;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
