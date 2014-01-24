package com.yonyou.nc.codevalidator.buildtool;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * 在自动构建脚本以及生成bundle时，经常需要手动调整bundle的版本，本工具可以直接调整bundle的目录
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
			System.out.println("构建工具是专门用于处理文件夹下的所有bundle版本控制，第一个参数: 对应的bundle文件夹；"
					+ "第二个参数，要更改的版本（版本中不能含有非数字，如果要设置的版本比当前版本低，不进行设置）");
			System.out.println("请重新输入参数!!!");
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
	 * 判断是否要设置的版本更高
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
		throw new RuntimeException(String.format("无法判断版本之间(当前：%s, 要设置的版本: %s)的大小关系...", currentVersion, bundleVersion));
	}

}
