package ncmdp.views.opImpl;

import ncmdp.parts.CellPart;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;

public class OpItfImplView extends Composite {

	private TableViewer tableViewer = null;

	private CellPart cellPart = null;

	public OpItfImplView(Composite com, int style) {
		super(com, style);
		initUI();
	}

	private void initUI() {
		setLayout(new FillLayout());
		ViewForm vf = new ViewForm(this, SWT.NONE);
		vf.setLayout(new FillLayout());
		TableViewer tableViewer = creatTableViewer(vf);
		ToolBar toolBar = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbManager = new ToolBarManager(toolBar);
		OpItfImplActionGroup actionGroup = new OpItfImplActionGroup(this);
		actionGroup.fillActionToolBar(tbManager);
		vf.setContent(tableViewer.getControl());
		vf.setTopLeft(toolBar);
		getShell().open();
	}

	private TableViewer creatTableViewer(ViewForm vf) {
		//1)
		tableViewer = new TableViewer(vf, SWT.SINGLE | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		//2)
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		//3)
		for(String columnName:OPImplCellModifier.columnProiprtties){
			layout.addColumnData(new ColumnWeightData(100));
			new TableColumn(table, SWT.NONE).setText(columnName);
		}
		//4)
		tableViewer.setContentProvider(new TableHelper());
		tableViewer.setLabelProvider(new TableHelper());
		//5)
		tableViewer.setColumnProperties(OPImplCellModifier.columnProiprtties);
		CellEditor[] cellEditors = new CellEditor[8];
		cellEditors[0] = new TextCellEditor(tableViewer.getTable());
		cellEditors[1] = new TextCellEditor(tableViewer.getTable());
		cellEditors[2] = new TextCellEditor(tableViewer.getTable());
		cellEditors[3] = new TextCellEditor(tableViewer.getTable());
		cellEditors[4] = new TextCellEditor(tableViewer.getTable());
		cellEditors[5] = null;
		cellEditors[6] = new TextCellEditor(tableViewer.getTable());
		cellEditors[7] = new TextCellEditor(tableViewer.getTable());
		tableViewer.setCellEditors(cellEditors);
		tableViewer.setCellModifier(new OPImplCellModifier(tableViewer));
		return tableViewer;
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public CellPart getCellPart() {
		return cellPart;
	}

	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
	}
}
