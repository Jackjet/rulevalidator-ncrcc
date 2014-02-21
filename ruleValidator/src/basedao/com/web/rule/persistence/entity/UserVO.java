package com.web.rule.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rv_users")
public class UserVO implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-7 ÏÂÎç3:28:42
	 */
	private static final long serialVersionUID = -1975738421899460527L;
	@Id
	@Column(name = "pk_user")
	private String pkUser;

	@Column(name = "user_code", unique = true)
	private String userCode;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "pwd")
	private String pwd;

	@Column(name = "cell_phone")
	private String cellPhone;

	@Column(name = "email")
	private String email;

	@Column(name = "sex")
	private String sex;

	@Column(name = "memo")
	private String memo;

	@Column(name = "create_ts")
	private String createTs;

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

}
