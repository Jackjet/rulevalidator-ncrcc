package ncmdp.ui.tree.mdptree;

import java.io.File;

import ncmdp.factory.ImageFactory;
import ncmdp.project.ProjectModel;
import ncmdp.tool.NCMDPTool;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 项目节点
 * @author wangxmn
 *
 */
public class ProjectTreeItem extends TreeItem implements IMDPTreeNode,
		IOwnComponentItf {

	private ProjectModel pm = null;

	private String moduleName = null;

	public ProjectTreeItem(Tree parent, ProjectModel pm) {
		super(parent, SWT.NONE);
		this.pm = pm;
		init();
	}

	protected void checkSubclass() {
	}

	public void updateState() {
		//没用
	}

	/**
	 * 创建组件工程的根节点
	 */
	private void init() {
		setText(pm.getProjectName() + " [" + getModuleName() + "]");
		setData(pm);
		setImage(getProjectImage());
	}

	/**
	 * 组件工程的图片
	 * @return
	 */
	private Image getProjectImage() {
		return ImageFactory.getProjectImage();
	}

	public ProjectModel getProjectModel() {
		return (ProjectModel) getData();
	}

	public File getFile() {
		return pm.getMDRoot();
	}

	public IFolder getRootFolder() {
		return pm.getMDRootIFolder();
	}

	public void deleteNode() {
		MessageDialog.openInformation(null, "Error",
				"can not delete this node ：" + getText());
	}

	public String getModuleName() {
		if (moduleName == null) {
			moduleName = NCMDPTool.getProjectModuleName(pm.getJavaProject());
		}
		return moduleName;
	}

	public IResource getIResource() {
		return getRootFolder();
	}

	@Override
	public String getComponentName() {
		return pm.getComDirName();
	}
}
