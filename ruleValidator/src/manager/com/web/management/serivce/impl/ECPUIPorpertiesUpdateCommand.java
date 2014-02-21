package com.web.management.serivce.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.web.pub.fw.tools.PropertyFileTool;

/**
 * 同步uiproperties文件命令
 * 
 * @author songhlc
 * @date 2013-11-21
 */
public class ECPUIPorpertiesUpdateCommand extends AbstractCommand {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory
			.getLog(ECPCacheRemoveCommand.class);
	private final String jsonData;

	public ECPUIPorpertiesUpdateCommand(String jsonData) {
		super();
		this.jsonData = jsonData;
	}

	@Override
	public String getId() {
		return "ECPUIPorpertiesUpdateCommand";
	}

	@Override
	public Object run() {
		Map<String, String> map = new HashMap<String, String>();
		InputStream is = null;
		InputStreamReader isr = null;
		Boolean result = false;
		try {
			// 读取应用下的配置文件
			String path = ServerRuntime.getECPAppHome()
					+ "/WEB-INF/classes/ecpui.properties";
			JSONObject jsonObj = JSONObject.fromObject(this.jsonData);
			@SuppressWarnings("unchecked")
			Map<String, String> jsonMap = jsonObj;
			result = PropertyFileTool.setProfileString(path, jsonMap);
		} catch (IOException e) {
			logger.error("读取系统配置配置文件ecpsystem.properties错误!", e);
		} finally {
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
		if (result) {
			map.put("result", "保存成功");
		} else {
			map.put("result", "读取系统配置配置文件ecpsystem.properties错误!");
		}
		return map;
	}

	@Override
	public String getName() {
		return "ECPUIPorpertiesUpdateCommand";
	}

}
