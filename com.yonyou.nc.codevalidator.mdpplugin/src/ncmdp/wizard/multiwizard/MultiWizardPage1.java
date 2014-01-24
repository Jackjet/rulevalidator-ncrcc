package ncmdp.wizard.multiwizard;

import java.util.ArrayList;
import java.util.List;

import ncmdp.tool.basic.StringUtil;
import ncmdp.wizard.multiwizard.util.IMultiElement;
import ncmdp.wizard.multiwizard.util.IWizardPageHandler;
import ncmdp.wizard.multiwizard.util.MLUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.actions.ActionGroup;

public class MultiWizardPage1 extends WizardPage implements IWizardPageHandler {

	public static String[] page1ColumnProiprtties = new String[] { "modify", "simpSource", "type", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"resid" }; //$NON-NLS-1$

	private TableViewer tableViewer = null;

	/*bmf中所有元素*/
	private List<IMultiElement> sourceMultiAttrLis;

	/*当前在表格中显示的元素*/
	private List<IMultiElement> curMultiAttrList = new ArrayList<IMultiElement>();

	private MultiWizardPage3 page3 = null;

	private Button btnCH = null;

	private Button btnEN = null;

	private Button btnOther = null;

	protected MultiWizardPage1(String pageName, List<IMultiElement> sourceMultiAttrList) {
		super(pageName);
		this.sourceMultiAttrLis = sourceMultiAttrList;
	}

	@Override
	public void createControl(Composite parent) {
		setTitle(Messages.MultiWizardPage1_4);
		setMessage(Messages.MultiWizardPage1_5);
		Composite topComp = new Composite(parent, SWT.NULL);
		setControl(topComp);
		//显示表格
		initUI(topComp);

		initTableData();
	}

	private void initTableData() {
		if (sourceMultiAttrLis != null && sourceMultiAttrLis.size() > 0) {
			refreshTableData();
		}
		tableViewer.setInput(curMultiAttrList);
		//设定选定的行
		TableItem[] items = tableViewer.getTable().getItems();
		for (TableItem item : items) {
			if (StringUtil.isEmptyWithTrim(item.getText(3))) {
				item.setChecked(true);
			}
		}
	}

	/**
	 * 刷新表格数据
	 */
	private void refreshTableData() {
		curMultiAttrList.clear();
		for (IMultiElement ele : sourceMultiAttrLis) {
			if (!StringUtil.isEmptyWithTrim(ele.getResid()) && !ele.getResid().trim().startsWith("2")) { //$NON-NLS-1$
				//前缀不以2开头
				ele.setResid(null);
			}
			String displayName = ele.getDisplayName();
			if (accept(displayName)) {
				curMultiAttrList.add(ele);
			}
		}
		tableViewer.refresh();
	}

