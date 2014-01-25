package ncmdp.ui;

import java.util.List;

import ncmdp.factory.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;

/**
 * 鬼知道这个类是干嘛用的，擦！
 * @author wangxmn
 *
 */
public class ColSelectDlg extends TitleAreaDialog {
	public static class ColSelObj {
		private String colName = null;
		private boolean sel = false;
		public ColSelObj(String colName, boolean sel){
			super();
			this.colName = colName;
			this.sel = sel;
		}
		public String getColName() {
			return colName;
		}
		public void setColName(String colName) {
			this.colName = colName;
		}
		public boolean isSel() {
			return sel;
		}
		public void setSel(boolean sel) {
			this.sel = sel;
		}
	}
	private class ColSelTableModifer implements ICellModifier {
		public boolean canModify(Object element, String property) {
			if(property.equals(colNames[0])){
				return true;
			}
			return false;
		}

		public Object getValue(Object element, String property) {
			if(element instanceof ColSelObj){
				ColSelObj obj = (ColSelObj)element;
				if(colNames[0].equals(property)){
					return new Boolean(obj.isSel());
				}else if(colNames[1].equals(property)){
					return obj.getColName();
				}
			}
			return null;
		}

		public void modify(Object element, String property, Object value) {
			TableItem ti = (TableItem) element;
			ColSelObj obj = (ColSelObj)ti.getData();
			if(colNames[0].equals(property)){
				obj.setSel(((Boolean)value).booleanValue());
			}else if(colNames[1].equals(property)){
				obj.setColName((String)value);
			}
			tableViewer.refresh(obj);
		}
		
	}
	private class ColSelTableProvider extends LabelProvider implements ITableLabelProvider, IStructuredContentProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			if(element instanceof ColSelObj){
				ColSelObj obj = (ColSelObj)element;
				switch(columnIndex){
					case 0: return ImageFactory.getCheckedImage(obj.isSel());
				}
			}
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			if(element instanceof ColSelObj){
				ColSelObj obj = (ColSelObj)element;
				switch(columnIndex){
				case 0: return ""; 
				case 1: return obj.getColName();
				}
			}
			return ""; 
		}

		@SuppressWarnings("rawtypes")
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return new Object[0];
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
	}
	private class AllSelAction extends Action{
		private boolean isSellAll = false;
		public AllSelAction(boolean isSelAll){
			super(isSelAll?Messages.ColSelectDlg_2:Messages.ColSelectDlg_3);
			this.isSellAll = isSelAll;
		}
		@Override
		public void run() {
			for (int i = 0; i < alCols.size(); i++) {
				alCols.get(i).setSel(isSellAll);
			}
			tableViewer.refresh();
		}
		
	}
	private class MoveAction extends Action{
		private boolean isUp = false;
		public MoveAction(boolean isUp){
			super(isUp? Messages.ColSelectDlg_4:"下移");
			this.isUp = isUp;
		}
		@SuppressWarnings("unchecked")
		public void run() {
			Table table = tableViewer.getTable();
			List<ColSelObj> al =(List<ColSelObj>)tableViewer.getInput();
			int[] selIndex = table.getSelectionIndices();
			if(selIndex == null || selIndex.length == 0)
				return;
			if(isUp && selIndex[0] == 0){
				return;
			}else if(!isUp && selIndex[selIndex.length-1] >=al.size() -1){
				return;
			}
			if(isUp){
				for (int i = 0; i < selIndex.length; i++) {
					ColSelObj obj = al.remove(selIndex[i]);
					al.add(selIndex[i]-1, obj);
				}
			}else{
				for (int i = selIndex.length-1; i >=0 ; i--) {
					ColSelObj obj = al.remove(selIndex[i]);
					al.add(selIndex[i]+1, obj);
				}
				
			}
			tableViewer.refresh();
		}
	}
	private String title=""; 
	private String msg = ""; 
	private TableViewer tableViewer = null;
	private List<ColSelObj> alCols = null;
	private String[] colNames={"", Messages.ColSelectDlg_9}; 
	public ColSelectDlg(Shell parentShell,String title,String msg,List<ColSelObj> cols) {
		super(parentShell);
		this.title = title;
		this.msg = msg;
		this.alCols = cols;
		
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new FillLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		ViewForm vf = new ViewForm(container, SWT.NONE);
		
		tableViewer = new TableViewer(vf, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		ColSelTableProvider provider = new ColSelTableProvider();
		tableViewer.setContentProvider(provider);
		tableViewer.setLabelProvider(provider);
		tableViewer.setColumnProperties(colNames);
		tableViewer.setCellModifier(new ColSelTableModifer());
		Table table =tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		for (int i = 0; i < colNames.length; i++) {
			int w = 100;
			if (i == 0)
				w = 30;
			createTableColumn(table, colNames[i], w, SWT.LEFT, i);
		}
		CellEditor[] editors = new CellEditor[colNames.length];
		editors[0] = new CheckboxCellEditor(table);
		tableViewer.setCellEditors(editors);
		setTitle(title);
		setMessage(msg);
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(new AllSelAction(true));
		tbm.add(new AllSelAction(false));
		tbm.add(new MoveAction(true));
		tbm.add(new MoveAction(false));
	
		tbm.update(true);
		vf.setTopLeft(tb);
		vf.setContent(tableViewer.getControl());
//		ArrayList<ColSelObj> al = new ArrayList<ColSelObj>();
//		if(cols != null)
//			al.addAll(Arrays.asList(cols));
		tableViewer.setInput(alCols);
		return container;
	}
	
	private TableColumn createTableColumn(org.eclipse.swt.widgets.Table table, String colName, int width, int align, int index) {
		TableColumn col = new TableColumn(table, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}
	@Override
	protected Point getInitialSize() {
		return new Point(350,500); 
	}

	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		super.okPressed();
	}

}
