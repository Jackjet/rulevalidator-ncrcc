package ncmdp.views;

import ncmdp.actions.AddEnumItemAction;
import ncmdp.actions.DelEnumItemAction;
import ncmdp.actions.EnumItemMoveUpOrDownAction;
import ncmdp.parts.CellPart;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
/**
 * 模型视图（枚举），采用是树状结构
 * @author wangxmn
 *
 */
public class EnumItemView extends Composite {
	private CellPart cellPart = null;
	private TableViewer enumItemTableViewer = null;

	public EnumItemView(Composite parent, int style) {
		super(parent, style);
		createPartControl(this);
	}
	private TableColumn createTableColumn(Table table, String colName, int width, int align, int index){
		TableColumn col = new TableColumn(table, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}
	
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm tableVF = new ViewForm(parent, SWT.NONE);
		tableVF.setLayout(new FillLayout());
		enumItemTableViewer = new TableViewer(tableVF,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION|SWT.BORDER);
		Table table = enumItemTableViewer.getTable();
		for (int i = 0; i < EnumItemCellModifer.colNames.length; i++) {
			createTableColumn(table, EnumItemCellModifer.colNames[i], 100, SWT.LEFT, i);
		}
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		EnumItemCellProvider provider = new EnumItemCellProvider();
		enumItemTableViewer.setLabelProvider(provider);
		enumItemTableViewer.setContentProvider(provider);
		
		CellEditor[] cellEditors = new CellEditor[EnumItemCellModifer.colNames.length];
		cellEditors[0] = new TextCellEditor(table);
		cellEditors[1] = new TextCellEditor(table);
		cellEditors[2] = new TextCellEditor(table);
		cellEditors[3] = new TextCellEditor(table);
		cellEditors[4] = new CheckboxCellEditor(table);
		enumItemTableViewer.setCellEditors(cellEditors);
		enumItemTableViewer.setColumnProperties(EnumItemCellModifer.colNames);
		enumItemTableViewer.setCellModifier(new EnumItemCellModifer(this));
			
		AddEnumItemAction addAction = new AddEnumItemAction(this);
		DelEnumItemAction delAction = new DelEnumItemAction(this);
		EnumItemMoveUpOrDownAction upAction = new EnumItemMoveUpOrDownAction(this, true);
		EnumItemMoveUpOrDownAction downAction = new EnumItemMoveUpOrDownAction(this, false);
		MenuManager mm = new MenuManager();
		mm.add(addAction);
		mm.add(delAction);
		mm.add(upAction);
		mm.add(downAction);
		Menu menu = mm.createContextMenu(table);
		table.setMenu(menu);
		ToolBar tb = new ToolBar(tableVF, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addAction);
		tbm.add(delAction);
		tbm.add(upAction);
		tbm.add(downAction);
		tbm.update(true);
		tableVF.setTopLeft(tb);
		enumItemTableViewer.setInput(null);
		tableVF.setContent(enumItemTableViewer.getControl());
		
	}

	@Override
	public boolean setFocus() {
		return enumItemTableViewer.getControl().setFocus();

	}

	public TableViewer getEnumItemTableViewer() {
		return enumItemTableViewer;
	}
	public CellPart getCellPart() {
		return cellPart;
	}
	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
	}
	public void refresh(){
		enumItemTableViewer.cancelEditing();
		enumItemTableViewer.refresh();
	}
}
