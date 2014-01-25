package ncmdp.project;

import java.io.File;
import java.io.Serializable;

import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class ProjectModel implements Serializable {
	private static final long serialVersionUID = -7095058753382687747L;

	private IProject javaProject = null;

	private File mdRoot = null;

	private IFolder mdRootIFolder = null;

	private String comDirName;

	public ProjectModel(IProject project) {
		super();
		this.javaProject = project;
	}

	public IProject getJavaProject() {
		return javaProject;
	}

	public File getMDRoot() {
		if (mdRoot == null) {
			File file = getJavaProject().getLocation().toFile();
			File tempFile = StringUtil.isEmptyWithTrim(comDirName) ? file : new File(file, comDirName);
			mdRoot = new File(tempFile, MDPExplorerTreeBuilder.METADATA_ROOT_DIR);
		}
		return mdRoot;
	}

	/**
	 * 获得METADATA，如果不存在，则新建一个
	 * @return
	 */
	public IFolder getMDRootIFolder() {
		if (mdRootIFolder == null) {
			IProject proj = getJavaProject();
			String dir = StringUtil.isEmptyWithTrim(comDirName) ? MDPExplorerTreeBuilder.METADATA_ROOT_DIR
					: comDirName + "//" + MDPExplorerTreeBuilder.METADATA_ROOT_DIR;
			mdRootIFolder = proj.getFolder(dir);
			if (mdRootIFolder != null && !mdRootIFolder.exists()) {
				try {
					mdRootIFolder.create(true, true, null);
				} catch (CoreException e) {
					MDPLogger.error(e.getMessage(),e);
				}
			}
		}
		return mdRootIFolder;
	}

	public String getProjectName() {
		return getJavaProject().getName();
	}

	public String getComDirName() {
		return comDirName;
	}

	public void setComDirName(String comDirName) {
		this.comDirName = comDirName;
	}
}
