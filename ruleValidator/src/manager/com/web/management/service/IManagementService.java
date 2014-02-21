package com.web.management.service;

import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface IManagementService {
	/*
	 * 集群同步更新ecpuiproperties文件
	 * @param jsonData 文件内容json字符串
	 * */
	public Map<String,String> uiPropConfigSave(String jsonData);
	
	/*
	 * 集群同步更新个性化图片文件
	 * @param partFile上传的图片文件
	 * */
	public String uiManagementPictureSave(CommonsMultipartFile partFile);
}
