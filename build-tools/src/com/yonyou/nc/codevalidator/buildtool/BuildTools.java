package com.yonyou.nc.codevalidator.buildtool;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * ���Զ������ű��Լ�����bundleʱ��������Ҫ�ֶ�����bundle�İ汾�������߿���ֱ�ӵ���bundle��Ŀ¼
 * 
 * @author mazhqa
 * @since V2.9
 */
public class BuildTools {

	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final Pattern MATCH_VERSION_PATTERN = Pattern.compile("Bundle-Version: ([\\d.]+)");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 2) {
			System.out.println("����������ר�����ڴ����ļ����µ�����bundle�汾���ƣ���һ������: ��Ӧ��bundle�ļ��У�"
					+ "�ڶ���������Ҫ���ĵİ汾���汾�в��ܺ��з����֣����Ҫ���õİ汾�ȵ�ǰ�汾�ͣ����������ã�");
			System.out.println("�������������!!!");
			return;
		}
		String bundleFolderPath = args[0].trim();
		String bundleVersion = args[1].trim();

		File bundleWorkspaceFolder = new File(bundleFolderPath);
		File[] bundleFolders = bundleWorkspaceFolder.listFiles(new BundleFileFilter());
		for (File bundleFolder : bundleFolders) {
			File metaInfFile = new File(String.format("%s/META-INF/MANIFEST.MF", bundleFolder.getAbsolutePath()));
			try {
				String originContent = FileUtils.readFileToString(metaInfFile, DEFAULT_ENCODING);
				Matcher matcher = MATCH_VERSION_PATTERN.matcher(originContent);
				if (matcher.find()) {
					String currentVersion = matcher.group(1).trim();
					if (isSetVersionNewer(currentVersion, bundleVersion)) {
						String newContent = originContent.replaceFirst(currentVersion, bundleVersion);
						FileUtils.writeStringToFile(metaInfFile, newContent, DEFAULT_ENCODING);
						System.out.println(String.format(
								"in bundle(%s): bundle version(%s) is older than current(%s), reset",
								bundleFolder.getName(), bundleVersion, currentVersion));
					} else {
						System.out.println(String.format(
								"in bundle(%s): bundle version(%s) is newer than current(%s), not set",
								bundleFolder.getName(), bundleVersion, currentVersion));
					}
				} else {
					System.out.println(String.format("can't find version in bundle : %s", bundleFolder.getName()));
				}
			} catch (IOException e) {
				System.out.println("IOException occured in read or write file");
				e.printStackTrace();
			} finally {
			}
		}
	}

	/**
	 * �ж��Ƿ�Ҫ���õİ汾����
	 * 
	 * @param currentVersion
	 * @param bundleVersion
	 * @return
	 */
	private static boolean isSetVersionNewer(String currentVersion, String bundleVersion) {
		if (currentVersion.equals(bundleVersion)) {
			return false;
		}
		String[] currentVersionArray = currentVersion.split("\\.");
		String[] bundleVersionArray = bundleVersion.split("\\.");
		int arrayLength = currentVersion.length() > bundleVersionArray.length ? currentVersionArray.length
				: bundleVersionArray.length;
		for (int i = 0; i < arrayLength; i++) {
			if (currentVersionArray.length < i + 1) {
				return true;
			}
			if (bundleVersionArray.length < i + 1) {
				return false;
			}
			int currentVersionSegment = Integer.parseInt(currentVersionArray[i]);
			int bundleVersionSegment = Integer.parseInt(bundleVersionArray[i]);
			if (currentVersionSegment != bundleVersionSegment) {
				return bundleVersionSegment > currentVersionSegment;
			}
		}
		throw new RuntimeException(String.format("�޷��жϰ汾֮��(��ǰ��%s, Ҫ���õİ汾: %s)�Ĵ�С��ϵ...", currentVersion, bundleVersion));
	}

}
