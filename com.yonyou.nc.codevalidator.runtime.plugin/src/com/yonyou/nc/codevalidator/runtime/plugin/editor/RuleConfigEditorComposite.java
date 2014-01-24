package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.browser.WebBrowserView;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.vo.CommonParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.ParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.RuleCheckConfigurationImpl;
import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;
import com.yonyou.nc.codevalidator.rule.vo.RuleItemConfigVO;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.config.RuleCaseSelectDialog;
import com.yonyou.nc.codevalidator.runtime.plugin.filter.FilteredRuleDefinitionSelectionDialog;
import com.yonyou.nc.codevalidator.runtime.plugin.filter.RuleDefinitionVOItem;

@SuppressWarnings("restriction")
public class RuleConfigEditorComposite extends Composite {

	public static final String[] COMMON_PARAM_TITLE = new String[] { "参数名", "参数值" };

	private RuleCheckConfigurationImpl ruleCheckConfiguration;

	private TableViewer ruleConfigTableViewer;

	private IRuleEditorListener ruleEditorListener;
	private TableViewer commonParamTableViewer;

	private List<ActionContributionItem> actionContributionItems = new ArrayList<ActionContributionItem>();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public RuleConfigEditorComposite(Composite parent, int style, IRuleEditorListener ruleEditorListener) {
		super(parent, style);
		this.ruleEditorListener = ruleEditorListener;
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ViewForm viewForm = new ViewForm(this, SWT.NONE);
		viewForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		initToolbarManager(viewForm);

		Composite composite = new Composite(viewForm, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		viewForm.setContent(composite);

		Group commonConfigComposite = new Group(composite, SWT.NONE);
		commonConfigComposite.setText("\u516C\u5171\u53C2\u6570");
		commonConfigComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		commonConfigComposite.setLayout(new GridLayout(1, false));
		initCommonParamTable(commonConfigComposite);

		Group ruleConfigComposite = new Group(composite, SWT.NONE);
		ruleConfigComposite.setText("\u5DF2\u914D\u7F6E\u89C4\u5219");
		ruleConfigComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		ruleConfigComposite.setLayout(new GridLayout(1, false));

		initRuleConfigTable(ruleConfigComposite);
	}

	private void initToolbarManager(ViewForm viewForm) {
		ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT | SWT.RIGHT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);
		actionContributionItems.add(new ActionContributionItem(new SingleRuleAddAction()));
		actionContributionItems.add(new ActionContributionItem(new BatchRuleAddAction()));
		actionContributionItems.add(new ActionContributionItem(new DeleteAction()));
		actionContributionItems.add(new ActionContributionItem(new JiraHelpAction()));
		actionContributionItems.add(new ActionContributionItem(new DeleteAllAction()));
		for (ActionContributionItem actionContributionItem : actionContributionItems) {
			toolBarManager.add(actionContributionItem);
		}
		toolBarManager.update(true);
		viewForm.setTopLeft(toolBar);
	}

