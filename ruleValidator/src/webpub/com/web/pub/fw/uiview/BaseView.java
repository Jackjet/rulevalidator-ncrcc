package com.web.pub.fw.uiview;

import java.io.Serializable;

import net.sf.json.JSONObject;

/**
 * 基础视图封装类
 * 
 * @author lixln
 * 
 */
@SuppressWarnings("serial")
public class BaseView implements Serializable {
	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
}
