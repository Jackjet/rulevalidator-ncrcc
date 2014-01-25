package ncmdp.views;

import java.util.ArrayList;
import java.util.List;

import ncmdp.actions.AddOperationAction;
import ncmdp.actions.AddParameterAction;
import ncmdp.actions.DelOperationAction;
import ncmdp.actions.DelParameterAction;
import ncmdp.actions.ParameterMoveDownAction;
import ncmdp.actions.ParameterMoveUpAction;
import ncmdp.common.ParamTypeEnum;
import ncmdp.model.Attribute;
import ncmdp.model.Constant;
import ncmdp.model.activity.Operation;
import ncmdp.model.activity.Parameter;
import ncmdp.parts.CellPart;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.MyComboboxCellEditor;
import ncmdp.ui.TypeSelectCellEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 模型视图，业务接口
 * @author wangxmn
 *
 */
public class OperationView extends Composite {
	public OperationView(Composite parent, int style) {
		super(parent, style);
		createPartControl(this);
	}

	private TreeViewer treeView = null;

	private SashForm sash = null;

	private TableViewer tableView = null;

	private CellPart cellPart = null;

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		sash = new SashForm(parent, SWT.NONE);
		sash.setOrientation(SWT.HORIZONTAL);
		createOperatorTreeView();
		createParamTableView();
	}

	private void createOperatorTreeView() {
		Group group = new Group(sash, SWT.NONE);
		group.setText(Messages.OperationView_0);
		group.setLayout(new FillLayout());
		ViewForm treeVF = new ViewForm(group, SWT.NONE);

		treeVF.setLayout(new FillLayout());
		treeView = new TreeViewer(treeVF, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		final Tree tree = treeView.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < OperationCellModifer.colNames.length; i++) {
			createTreeColumn(tree, OperationCellModifer.colNames[i], 80,
					SWT.LEFT, i);
		}
		//业务接口操作的viewer提供者，主要为内容和标签
		OperationCellProvider provider = new OperationCellProvider();
		treeView.setContentProvider(provider);
		treeView.setLabelProvider(provider);

		//
		CellEditor[] cellEditors = new CellEditor[OperationCellModifer.colNames.length];
		cellEditors[0] = null;
		cellEditors[1] = new TextCellEditor(tree);
		cellEditors[2] = new TextCellEditor(tree);
		cellEditors[3] = new MyComboboxCellEditor(tree, Attribute.TYPE_STYLES,
				SWT.READ_ONLY);
		cellEditors[4] = new TypeSelectCellEditor(tree,
				NCMDPTool.getBaseTypes());
		cellEditors[5] = new CheckboxCellEditor(tree);
		cellEditors[6] = new TextCellEditor(tree);
		cellEditors[7] = new ComboBoxCellEditor(tree, Constant.VISIBILITYS,
				SWT.READ_ONLY);
		cellEditors[8] = new ComboBoxCellEditor(tree, Constant.TRANSKINDS,
				SWT.READ_ONLY);
		cellEditors[9] = new CheckboxCellEditor(tree);
		cellEditors[10] = new TextCellEditor(tree);
		cellEditors[11] = new TextCellEditor(tree);
		cellEditors[12] = new TextCellEditor(tree);
		cellEditors[13] = new TextCellEditor(tree);
		treeView.setCellEditors(cellEditors);
		treeView.setColumnProperties(OperationCellModifer.colNames);
		treeView.setCellModifier(new OperationCellModifer(this));
		treeView.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				TreeItem[] tis = tree.getSelection();
				if (tis != null && tis.length > 0) {
					TreeItem ti = tis[0];
					Object o = ti.getData();
					if (o instanceof ArrayList) {
						tableView.setInput(null);
					} else if (o instanceof Operation) {
						Operation oper = (Operation) o;
						List<Parameter> al = oper.getParas();
						tableView.setInput(al);
					}
				}
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				TreeItem[] tis = tree.getSelection();
				if (tis != null && tis.length > 0) {
					TreeItem ti = tis[0];
					Object o = ti.getData();
					if (o instanceof Operation) {
						NCMDPTool.showErrDlg(((Operation) o).validate());
					}
				}
			}
		});
		Action addOper = new AddOperationAction(this);
		Action delOper = new DelOperationAction(this);

		MenuManager mm = new MenuManager();
		mm.add(addOper);
		mm.add(delOper);
		Menu menu = mm.createContextMenu(tree);
		tree.setMenu(menu);

		ToolBar tb = new ToolBar(treeVF, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addOper);
		tbm.add(delOper);
		tbm.update(true);

		treeVF.setTopLeft(tb);
		treeVF.setContent(treeView.getControl());

	}

	private void createParamTableView() {

		Group group = new Group(sash, SWT.NONE);
		group.setText(Messages.OperationView_1);
		group.setLayout(new FillLayout());
		ViewForm tableVF = new ViewForm(group, SWT.NONE);
		tableVF.setLayout(new FillLayout());
		tableView = new TableViewer(tableVF, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		Table table = tableView.getTable();
		for (int i = 0; i < ParamCellModifer.colNames.length; i++) {
			createTableColumn(table, ParamCellModifer.colNames[i], 100,
					SWT.LEFT, i);
		}
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		ParamCellProvider provider = new ParamCellProvider();
		tableView.setLabelProvider(provider);
		tableView.setContentProvider(provider);

		CellEditor[] cellEditors = new CellEditor[ParamCellModifer.colNames.length];
		cellEditors[0] = new TextCellEditor(table);
		cellEditors[1] = new TextCellEditor(table);
		cellEditors[2] = new TypeSelectCellEditor(table,
				NCMDPTool.getBaseTypes());
		cellEditors[3] = new CheckboxCellEditor(table);
		cellEditors[4] = new TextCellEditor(table);
		cellEditors[5] = new MyComboboxCellEditor(table, Attribute.TYPE_STYLES,
				SWT.READ_ONLY);
		cellEditors[6] = new TextCellEditor(table);
		cellEditors[7] = new TextCellEditor(table);
		cellEditors[8] = new ComboBoxCellEditor(table,
				ParamTypeEnum.getAlltype(), SWT.READ_ONLY);
		tableView.setCellEditors(cellEditors);
		tableView.setColumnProperties(ParamCellModifer.colNames);
		tableView.setCellModifier(new ParamCellModifer(this));

		AddParameterAction addAction = new AddParameterAction(this);
		DelParameterAction delAction = new DelParameterAction(this);
		ParameterMoveUpAction moveUpAction = new ParameterMoveUpAction(this);
		ParameterMoveDownAction moveDownAction = new ParameterMoveDownAction(
				this);
		MenuManager mm = new MenuManager();
		mm.add(addAction);
		mm.add(delAction);
		mm.add(moveUpAction);
		mm.add(moveDownAction);
		Menu menu = mm.createContextMenu(table);
		table.setMenu(menu);
		ToolBar tb = new ToolBar(tableVF, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addAction);
		tbm.add(delAction);
		tbm.add(moveUpAction);
		tbm.add(moveDownAction);
		tbm.update(true);
		tableVF.setTopLeft(tb);
		tableView.setInput(null);
		tableVF.setContent(tableView.getControl());

	}

	public TableViewer getParamTableView() {
		return tableView;
	}

	private TableColumn createTableColumn(Table table, String colName,
			int width, int align, int index) {
		TableColumn col = new TableColumn(table, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}

	@Override
	public boolean setFocus() {
		return treeView.getControl().setFocus();

	}

	private TreeColumn createTreeColumn(Tree tree, String colName, int width,
			int align, int index) {
		TreeColumn col = new TreeColumn(tree, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);

		col.setAlignment(align);
		return col;
	}

	public TreeViewer getTreeView() {
		return treeView;
	}

	public CellPart getCellPart() {
		return cellPart;
	}

	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
	}
}
