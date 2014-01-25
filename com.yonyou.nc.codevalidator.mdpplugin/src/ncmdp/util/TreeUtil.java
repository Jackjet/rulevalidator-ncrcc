package ncmdp.util;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeUtil {
	public static void expandTree(Tree tree) {
		int count = tree.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem child = tree.getItem(i);
			expandTreeItem(child);
		}
	}

	private static void expandTreeItem(TreeItem ti) {
		ti.setExpanded(true);
		int count = ti.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem child = ti.getItem(i);
			expandTreeItem(child);
		}
	}
}
