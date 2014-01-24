package ncmdp.views;

import ncmdp.actions.AddBusiItfAttrAction;
import ncmdp.actions.BusinessInterfaceAttrsUpOrDownAction;
import ncmdp.actions.DelBusiItfAttrAction;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.Constant;
import ncmdp.parts.CellPart;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.MyComboboxCellEditor;
import ncmdp.ui.TypeSelectCellEditor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class BusinessInterfaceAttrsView extends Composite {

	private CellPart cellPart = null;
	private TreeViewer tv = null;
	public BusinessInterfaceAttrsView(Composite parent, int style) {
		super(parent, style);
		createPartControl(this);
	}
	public TreeViewer getTreeViewer(){
		return tv;
	}
	
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm vf = new ViewForm(parent, SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < BusinessInterfaceAttrsModifier.colNames.length; i++) {
			createColumn(tree, BusinessInterfaceAttrsModifier.colNames[i], 80, SWT.LEFT, i);
		}
		BusinessInterfaceAttrsViewProvider provider = new BusinessInterfaceAttrsViewProvider();
		tv.setContentProvider(provider);
		tv.setLabelProvider(provider);
		tv.setColumnProperties(BusinessInterfaceAttrsModifier.colNames);
		CellEditor[] editors = new CellEditor[BusinessInterfaceAttrsModifier.colNames.length];
		editors[0] = null;
		editors[1] = new TextCellEditor(tree);
		editors[2] = new TextCellEditor(tree);
		editors[3] = new TypeSelectCellEditor(tree, NCMDPTool.getBaseTypes());//与实体属性相同的类型编辑
		editors[4] = new MyComboboxCellEditor(tree,Constant.TYPE_STYLES,SWT.READ_ONLY);
		editors[5] = new TextCellEditor(tree);
		tv.setCellEditors(editors);
		tv.setCellModifier(new BusinessInterfaceAttrsModifier(this));
		
		Action addAction = new AddBusiItfAttrAction(this);
		Action delAction = new DelBusiItfAttrAction(this);
		Action moveUp = new BusinessInterfaceAttrsUpOrDownAction(this, true);
		Action moveDown = new BusinessInterfaceAttrsUpOrDownAction(this, false);
		MenuManager mm = new MenuManager();
		mm.add(addAction);
		mm.add(delAction);
		mm.add(moveUp);
		mm.add(moveDown);
		Menu menu = mm.createContextMenu(tree);
		tree.setMenu(menu);
		
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addAction);
		tbm.add(delAction);
		tbm.add(moveUp);
		tbm.add(moveDown);
		tbm.update(true);
		vf.setContent(tv.getControl());
		vf.setTopLeft(tb);
	}
	
	private TreeColumn createColumn(Tree tree, String colName , int width, int align, int index){
		TreeColumn col = new TreeColumn(tree,SWT.NONE, index );
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}
	
	public boolean setFocus() {
		return tv.getTree().setFocus();
	}

	public CellPart getCellPart() {
		return cellPart;
	}

	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
	}

	public static void refresh(BusiItfAttr attr){
		NCMDPViewPage page = NCMDPViewSheet.getNCMDPViewPage();
		if(page != null){
			page.getBusinessInterfaceAttrsView().getTreeViewer().refresh(attr);
		}
	}
}