	private void initCommonParamTable(final Composite commonConfigComposite) {
		commonParamTableViewer = new TableViewer(commonConfigComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = commonParamTableViewer.getTable();
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdTable.heightHint = 70;
		table.setLayoutData(gdTable);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn paramNameColumn = new TableColumn(table, SWT.NONE);
		paramNameColumn.setWidth(350);
		paramNameColumn.setText("参数名");
		TableColumn paramValueColumn = new TableColumn(table, SWT.NONE);
		paramValueColumn.setWidth(400);
		paramValueColumn.setText("参数值");

		commonParamTableViewer.setColumnProperties(COMMON_PARAM_TITLE);
		CellEditor[] cellEditors = new CellEditor[COMMON_PARAM_TITLE.length];
		commonParamTableViewer.setCellEditors(cellEditors);
		
		TableViewerColumn paramNameViewerColumn = new TableViewerColumn(commonParamTableViewer, paramNameColumn);
		paramNameViewerColumn.setEditingSupport(new EmptyEditingSupport(commonParamTableViewer));
		
		TableViewerColumn paramValueViewerColumn = new TableViewerColumn(commonParamTableViewer, paramValueColumn);
		paramValueViewerColumn.setEditingSupport(new ConfigurableEditingSupport(commonParamTableViewer, ruleEditorListener));
		
		RuleCommonParamTableLabelContentProvider tableLabelContentProvider = new RuleCommonParamTableLabelContentProvider();
		commonParamTableViewer.setLabelProvider(tableLabelContentProvider);
		commonParamTableViewer.setContentProvider(tableLabelContentProvider);
	}

	private void initRuleConfigTable(Composite ruleConfigComposite) {
		ruleConfigTableViewer = new TableViewer(ruleConfigComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = ruleConfigTableViewer.getTable();
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdTable.heightHint = 300;
		table.setLayoutData(gdTable);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		RuleConfigTableLabelContentProvider labelContentProvider = new RuleConfigTableLabelContentProvider();
		ruleConfigTableViewer.setLabelProvider(labelContentProvider);
		ruleConfigTableViewer.setContentProvider(labelContentProvider);
		List<String> columnProperties = new ArrayList<String>(RuleConfigConstants.RULE_CONFIG_MAIN_COLUMNS.size());
		for (RuleConfigColumnVO entry : RuleConfigConstants.RULE_CONFIG_MAIN_COLUMNS) {
			TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
			tblclmnNewColumn.setText(entry.getDisplayName());
			columnProperties.add(entry.getDisplayName());
			tblclmnNewColumn.setWidth(entry.getColumnWidth());
			final RuleConfigSorter ruleConfigSorter = new RuleConfigSorter(entry.getRuleConfigVOProperty());
			tblclmnNewColumn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ruleConfigSorter.changeAsc();
					ruleConfigTableViewer.setSorter(ruleConfigSorter);
					ruleConfigTableViewer.refresh();
				}
			});
		}
		ruleConfigTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				refreshActionStatus();
			}
		});
		ruleConfigTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof StructuredSelection) {
					StructuredSelection selection = (StructuredSelection) event.getSelection();
					if (selection.getFirstElement() instanceof RuleItemConfigVO) {
						RuleConfigDetailDialog dialog = new RuleConfigDetailDialog(RuleConfigEditorComposite.this
								.getShell(), (RuleItemConfigVO) selection.getFirstElement(), ruleEditorListener);
						if (dialog.open() == IDialogConstants.OK_ID) {
							fireRuleEditorChanged();
						}
					}
				}
			}
		});
		
		CellEditor[] cellEditors = new CellEditor[RuleConfigConstants.RULE_CONFIG_MAIN_COLUMNS.size()];
		cellEditors[0] = new ObjectListComboBoxCellEditor(ruleConfigTableViewer.getTable(), SWT.NONE, RuleExecuteLevel.getRuleExecuteLevels());
		ruleConfigTableViewer.setCellEditors(cellEditors);
		ruleConfigTableViewer.setColumnProperties(columnProperties.toArray(new String[columnProperties.size()]));
		ruleConfigTableViewer.setCellModifier(new ICellModifier() {
			
			@Override
			public void modify(Object element, String property, Object value) {
				if(property.equals(RuleConfigConstants.EXECUTE_LEVEL_FIELDNAME)) {
					TableItem tableItem = (TableItem) element;
					RuleItemConfigVO ruleItemConfigVo = (RuleItemConfigVO) tableItem.getData();;
					RuleExecuteLevel ruleExecuteLevel = (RuleExecuteLevel) value;
					ruleItemConfigVo.setRuleExecuteLevel(ruleExecuteLevel);
					ruleConfigTableViewer.refresh();
					ruleEditorListener.ruleEditorChanged();
				}
			}
			
			@Override
			public Object getValue(Object element, String property) {
				if(property.equals(RuleConfigConstants.EXECUTE_LEVEL_FIELDNAME)) {
					RuleItemConfigVO ruleItemConfigVo = (RuleItemConfigVO) element;
					return ruleItemConfigVo.getRuleExecuteLevel();
				}
				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				return property.equals(RuleConfigConstants.EXECUTE_LEVEL_FIELDNAME);
			}
		});
	}

	/**
	 * 加载数据
	 * 
	 * @param ruleItemConfigVoList
	 */
	public void loadRuleConfigData(RuleCheckConfigurationImpl ruleCheckConfiguration) {
		this.ruleCheckConfiguration = ruleCheckConfiguration;
		refreshConfigData();

	}

	void refreshConfigData() {
		CommonParamConfiguration commonParamConfiguration = ruleCheckConfiguration.getCommonParamConfiguration();
		commonParamTableViewer.setInput(commonParamConfiguration);
		commonParamTableViewer.refresh();

		List<RuleItemConfigVO> ruleItemConfigVoList = ruleCheckConfiguration.getRuleItemConfigVoList();
		ruleConfigTableViewer.setInput(ruleItemConfigVoList);
		ruleConfigTableViewer.refresh();

		refreshActionStatus();
	}

	void refreshActionStatus() {
		for (ActionContributionItem actionContributionItem : actionContributionItems) {
			actionContributionItem.update();
		}
	}

	private class BatchRuleAddAction extends Action {

		public BatchRuleAddAction() {
			setToolTipText("批量新增");
		}

		@Override
		public void run() {
			List<RuleItemConfigVO> ruleItemConfigVoList = ruleCheckConfiguration.getRuleItemConfigVoList();
			List<RuleDefinitionAnnotationVO> originDefinitionVoList = new ArrayList<RuleDefinitionAnnotationVO>();
			if (ruleItemConfigVoList != null && ruleItemConfigVoList.size() > 0) {
				for (RuleItemConfigVO ruleItemConfigVO : ruleItemConfigVoList) {
					originDefinitionVoList.add(ruleItemConfigVO.getRuleDefinitionVO());
				}
			}
			RuleCaseSelectDialog dialog = new RuleCaseSelectDialog(RuleConfigEditorComposite.this.getShell(),
					originDefinitionVoList);
			if (dialog.open() == IDialogConstants.OK_ID) {
				RuleDefinitionAnnotationVO[] selectVos = dialog.getSelectVos();
				if (selectVos != null && selectVos.length > 0) {
					for (RuleDefinitionAnnotationVO ruleDefinitionVo : selectVos) {
						ruleCheckConfiguration.addRuleItemConfigVo(new RuleItemConfigVO(ruleDefinitionVo));
					}
					refreshConfigData();
					fireRuleEditorChanged();
				}
			}
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/resource.gif");
		}
	}

	private class SingleRuleAddAction extends Action {

		public SingleRuleAddAction() {
			setToolTipText("新增一条");
		}

		@Override
		public void run() {
			FilteredRuleDefinitionSelectionDialog dialog = new FilteredRuleDefinitionSelectionDialog(
					RuleConfigEditorComposite.this.getShell());
			if (dialog.open() == IDialogConstants.OK_ID) {
				RuleDefinitionVOItem ruleDefinitionVoItem = (RuleDefinitionVOItem) dialog.getFirstResult();
				RuleItemConfigVO ruleItemConfigVO = new RuleItemConfigVO(ruleDefinitionVoItem.getRuleDefinitionVO());
				ruleCheckConfiguration.addRuleItemConfigVo(ruleItemConfigVO);
				refreshConfigData();
				fireRuleEditorChanged();
			}
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/addsinglerule.gif");
		}
	}

	private class DeleteAction extends Action {

		public DeleteAction() {
			setToolTipText("删除");
		}

		@Override
		public void run() {
			if (ruleConfigTableViewer.getSelection() instanceof StructuredSelection) {
				StructuredSelection selection = (StructuredSelection) ruleConfigTableViewer.getSelection();
				if (selection.getFirstElement() instanceof RuleItemConfigVO) {
					RuleItemConfigVO ruleItemConfigVo = (RuleItemConfigVO) selection.getFirstElement();
					int itemIndex = ruleCheckConfiguration.getRuleItemConfigVoList().indexOf(ruleItemConfigVo);
					if (itemIndex != -1) {
						ruleCheckConfiguration.removeRuleItemConfigVo(ruleItemConfigVo);
						ruleConfigTableViewer.refresh();
						commonParamTableViewer.refresh();
						if (--itemIndex >= 0) {
							ruleConfigTableViewer.getTable().select(itemIndex);
						} else {
							ruleConfigTableViewer.getTable().select(0);
						}
					}
					refreshConfigData();
					fireRuleEditorChanged();
				}
			}
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/delete.gif");
		}

		@Override
		public boolean isEnabled() {
			return ruleConfigTableViewer != null && !ruleConfigTableViewer.getSelection().isEmpty();
		}
	}
	
	private class DeleteAllAction extends Action {

		public DeleteAllAction() {
			setToolTipText("删除所有");
		}

		@Override
		public void run() {
			ruleCheckConfiguration.removeAll();
			refreshConfigData();
			fireRuleEditorChanged();
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/removeall.gif");
		}

		@Override
		public boolean isEnabled() {
			return ruleConfigTableViewer != null && !ruleConfigTableViewer.getSelection().isEmpty();
		}
	}

	private class JiraHelpAction extends Action {

		public JiraHelpAction() {
			setToolTipText("jira帮助");
		}

		@Override
		public void run() {
			if (ruleConfigTableViewer.getSelection() instanceof StructuredSelection) {
				StructuredSelection selection = (StructuredSelection) ruleConfigTableViewer.getSelection();
				if (selection.getFirstElement() instanceof RuleItemConfigVO) {
					RuleItemConfigVO ruleItemConfigVo = (RuleItemConfigVO) selection.getFirstElement();
					String relatedSystemIssueLink = ruleItemConfigVo.getRuleDefinitionVO().getRelatedSystemIssueLink();
					IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IViewPart viewPart = activePage.findView("org.eclipse.ui.browser.view");
					WebBrowserView webBrowserView = (WebBrowserView) viewPart;
					webBrowserView.setURL(relatedSystemIssueLink);
					activePage.activate(viewPart);
				}
			}
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/jirahelpsearch.gif");
		}

		@Override
		public boolean isEnabled() {
			return ruleConfigTableViewer != null && !ruleConfigTableViewer.getSelection().isEmpty();
		}
	}

	void setRuleEditorListener(IRuleEditorListener ruleEditorListener) {
		this.ruleEditorListener = ruleEditorListener;
	}

	public void fireRuleEditorChanged() {
		commonParamTableViewer.refresh();
		ruleConfigTableViewer.refresh();
		if (ruleEditorListener != null) {
			ruleEditorListener.ruleEditorChanged();
		}
	}

	public RuleCheckConfigurationImpl getRuleCheckConfiguration() {
		return ruleCheckConfiguration;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	class RuleCommonParamCellModifier implements ICellModifier {

		@Override
		public boolean canModify(Object element, String property) {
			return COMMON_PARAM_TITLE[1].equals(property);
		}

		@Override
		public Object getValue(Object element, String property) {
			List<String> propertyList = Arrays.asList(COMMON_PARAM_TITLE);
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
			if (COMMON_PARAM_TITLE[1].equals(property)) {
				paramConfiguration.setParamValue(String.valueOf(value));
			}
			refreshConfigData();
			fireRuleEditorChanged();
		}

	}
	
	public static class RuleConfigSorter extends ViewerSorter{
		
		private boolean asc = false;
		
		private final IRuleConfigVOProperty ruleConfigVOProperty;
		
		public RuleConfigSorter(IRuleConfigVOProperty ruleConfigVOProperty) {
			super();
			this.ruleConfigVOProperty = ruleConfigVOProperty;
		}
		
		public void changeAsc() {
			asc = !asc;
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			RuleItemConfigVO ruleItemConfigVo1 = (RuleItemConfigVO) e1;
			RuleItemConfigVO ruleItemConfigVo2 = (RuleItemConfigVO) e2;
			String propertyValue1 = ruleConfigVOProperty.getPropertyValue(ruleItemConfigVo1);
			String propertyValue2 = ruleConfigVOProperty.getPropertyValue(ruleItemConfigVo2);
			return asc ? propertyValue1.compareTo(propertyValue2) : propertyValue2.compareTo(propertyValue1);
		}
	}
}
