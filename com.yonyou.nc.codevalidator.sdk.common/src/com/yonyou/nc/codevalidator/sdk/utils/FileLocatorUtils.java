package com.yonyou.nc.codevalidator.sdk.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * 文件操作的一些工具类方法
 * 
 * @author mazhqa
 * @since V1.0
 */
public final class FileLocatorUtils {

	/**
	 * 只过滤文件夹的file filter
	 */
	private static final FileFilter DIRECTORY_FILE_FILTER = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};

	private FileLocatorUtils() {

	}

	public static String getComponentCodePath(BusinessComponent busisBusinessComponent) {
		String codePath = busisBusinessComponent.getCodePath();
		return codePath.replace("//", "/");
	}

	public static String getComponentClientPath(BusinessComponent businessComponent) {
		return getComponentCodePath(businessComponent) + "/client/";
	}

	public static String getComponentSrcPath(BusinessComponent businessComponent) {
		return businessComponent.getBusinessComponentPath() + "/src/";
	}

	/**
	 * 在文件夹下查找指定文件名的子文件夹，递归查询，忽略大小写
	 * <p>
	 * 注：如果目录下存在与此目录相同的文件夹比如 a/b/a，只返回第一层a文件夹
	 * 
	 * @param directory
	 * @param folderName
	 * @return
	 */
	public static Collection<File> findDirWithGivenName(File directory, final String folderName) {
		Collection<File> result = new ArrayList<File>();

		if (directory.isDirectory()) {
			File[] dirFiles = directory.listFiles(DIRECTORY_FILE_FILTER);
			innerFindDirs(dirFiles, result, new DirFileFilter() {

				@Override
				public boolean filterFolder(File folder) {
					return folder.getName().equalsIgnoreCase(folderName);
				}
			});
		}
		return result;
	}

	/**
	 * 在文件夹下查找指定文件夹，文件夹中须包含fileSuffix后缀名的文件，并且其全路径中不能包含excludeFolderName
	 * @param directory
	 * @param excludeFolderName
	 *            - 排除文件夹列表，如果是某个名称，通常使用时会是dbcreate
	 * @param fileSuffix
	 *            - 带有该后缀名的文件所在的文件夹
	 * @return
	 */
	public static Collection<File> findDirWithSuffixFiles(File directory, final String excludeFolderName,
			final String fileSuffix) {
		Collection<File> result = new ArrayList<File>();

		if (directory.isDirectory()) {
			File[] dirFiles = directory.listFiles(DIRECTORY_FILE_FILTER);
			innerFindDirs(dirFiles, result, new DirFileFilter() {

				@Override
				public boolean filterFolder(File folder) {
					if (folder.getAbsolutePath().contains(excludeFolderName)) {
						return false;
					}
					File[] sqlFiles = folder.listFiles(new FileFilter() {

						@Override
						public boolean accept(File pathname) {
							return pathname.getName().toLowerCase().endsWith(fileSuffix.toLowerCase());
						}
					});
					return sqlFiles.length > 0;
				}
			});
		}
		return result;
	}

	private static void innerFindDirs(File[] dirFiles, Collection<File> result, DirFileFilter dirFileFilter) {
		if (dirFiles != null && dirFiles.length > 0) {
			for (File dirFile : dirFiles) {
				// if (dirFile.getName().equalsIgnoreCase(folderName)) {
				if (dirFileFilter.filterFolder(dirFile)) {
					result.add(dirFile);
				} else {
					File[] listFiles = dirFile.listFiles(DIRECTORY_FILE_FILTER);
					innerFindDirs(listFiles, result, dirFileFilter);
				}
			}
		}
	}

	public static interface DirFileFilter {
		boolean filterFolder(File folder);
	}

}
