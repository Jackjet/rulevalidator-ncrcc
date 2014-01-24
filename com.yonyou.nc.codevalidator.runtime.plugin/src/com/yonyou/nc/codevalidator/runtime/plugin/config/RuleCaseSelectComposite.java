package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.runtime.plugin.editor.IRuleConfigVOProperty;
import com.yonyou.nc.codevalidator.runtime.plugin.editor.RuleConfigColumnVO;
import com.yonyou.nc.codevalidator.runtime.plugin.editor.RuleConfigConstants;

/**
 * NC规则批量选择界面
 * 
 * @author luoweid
 * @modify mazhqa 进行了重构
 */
public class RuleCaseSelectComposite extends Composite {

	/**
	 * 用于记录本次选择的规则
	 */
	private final List<RuleDefinitionAnnotationVO> selectedRuleDefinitionList = new ArrayList<RuleDefinitionAnnotationVO>();
	
	/**
	 * 在打开对话框之前，就已经选择的规则
	 */
	private final List<RuleDefinitionAnnotationVO> checkedRuleDefinitionVoList;
	private RuleConfigFilterTreeComposite ruleConfigFilterTreeComposite;
	private CheckBoxFilterTreeTable ruleDefinitionSelectedTreeTable;

	public RuleCaseSelectComposite(List<RuleDefinitionAnnotationVO> configedVos, Composite parent, int style) {
		super(parent, style);
		this.checkedRuleDefinitionVoList = configedVos;

		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ViewForm composite = new ViewForm(this, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.widthHint = 700;
		gd.heightHint = 250;
		composite.setLayoutData(gd);

		ToolBar toolBar = new ToolBar(composite, SWT.NONE);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);
		toolBarManager.add(createContributionItem(new CheckAllAction(true)));
		toolBarManager.add(createContributionItem(new CheckAllAction(false)));
		toolBarManager.update(true);
		composite.setTopLeft(toolBar);

		SashForm sashForm = new SashForm(composite, SWT.NONE);
		composite.setContent(sashForm);
		initCategoryTree(sashForm);
		initRuleDefintionSelectTree(sashForm);
		sashForm.setWeights(new int[] { 1, 3 });
	}

	private void initCategoryTree(SashForm sashForm) {
		ruleConfigFilterTreeComposite = new RuleConfigFilterTreeComposite(sashForm, SWT.FULL_SELECTION);
		ruleConfigFilterTreeComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TreeViewer conditionTreeViewer = ruleConfigFilterTreeComposite.getConditionTreeViewer();
		conditionTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof TreeSelection) {
					TreeSelection treeSelection = (TreeSelection) selection;
					IConfigTreeNode configTreeNode = (IConfigTreeNode) treeSelection.getFirstElement();
					CheckboxTreeViewer treeViewer = ruleDefinitionSelectedTreeTable.getViewer();
					RuleDefinitionFilterContext filterContext = new RuleDefinitionFilterContext(RuleDefinitionsReader
							.getInstance().getAllDefinitionVos(), checkedRuleDefinitionVoList, selectedRuleDefinitionList);
					treeViewer.setInput(configTreeNode.filterRuleDefinitions(filterContext));
					treeViewer.setGrayedElements(checkedRuleDefinitionVoList.toArray());
					treeViewer.setCheckedElements(selectedRuleDefinitionList.toArray());
				}
			}
		});
	}

	private void initRuleDefintionSelectTree(SashForm sashForm) {
		PatternFilter filter = new PatternFilter() {
			protected boolean isLeafMatch(Viewer viewer, Object element) {
				if (element instanceof RuleDefinitionAnnotationVO) {
					RuleDefinitionAnnotationVO ruleVO = (RuleDefinitionAnnotationVO) element;
					return wordMatches(ruleVO.getSimpleIdentifier())
							|| wordMatches(ruleVO.getRuleDefinitionIdentifier())
							|| wordMatches(ruleVO.getCatalog().getName())
							|| wordMatches(ruleVO.getSubCatalog().getName()) || wordMatches(ruleVO.getDescription())
							|| wordMatches(ruleVO.getSpecialParamStr());
				}
				return false;
			}
		};
		ruleDefinitionSelectedTreeTable = new CheckBoxFilterTreeTable(sashForm, SWT.FULL_SELECTION, filter, true);
		if (ruleDefinitionSelectedTreeTable.getFilterControl() != null) {
			Composite filterComposite = ruleDefinitionSelectedTreeTable.getFilterControl().getParent();
			GridData gd2 = (GridData) filterComposite.getLayoutData();
			gd2.verticalIndent = 2;
			gd2.horizontalIndent = 1;
		}
		ruleDefinitionSelectedTreeTable.setBackground(sashForm.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		ruleDefinitionSelectedTreeTable.setLayoutData(new GridData(GridData.FILL_BOTH));

		final CheckboxTreeViewer treeViewer = ruleDefinitionSelectedTreeTable.getViewer();
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		for (RuleConfigColumnVO configEntry : RuleConfigConstants.RULE_MULTI_CONFIG_COLUMNS) {
			TreeColumn treeColumn = new TreeColumn(tree, SWT.LEFT);
			treeColumn.setText(configEntry.getDisplayName());
			treeColumn.setWidth(configEntry.getColumnWidth());
			final RuleConfigSorter ruleConfigSorter = new RuleConfigSorter(configEntry.getRuleConfigVOProperty());
			treeColumn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ruleConfigSorter.changeAsc();
					treeViewer.setSorter(ruleConfigSorter);
					treeViewer.refresh();
				}
			});
		}
		tree.setHeaderVisible(true);
		treeViewer.setAutoExpandLevel(1);
		RuleCaseSelectLabelContentProvider labelContentProvider = new RuleCaseSelectLabelContentProvider();
		treeViewer.setContentProvider(labelContentProvider);
		treeViewer.setLabelProvider(labelContentProvider);
