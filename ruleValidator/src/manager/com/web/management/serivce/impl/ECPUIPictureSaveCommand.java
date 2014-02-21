package com.web.management.serivce.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 同步ecp个性化图片文件命令
 * 
 * @author songhlc
 * @date 2013-11-21
 */
public class ECPUIPictureSaveCommand extends AbstractCommand {
	private static final Log logger = LogFactory
			.getLog(ECPUIPictureSaveCommand.class);
	private static final long serialVersionUID = 8521949207090016592L;
	/*
	 * 上传的文件
	 */
	private final byte[] resourceBytes;
	/*
	 * 图片文件时间戳作为文件名的一部分
	 */
	private final String strTimeStamp;
	/*
	 * 图片文件时间戳作为文件名的一部分
	 */
	private final String filename;

	/*
	 * 默认构造函数
	 */
	public ECPUIPictureSaveCommand(byte[] resourceBytes, String filename,
			String strTimeStamp) {
		super();
		this.resourceBytes = resourceBytes;
		this.strTimeStamp = strTimeStamp;
		this.filename = filename;
	}

	@Override
	public String getId() {
		return "ECPUIPictureSaveCommand";
	}

	@Override
	public Object run() {
		String fileListString = "";
		// 获取图片保存路径
		String url = ServerRuntime.getECPAppHome()
				+ "/component/ecp/common/management/customsetting/src/theme/default/img";
		File uploadfile = new File(url);
		if (!uploadfile.exists()) {
			uploadfile.mkdirs();
		}
		fileListString += this.filename + "?ts=" + this.strTimeStamp + " ";
		// 文件保存路径
		String filepath = uploadfile + "\\" + this.filename;
		FileOutputStream outfile = null;
		try {
			outfile = new FileOutputStream(filepath);
			IOUtils.copy(new ByteArrayInputStream(resourceBytes), outfile);
		} catch (FileNotFoundException e) {
			logger.error("没有找到文件:" + filepath, e);
		} catch (IOException e) {
			logger.error("文件:" + filepath + " 写入出错", e);
		} finally {
			IOUtils.closeQuietly(outfile);
		}
		return fileListString;
	}

	@Override
	public String getName() {
		return "ECPUIPictureSaveCommand";
	}

}
