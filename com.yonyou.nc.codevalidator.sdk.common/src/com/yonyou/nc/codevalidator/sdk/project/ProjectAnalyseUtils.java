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
 * NC mde工程分析的工具类
 * 
 * @author mazhqa
 * @since V1.0
 */
public final class ProjectAnalyseUtils {

	private static ConcurrentHashMap<String, String> moduleMap = new ConcurrentHashMap<String, String>();

	private ProjectAnalyseUtils() {

	}

	/**
	 * 根据源码路径和工程名称得到所有的业务组件，返回全局执行的单元，用此单元即可将规则全部执行
	 * <p>
	 * 注：前台插件代码使用此方法
	 * 
	 * @param srcLocation
	 * @param projectName
	 *            - 工程名称，仅前台插件需要设置此方法，后台调用规则时，可以将其设置为null
	 * @return
	 */
	public static BusinessComponent getBusinessComponent(String srcLocation, String projectName, String productCode) {
		GlobalExecuteUnit globalExecuteUnit = new GlobalExecuteUnit(projectName, productCode);
		BusinessComponent innerBusinessComponents = getInnerBusinessComponent(srcLocation, projectName, productCode);
		globalExecuteUnit.addSubBusinessComponentList(innerBusinessComponents);
		return globalExecuteUnit;
	}

	/**
	 * 非Global包装的业务组件，返回工程源代码路径下的具体业务组件
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
				// TODO : mazhqa 未设置业务组件名称
				BusinessComponent businessCompExecuteUnit = new DefaultBusinessComponent(projectPath, projectName,
						productCode, moduleDir, moduleName, "");
				return businessCompExecuteUnit;
			}
		} catch (RuleBaseException e) {
			Logger.error("读取配置文件错误");
			throw new RuleBaseRuntimeException(e);
		}
	}

	/**
	 * 根据源码路径得到所有的业务组件
	 * <p>
	 * 注：仅供后台代码使用，前台代码需要传递工程名称
	 * 
	 * @param srcLocation
	 * @return
	 */
	public static BusinessComponent getBusinessComponents(String srcLocation, String productCode) {
		return getBusinessComponent(srcLocation, srcLocation, productCode);
	}

	/**
	 * 根据路径得到当前模块的模块名，如果当前路径并不是一个模块而是一个业务组件，返回null
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
