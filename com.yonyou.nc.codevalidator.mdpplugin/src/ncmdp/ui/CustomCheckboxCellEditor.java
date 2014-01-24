package ncmdp.ui;

import java.lang.reflect.Field;
import ncmdp.factory.ImageFactory;
import ncmdp.util.MDPLogger;

import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 复选框编辑器
 */
public class CustomCheckboxCellEditor extends CheckboxCellEditor {
	private TreeViewer tv = null;
	private TreeItem selItem = null;
	private int selIndex = -1;

	public CustomCheckboxCellEditor(TreeViewer tv) {
		super(tv.getTree());
		this.tv = tv;
	}

	private void updateSelItemAndSelIndex() {
		if (tv == null)
			return;
		Tree tree = tv.getTree();
		TreeItem[] items = tree.getSelection();// 获得选中的item
		if (items != null && items.length > 0) {
			selItem = items[0];
			try {
				ColumnViewerEditor ce = tv.getColumnViewerEditor();
				Field ff = ce.getClass().getDeclaredField("treeEditor");
				ff.setAccessible(true);
				TreeEditor tee = (TreeEditor) ff.get(ce);
				selIndex = tee.getColumn();
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}

	}

	private void updateTreeItem(boolean showImg) {
		if (selItem != null && selIndex != -1) {
			Tree tree = tv.getTree();
			TreeColumn col = tree.getColumn(selIndex);
			if (!showImg) {
				selItem.setImage(selIndex, null);
			} else {
				selItem.setImage(selIndex,
						ImageFactory.getCheckedImage(((Button) getControl())
								.getSelection()));
			}
			col.setWidth(col.getWidth());
		}
	}

	//
	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
		super.deactivate();
		updateTreeItem(true);
	}

	@Override
	protected Control createControl(Composite parent) {
		Button btn = new Button(parent, SWT.CHECK);
		btn.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});
		return btn;
	}

	@Override
	protected Object doGetValue() {
		updateTreeItem(true);
		boolean sel = ((Button) getControl()).getSelection();
		return new Boolean(sel);
	}

	@Override
	protected void doSetFocus() {
		updateSelItemAndSelIndex();
		updateTreeItem(false);
		getControl().setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		((Button) getControl()).setSelection(((Boolean) value).booleanValue());
	}

	/**
	 * 更改确认为鼠标双击事件的事件间隔，毫秒级
	 */
	@Override
	protected int getDoubleClickTimeout() {
		return 200;
	}

}
