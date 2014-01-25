package ncmdp.dnd;

import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;

import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 将cell从资源管理器中进行拖拽时的监听器
 * @author wangxmn
 *
 */
public class MDPExplorerTreeDragListener implements DragSourceListener {
	public MDPExplorerTreeDragListener() {
		super();
	}

	public void dragFinished(DragSourceEvent event) {
		TemplateTransfer.getInstance().setObject(null);
	}

	public void dragSetData(DragSourceEvent event) {
		DragSource ds =(DragSource) event.widget;
		Tree tree = (Tree)ds.getControl();
		TreeItem[] items = tree.getSelection();
		if (items != null && items.length > 0) {
			event.data = items[0];
		}
	}

	public void dragStart(DragSourceEvent event) {
		DragSource ds =(DragSource) event.widget;
		Tree tree = (Tree)ds.getControl();
		TreeItem[] items = tree.getSelection();
		if (items == null || items.length == 0 || !(items[0] instanceof RefMDCellTreeItem)) {
			event.doit = false;
		}
		TemplateTransfer.getInstance().setObject(items[0]);
	}

}
