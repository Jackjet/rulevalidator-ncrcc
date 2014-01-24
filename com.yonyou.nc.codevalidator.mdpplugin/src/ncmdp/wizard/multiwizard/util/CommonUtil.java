package ncmdp.wizard.multiwizard.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;

public class CommonUtil {
	/**
	 * 解析 下列目录下 资源文件 中的 value- key的映射（不存在子目录） 元数据 只处理以2开头的
	 * 
	 * @param depCommonModule2
	 * @return
	 */
	public static Map<String, String> initCommonFile(List<String> depResDir) {
		Map<String, String> resMap = new HashMap<String, String>();

		ClassLoader cl = getCurClassLoader();

		// 异常打印
		File file1 = new File("c:/mdp1.log");
		try {
			if (!file1.exists()) {
				file1.createNewFile();
			}
			PrintStream ps = new PrintStream(new FileOutputStream(file1, true));
			System.setOut(ps);
		} catch (IOException e) {
			MDPLogger.error(e.getMessage(), e);
		}

		// 获得指定路径下所有properties文件
		List<String> propertyPathList = null;
		try {
			propertyPathList = getAllPropertiesPath(depResDir);
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		}
		// if (propertyPathList == null || propertyPathList.size() == 0) {
		// return resMap; }
		UTFProperties properties = new UTFProperties(getCurCharSet());
		for (String proPath : propertyPathList) {
			InputStream in = null;
			in = cl.getResourceAsStream(proPath);
			if (in != null) {
				try {
					in = new BufferedInputStream(in);
					properties.load(in);
					for (Object ss : properties.keySet()) {
						String key = (String) ss;
						String value = properties.getProperty(key);
						if (!StringUtil.isEmptyWithTrim(value)
								&& !StringUtil.isEmptyWithTrim(key)
								&& key.trim().startsWith("2")) {
							resMap.put(value, (String) ss);
						}
					}
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(), e);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (Exception e2) {
						}
					}
				}
			}
		}
		return resMap;
	}

	private static List<String> getAllPropertiesPath(List<String> depResDir)
			throws ZipException, IOException {
		List<String> propertyPathList = new ArrayList<String>();
		if (depResDir != null && depResDir.size() > 0) {
			Set<String> resModuleSet = new HashSet<String>();
			for (String resDir : depResDir) {
				resModuleSet.add(resDir);
			}
			File[] files = getAllJarFile();
			for (File file : files) {
				ZipFile zip = new ZipFile(file);
				Enumeration<ZipEntry> enumer = (Enumeration<ZipEntry>) zip
						.entries();
				while (enumer.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) enumer.nextElement();
					String name = entry.getName();
					if (!entry.isDirectory()
							&& name.startsWith("lang/simpchn/")
							&& name.endsWith(".properties")
							&& name.length() > 24) {
						if ("lang/simpchn/".length() >= name.lastIndexOf("/")) {
							continue;
						}
						String resModule = name
								.substring("lang/simpchn/".length(),
										name.lastIndexOf("/"));
						if (resModuleSet.contains(resModule)) {
							propertyPathList.add(name);
						}
					}
				}
			}
		}
		return propertyPathList;
	}

	private static ClassLoader getCurClassLoader() {
		URLClassLoader loader = null;
		ArrayList<URL> allUrls = new ArrayList<URL>();
		try {
			File[] jarFiles = getAllJarFile();
			for (File jarFile : jarFiles) {
				allUrls.add(jarFile.toURL());
			}
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		}
		loader = new URLClassLoader(allUrls.toArray(new URL[0]));
		return loader;
	}

	private static File[] getAllJarFile() {
		String ncHomeStr = NCMDPTool.getNCHome();
		File dir = new File(ncHomeStr + File.separator + "langlib");
		return dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("_sim_langres.jar");
			}
		});
	}

	public static String getCurCharSet() {
		// "GBK"
		File configFile = new File(NCMDPTool.getNCHome() + File.separator
				+ "ierp" + File.separator + "language" + File.separator
				+ "simpchn.lang");

		FileInputStream fis = null;
		if (!configFile.exists()) {
			return "GBK";
		}
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			fis = new FileInputStream(configFile);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			if ("language".equals(root.getNodeName())) {
				String charSet = root.getAttributes().getNamedItem("charset")
						.getNodeValue();
				if (!StringUtil.isEmptyWithTrim(charSet)) {
					return charSet;
				}
			}
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(), e);
				}
		}
		return "GBK";
	}
}
