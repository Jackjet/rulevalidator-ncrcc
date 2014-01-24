package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;

public class ScriptResultDetailComposite extends Composite implements IResultDetailComposite<ScriptRuleExecuteResult> {

	private TableViewer tableViewer;
	private Table table;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ScriptResultDetailComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Override
	public void loadRuleExecuteResult(ScriptRuleExecuteResult executeResult) {
		DataSet dataSet = executeResult.getDataSet();
		List<String> columnNames = dataSet.getColumnNames();
		for (String columnName : columnNames) {
			TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
			tableColumn.setText(columnName.toUpperCase());
			tableColumn.setWidth(100);
		}
		table.layout();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		ScriptResultLabelContentProvider labelContentProvider = new ScriptResultLabelContentProvider(columnNames);
		tableViewer.setLabelProvider(labelContentProvider);
		tableViewer.setContentProvider(labelContentProvider);
		tableViewer.setColumnProperties(columnNames.toArray(new String[0]));

		if (dataSet == null || dataSet.isEmpty()) {
			tableViewer.setInput(Collections.emptyList());
		} else {
			tableViewer.setInput(dataSet);
		}
		tableViewer.refresh();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public static final class ScriptResultLabelContentProvider implements IStructuredContentProvider,
			ITableLabelProvider {

		private List<String> displayColumns;

		public ScriptResultLabelContentProvider(List<String> displayColumns) {
			this.displayColumns = displayColumns;
		}

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

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

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof DataRow) {
				DataRow dataRow = (DataRow) element;
				String columnName = displayColumns.get(columnIndex);
				return dataRow.getValue(columnName.toUpperCase()) == null ? "" : dataRow.getValue(
						columnName.toUpperCase()).toString();
			}
			return "";
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof DataSet) {
				return ((DataSet) inputElement).getRows().toArray();
			}
			return new Object[0];
		}

	}

}
