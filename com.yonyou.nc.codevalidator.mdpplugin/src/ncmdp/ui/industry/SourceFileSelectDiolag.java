package ncmdp.ui.industry;

import java.io.File;

import ncmdp.project.BuildFileTreeUtil;
import ncmdp.project.MDPExplorerTreeBuilder;
import ncmdp.ui.tree.mdptree.RefMDFileDirTreeItem;
import ncmdp.ui.tree.mdptree.RefMDRootTreeItem;
import ncmdp.util.MDPFileTagFilter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class SourceFileSelectDiolag extends TitleAreaDialog {

	Tree tree1 = null;
	File selectFile = null;
	String[] fileTags = null;

	/**
	 * 
	 * @param parentShell
	 * @param fileTags
	 *            文件扩展名，传null列出全部文件
	 */
	public SourceFileSelectDiolag(Shell parentShell, String[] fileTags) {
		super(parentShell);
		this.fileTags = fileTags;
	}

	public File getSelectFile() {
		return selectFile;
	}

	@Override
	protected void okPressed() {
		TreeItem[] selectItems = tree1.getSelection();
		if (selectItems != null && selectItems.length > 0) {
			TreeItem selectItem = selectItems[0];
			Object obj = selectItem.getData();
			if (obj != null && obj instanceof File) {
				selectFile = (File) obj;
				if (selectFile.isDirectory()) {
					selectFile = null;
					MessageDialog.openError(getShell(), "Error", "Please select the file");
					return;
				}
			}
		}
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 500);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Source File Dialog");
		setMessage("Please select the Source MD File");
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setSize(300, 500);
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayout(new FillLayout());
		group.setText("to Select File");

		tree1 = new Tree(group, SWT.NONE);
		initTree(tree1);

		return container;
	}

	private void initTree(Tree tree1) {
		File[] refDir = BuildFileTreeUtil.getRefMDFiles();
		int count = refDir.length;
		if (count > 0) {
			RefMDRootTreeItem refRoot = new RefMDRootTreeItem(tree1, SWT.NONE);
			for (int i = 0; i < count; i++) {
				File f = refDir[i];
				RefMDFileDirTreeItem item = new RefMDFileDirTreeItem(refRoot, f);
				String moduleName = f.getName();
				File mdDir = new File(f,
						MDPExplorerTreeBuilder.METADATA_ROOT_DIR);
				if (mdDir.exists()) {
					initRefSubTree(item, mdDir, moduleName);
				}
			}
		}
	}

	private void initRefSubTree(TreeItem parent, File f, String moduleName) {
		File[] childs = f.listFiles(new MDPFileTagFilter(fileTags));
		int count = childs == null ? 0 : childs.length;
		for (int i = 0; i < count; i++) {
			File child = childs[i];
			TreeItem ti = new RefMDFileDirTreeItem(parent, child);
			ti.setData(child);
			if (child.isDirectory()) {
				initRefSubTree(ti, child, moduleName);
			}
		}
	}
}
