package com.yonyou.nc.codevalidator.buildtool;

import java.io.File;
import java.io.FileFilter;

/**
 * �����жϹ���Ϊbundle���ļ��й�����
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
