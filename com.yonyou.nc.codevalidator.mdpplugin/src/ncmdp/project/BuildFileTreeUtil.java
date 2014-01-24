package ncmdp.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

/**
 * 创建资源管理树的util类
 * @author wangxmn
 *
 */
public class BuildFileTreeUtil {
	/**
	 * 获取nchome下的模型文件
	 * @return
	 */
	public static File[] getRefMDFiles() {
		ArrayList<File> al = new ArrayList<File>();
		String[] moduleNames = NCMDPTool.getExModuleNames();
		ArrayList<String> alModules = new ArrayList<String>(
				Arrays.asList(moduleNames));
		String ncHomeStr = NCMDPTool.getNCHome();
		File dir = new File(ncHomeStr + File.separator + "modules");
		File[] childs = dir.listFiles(new MDPFileFilter() );
		int count = childs == null ? 0 : childs.length;
		for (int i = 0; i < count; i++) {
			File f = childs[i];
			if (!alModules.contains(f.getName())) {
				al.add(f);
			}
		}
		return al.toArray(new File[0]);
	}

	/**
	 * 复制文件，没有进行任何改动。如果这个文件已经在系统中存在，这时候就会出现问题
	 * @param sourceFile
	 * @param destFile
	 */
	public static void copyFile(File sourceFile, File destFile) {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = new FileInputStream(sourceFile).getChannel();
			outChannel = new FileOutputStream(destFile).getChannel();
			ByteBuffer bf = ByteBuffer.allocate(1024);
			while (inChannel.read(bf) != -1) {
				bf.flip();
				outChannel.write(bf);
				bf.clear();
			}
		} catch (FileNotFoundException e) {
			MDPLogger.error(e.getMessage(),e);
		} catch (IOException e) {
			MDPLogger.error(e.getMessage(),e);
		} finally {
			if (inChannel != null) {
				try {
					inChannel.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(),e);
				}
			}
			if (outChannel != null) {
				try {
					outChannel.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(),e);
				}
			}
		}
	}
}
