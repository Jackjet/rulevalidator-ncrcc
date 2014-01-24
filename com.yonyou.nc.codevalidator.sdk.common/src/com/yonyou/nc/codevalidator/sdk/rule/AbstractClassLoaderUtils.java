package com.yonyou.nc.codevalidator.sdk.rule;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.sdk.code.JavaCodeFeature;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

public abstract class AbstractClassLoaderUtils implements IClassLoaderUtils {

	protected static final String PROTOCAL_PREFIX = "file:///";
	protected static final Pattern JAR_PRIVATE_PATTERN = Pattern.compile("/modules/(\\w+)/META-INF/lib/(\\w+).jar");
	protected static final Pattern JAR_CLIENT_PATTERN = Pattern.compile("/modules/(\\w+)/client/lib/ui(\\w+).jar");
	protected static final Pattern JAR_PUBLIC_PATTERN = Pattern.compile("/modules/(\\w+)/lib/pub(\\w+).jar");
	protected static final Pattern CLASSRESOURCE_PATTERN = Pattern.compile("/(\\w+)/(\\w+)/out/(\\w+)");

	@Override
	public Class<?> loadClass(String projectName, String className) throws RuleClassLoadException {
		try {
			ClassLoader classLoader = getClassLoader(projectName);
			return classLoader.loadClass(className.trim());
		} catch (ClassNotFoundException e) {
			throw new RuleClassLoadException(e);
		}
	}

	@Override
	public boolean isParentClass(String projectName, String className, String parentClassName)
			throws RuleClassLoadException {
		try {
			ClassLoader classLoader = getClassLoader(projectName);
			Class<?> currentClass = classLoader.loadClass(className.trim());
			if (currentClass.isInterface()) {
				return false;
			}
			Class<?> parentClass = classLoader.loadClass(parentClassName.trim());
			return parentClass.isAssignableFrom(currentClass);
			// return isParentClassInner(currentClass, parentClass);
		} catch (ClassNotFoundException e) {
			throw new RuleClassLoadException(e);
		}
	}

	// private boolean isParentClassInner(Class<?> currentClass, Class<?>
	// parentClass) {
	// if (currentClass == parentClass) {
	// return true;
	// }
	// if (currentClass == Object.class) {
	// return false;
	// }
	// return isParentClassInner(currentClass.getSuperclass(), parentClass);
	// }

//	public boolean isPrivateClass(BusinessComponent businessComponent, String className) throws RuleClassLoadException {
//		return isInClassDir("/private/", className, businessComponent);
//	}
//
//	public boolean isPublicClass(BusinessComponent businessComponent, String className) throws RuleClassLoadException {
//		return isInClassDir("/public/", className, businessComponent);
//	}
//
//	public boolean isClientClass(BusinessComponent businessComponent, String className) throws RuleClassLoadException {
//		return isInClassDir("/client/", className, businessComponent);
//	}
//
//	private boolean isInClassDir(String dirName, String className, BusinessComponent businessComponent)
//			throws RuleClassLoadException {
//		URL url = loadClass(businessComponent.getProjectName(), className).getResource(convertClassToResourcePath(className));
//		String classPath;
//		try {
//			classPath = URLDecoder.decode(url.getPath(), "UTF-8").replaceFirst("jar:", "")
//					.replaceFirst("file:[/\\\\]+", "");
//		} catch (UnsupportedEncodingException e) {
//			throw new RuleClassLoadException(e);
//		}
//		if (classPath.indexOf(dirName) > 0) {
//			return true;
//		}
//		StringBuffer srcSb = new StringBuffer(businessComponent.getBusinessComponentPath());
//		srcSb.append(File.separatorChar);
//		srcSb.append("src");
//		srcSb.append(File.separatorChar);
//		srcSb.append(dirName);
//		srcSb.append(File.separatorChar);
//		srcSb.append(className.replace(".", File.separator));
//		srcSb.append(".java");
//		return (new File(srcSb.toString())).exists();
//	}
	
	private String convertClassToResourcePath(String className) {
		return String.format("/%s.class",  className.replaceAll("\\.", "/"));
	}
	
