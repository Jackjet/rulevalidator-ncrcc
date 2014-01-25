package com.yonyou.nc.codevalidator.runtime.plugin.mde;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

import com.yonyou.nc.codevalidator.sdk.rule.AbstractClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

public class MdeClassLoaderUtils extends AbstractClassLoaderUtils {

	private static Map<String, ClassLoader> classLoaderMap = new ConcurrentHashMap<String, ClassLoader>();

	/**
	 * get mde project class loader.
	 * 
	 * @param projectName
	 * @return
	 * @throws RuleClassLoadException
	 */
	public ClassLoader getClassLoader(String projectName) throws RuleClassLoadException {
		ClassLoader result = classLoaderMap.get(projectName);
		if (result == null) {
			try {
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				IJavaProject javaProject = JavaCore.create(project);
				String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
				URL[] urls = new URL[classPaths.length];
				for (int i = 0; i < classPaths.length; i++) {
					urls[i] = new URL(PROTOCAL_PREFIX + computeForURLClassLoader(classPaths[i]));
				}
				result = new URLClassLoader(urls);
				classLoaderMap.put(projectName, result);
			} catch (CoreException e) {
				throw new RuleClassLoadException(e);
			} catch (MalformedURLException e) {
				throw new RuleClassLoadException(e);
			}
		}
		return result;
	}

}
