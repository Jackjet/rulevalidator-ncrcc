package ncmdp.views;

import ncmdp.actions.SetCaozhaoFromDBAction;
import ncmdp.parts.CellPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * 参照视图，需要启动中间件
 * @author wangxmn
 *
 */
public class CanzhaoView extends Composite {
	private CellPart cellPart = null;
	private TreeViewer tv = null;
	public CanzhaoView(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FillLayout());
		initComponet();
	}
	private TreeColumn createTableColumn(Tree tree, String colName, int width, int align, int index){
		TreeColumn col = new TreeColumn(tree, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}
	private void initComponet() {
	
		ViewForm vf = new ViewForm(this, SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION|SWT.BORDER);
		Tree tree = tv.getTree();
		for (int i = 0; i < CanzhaoCellModifer.colNames.length; i++) {
			createTableColumn(tree, CanzhaoCellModifer.colNames[i], 100, SWT.LEFT, i);
		}
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		CanzhaoCellProvider provider = new CanzhaoCellProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		CellEditor[] cellEditors = new CellEditor[CanzhaoCellModifer.colNames.length];
		cellEditors[2] = new CheckboxCellEditor(tree);
		
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new CanzhaoCellModifer(this));
		tv.setColumnProperties(CanzhaoCellModifer.colNames);
		//设置参照
		Action setCZAction = new SetCaozhaoFromDBAction(this);
		MenuManager mm = new MenuManager();
		mm.add(setCZAction);
		Menu menu = mm.createContextMenu(tree);
		tree.setMenu(menu);
		
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(setCZAction);
		tbm.update(true);
		vf.setTopLeft(tb);
		vf.setContent(tv.getControl());
	}
	public TreeViewer getTreeViewer(){
		return tv;
	}
	public CellPart getCellPart() {
		return cellPart;
	}
	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
	}
	@Override
	public boolean setFocus() {
		return tv.getControl().setFocus();
	}
}
