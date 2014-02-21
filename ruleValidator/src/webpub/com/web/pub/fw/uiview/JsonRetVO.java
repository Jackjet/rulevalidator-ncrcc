package com.web.pub.fw.uiview;

import java.io.Serializable;

/**
 * ajax请求返回对象
 * 
 * @author:sunshine
 * @date:2014-1-24 下午5:00:25
 */
public class JsonRetVO implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-24 下午5:00:52
	 */
	private static final long serialVersionUID = -4979911984469148172L;

	private Boolean isSuccess;
	private String message;

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
