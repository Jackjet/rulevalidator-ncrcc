package ncmdp.ui.tree.mdptree;

import java.io.File;

import ncmdp.factory.ImageFactory;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.project.MDFileEditInput;
import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;

public class MDPFileTreeItem extends TreeItem implements IMDPTreeNode,
		IOwnComponentItf {
	private IEditorInput editorInput = null;
	String moduleName = null;
	String mdFileRelativePath = null;
	String componentName = null;
	private JGraph graph = null;
	public MDPFileTreeItem(TreeItem parentItem, IFile ifile) {
		super(parentItem, SWT.NONE);
		setData(ifile);
		File file = ifile.getLocation().toFile();
		setData("localFile", file);
		setText(file.getName());
		setImage(getFileImage());
		if (parentItem instanceof IOwnComponentItf) {
			componentName = ((IOwnComponentItf) parentItem).getComponentName();
		}
	}

	public JGraph getGraph() {
		return graph;
	}

	public void setGraph(JGraph graph) {
		this.graph = graph;
	}

	protected void checkSubclass() {
	}

	private Image getFileImage() {
		PackageExplorerPart part = PackageExplorerPart
				.getFromActivePerspective();
		if (part != null) {
			return ((ILabelProvider) part.getTreeViewer().getLabelProvider())
					.getImage(getIFile());
		}
		if (getFile() != null) {
			if (getFile().toString().endsWith(Constant.MDFILE_SUFFIX_EXTENTION)) {
				return ImageFactory.getFileImage();
			} else {
				return ImageFactory.getBpfImg();
			}
		}
		return ImageFactory.getFileImage();
	}

	public void updateState() {
		setImage(getFileImage());
	}

	public File getFile() {
		return (File) getData("localFile");
	}

	public IEditorInput getEditorInput() {
		if (editorInput == null) {
			try {
				editorInput = new MDFileEditInput(getFile(), getIFile(),
						getComponentName());
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		return editorInput;
	}

	public IFile getIFile() {
		return (IFile) getData();
	}

	public void deleteNode() {
		try {
			getIFile().delete(true, null);
		} catch (CoreException e) {
			MDPLogger.error(e.getMessage(), e);
		}
		dispose();
	}

	public String getModuleName() {
		if (moduleName == null) {
			TreeItem parent = getParentItem();
			while (!(parent instanceof ProjectTreeItem)) {
				parent = parent.getParentItem();
			}
			if (parent != null && parent instanceof ProjectTreeItem) {
				ProjectTreeItem item = (ProjectTreeItem) parent;
				moduleName = item.getModuleName();
			}
		}
		return moduleName;
	}

	public String getMDFileRelativePath() {
		if (mdFileRelativePath == null) {
			TreeItem parent = getParentItem();
			while (!(parent instanceof ProjectTreeItem)) {
				parent = parent.getParentItem();
			}
			if (parent != null && parent instanceof ProjectTreeItem) {
				ProjectTreeItem item = (ProjectTreeItem) parent;
				String rootFileStr = item.getFile().getPath();
				String fileStr = getFile().getPath();
				mdFileRelativePath = fileStr.substring(rootFileStr.length());
			}
		}
		return mdFileRelativePath;
	}

	public IResource getIResource() {
		return getIFile();
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

}
