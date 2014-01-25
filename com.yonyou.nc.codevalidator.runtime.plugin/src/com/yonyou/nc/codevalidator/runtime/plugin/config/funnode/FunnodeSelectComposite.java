package com.yonyou.nc.codevalidator.runtime.plugin.config.funnode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.dialogs.FilteredTree;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCCommonQueryUtils;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCFunnodeVO;
import com.yonyou.nc.codevalidator.resparser.function.utils.NCModuleVO;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.runtime.plugin.config.CheckBoxFilterTreeTable;
import com.yonyou.nc.codevalidator.runtime.plugin.config.PatternFilter;

/**
 * 功能节点编码名称选择对话框
 * 
 * @author zhangwch1
 * @modify mazhqa
 */
public class FunnodeSelectComposite extends Composite {

	private static final String[] FUNNCODECOLUMNNAMES = { "功能节点编码", "功能节点名称" };
	private final List<NCFunnodeVO> configedVos = new ArrayList<NCFunnodeVO>();
	private List<NCFunnodeVO> funNodeList;
	private Map<String, NCFunnodeVO> funcodeToNodeVoMap = new HashMap<String, NCFunnodeVO>();
	private CheckboxTreeViewer filterCheckboxTreeViewer;
	private FuncNodeTreeTableContentProvider funNodeLabelContentProvider;

	public FunnodeSelectComposite(List<String> selectedcodes, Composite parent, int style)
			throws RuleBaseException {
		super(parent, style);
		initContent();
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
		initLeftFilterTree(sashForm);
		initRightDisplayTree(sashForm);

		initSelection(selectedcodes);
	}

	private void initContent() throws ResourceParserException {
		funNodeList = NCCommonQueryUtils.queryAllFunnodeVOs();
		funcodeToNodeVoMap.clear();
		for (NCFunnodeVO ncFuncVo : funNodeList) {
			funcodeToNodeVoMap.put(ncFuncVo.getFuncode(), ncFuncVo);
		}
	}

	private void initSelection(List<String> selectedNodeList) {
		if (selectedNodeList != null && selectedNodeList.size() > 0) {
			configedVos.clear();
			for (String selectedNode : selectedNodeList) {
				NCFunnodeVO ncFunnodeVO = funcodeToNodeVoMap.get(selectedNode);
				configedVos.add(ncFunnodeVO);
			}
			filterCheckboxTreeViewer.setCheckedElements(configedVos.toArray(new NCFunnodeVO[configedVos.size()]));
		}
	}

