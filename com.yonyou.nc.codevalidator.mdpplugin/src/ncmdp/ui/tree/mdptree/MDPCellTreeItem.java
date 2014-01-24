package ncmdp.ui.tree.mdptree;

import ncmdp.model.Cell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
/**
 * 暂时未被使用 无论工程模型下还是引用模型下都使用 RefMDCellTreeItem
 * @author dingxm
 *
 */
public class MDPCellTreeItem extends TreeItem {
	
	public MDPCellTreeItem(MDPFileTreeItem parent, Cell cell) {
		super(parent, SWT.NONE);
		setText(cell.getDisplayName()+":[4.MDPCellTreeItem]");
		setData(cell);
	}
	protected void checkSubclass () {
	}
	public Cell getCell(){
		return (Cell)getData();
	}
	public MDPFileTreeItem getParentTreeItem(){
		return (MDPFileTreeItem)getParentItem();
	}

}
