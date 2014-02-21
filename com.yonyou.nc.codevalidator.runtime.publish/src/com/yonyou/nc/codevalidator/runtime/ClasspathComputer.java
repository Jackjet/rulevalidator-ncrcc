package com.yonyou.nc.codevalidator.runtime;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * 
 * @author hgy
 *
 */
public class ClasspathComputer {

	public static File[] computeStandCP(File stdDir) {

		File file = null;
		List<File> files = new ArrayList<File>();

		file = new File(stdDir, "extension/_classes/");
		if (file.exists())
			files.add(file);
		file = new File(stdDir, "extension/classes/");
		if (file.exists())
			files.add(file);
		file = new File(stdDir, "extension/resources/");
		if (file.exists())
			files.add(file);
		files = listFiles(files, new File(stdDir, "extension/lib"),
				new JarFilter());
		files = listFiles(files, new File(stdDir, "extension/_lib"),
				new JarFilter());

		file = new File(stdDir, "hyext/_classes/");
		if (file.exists())
			files.add(file);
		file = new File(stdDir, "hyext/classes/");
		if (file.exists())
			files.add(file);
		file = new File(stdDir, "hyext/resources/");
		if (file.exists())
			files.add(file);
		files = listFiles(files, new File(stdDir, "hyext/lib"), new JarFilter());
		files = listFiles(files, new File(stdDir, "hyext/_lib"),
				new JarFilter());

		file = new File(stdDir, "_classes/");
		if (file.exists())
			files.add(file);

		file = new File(stdDir, "classes/");
		if (file.exists())
			files.add(file);

		file = new File(stdDir, "resources/");
		if (file.exists())
			files.add(file);

		files = listFiles(files, new File(stdDir, "_lib"), new JarFilter());
		files = listFiles(files, new File(stdDir, "lib"), new JarFilter());

		File[] fileArray = new File[files.size()];
		files.toArray(fileArray);
		return fileArray;
	}

	public static void computeJarsInLib(File lib, List<File> list) {
		List<File> files = listFiles(lib, new JarFilter());
		list.addAll(files);
	}

	public static File[] computeJarsInLib(File lib) {
		List<File> files = listFiles(lib, new JarFilter());
		File[] fileArray = new File[files.size()];
		files.toArray(fileArray);
		return fileArray;
	}

	public static URL[] computeJarCPInLib(File lib) {
		if (!lib.exists())
			return new URL[0];
		List<File> files = listFiles(lib, new JarFilter());
		List<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < files.size(); i++) {
			try {
				urls.add(((File) files.get(i)).toURI().toURL());
			} catch (MalformedURLException e) {

			}
		}
		URL[] urlArray = new URL[urls.size()];
		urls.toArray(urlArray);
		return urlArray;
	}

	public static URL[] computeStandCP(URL stdURL) {
		File stdDir = new File(stdURL.getFile());
		if (!stdDir.exists())
			return new URL[0];
		File[] files = computeStandCP(stdDir);
		URL[] urls = new URL[files.length];
		for (int i = 0; i < urls.length; i++) {
			try {
				urls[i] = files[i].toURI().toURL();
			} catch (MalformedURLException e) {

			}
		}
		return urls;
	}

	public static URL[] computeModulePublicCP(File modulesDir) {
		if (!modulesDir.exists())
			return new URL[0];
		List<File> newModuleList = computeModuleDirs(modulesDir);
		ArrayList<URL> cpList = new ArrayList<URL>();
		for (int i = 0; i < newModuleList.size(); i++) {
			try {
				URL[] urls = computeStandCP(((File) newModuleList.get(i))
						.toURI().toURL());
				for (int j = 0; j < urls.length; j++) {
					cpList.add(urls[j]);
				}
			} catch (MalformedURLException e) {

			}
		}

		URL[] publics = new URL[cpList.size()];
		cpList.toArray(publics);
		return publics;
	}

	public static List<File> computeModuleDirs(File dir) {
		List<File> moduleList = new ArrayList<File>();
		computeModuleDirs(dir, moduleList);

		return moduleList;
	}

	public static void computeModuleDirs(File dir, List<File> moduleList) {
		if (!dir.exists())
			return;
		List<File> newModuleList = Arrays.asList(dir
				.listFiles(new ModuleFilter()));
		moduleList.addAll(newModuleList);

		File[] dirs = dir.listFiles(new AndFilter(new FileFilter[] {
				new DirectoryFilter(), new ExcludeFilter(newModuleList) }));

		for (int i = 0; i < dirs.length; i++) {
			computeModuleDirs(dirs[i], moduleList);
		}
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Collections.sort(moduleList, new ModuleComparator(dbf));
	}

	public static List<File> listFiles(File dir, FileFilter filter) {
		List<File> files = new ArrayList<File>();
		if (!dir.exists() || dir.isFile())
			return files;
		listFiles(files, dir, filter);
		return files;
	}

	private static List<File> listFiles(List<File> filesList, File dir,
			FileFilter filter) {
		if (dir.exists()) {
			File[] files = dir.listFiles(filter);
			List<File> temp = Arrays.asList(files);
			Collections.sort(temp);
			filesList.addAll(temp);

			File[] subDirs = dir.listFiles(new DirectoryFilter());
			for (int i = 0; i < subDirs.length; i++) {
				listFiles(filesList, subDirs[i], filter);
			}
		}

		return filesList;
	}

}

