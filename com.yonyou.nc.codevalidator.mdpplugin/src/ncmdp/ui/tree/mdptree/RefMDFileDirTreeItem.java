package ncmdp.ui.tree.mdptree;

import java.io.File;

import ncmdp.factory.ImageFactory;
import ncmdp.model.Constant;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class RefMDFileDirTreeItem extends TreeItem {

	public RefMDFileDirTreeItem(Tree parent, File file) {
		super(parent, SWT.NONE);
		init(file);
	}

	public RefMDFileDirTreeItem(TreeItem parentItem, File file) {
		super(parentItem, SWT.NONE);
		init(file);
	}

	private void init(File file) {
		setText(file.getName());
		setData(file);
		if (file.isDirectory()) {
			setImage(ImageFactory.getDirectoryImage());
		} else {
			if (file.getAbsolutePath().endsWith(Constant.MDFILE_SUFFIX)) {
				setImage(ImageFactory.getFileImage());
			} else
				setImage(ImageFactory.getBpfImg());
		}
	}

	public File getFile() {
		return (File) getData();
	}

	protected void checkSubclass() {
	}

}
