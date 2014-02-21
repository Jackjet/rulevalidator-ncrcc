package com.web.management.serivce.impl;

import java.util.Calendar;
import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.web.management.service.IManagementService;

public class ManagementServiceImpl implements IManagementService {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> uiPropConfigSave(String jsonData) {
		ECPUIPorpertiesUpdateCommand command = new ECPUIPorpertiesUpdateCommand(
				jsonData);
		return (Map<String, String>) CommandRunner.runRequestReply(command);
	}

	@Override
	public String uiManagementPictureSave(CommonsMultipartFile partFile) {
		Calendar cdCalendar = Calendar.getInstance();
		// 更新照片时间戳作为文件名的一部分
		String strTimeStamp = String.valueOf(cdCalendar.getTimeInMillis());
		// 获取文件
		byte[] resourceBytes = partFile.getBytes();
		// 获取文件扩展名
		int index = partFile.getOriginalFilename().lastIndexOf(".");
		String fileShortName = partFile.getOriginalFilename().substring(
				index + 1);
		// 获取完整文件名
		String filename = partFile.getFileItem().getFieldName() + strTimeStamp
				+ "." + fileShortName;
		ECPUIPictureSaveCommand command = new ECPUIPictureSaveCommand(
				resourceBytes, filename, strTimeStamp);
		return (String) CommandRunner.runRequestReply(command);
	}

}
