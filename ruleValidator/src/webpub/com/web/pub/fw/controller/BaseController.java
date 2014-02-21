package com.web.pub.fw.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.web.pub.fw.editor.BigDecimalEditor;
import com.web.pub.fw.editor.BooleanEditor;
import com.web.pub.fw.editor.DateEditor;
import com.web.pub.fw.editor.StringEditor;

/**
 * 控制器父类
 * 
 * @author:sunshine
 * @date:2013-12-25 上午10:27:57
 */
public class BaseController {

	/** 获取HttpServletResponse */
	private HttpServletResponse response;

	/** 获取HttpServletRequest */
	private HttpServletRequest request;

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpSession getSession() {
		return this.request.getSession();
	}

	/**
	 * 转化String到Date
	 * 
	 * @param binder
	 * @throws Exception
	 * @author:sunshine
	 * @date:2013-12-25 上午10:28:09
	 */
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		binder.registerCustomEditor(Boolean.class, new BooleanEditor());
		binder.registerCustomEditor(BigDecimal.class, new BigDecimalEditor());
		binder.registerCustomEditor(String.class, new StringEditor());
	}
}