	/**
	 * 根据加载的class路径，得到代码feature
	 * @param classPath
	 * @returnch
	 */
	private JavaCodeFeature getJarFeature(String classPath) {
		JavaResPrivilege resPrivilege = null;
		String moduleName = "";
		String busiCompName = "";
		Matcher matcher = null;
		if(JAR_PRIVATE_PATTERN.matcher(classPath).find()) {
			matcher = JAR_PRIVATE_PATTERN.matcher(classPath);
			matcher.find();
			resPrivilege = JavaResPrivilege.PRIVATE;
			moduleName = matcher.group(1);
//			busiCompName = matcher.group(1);
			busiCompName = matcher.group(2).replace(moduleName+"_", "");
		} else if(JAR_CLIENT_PATTERN.matcher(classPath).find()){
			matcher = JAR_CLIENT_PATTERN.matcher(classPath);
			resPrivilege = JavaResPrivilege.CLIENT;
			matcher.find();
			moduleName = matcher.group(1);
			busiCompName = matcher.group(2).replace(moduleName+"_", "");
		} else if(JAR_PUBLIC_PATTERN.matcher(classPath).find()) {
			matcher = JAR_PUBLIC_PATTERN.matcher(classPath);
			resPrivilege = JavaResPrivilege.PUBLIC;
			matcher.find();
			moduleName = matcher.group(1);
			busiCompName = matcher.group(2).replace(moduleName+"_", "");
		} else {
			//不属于任何模块的jar包
			resPrivilege = JavaResPrivilege.SRC;
		}
		return new JavaCodeFeature(resPrivilege, moduleName, busiCompName);
	}
	
	private JavaCodeFeature getJavaClassFeature(String classPath) {
		JavaResPrivilege resPrivilege = null;
		String moduleName = "";
		String busiCompName = "";
		Matcher matcher = CLASSRESOURCE_PATTERN.matcher(classPath);
		if(matcher.find()) {
			moduleName = matcher.group(1);
			busiCompName = matcher.group(2);
			String privilege = matcher.group(3);
			resPrivilege = privilege.equals("private") ? JavaResPrivilege.PRIVATE:
				privilege.equals("client") ? JavaResPrivilege.CLIENT:
					JavaResPrivilege.PUBLIC;
		} else {
			//无法确定其具体业务组件和模块
			resPrivilege = JavaResPrivilege.SRC;
		}
		return new JavaCodeFeature(resPrivilege, moduleName, busiCompName);
	}

	@Override
	public boolean isImplementedInterface(String projectName, String className, String interfaceName)
			throws RuleClassLoadException {
		try {
			ClassLoader classLoader = getClassLoader(projectName);
			Class<?> interfaceClass = classLoader.loadClass(interfaceName.trim());
			if (!interfaceClass.isInterface()) {
				return false;
			}
			Class<?> currentClass = classLoader.loadClass(className.trim());
			return interfaceClass.isAssignableFrom(currentClass);
		} catch (ClassNotFoundException e) {
			throw new RuleClassLoadException(e);
		}
	}

	/**
	 * because of URLClassLoader assumes any URL not ends with '/' to refer to a
	 * jar file. so we need to append '/' to classpath if it is a folder but not
	 * ends with '/'.
	 * 
	 * @param classpath
	 * @return
	 */
	protected String computeForURLClassLoader(String classpath) {
		if (!classpath.endsWith("/")) {
			File file = new File(classpath);
			if (file.exists() && file.isDirectory()) {
				classpath = classpath.concat("/");
			}
		}
		return classpath;
	}

	@Override
	public boolean isExtendsParentClass(String projectName, String className, String parentClassName)
			throws RuleClassLoadException {
		try {
			ClassLoader classLoader = getClassLoader(projectName);
			Class<?> parentClass = classLoader.loadClass(parentClassName.trim());
			Class<?> currentClass = classLoader.loadClass(className.trim());
			return parentClass.isAssignableFrom(currentClass);
		} catch (ClassNotFoundException e) {
			throw new RuleClassLoadException(e);
		}
	}

	@Override
	public JavaCodeFeature getCodeFeature(BusinessComponent businessComponent, String className) throws RuleClassLoadException{
		URL url = loadClass(businessComponent.getProjectName(), className.trim()).getResource(convertClassToResourcePath(className));
		String classPath;
		try {
			classPath = URLDecoder.decode(url.getPath(), "UTF-8").replaceFirst("jar:", "")
					.replaceFirst("file:[/\\\\]+", "");
		} catch (UnsupportedEncodingException e) {
			throw new RuleClassLoadException(e);
		}
		if(classPath.contains(".jar")) {
			return getJarFeature(classPath);
		} 
		return getJavaClassFeature(classPath);
	}
	
	@Override
	public InputStream getResourceStream(String projectName, String resourcePath) throws RuleClassLoadException{
		ClassLoader classLoader = getClassLoader(projectName);
		return classLoader.getResourceAsStream(resourcePath);
	}

	
	protected abstract ClassLoader getClassLoader(String projectName) throws RuleClassLoadException;
}
