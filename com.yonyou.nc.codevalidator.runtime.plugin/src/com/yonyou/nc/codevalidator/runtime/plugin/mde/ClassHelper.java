package com.yonyou.nc.codevalidator.runtime.plugin.mde;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Class Helper to load class of java project.
 * 
 * @author elvis
 */
public final class ClassHelper {
	private static final String PROTOCAL_PREFIX = "file:///";
	
	private ClassHelper() {
		
	}

	/**
	 * get the ClassLoader of java project specified.
	 * 
	 * @param project
	 *            IJavaProject
	 * @return ClassLoader of java project
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	public static ClassLoader getProjectClassLoader(IJavaProject project) throws CoreException, MalformedURLException {
		// compute the project classpaths
		// REVIEW: Are the classpaths returned by computeDefaultRuntimeClassPath
		// enough to load class?
		String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(project);
		URL[] urls = new URL[classPaths.length];
		for (int i = 0; i < classPaths.length; i++) {
			urls[i] = new URL(PROTOCAL_PREFIX + computeForURLClassLoader(classPaths[i]));
		}
		return new URLClassLoader(urls);
	}

	/**
	 * load Class in java project
	 * 
	 * @param project
	 *            IJavaProject
	 * @param className
	 *            name of class to load
	 * @return Class
	 * @throws ClassNotFoundException
	 * @throws CoreException
	 * @throws MalformedURLException
	 */
	public static Class<?> loadClass(IJavaProject project, String className) throws CoreException,
			ClassNotFoundException, MalformedURLException {
		ClassLoader loader = getProjectClassLoader(project);
		Class<?> clazz = loader.loadClass(className);
		loader = null;
		return clazz;
	}

	/**
	 * transform the IType to Class
	 * 
	 * @param type
	 *            IType
	 * @return Class
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 */
	public static Class<?> typeToClass(IType type) throws CoreException, ClassNotFoundException, MalformedURLException {
		try {
			if (null != type && (type.isClass() || type.isInterface())) {
				String className = type.getFullyQualifiedName('$');
				return loadClass(type.getJavaProject(), className);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * because of URLClassLoader assumes any URL not ends with '/' to refer to a
	 * jar file. so we need to append '/' to classpath if it is a folder but not
	 * ends with '/'.
	 * 
	 * @param classpath
	 * @return
	 */
	private static String computeForURLClassLoader(String classpath) {
		String newClasspath = classpath;
		if (!classpath.endsWith("/")) {
			File file = new File(classpath);
			if (file.exists() && file.isDirectory()) {
				newClasspath = classpath.concat("/");
			}
		}
		return newClasspath;
	}
}
