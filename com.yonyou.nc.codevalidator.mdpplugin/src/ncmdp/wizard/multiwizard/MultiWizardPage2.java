package ncmdp.wizard.multiwizard;

import java.util.ArrayList;
import java.util.List;

import ncmdp.tool.basic.StringUtil;
import ncmdp.wizard.multiwizard.util.IWizardPageHandler;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class MultiWizardPage2 extends WizardPage implements IWizardPageHandler {
	public static String[] page2ColumnProiprtties = new String[] { "depend", "module" }; //$NON-NLS-1$ //$NON-NLS-2$

	private Text fileNameText;

	private Text preResIDText;

	private MultiWizardPage3 page3 = null;

	private TableViewer tableViewer = null;

	/*本模块*/
	private String ownResModule = ""; //$NON-NLS-1$

	protected MultiWizardPage2(String pageName, String ownModule) {
		super(pageName);
		this.ownResModule = StringUtil.isEmptyWithTrim(ownModule) ? "" : ownModule; //$NON-NLS-1$
	}

	@Override
	public void createControl(Composite parent) {
		setTitle(Messages.MultiWizardPage2_4);
		setMessage(Messages.MultiWizardPage2_5);

		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayout(new GridLayout(1, false));

		Composite topComp = new Composite(comp, SWT.NULL);
		topComp.setLayout(new GridLayout(4, false));
		//文件框
		Label fileLable = new Label(topComp, SWT.NONE);
		fileLable.setText(Messages.MultiWizardPage2_6);
		fileNameText = new Text(topComp, SWT.BORDER);
		GridData textData = new GridData(300, -1);
		textData.horizontalSpan = 2;
		fileNameText.setLayoutData(textData);
		//文件选择按钮
		Button fileButton = new Button(topComp, SWT.NULL);//PUSH|SWT.DOWN|SWT.BORDER);
		fileButton.setText(Messages.MultiWizardPage2_7);

		fileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setFilterNames(new String[] { Messages.MultiWizardPage2_0 });
				fd.setFilterExtensions(new String[] { "*.properties" }); //$NON-NLS-1$
				fd.setText(Messages.MultiWizardPage2_10);

				fd.setText(fileNameText.getText());
				String text = fd.open();
				if (text != null) {
					fileNameText.setText(text);
				}
			}
		});

		//多语资源前缀		
		Label resIDLable = new Label(topComp, SWT.NONE);
		resIDLable.setText(Messages.MultiWizardPage2_11);
		preResIDText = new Text(topComp, SWT.BORDER);
		preResIDText.setLayoutData(new GridData(100, -1));
		Label resIDLable2 = new Label(topComp, SWT.NONE);
		resIDLable2.setText(Messages.MultiWizardPage2_12);

		//表格 
		Group botGroup = new Group(comp, SWT.NULL);
		botGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		botGroup.setLayout(new FillLayout());
		botGroup.setText(Messages.MultiWizardPage2_13);
		if (tableViewer == null) {
			tableViewer = createTable(botGroup);
		}
		List<String> moduleList = new ArrayList<String>();
		moduleList.add("common"); //$NON-NLS-1$
		moduleList.add(ownResModule);
		tableViewer.setInput(moduleList);
		//设定选定的行
		TableItem[] items = tableViewer.getTable().getItems();
		for (TableItem item : items) {
			item.setChecked(true);
		}

		topComp.layout();
		botGroup.layout();
		parent.layout();
		setControl(comp);
	}

	private TableViewer createTable(Composite parent) {
		if (tableViewer == null) {
			tableViewer = new TableViewer(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION
					| SWT.V_SCROLL | SWT.H_SCROLL | SWT.CHECK);
			Table table = tableViewer.getTable();

			table.setHeaderVisible(true);
			table.setLinesVisible(true);

			TableLayout layout = new TableLayout();
			table.setLayout(layout);
			createTableColumn(table, SWT.LEFT, Messages.MultiWizardPage2_15, 160);
			createTableColumn(table, SWT.LEFT, "module", 390); //$NON-NLS-1$
			tableViewer.setContentProvider(new page2TableHelper());
			tableViewer.setLabelProvider(new page2TableHelper());

			CellEditor[] cellEditors = new CellEditor[2];
			cellEditors[0] = null;
			cellEditors[1] = null;
			tableViewer.setCellEditors(cellEditors);

			tableViewer.setColumnProperties(page2ColumnProiprtties);

		}
		return tableViewer;
	}

	protected TableColumn createTableColumn(Table table, int style, String title, int width) {
		TableColumn tc = new TableColumn(table, style);
		tc.setText(title);
		tc.setResizable(true);
		tc.setWidth(width);
		return tc;
	}

	@Override
	public void onPressedNext() {
		String preIndex = preResIDText.getText();
		if (StringUtil.isEmptyWithTrim(fileNameText.getText())
				|| StringUtil.isEmptyWithTrim(preIndex)) {
			MessageDialog.openError(getShell(), "ERROE", Messages.MultiWizardPage2_18); //$NON-NLS-1$
			throw new RuntimeException("ERROE"); //$NON-NLS-1$
		}else if(!preIndex.trim().startsWith("2")){ //$NON-NLS-1$
			MessageDialog.openError(getShell(), "ERROE", Messages.MultiWizardPage2_22); //$NON-NLS-1$
			throw new RuntimeException("ERROE"); //$NON-NLS-1$
		}else if(preIndex.trim().startsWith("2")){ //$NON-NLS-1$
			preIndex = 	preIndex.trim().endsWith("-")?preIndex.substring(0,preIndex.length()-1):preIndex; //$NON-NLS-1$
			if(preIndex.length()>10){
				MessageDialog.openError(getShell(), "ERROE", Messages.MultiWizardPage2_27); //$NON-NLS-1$
				throw new RuntimeException("ERROE"); //$NON-NLS-1$
			}
		}
		
		page3.setPreResID(preIndex);
		page3.setToFilePath(fileNameText.getText());
		page3.setDepCommonModule(getMultiList());
		page3.updateTableData();
	}

	public Text getFileNameText() {
		return fileNameText;
	}

	public void setPage3(MultiWizardPage3 page3) {
		this.page3 = page3;
	}

	/**
	 * 获取 选择的元素
	 * @return
	 */
	private List<String> getMultiList() {
		List<String> multiAttrList = new ArrayList<String>();
		TableItem[] items = tableViewer.getTable().getItems();
		for (TableItem item : items) {
			if (item.getChecked()) {
				multiAttrList.add((String) (item.getData()));
			}
		}
		return multiAttrList;
	}

	/**
	 * 
	 * @author dingxm   2011-6-1
	 */
	class page2TableHelper implements IStructuredContentProvider, ITableLabelProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			} else {
				return new Object[0];
			}
		}

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		/***********************************************************/
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element != null && element instanceof String) {
				String multiContent = (String) element;
				if (columnIndex == 1) { return multiContent; }
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {

		}
	}
}
