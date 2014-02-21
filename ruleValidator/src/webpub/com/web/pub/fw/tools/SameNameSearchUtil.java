package com.web.pub.fw.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SameNameSearchUtil {

	private static DotPrint p = new DotPrint();

	public static void main(String[] args) {

		String folderPath = "D:\\view\\NC_EC_ECP6.31_dev\\NC6_EC_VOB\\NC_EC_ECP"; // 搜索路径
		String[] fileNameFilters = new String[] { ".java", ".js", ".jsp",
				".html", ".jar" };// 文件名包含字符串

		Map<String, List<File>> sameNameFileMap = new HashMap<String, List<File>>();

		File folder = new File(folderPath);
		if (null != folder && folder.isDirectory()) {
			p.reset();
			search(folder, fileNameFilters, sameNameFileMap);
		}
		// System.out.println();
		// System.out.println("=====================================================");
		// 输出结果
		for (String fileName : sameNameFileMap.keySet()) {
			List<File> l = sameNameFileMap.get(fileName);
			if (null != l && l.size() > 1) {
				// System.out.println("*********************************");
				// System.out.println("**" + fileName + ":" + l.size());
				for (File f : l) {
					if (null != f) {
						// System.out.println(f.getAbsolutePath());
					}
				}
			}
		}
		// System.out.println("=====================================================");
	}

	public static void search(File targetFolder, String[] filters,
			Map<String, List<File>> sameNameFileMap) {

		if (null != targetFolder && null != sameNameFileMap
				&& targetFolder.isDirectory()) {

			File[] files = targetFolder.listFiles();
			if (null != files && files.length > 0) {
				for (File f : files) {
					p.print();
					if (null != f && f.isDirectory()) {
						search(f, filters, sameNameFileMap);
					} else if (null != f && f.isFile()) {
						String name = f.getName();
						if (!accepted(filters, name)) {
							continue;
						}
						List<File> fileList = sameNameFileMap.get(name);
						if (null == fileList) {
							fileList = new ArrayList<File>();
							sameNameFileMap.put(name, fileList);
						}

						fileList.add(f);
					}
				}
			}

		}

	}

	private static boolean accepted(String[] filters, String name) {
		boolean isAccepted = false;
		if (null != name && null != filters && filters.length > 0) {
			for (String filter : filters) {
				if (null != filter && !"".equals(filter)
						&& name.indexOf(filter) > 0) {
					isAccepted = true;
					break;
				}
			}
		}

		return isAccepted;
	}

	public static class DotPrint {

		private int curRow = 1;

		private int curCol = 0;

		public void reset() {
			// System.out.println();
			this.curRow = 1;
			this.curCol = 0;
		}

		public void print() {
			// System.out.print(".");
			curCol++;
			if (curRow == curCol) {
				curRow++;
				curCol = 0;
				// System.out.println();
			}
		}

	}
}
