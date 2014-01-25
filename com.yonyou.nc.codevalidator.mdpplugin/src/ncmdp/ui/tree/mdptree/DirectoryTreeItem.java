package ncmdp.ui.tree.mdptree;

import java.io.File;

import ncmdp.factory.ImageFactory;
import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class DirectoryTreeItem extends TreeItem implements IMDPTreeNode,
		IOwnComponentItf {
	private String moduleName = null;

	private String componentName = null;

	public DirectoryTreeItem(TreeItem parentItem, IFolder ifolder) {
		super(parentItem, SWT.NONE);
		setData(ifolder);
		File file = ifolder.getLocation().toFile();
		setData("localFile", file);
		setText(file.getName());
		setImage(getDirImage());
		if (parentItem instanceof IOwnComponentItf) {
			componentName = ((IOwnComponentItf) parentItem).getComponentName();
		}

	}

	protected void checkSubclass() {
	}

	private Image getDirImage() {
		PackageExplorerPart part = PackageExplorerPart
				.getFromActivePerspective();
		if (part != null) {
			return ((ILabelProvider) part.getTreeViewer().getLabelProvider())
					.getImage(getIFolder());
		}
		return ImageFactory.getDirectoryImage();
	}

	public void updateState() {
		setImage(getDirImage());

	}

	public File getFile() {
		return (File) getData("localFile");
	}

	public void deleteNode() {
		try {
			getIFolder().delete(true, null);
		} catch (CoreException e) {
			MDPLogger.error(e.getMessage(), e);
		}
		dispose();
	}
	public IFolder getIFolder() {
		return (IFolder) getData();
	}

	public IResource getIResource() {
		return getIFolder();
	}

	public String getModuleName() {
		if (moduleName == null) {
			TreeItem parent = getParentItem();
			while (!(parent instanceof ProjectTreeItem)) {
				parent = parent.getParentItem();
			}
			if (parent != null && parent instanceof ProjectTreeItem) {
				ProjectTreeItem item = (ProjectTreeItem) parent;
				moduleName = item.getModuleName();// NCMDPTool.getProjectModuleName(item.getProjectModel().getJavaProject());
			}
		}
		return moduleName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

}
