package ncmdp.tool;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
/**
 * 关于类加载的工具类
 * @author wangxmn
 *
 */
public class ClassTool {

	/**
	 * 获得该项目的类加载器
	 * @param project
	 * @return
	 */
	public static URLClassLoader getURLClassLoader(IProject project) {
		URLClassLoader loader = null;
		ArrayList<URL> allUrls = new ArrayList<URL>();
		IJavaProject elementJavaProject = JavaCore.create(project);
		if (elementJavaProject != null) {
			try {
				String[] classPathArray = JavaRuntime
						.computeDefaultRuntimeClassPath(elementJavaProject);
				for (int i = 0; i < classPathArray.length; i++) {
					File file = new File(classPathArray[i]);
					allUrls.add(file.toURI().toURL());
				}
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		loader = new URLClassLoader(allUrls.toArray(new URL[0]),
				ClassTool.class.getClassLoader());
		return loader;
	}

	/**
	 * 加载类
	 * @param clsName
	 * @param project
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String clsName, IProject project)
			throws ClassNotFoundException {
		return getURLClassLoader(project).loadClass(clsName);
	}
}