class ModuleComparator implements Comparator<File> {

	private Map<String, Integer> map = new HashMap<String, Integer>();
	
	private  DocumentBuilderFactory dbf;
	
	ModuleComparator(DocumentBuilderFactory dbf) {
		this.dbf = dbf;
	}

	public int compare(File md1, File md2) {

		Integer priority1 = getPriority(md1);
		Integer priority2 = getPriority(md2);

		int retValue = priority1.compareTo(priority2);

		if (retValue != 0)
			return retValue;
		else {
			boolean moduleStartsWithUap1 = md1.getName().startsWith("uap");
			boolean moduleStartsWithUap2 = md2.getName().startsWith("uap");

			if ((moduleStartsWithUap1 ^ moduleStartsWithUap2)) {
				return moduleStartsWithUap1 ? -1 : 1;
			} else {
				return md1.getName().compareTo(md2.getName());
			}
		}
	}

	private Integer getPriority(File module) {
		String moduleName1 = module.getName();

		Integer priority = null;

		if ((priority = (Integer) map.get(moduleName1)) == null) {
			File moduleFile = new File(module, "META-INF/module.xml");

			if (moduleFile.exists()) {
				try {
					Document dom = dbf.newDocumentBuilder().parse(moduleFile);
					String str = dom.getDocumentElement().getAttribute(
							"priority");
					if (str != null) {
						int value = Integer.parseInt(str);
						priority = new Integer(value);
					}
				} catch (Exception e) {
				}
			}

			if (priority == null)
				priority = new Integer(10);

			map.put(moduleName1, priority);
		}

		return priority;

	}

}

class JarFilter implements FileFilter {
	public boolean accept(File pathname) {
		String name = pathname.getName().toLowerCase();
		if (name.endsWith(".jar")) {
			return !name.endsWith("_src.jar") && !name.endsWith("_doc.jar");
		} else if (name.endsWith(".zip")) {
			return !name.endsWith("_src.zip") && !name.endsWith("_doc.zip");
		}
		return false;
	}
}

class ModuleFilter implements FileFilter {
	public boolean accept(File pathname) {
		if (pathname.isDirectory()) {
			File subFile = new File(pathname, "META-INF/module.xml");
			if (subFile.exists())
				return true;
			else
				return false;
		}
		return false;
	}
}

class DirectoryFilter implements FileFilter {
	public boolean accept(File pathname) {
		return pathname.exists() && pathname.isDirectory();
	}
}

class ExcludeFilter implements FileFilter {

	List<File> excludes;

	public ExcludeFilter(List<File> excludes) {
		this.excludes = excludes;
	}

	public boolean accept(File pathname) {
		if (excludes == null)
			return true;
		return !excludes.contains(pathname);
	}
}

class AndFilter implements FileFilter {
	public AndFilter(FileFilter[] array) {
		this.array = array;
	}

	FileFilter[] array;

	public boolean accept(File pathname) {
		if (array == null)
			return true;
		boolean ret = true;
		for (int i = 0; i < array.length && ret; i++) {
			ret = ret && array[i].accept(pathname);
		}
		return ret;
	}

}