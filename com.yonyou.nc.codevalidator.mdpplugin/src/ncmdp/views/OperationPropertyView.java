package ncmdp.views;

import java.util.ArrayList;

import ncmdp.actions.AddOperationPropertyAction;
import ncmdp.actions.DelOperationPropertyAction;
import ncmdp.model.Constant;
import ncmdp.model.property.XMLAttribute;
import ncmdp.model.property.XMLElement;
import ncmdp.tool.NCMDPTool;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;

public class OperationPropertyView extends Composite implements IMenuListener{
	private TreeViewer tv = null;
	public OperationPropertyView(Composite parent, int style) {
		super(parent, style);
		createPartControl();
	}

	private void createPartControl() {
		setLayout(new FillLayout());
		ViewForm vf = new ViewForm(this, SWT.NONE);
		tv =  new TreeViewer(vf, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION|SWT.BORDER);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		
		for (int i = 0; i < OperationPropertyModifer.colNames.length; i++) {
			NCMDPTool.createTreeColumn(tree, OperationPropertyModifer.colNames[i], 200, SWT.LEFT, i);
		}
		OperationPropertyProvider provider = new OperationPropertyProvider();
		tv.setContentProvider(provider);
		tv.setLabelProvider(provider);
		
		tv.setColumnProperties(OperationPropertyModifer.colNames);
		tv.setCellModifier(new OperationPropertyModifer(this));
		CellEditor[] editors = new CellEditor[OperationPropertyModifer.colNames.length];
		editors[1] = new TextCellEditor(tree);
		tv.setCellEditors(editors);
		
		AddOperationPropertyAction[] actions = new AddOperationPropertyAction[9];
		actions[0] = new AddOperationPropertyAction(this, "add property", Constant.XML_TYPE_PROPERTY);
		actions[1] = new AddOperationPropertyAction(this, "add map", Constant.XML_TYPE_MAP);
		actions[2] = new AddOperationPropertyAction(this, "add set", Constant.XML_TYPE_SET);
		actions[3] = new AddOperationPropertyAction(this, "add list", Constant.XML_TYPE_LIST);
		actions[4] = new AddOperationPropertyAction(this, "add props", Constant.XML_TYPE_PROPS);
		actions[5] = new AddOperationPropertyAction(this, "add prop", Constant.XML_TYPE_PROP);
		actions[6] = new AddOperationPropertyAction(this, "add entry", Constant.XML_TYPE_ENTRY);
		actions[7] = new AddOperationPropertyAction(this, "add ref", Constant.XML_TYPE_REF);
		actions[8] = new AddOperationPropertyAction(this, "add value", Constant.XML_TYPE_VALUE);
		
		MenuManager mm = new MenuManager();
		mm.addMenuListener(this);
		for (int i = 0; i < actions.length; i++) {
			mm.add(actions[i]);
		}
		Menu menu = mm.createContextMenu(tree);
		tree.setMenu(menu);
		
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(new DelOperationPropertyAction(this));
		tbm.update(true);
		vf.setTopLeft(tb);
		vf.setContent(tv.getControl());
		
		
		
	}
	public TreeViewer getTreeViewer(){
		return tv;
	}

	public void menuAboutToShow(IMenuManager manager) {
		IStructuredSelection sel = (IStructuredSelection) tv.getSelection();
		Object selObj = sel.getFirstElement();
		IContributionItem[] items = manager.getItems();
		for (int i = 0; i < items.length; i++) {
			if(items[i] instanceof ActionContributionItem){
				IAction a = ((ActionContributionItem)items[i]).getAction();
				if(a instanceof AddOperationPropertyAction){
					AddOperationPropertyAction action = (AddOperationPropertyAction)a;
					String eleType = action.getEleType();
					if(selObj instanceof ArrayList){
						action.setEnabled(Constant.XML_TYPE_PROPERTY.equals(eleType));
					}else if(selObj instanceof XMLElement){
						action.setEnabled(((XMLElement)selObj).getAvailableChildElementType().contains(eleType));
					}else if(selObj instanceof XMLAttribute){
						action.setEnabled(false);
					}
				}
			}
		}
		
	}





}
