package ncmdp.importattr.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ncmdp.factory.ImageFactory;
import ncmdp.model.Type;
import ncmdp.pdmxml.Field;
import ncmdp.pdmxml.Table;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.TypeSelectCellEditor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
/**
 * 导入属性的向导
 * @author wangxmn
 *
 */
public class SelTableColumnWizardPage extends WizardPage {
	private class TableListProvider extends LabelProvider implements ITableLabelProvider, IStructuredContentProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Table) {
				return ((Table) element).getDisplayName();
			}
			return null;
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

	private class FieldTableProvider extends LabelProvider implements ITableLabelProvider, IStructuredContentProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof Field) {
				Field fld = (Field) element;
				switch (columnIndex) {
				case 0:
					return ImageFactory.getCheckedImage(fld.isSel());
				case 3:
					return ImageFactory.getCheckedImage(fld.isKey());
				}
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Field) {
				switch (columnIndex) {
				case 0:
					return ""; 
				case 1:
					return ((Field) element).getDisplayName();
				case 2:
					return ((Field) element).getTypeDisplayString();
				case 3:
					return "";
				case 4:
					Type type = ((Field) element).getModuleType();
					return type == null ? "" : type.getDisplayName(); 
				}
			}
			return null;
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

	private class FieldTableModifer implements ICellModifier {
		public boolean canModify(Object element, String property) {
			if (property.equals(colNames[0]) || property.equals(colNames[3]))
				return true;
			return false;
		}

		public Object getValue(Object element, String property) {
			if (element instanceof Field) {
				Field field = (Field) element;
				if (property.equals(colNames[0])) {
					return new Boolean(field.isSel());
				} else if (property.equals(colNames[1])) {
					return field.getDisplayName();
				} else if (property.equals(colNames[2])) {
					return field.getTypeDisplayString();// getType();
				} else if (property.equals(colNames[3])) {
					return field.isKey();// ?"是":"";//getType();
				} else if (property.equals(colNames[4])) {
					return field.getModuleType();
				}
			}

			return ""; 
		}

		public void modify(Object element, String property, Object value) {
			TableItem ti = (TableItem) element;
			Field field = (Field) ti.getData();
			if (colNames[0].equals(property)) {
				field.setSel(((Boolean) value).booleanValue());
			} else if (colNames[3].equals(property)) {
				field.setModuleType((Type) value);
			}
			fieldTable.refresh(field);
		}

	}

	private final String[] colNames = { "", Messages.SelTableColumnWizardPage_5, Messages.SelTableColumnWizardPage_6, Messages.SelTableColumnWizardPage_7, Messages.SelTableColumnWizardPage_8 }; 

	private ListViewer tableList = null;

	private TableViewer fieldTable = null;
	private IImportTables importTables = null;
	public SelTableColumnWizardPage(String pageName) {
		super(pageName);
	}

	public SelTableColumnWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		SashForm container = new SashForm(parent, SWT.NONE);
		container.setOrientation(SWT.HORIZONTAL);
		tableList = new ListViewer(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		TableListProvider provider = new TableListProvider();
		tableList.setLabelProvider(provider);
		tableList.setContentProvider(provider);
		tableList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleTableListSelectionChanged(event);
			}
		});
		ViewForm vf = new ViewForm(container, SWT.NONE);
		fieldTable = new TableViewer(vf, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		FieldTableProvider fieldProvider = new FieldTableProvider();
		fieldTable.setContentProvider(fieldProvider);
		fieldTable.setLabelProvider(fieldProvider);
		fieldTable.setColumnProperties(colNames);
		fieldTable.setCellModifier(new FieldTableModifer());
		org.eclipse.swt.widgets.Table ttt = fieldTable.getTable();
		ttt.setHeaderVisible(true);
		ttt.setLinesVisible(true);
		for (int i = 0; i < colNames.length; i++) {
			int w = 100;
			if (i == 0)
				w = 30;
			createTableColumn(ttt, colNames[i], w, SWT.LEFT, i);
		}
		CellEditor[] editors = new CellEditor[colNames.length];
		editors[0] = new CheckboxCellEditor(ttt);
		editors[4] = new TypeSelectCellEditor(ttt, NCMDPTool.getBaseTypes(), false);
		fieldTable.setCellEditors(editors);
		vf.setContent(fieldTable.getControl());
		setControl(container);
	}

	private TableColumn createTableColumn(org.eclipse.swt.widgets.Table table, String colName, int width, int align, int index) {
		TableColumn col = new TableColumn(table, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}

	protected void handleTableListSelectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		StructuredSelection ss = (StructuredSelection) selection;
		Table table = (Table) ss.getFirstElement();
		ArrayList<Field> al = getImportTables().getFields(table);
		fieldTable.setInput(al);

	}

	@SuppressWarnings("unchecked")
	public Field[] getSelectedFields() {
		ArrayList<Field> al = (ArrayList<Field>) fieldTable.getInput();
		ArrayList<Field> alSel = new ArrayList<Field>();
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).isSel()) {
				alSel.add(al.get(i));
			}
		}
		return alSel.toArray(new Field[0]);
	}

	@SuppressWarnings("rawtypes")
	public void initTableListData() {
		fieldTable.setInput(new ArrayList());
		ArrayList<Table> al = new ArrayList<Table>();
		Table[] tables = getImportTables().getAllTables();
		if (tables != null) {
			Arrays.sort(tables, new Comparator<Table>(){
				public int compare(Table o1, Table o2) {
					String name1 =o1==null? "": o1.getDisplayName(); 
					String name2 =o2==null? "" : o2.getDisplayName(); 
					return name1== null? -1 : name1.compareTo(name2);
				}
			});
			al.addAll(Arrays.asList(tables));
		}
		tableList.setInput(al);
	}

	public IImportTables getImportTables() {
		return importTables;
	}

	public void setImportTables(IImportTables importTables) {
		this.importTables = importTables;
	}
}
