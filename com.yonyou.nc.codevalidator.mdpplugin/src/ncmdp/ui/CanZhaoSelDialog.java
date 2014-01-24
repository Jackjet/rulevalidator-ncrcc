package ncmdp.ui;

import java.util.ArrayList;
import java.util.List;

import ncmdp.factory.ImageFactory;
import ncmdp.model.CanZhao;
import ncmdp.tool.NCMDPTool;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * 参照设置dialog
 * @author wangxmn
 *
 */
public class CanZhaoSelDialog extends TitleAreaDialog {
	public class CanzhaoSelModifer implements ICellModifier {
		public boolean canModify(Object element, String property) {
			if(colNames[0].equals(property)){
				return true;
			}else if(colNames[1].equals(property)){
				return false;
			}
			return false;
		}
		public Object getValue(Object element, String property) {
			if(element instanceof Object[]){
				Object[] cz = (Object[])element;
				if(colNames[0].equals(property)){
					return alSels.contains(element);
				}else if(colNames[1].equals(property)){
					return cz[0];
				}
			}
			return ""; 
		}
		public void modify(Object element, String property, Object value) {
			TableItem ti = (TableItem)element;
			Object obj = ti.getData();
			if(obj instanceof Object[]){
				Object[] cz = (Object[])obj;
				if(colNames[0].equals(property)){
					if(((Boolean)value).booleanValue() && !alSels.contains(cz)){
						alSels.add(cz);
						cz[1]=mdTypeName;
					}else{
						alSels.remove(cz);
						cz[1]=null;
					}
					tv.refresh(cz);
				}
			}
		}
	}
	public class CanzhaoSelProvider implements IStructuredContentProvider,
	ITableLabelProvider {
		@SuppressWarnings("rawtypes")
		public Object[] getElements(Object inputElement) {
			if(inputElement instanceof List){
				return ((List)inputElement).toArray();
			}
			return new Object[0];
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		public Image getColumnImage(Object element, int columnIndex) {
			if(columnIndex == 0){
				return ImageFactory.getCheckedImage(alSels.contains(element)); 
			}
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			if(element instanceof Object[]){
				Object[] cz = (Object[])element;
				switch(columnIndex){
				case 0 : return ""; 
				case 1 : return cz[0].toString();
				}
			}
			return ""; 
		}
		public void addListener(ILabelProviderListener listener) {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		public void removeListener(ILabelProviderListener listener) {
			
		}
	}
	private String[] colNames = {"",Messages.CanZhaoSelDialog_4}; 
	private TableViewer tv = null;
	private List<Object[]> czs = null;
	private String mdTypeName = null;
	private ArrayList<Object[]> alSels = new ArrayList<Object[]>();
	private  IProject project=null;
	public CanZhaoSelDialog(Shell parentShell,CanZhao[] exist,String mdTypeName, IProject project) {
		super(parentShell);
		this.mdTypeName = mdTypeName;
		this.project = project;
		ArrayList<String> existCZNames = new ArrayList<String>();
		int count = exist == null ? 0 :exist.length;
		for (int i = 0; i < count; i++) {
			existCZNames.add(exist[i].getName());
		}
		//从数据库中查找与该实体和模块对应的参照
		czs = NCMDPTool.getCanzhaoFromDB(mdTypeName, project);
		count = czs == null ? 0 : czs.size();
		for (int i = 0; i < count; i++) {
			if(existCZNames.contains(czs.get(i)[0])){
				alSels.add(czs.get(i));
			}
		}
	}
	public CanZhao[] getSelCanzhaos(){
		CanZhao[] czs = new CanZhao[alSels.size()];
		for (int i = 0; i < czs.length; i++) {
			czs[i] = new CanZhao();
			czs[i].setName(alSels.get(i)[0].toString());
		}
		return czs;
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		tv = new TableViewer(container, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION|SWT.BORDER);
		Table table = tv.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		NCMDPTool.createTableColumn(table, colNames[0], 30, SWT.LEFT, 0);
		NCMDPTool.createTableColumn(table, colNames[1], 300, SWT.LEFT, 1);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		CanzhaoSelProvider provider = new CanzhaoSelProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		CellEditor[] ces = new CellEditor[colNames.length];
		ces[0] = new CheckboxCellEditor();
		
		tv.setCellEditors(ces);
		tv.setColumnProperties(colNames);
		tv.setCellModifier(new CanzhaoSelModifer());
		tv.setInput(czs);
		setTitle(Messages.CanZhaoSelDialog_5); 
		setMessage(Messages.CanZhaoSelDialog_6); 
		return container;
	}

	@Override
	protected void okPressed() {
		NCMDPTool.updateCanzhaoInfo(mdTypeName, project, czs);
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(350,400); 
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX;
	}

}
