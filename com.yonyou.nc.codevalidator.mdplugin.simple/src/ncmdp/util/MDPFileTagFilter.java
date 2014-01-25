package ncmdp.util;

import java.io.File;
import java.io.FileFilter;

import ncmdp.tool.basic.StringUtil;

public class MDPFileTagFilter implements FileFilter {
	/* 接受的扩展名列表，传null列出全部 */
	String[] fileTags = null;

	public MDPFileTagFilter(String[] fileTags) {
		this.fileTags = fileTags;
	}

	@Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory() || fileTags == null || fileTags.length == 0) {
			return true;
		}
		for (String fileTag : fileTags) {
			if (StringUtil.isEmptyWithTrim(fileTag)) {
				continue;
			}
			if (pathname.getName().endsWith("." + fileTag)) {
				return true;
			}
		}
		return false;
	}

}