	protected void initLeftFilterTree(SashForm sashForm) throws RuleBaseException {
		FilteredTree leftfilteredTree = new FilteredTree(sashForm, SWT.FULL_SELECTION,
				new org.eclipse.ui.dialogs.PatternFilter(), true);
		leftfilteredTree.setBackground(sashForm.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		leftfilteredTree.setLayoutData(new GridData(GridData.FILL_BOTH));
		if (leftfilteredTree.getFilterControl() != null) {
			Composite filterComposite = leftfilteredTree.getFilterControl().getParent();
			GridData gd1 = (GridData) filterComposite.getLayoutData();
			gd1.verticalIndent = 2;
			gd1.horizontalIndent = 1;
		}

		TreeViewer treeViewer = leftfilteredTree.getViewer();
		ModuleTreeContentProvider labelContentProvider = new ModuleTreeContentProvider();
		treeViewer.setContentProvider(labelContentProvider);
		treeViewer.setLabelProvider(labelContentProvider);
		treeViewer.setAutoExpandLevel(2);
		treeViewer.setInput(Arrays.asList(ModuleTreeContentProvider.ROOT_ID));

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// 接收树节点变化事件,树节点变化去刷新所选的数据
				if (event.getSelection() instanceof TreeSelection) {
					TreeSelection treeSelection = (TreeSelection) event.getSelection();
					Object selectObj = treeSelection.getFirstElement();
					if (selectObj instanceof NCModuleVO) {
						NCModuleVO moduleVo = (NCModuleVO) selectObj;
						filterCheckboxTreeViewer.setInput(moduleVo);
						filterCheckboxTreeViewer.setCheckedElements(configedVos.toArray());
					} else if (selectObj instanceof String) {
						filterCheckboxTreeViewer.setInput("ROOT");
						filterCheckboxTreeViewer.setCheckedElements(configedVos.toArray());
					}
				}
			}
		});

	}

	private void initRightDisplayTree(SashForm sashForm) throws RuleBaseException {
		PatternFilter filter = new PatternFilter() {
			protected boolean isLeafMatch(Viewer viewer, Object element) {
				if (element instanceof NCFunnodeVO) {
					NCFunnodeVO ruleVO = (NCFunnodeVO) element;
					return wordMatches(ruleVO.getFuncode()) || wordMatches(ruleVO.getFunname());
				}
				return false;
			}
		};
		CheckBoxFilterTreeTable filteredTree = new CheckBoxFilterTreeTable(sashForm, SWT.FULL_SELECTION, filter, true);
		if (filteredTree.getFilterControl() != null) {
			Composite filterComposite = filteredTree.getFilterControl().getParent();
			GridData gd2 = (GridData) filterComposite.getLayoutData();
			gd2.verticalIndent = 2;
			gd2.horizontalIndent = 1;
		}
		filteredTree.setBackground(sashForm.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		filteredTree.setLayoutData(new GridData(GridData.FILL_BOTH));
		sashForm.setWeights(new int[] { 1, 3 });
		filterCheckboxTreeViewer = filteredTree.getViewer();
		Tree tree = filterCheckboxTreeViewer.getTree();
		tree.setLinesVisible(true);
		for (int i = 0; i < FUNNCODECOLUMNNAMES.length; i++) {
			TreeColumn fColumn = new TreeColumn(tree, SWT.LEFT);
			fColumn.setText(FUNNCODECOLUMNNAMES[i]);
			fColumn.setWidth(200);
		}
		tree.setHeaderVisible(true);
		filterCheckboxTreeViewer.setAutoExpandLevel(1);
		funNodeLabelContentProvider = new FuncNodeTreeTableContentProvider(configedVos);
		filterCheckboxTreeViewer.setContentProvider(funNodeLabelContentProvider);
		filterCheckboxTreeViewer.setLabelProvider(funNodeLabelContentProvider);
//		filterCheckboxTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
//
//			@Override
//			public void selectionChanged(SelectionChangedEvent event) {
//				if (event.getSelection() instanceof TreeSelection) {
//					TreeSelection treeSelection = (TreeSelection) event.getSelection();
//					if (treeSelection.getFirstElement() instanceof NCFunnodeVO) {
//						NCFunnodeVO ncFunnodeVo = (NCFunnodeVO) treeSelection.getFirstElement();
//						if (!configedVos.contains(ncFunnodeVo)) {
//							configedVos.add(ncFunnodeVo);
//						}
//					}
//				}
//			}
//		});
		filterCheckboxTreeViewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				boolean checked = event.getChecked();
				NCFunnodeVO ncFunnodeVo = (NCFunnodeVO) event.getElement();
				if (checked && !configedVos.contains(ncFunnodeVo)) {
					configedVos.add(ncFunnodeVo);
				} else if (!checked && configedVos.contains(ncFunnodeVo)) {
					configedVos.remove(ncFunnodeVo);
				}
			}
		});
	}

	private IContributionItem createContributionItem(IAction action) {
		ActionContributionItem aci = new ActionContributionItem(action);
		return aci;
	}

	public List<NCFunnodeVO> getSelectedVos() {
		return configedVos;
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
			List<NCFunnodeVO> currentDisplayFuncVoList = funNodeLabelContentProvider.getCurrentDisplayFuncVoList();
			Set<NCFunnodeVO> configedVoSet = new HashSet<NCFunnodeVO>(configedVos);
			if (isCheckAll) {
				configedVoSet.addAll(currentDisplayFuncVoList);
				filterCheckboxTreeViewer.setCheckedElements(configedVoSet.toArray());
			} else {
				configedVoSet.removeAll(currentDisplayFuncVoList);
				filterCheckboxTreeViewer.setCheckedElements(configedVoSet.toArray());
			}
			configedVos.clear();
			configedVos.addAll(configedVoSet);
		}
	}

}
