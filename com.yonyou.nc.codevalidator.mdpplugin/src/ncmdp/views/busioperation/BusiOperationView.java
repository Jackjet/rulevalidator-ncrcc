package ncmdp.views.busioperation;

import ncmdp.parts.CellPart;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;

public class BusiOperationView extends Composite {
	private TableViewer tableView = null;

	/* 这是干嘛的??*/
	private CellPart cellPart = null;

	//	private SashForm sash = null;//用来把下面的view分成两块的，可以参考操作接口的建模

	public BusiOperationView(Composite parent, int style) {
		super(parent, style);
		createPartControl(this);

	}

	private void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		//		sash = new SashForm(parent, SWT.NONE);
		//		sash.setOrientation(SWT.HORIZONTAL);
		ViewForm viewForm = new ViewForm(parent, SWT.NONE);
		viewForm.setLayout(new FillLayout());
		createTableView(viewForm);
		ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);
		BusiOperationMenuActionGroup actionGroup = new BusiOperationMenuActionGroup(this);
		actionGroup.fillActionToolBar(toolBarManager);
		viewForm.setContent(tableView.getControl());
		viewForm.setTopLeft(toolBar);
		parent.getShell().open();
	}

	private void createTableView(Composite parent) {
		//1)
		tableView = new TableViewer(parent, SWT.SINGLE | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER
				| SWT.CHECK);
		//2)
		Table table = tableView.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		//3)
		tableView.setColumnProperties(BusiOPTableModifier.columnProiprtties);
		String[] tableNames = { "name", "displayName", "opInterface", "description" };
		for (String colName : tableNames) {
			layout.addColumnData(new ColumnWeightData(40));
			new TableColumn(table, SWT.NONE).setText(colName);
		}
		CellEditor[] cellEditors = new CellEditor[5];
		cellEditors[0] = null;
		cellEditors[1] = null;
		cellEditors[2] = null;
		cellEditors[3] = null;
		//		cellEditors[4] = new CheckboxCellEditor(tableView.getTable());
		tableView.setCellEditors(cellEditors);
		//4）
		tableView.setContentProvider(new BusiOPTableContentProvider());
		tableView.setLabelProvider(new BusiOPTableViewerProvider());
		tableView.setCellModifier(new BusiOPTableModifier(tableView));
		//5）
	}

	public TableViewer getTableView() {
		return tableView;
	}

	public CellPart getCellPart() {
		return cellPart;
	}

	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
	}
}
