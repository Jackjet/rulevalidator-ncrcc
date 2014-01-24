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
 * ������Դ��������util��
 * @author wangxmn
 *
 */
public class BuildFileTreeUtil {
	/**
	 * ��ȡnchome�µ�ģ���ļ�
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
	 * �����ļ���û�н����κθĶ����������ļ��Ѿ���ϵͳ�д��ڣ���ʱ��ͻ��������
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
