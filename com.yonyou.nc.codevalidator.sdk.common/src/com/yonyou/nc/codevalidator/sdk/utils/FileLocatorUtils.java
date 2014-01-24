package com.yonyou.nc.codevalidator.sdk.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * �ļ�������һЩ�����෽��
 * 
 * @author mazhqa
 * @since V1.0
 */
public final class FileLocatorUtils {

	/**
	 * ֻ�����ļ��е�file filter
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
	 * ���ļ����²���ָ���ļ��������ļ��У��ݹ��ѯ�����Դ�Сд
	 * <p>
	 * ע�����Ŀ¼�´������Ŀ¼��ͬ���ļ��б��� a/b/a��ֻ���ص�һ��a�ļ���
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
	 * ���ļ����²���ָ���ļ��У��ļ����������fileSuffix��׺�����ļ���������ȫ·���в��ܰ���excludeFolderName
	 * @param directory
	 * @param excludeFolderName
	 *            - �ų��ļ����б������ĳ�����ƣ�ͨ��ʹ��ʱ����dbcreate
	 * @param fileSuffix
	 *            - ���иú�׺�����ļ����ڵ��ļ���
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
