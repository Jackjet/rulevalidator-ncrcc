package ncmdp.ui.tree.mdptree;

import ncmdp.factory.ImageFactory;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class RefMDRootTreeItem extends TreeItem {

	public RefMDRootTreeItem(Tree parent, int style) {
		super(parent, style);
		setText("Ref Model");
		setData("");
		setImage(ImageFactory.getRefModuleTreeItemImage());
	}
	protected void checkSubclass () {
	}
}
