package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.yonyou.nc.codevalidator.rule.vo.ParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.PrivateParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.RuleItemConfigVO;
import com.yonyou.nc.codevalidator.runtime.plugin.common.RuleDefinitionVOComposite;

public class RuleConfigDetailComposite extends Composite {

	public static final String[] COMMON_PARAM_TITLE = new String[] { "参数名", "参数值" };

	private RuleDefinitionVOComposite ruleDefinitionVOComposite;
	private Group ruleDefinitionDetailGroup;
	private TableViewer ruleDetailTableViewer;
	private IRuleEditorListener ruleEditorListener;

	private boolean modified = false;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public RuleConfigDetailComposite(Composite parent, int style, IRuleEditorListener ruleEditorListener) {
		super(parent, style);
		this.ruleEditorListener = ruleEditorListener;
		setLayout(new GridLayout(1, false));

		ruleDefinitionVOComposite = new RuleDefinitionVOComposite(this, SWT.NONE);
		ruleDefinitionVOComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		initRuleDefinitionDetailGroup();
	}

	private void initRuleDefinitionDetailGroup() {
		ruleDefinitionDetailGroup = new Group(this, SWT.NONE);
		ruleDefinitionDetailGroup.setText("\u89C4\u5219\u8BE6\u7EC6\u53C2\u6570");
		ruleDefinitionDetailGroup.setLayout(new GridLayout(1, false));
		ruleDefinitionDetailGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ruleDetailTableViewer = new TableViewer(ruleDefinitionDetailGroup, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = ruleDetailTableViewer.getTable();
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdTable.heightHint = 100;
		table.setLayoutData(gdTable);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn paramNameColumn = new TableColumn(table, SWT.NONE);
		paramNameColumn.setWidth(350);
		paramNameColumn.setText("参数名");
		TableColumn paramValueColumn = new TableColumn(table, SWT.NONE);
		paramValueColumn.setWidth(400);
		paramValueColumn.setText("参数值");

		ruleDetailTableViewer.setColumnProperties(COMMON_PARAM_TITLE);
		
//		RuleCommonParamCellModifier ruleCommonParamModifier = new RuleCommonParamCellModifier();
//		ruleDetailTableViewer.setCellModifier(ruleCommonParamModifier);
//
//		CellEditor[] cellEditors = new CellEditor[COMMON_PARAM_TITLE.length];
//		cellEditors[1] = new TextCellEditor(table);
//		ruleDetailTableViewer.setCellEditors(cellEditors);
		
		TableViewerColumn paramNameViewerColumn = new TableViewerColumn(ruleDetailTableViewer, paramNameColumn);
		paramNameViewerColumn.setEditingSupport(new EmptyEditingSupport(ruleDetailTableViewer));
		
		TableViewerColumn paramValueViewerColumn = new TableViewerColumn(ruleDetailTableViewer, paramValueColumn);
		paramValueViewerColumn.setEditingSupport(new ConfigurableEditingSupport(ruleDetailTableViewer, ruleEditorListener));

		RuleDetailTableLabelContentProvider labelContentProvider = new RuleDetailTableLabelContentProvider();
		ruleDetailTableViewer.setLabelProvider(labelContentProvider);
		ruleDetailTableViewer.setContentProvider(labelContentProvider);

		ruleDetailTableViewer.setColumnProperties(COMMON_PARAM_TITLE);
//		ruleDetailTableViewer.setCellModifier(new RuleDetailParamCellModifier());

	}

	void loadRuleItemConfigVO(RuleItemConfigVO ruleItemConfigVO) {
		ruleDefinitionVOComposite.loadRuleDefinitionVo(ruleItemConfigVO.getRuleDefinitionVO());
		PrivateParamConfiguration privateParamConfiguration = ruleItemConfigVO.getPrivateParamConfiguration();
		ruleDetailTableViewer.setInput(privateParamConfiguration);
	}

	public boolean isModified() {
		return modified;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public static class RuleDetailTableLabelContentProvider implements IStructuredContentProvider, ITableLabelProvider {

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
			ParamConfiguration paramConfiguration = (ParamConfiguration) element;
			switch (columnIndex) {
			case 0:
				return paramConfiguration.getParamName();
			case 1:
				return paramConfiguration.getParamValue() == null ? "" : paramConfiguration.getParamValue();
			default:
				break;
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof PrivateParamConfiguration) {
				PrivateParamConfiguration privateParamConfiguration = (PrivateParamConfiguration) inputElement;
				return privateParamConfiguration.getParamConfigurationList().toArray();
			}
			return null;
		}

	}

	class RuleDetailParamCellModifier implements ICellModifier {

		@Override
		public boolean canModify(Object element, String property) {
			return RuleConfigEditorComposite.COMMON_PARAM_TITLE[1].equals(property);
		}

		@Override
		public Object getValue(Object element, String property) {
			List<String> propertyList = Arrays.asList(RuleConfigEditorComposite.COMMON_PARAM_TITLE);
			int propertyIndex = propertyList.indexOf(property);
			if (1 == propertyIndex) {
				ParamConfiguration paramConfiguration = (ParamConfiguration) element;
				return paramConfiguration.getParamValue() == null ? "" : paramConfiguration.getParamValue();
			}
			return null;
		}

		@Override
		public void modify(Object element, String property, Object value) {
			TableItem tableItem = (TableItem) element;
			ParamConfiguration paramConfiguration = (ParamConfiguration) tableItem.getData();
			if (RuleConfigEditorComposite.COMMON_PARAM_TITLE[1].equals(property)) {
				paramConfiguration.setParamValue(String.valueOf(value));
				modified = true;
				ruleDetailTableViewer.refresh();
			}
		}

	}

}