	private void initUI(Composite parent) {
		parent.setLayout(new FillLayout());

		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayout(new GridLayout(1, false));
		//topComp
		Composite topComp = new Composite(comp, SWT.NULL);
		topComp.setLayout(new GridLayout(12, false));

		Label fileLable = new Label(topComp, SWT.NONE);
		fileLable.setText(Messages.MultiWizardPage1_7);

		GridData gridData = new GridData();
		gridData.horizontalIndent = 20;
		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initTableData();
			}
		};

		btnCH = new Button(topComp, SWT.CHECK);
		btnCH.setSelection(true);
		btnCH.addSelectionListener(adapter);
		Label fileLable1 = new Label(topComp, SWT.NONE);
		fileLable1.setText(Messages.MultiWizardPage1_8);

		btnEN = new Button(topComp, SWT.CHECK);
		btnEN.setSelection(false);
		btnEN.setLayoutData(gridData);
		btnEN.addSelectionListener(adapter);
		Label fileLable2 = new Label(topComp, SWT.NONE);
		fileLable2.setText(Messages.MultiWizardPage1_9);

		btnOther = new Button(topComp, SWT.CHECK);
		btnOther.setSelection(false);
		btnOther.setLayoutData(gridData);
		btnOther.setLayoutData(gridData);
		Label fileLable3 = new Label(topComp, SWT.NONE);
		fileLable3.setText(Messages.MultiWizardPage1_10);

		//table
		ViewForm viewForm = new ViewForm(comp, SWT.NONE);
		viewForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewForm.setLayout(new FillLayout());
		if (tableViewer == null) {
			tableViewer = createTable(viewForm);
		}
		ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);
		MyMenuActionGroup actionGroup = new MyMenuActionGroup();
		actionGroup.fillActionToolBar(toolBarManager);
		viewForm.setContent(tableViewer.getControl());
		viewForm.setTopLeft(toolBar);

		setControl(parent);
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
			createTableColumn(table, SWT.LEFT, Messages.MultiWizardPage1_11, 60);
			createTableColumn(table, SWT.LEFT, "displayName", 190); //$NON-NLS-1$
			createTableColumn(table, SWT.LEFT, Messages.MultiWizardPage1_13, 150);
			createTableColumn(table, SWT.LEFT, Messages.MultiWizardPage1_14, 185);
			tableViewer.setContentProvider(new page1TableHelper());
			tableViewer.setLabelProvider(new page1TableHelper());

			CellEditor[] cellEditors = new CellEditor[4];
			cellEditors[0] = null;
			cellEditors[1] = null;
			cellEditors[2] = null;
			cellEditors[3] = null;
			tableViewer.setCellEditors(cellEditors);

			tableViewer.setColumnProperties(page1ColumnProiprtties);
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

	public MultiWizardPage3 getPage3() {
		return page3;
	}

	public void setPage3(MultiWizardPage3 page3) {
		this.page3 = page3;
	}

	@Override
	public void onPressedNext() {
		page3.setMultiAttrList(getMultiList());
	}

	/**
	 * 获取 选择的元素
	 * @return
	 */
	private List<IMultiElement> getMultiList() {
		List<IMultiElement> multiAttrList = new ArrayList<IMultiElement>();
		TableItem[] items = tableViewer.getTable().getItems();
		for (TableItem item : items) {
			if (item.getChecked()) {
				multiAttrList.add((IMultiElement) (item.getData()));
			}
		}
		return multiAttrList;
	}

	private boolean accept(String displayName) {
		if (btnCH.getSelection()) {
			boolean conCH = MLUtil.containsChinese(displayName);
			if (conCH) { return true; }
		}
		if (btnEN.getSelection()) {
			boolean conEN = MLUtil.containsEN(displayName);
			if (conEN) { return true; }
		}
		if (btnOther.getSelection()) {
			boolean conOthers = MLUtil.containsOthers(displayName);
			if (conOthers) { return true; }
		}
		return false;
	}

	/**
	 * 
	 * @author dingxm   2010-8-11
	 *
	 */
	class page1TableHelper implements IStructuredContentProvider, ITableLabelProvider {

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
			if (element != null && element instanceof IMultiElement) {
				IMultiElement multiContent = (IMultiElement) element;
				if (columnIndex == 0) {
					return null;
				} else if (columnIndex == 1) {
					return multiContent.getDisplayName();
				} else if (columnIndex == 2) {
					return multiContent.getElementType();
				} else if (columnIndex == 3) { return multiContent.getResid(); }

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

	class MyMenuActionGroup extends ActionGroup {

		public void fillActionToolBar(ToolBarManager tm) {
			Action selectAllAvtion = new SelectAllAvtion();
			Action removeAllAvtion = new RemoveAllAvtion();
			tm.add(selectAllAvtion);
			tm.add(removeAllAvtion);
			tm.update(true);
		}
	}

	private class SelectAllAvtion extends Action {
		public SelectAllAvtion() {
			setText(Messages.MultiWizardPage1_15);
		}

		@Override
		public void run() {
			TableItem[] items = tableViewer.getTable().getItems();
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					items[i].setChecked(true);
				}
			}
		}
	}

	private class RemoveAllAvtion extends Action {
		public RemoveAllAvtion() {
			setText(Messages.MultiWizardPage1_16);
		}

		@Override
		public void run() {
			TableItem[] items = tableViewer.getTable().getItems();
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					items[i].setChecked(false);
				}
			}
		}
	}
}