//		treeViewer.setInput(this);
		// 已选中的rulecase置灰
		treeViewer.setGrayedElements(checkedRuleDefinitionVoList.toArray());
		treeViewer.setSorter(new ViewerSorter());
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				RuleDefinitionAnnotationVO ruleDefinitionVo = (RuleDefinitionAnnotationVO) event.getElement();
				if(event.getChecked()){
					selectedRuleDefinitionList.add(ruleDefinitionVo);
				} else {
					selectedRuleDefinitionList.remove(ruleDefinitionVo);
				}
			}
		});
	}

	private IContributionItem createContributionItem(IAction action) {
		ActionContributionItem aci = new ActionContributionItem(action);
		return aci;
	}

	private class CheckAllAction extends Action {

		private boolean isCheckAll;

		public CheckAllAction(boolean isCheckAll) {
			super();
			this.isCheckAll = isCheckAll;
			setText(isCheckAll ? "全选" : "全不选");
		}

		@Override
		public void run() {
			CheckboxTreeViewer viewer = ruleDefinitionSelectedTreeTable.getViewer();
			@SuppressWarnings("unchecked")
			List<RuleDefinitionAnnotationVO> ruleVoList = (List<RuleDefinitionAnnotationVO>)viewer.getInput();
			for (RuleDefinitionAnnotationVO ruleDefinitionVO : ruleVoList) {
				viewer.setSubtreeChecked(ruleDefinitionVO, isCheckAll);
				if(isCheckAll && !selectedRuleDefinitionList.contains(ruleDefinitionVO)){
					selectedRuleDefinitionList.add(ruleDefinitionVO);
				} else if(!isCheckAll && selectedRuleDefinitionList.contains(ruleDefinitionVO)){
					selectedRuleDefinitionList.remove(ruleDefinitionVO);
				}
			}
		}
	}

	/**
	 * 得到最终选择的规则定义VO对象
	 * 
	 * @return
	 */
	RuleDefinitionAnnotationVO[] getSelectedDefinitionVos() {
		return selectedRuleDefinitionList.toArray(new RuleDefinitionAnnotationVO[selectedRuleDefinitionList.size()]);
	}

	public static class RuleConfigSorter extends ViewerSorter {

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
			RuleDefinitionAnnotationVO ruleItemConfigVo1 = (RuleDefinitionAnnotationVO) e1;
			RuleDefinitionAnnotationVO ruleItemConfigVo2 = (RuleDefinitionAnnotationVO) e2;
			String propertyValue1 = ruleConfigVOProperty.getPropertyValue(ruleItemConfigVo1);
			String propertyValue2 = ruleConfigVOProperty.getPropertyValue(ruleItemConfigVo2);
			return asc ? propertyValue1.compareTo(propertyValue2) : propertyValue2.compareTo(propertyValue1);
		}
	}

}
