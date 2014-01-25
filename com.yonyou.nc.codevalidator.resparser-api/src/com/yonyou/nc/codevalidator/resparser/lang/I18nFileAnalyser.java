package com.yonyou.nc.codevalidator.resparser.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 多语jar包读取properties的分析器
 * 
 * @author luoweid
 * @modify mazhqa
 */
public class I18nFileAnalyser {


	/**
	 * 分析jar文件， 根据其中的properties文件，得出所有的多语elements
	 * @param files - jar文件列表
	 * @return
	 * @throws RuleBaseException 
	 */
	public static Map<String, I18nPropertyElement> analyseI18nElements(File[] files) throws RuleBaseException {
		Map<String, I18nPropertyElement> resIdToPropertyMap = new HashMap<String, I18nPropertyElement>();
		for (File file : files) {
			try {
				JarFile jarFile = new JarFile(file);
				I18nType langType = getJarI18nType(file);
				Enumeration<JarEntry> enumeration = jarFile.entries();
				while (enumeration.hasMoreElements()) {
					processJarEntry(file, jarFile, enumeration.nextElement(), langType, resIdToPropertyMap);
				}
			} catch (IOException e) {
				throw new RuleBaseException(e);
			}
		}
		return resIdToPropertyMap;
	}
	
	/**
	 * 分析多个csv属性配置文件，返回分析完成的map
	 * @param file
	 * @return
	 * @throws RuleBaseException 
	 */
	public static Map<String, I18nPropertyElement> analyseCsvElements(Collection<File> fileList) throws RuleBaseException{
		Map<String, I18nPropertyElement> resIdToPropertyMap = new HashMap<String, I18nPropertyElement>();
		for (File csvFile : fileList) {
			FileInputStream fis = null;
			I18nType langType = getCsvI18nType(csvFile);
			try {
				fis = new FileInputStream(csvFile);
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(isr);
				String line;
				reader.readLine();
				reader.readLine();
				while ((line = reader.readLine()) != null) {
					// System.out.println(line);
					addCsvElementToMap(langType, line, csvFile.getAbsolutePath(), resIdToPropertyMap);
				}
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
				throw new RuleBaseException(e);
			} finally {
				IOUtils.closeQuietly(fis);
			}
		}
		return resIdToPropertyMap;
	}
	
	private static void addCsvElementToMap(I18nType langType, String readResource, String filePath, Map<String, I18nPropertyElement> resIdToPropertyMap) {
		if (null == readResource || 0 == readResource.trim().length()) {
			return;
		}
		int firstEquInx = readResource.indexOf(',');
		if (-1 == firstEquInx) {
			return;
		}
		String resId = readResource.substring(0, firstEquInx).trim();
		String resLineValue = readResource.substring(firstEquInx + 1);
		I18nPropertyElement propertyElement = null;
		if (resIdToPropertyMap.containsKey(resId)) {
			propertyElement = resIdToPropertyMap.get(resId);
		} else {
			propertyElement = new I18nPropertyElement();
			resIdToPropertyMap.put(resId, propertyElement);
		}
		switch (langType) {
		case ENGLISH:
			propertyElement.setEnglishValue(resLineValue);
			break;
		case SIMP:
			propertyElement.setSimpValue(resLineValue);
			break;
		case TRAD:
			propertyElement.setTradValue(resLineValue);
			break;
		default:
			break;
		}
		propertyElement.setFileName(filePath);
		propertyElement.setEntryName(filePath);
	}
	
	private static I18nType getJarI18nType(File file){
		I18nType langType = null;
		String fileName = file.getName();
		if (fileName.matches(".*_tra_langres.jar")) {
			langType = I18nType.TRAD;
		} else if (fileName.matches(".*_sim_langres.jar")) {
			langType = I18nType.SIMP;
		} else if (fileName.matches(".*_eng_langres.jar")) {
			langType = I18nType.ENGLISH;
		}
		return langType;
	}
	
	private static I18nType getCsvI18nType(File file){
		I18nType langType = null;
		if (file.getAbsolutePath().contains("tradchn")) {
			langType = I18nType.TRAD;
		} else if (file.getAbsolutePath().contains("simpchn")) {
			langType = I18nType.SIMP;
		} else if (file.getAbsolutePath().contains("english")) {
			langType = I18nType.ENGLISH;
		}
		return langType;
	}

	/**
	 * 处理jar包中的properties文件，并加入至resIdToPropertyMap中
	 * 
	 * @param file
	 * @param jarFile
	 * @param entry
	 * @param langType
	 * @param resIdToPropertyMap
	 */
	private static void processJarEntry(File file, JarFile jarFile, JarEntry entry, I18nType langType, Map<String, I18nPropertyElement> resIdToPropertyMap) {
		String entryName = entry.getName();
		long size = entry.getSize();
		if (size != 0 && entryName.toUpperCase().matches(".*.PROPERTIES")) {
			InputStream is = null;
			try {
				is = jarFile.getInputStream(entry);
				InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-16"));
				BufferedReader reader = new BufferedReader(isr);
				String line;
				while ((line = reader.readLine()) != null) {
					if (!line.trim().startsWith("#")) {
						addLangResourceLine(file, langType, line, entryName, resIdToPropertyMap);
					}
				}
			} catch (IOException e) {
				Logger.error("", e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
	}

	/**
	 * 增加多语资源行至map中
	 * 
	 * @param langType
	 *            - 多语类型
	 * @param resourceLine
	 *            - 多语资源行
	 * @param entryName
	 *            - entry名称
	 */
	private static void addLangResourceLine(File file, I18nType langType, String resourceLine, String entryName,
			Map<String, I18nPropertyElement> resIdToPropertyMap) {
		if (null == resourceLine || 0 == resourceLine.trim().length()) {
			return;
		}
		int firstEquInx = resourceLine.indexOf('=');
		if (-1 == firstEquInx) {
			return;
		}
//		String[] names = entryName.split("\\/");
//		String residFolderName = "";
//		if (names.length > 2) {
//			residFolderName = names[2]; // 文件夹名
//		}
		String resId = resourceLine.substring(0, firstEquInx).trim();
		String resLineValue = resourceLine.substring(firstEquInx + 1).trim();
//		String resKey = String.format("%s, %s", resId, residFolderName);
		// System.out.println(resValue);
		I18nPropertyElement propertyElement = resIdToPropertyMap.containsKey(resId) ? resIdToPropertyMap
				.get(resId) : new I18nPropertyElement();
		propertyElement.setFileName(file.getName());
		switch (langType) {
		case ENGLISH:
			propertyElement.setEnglishValue(resLineValue);
			break;
		case SIMP:
			propertyElement.setSimpValue(resLineValue);
			break;
		case TRAD:
			propertyElement.setTradValue(resLineValue);
			break;
		default:
			break;
		}
		propertyElement.setEntryName(entryName);
		propertyElement.setFileName(file.getName());
		// TODO what's this mean?
		// resValueAry[5] = "0";
		resIdToPropertyMap.put(resId, propertyElement);
	}

}