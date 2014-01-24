package com.yonyou.nc.codevalidator.runtime;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.ICompositeExecuteUnit;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.project.ProjectAnalyseUtils;
import com.yonyou.nc.codevalidator.sdk.rule.AbstractClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 服务端ClassLoader的工具类实现
 * <p>
 * @author mazhqa
 * @since V2.0
 */
public class ClassLoaderUtilsImpl extends AbstractClassLoaderUtils {

	private static final List<String> CODE_PATH_LIST = Arrays.asList(new String[] { "private", "client", "public" });
	private Map<String, ClassLoader> nameToLoaderMap = new WeakHashMap<String, ClassLoader>();

	@Override
	protected ClassLoader getClassLoader(String projectPath) throws RuleClassLoadException {
		RuntimeContext runtimeContext;
		try {
			runtimeContext = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext();
		} catch (RuleBaseException e) {
			throw new RuleClassLoadException(e);
		}
		String ncHomeFolder = runtimeContext.getNcHome();
		if(StringUtils.isBlank(ncHomeFolder)) {
			throw new RuleClassLoadException("未设置对应的nchome，不能进行ClassLoader相关操作!");
		}
		String identifier = String.format("%s;%s", ncHomeFolder, projectPath);
		if (nameToLoaderMap.get(identifier) != null) {
			return nameToLoaderMap.get(identifier);
		}
		synchronized (this) {
			try {
				if (nameToLoaderMap.get(identifier) != null) {
					return nameToLoaderMap.get(identifier);
				}
				List<URL> urlList = new ArrayList<URL>();
				for (String projectOutPath : getProjectSourceUrls(projectPath)) {
					urlList.add(new URL(PROTOCAL_PREFIX + computeForURLClassLoader(projectOutPath)));
				}
				@SuppressWarnings("rawtypes")
				Collection listFiles = FileUtils.listFiles(new File(ncHomeFolder), new String[] { "jar" }, true);
				for (Object fileObject : listFiles) {
					File jarFile = (File) fileObject;
					urlList.add(new URL(PROTOCAL_PREFIX + computeForURLClassLoader(jarFile.getAbsolutePath())));
				}
				URLClassLoader result = new URLClassLoader(urlList.toArray(new URL[0]));
				nameToLoaderMap.put(identifier, result);
				return result;
			} catch (MalformedURLException e) {
				throw new RuleClassLoadException(e);
			}
		}
	}

	private List<String> getProjectSourceUrls(String projectPath) {
		List<String> result = new ArrayList<String>();
		BusinessComponent businessComponent = ProjectAnalyseUtils.getBusinessComponents(projectPath, GlobalExecuteUnit.DEFAULT_GLOBAL_NAME);
		result.addAll(getCodePaths(businessComponent));
		if (businessComponent instanceof ICompositeExecuteUnit) {
			ICompositeExecuteUnit moduleExecuteUnit = (ICompositeExecuteUnit) businessComponent;
			List<BusinessComponent> subBusinessComponentList = moduleExecuteUnit.getSubBusinessComponentList();
			for (BusinessComponent subBusinessComponent : subBusinessComponentList) {
				result.addAll(getCodePaths(subBusinessComponent));
			}
		}
		return result;
	}
	
	private List<String> getCodePaths(BusinessComponent businessComponent){
		if(businessComponent instanceof GlobalExecuteUnit) {
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<String>();
		for (String codePath : CODE_PATH_LIST) {
			if (new File(businessComponent.getCodePath(codePath)).exists()) {
				result.add(businessComponent.getCodePath(codePath).replace("src", "out"));
			}
		}
		return result;
	}

}
