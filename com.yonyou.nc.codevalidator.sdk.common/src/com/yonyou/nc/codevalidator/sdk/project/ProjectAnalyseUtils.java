package com.yonyou.nc.codevalidator.sdk.project;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.DefaultBusinessComponent;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.ModuleExecuteUnit;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.utils.XmlUtils;

/**
 * NC mde���̷����Ĺ�����
 * 
 * @author mazhqa
 * @since V1.0
 */
public final class ProjectAnalyseUtils {

	private static ConcurrentHashMap<String, String> moduleMap = new ConcurrentHashMap<String, String>();

	private ProjectAnalyseUtils() {

	}

	/**
	 * ����Դ��·���͹������Ƶõ����е�ҵ�����������ȫ��ִ�еĵ�Ԫ���ô˵�Ԫ���ɽ�����ȫ��ִ��
	 * <p>
	 * ע��ǰ̨�������ʹ�ô˷���
	 * 
	 * @param srcLocation
	 * @param projectName
	 *            - �������ƣ���ǰ̨�����Ҫ���ô˷�������̨���ù���ʱ�����Խ�������Ϊnull
	 * @return
	 */
	public static BusinessComponent getBusinessComponent(String srcLocation, String projectName, String productCode) {
		GlobalExecuteUnit globalExecuteUnit = new GlobalExecuteUnit(projectName, productCode);
		BusinessComponent innerBusinessComponents = getInnerBusinessComponent(srcLocation, projectName, productCode);
		globalExecuteUnit.addSubBusinessComponentList(innerBusinessComponents);
		return globalExecuteUnit;
	}

	/**
	 * ��Global��װ��ҵ����������ع���Դ����·���µľ���ҵ�����
	 * 
	 * @param srcLocation
	 * @param projectName
	 * @return
	 */
	public static BusinessComponent getInnerBusinessComponent(String srcLocation, String projectName, String productCode) {
		File projectFolder = new File(srcLocation);
		String moduleDir = projectFolder.getName();
		String projectPath = srcLocation.substring(0, srcLocation.lastIndexOf(moduleDir));
		try {
			String moduleName = getModuleName(srcLocation);
			boolean isModule = moduleName != null;
			if (isModule) {
				ModuleExecuteUnit moduleExecuteUnit = new ModuleExecuteUnit(projectName, projectPath, productCode,
						moduleName, moduleDir);
				File[] busiCompFolders = projectFolder.listFiles(new FileFilter() {

					@Override
					public boolean accept(File file) {
						return isBusinessComponent(file);
					}

				});
				for (File businessFolder : busiCompFolders) {
					moduleExecuteUnit.addSubBusinessComponentList(new DefaultBusinessComponent(projectPath,
							projectName, productCode, moduleDir, moduleName, businessFolder.getName()));
				}
				return moduleExecuteUnit;
			} else {
				// TODO : mazhqa δ����ҵ���������
				BusinessComponent businessCompExecuteUnit = new DefaultBusinessComponent(projectPath, projectName,
						productCode, moduleDir, moduleName, "");
				return businessCompExecuteUnit;
			}
		} catch (RuleBaseException e) {
			Logger.error("��ȡ�����ļ�����");
			throw new RuleBaseRuntimeException(e);
		}
	}

	/**
	 * ����Դ��·���õ����е�ҵ�����
	 * <p>
	 * ע��������̨����ʹ�ã�ǰ̨������Ҫ���ݹ�������
	 * 
	 * @param srcLocation
	 * @return
	 */
	public static BusinessComponent getBusinessComponents(String srcLocation, String productCode) {
		return getBusinessComponent(srcLocation, srcLocation, productCode);
	}

	/**
	 * ����·���õ���ǰģ���ģ�����������ǰ·��������һ��ģ�����һ��ҵ�����������null
	 * 
	 * @param srcLocation
	 * @param projectPath
	 * @return
	 * @throws RuleBaseException
	 */
	private static String getModuleName(String srcLocation) throws RuleBaseException {
		String result = moduleMap.get(srcLocation);
		if (result != null) {
			return result;
		}
		File file = new File(srcLocation + "/META-INF/module.xml");
		if (file.exists()) {
			try {
				InputStream is = new FileInputStream(file);
				Document doc = XmlUtils.parseNormalXml(is);
				result = ((Element) doc.getElementsByTagName("module").item(0)).getAttribute("name");
				moduleMap.put(srcLocation, result);
			} catch (FileNotFoundException e) {
				throw new RuleBaseException(e);
			}
		} else {
			// File projectFolder = new File(srcLocation);
			// result = projectFolder.getName();
			return null;
		}
		return result;
	}

	public static boolean isBusinessComponent(File parentFolder) {
		if (!parentFolder.isDirectory()) {
			return false;
		}
		File[] srcFolders = parentFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory() && file.getName().toLowerCase().equals("src");
			}
		});
		return srcFolders.length == 1;
	}

	public static class BusinessComponentComparator implements Comparator<BusinessComponent> {

		@Override
		public int compare(BusinessComponent o1, BusinessComponent o2) {
			return o1.getDisplayBusiCompName().compareTo(o2.getDisplayBusiCompName());
		}
	}

}
