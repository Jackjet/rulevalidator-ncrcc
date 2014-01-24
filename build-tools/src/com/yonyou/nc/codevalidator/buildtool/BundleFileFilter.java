package com.yonyou.nc.codevalidator.buildtool;

import java.io.File;
import java.io.FileFilter;

/**
 * 用于判断工程为bundle的文件夹过滤器
 * 
 * @author mazhqa
 * @since V2.9
 */
public class BundleFileFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			String manifestFilePath = String.format("%s/META-INF/MANIFEST.MF", file.getAbsolutePath());
			return new File(manifestFilePath).exists();
		}
		return false;
	}

}
