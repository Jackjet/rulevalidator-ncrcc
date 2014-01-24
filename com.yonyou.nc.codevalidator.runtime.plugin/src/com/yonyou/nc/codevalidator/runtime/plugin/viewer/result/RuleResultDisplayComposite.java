package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.executor.SessionExecResultManager;
import com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree.IResultTreeNode;
import com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree.RuleSessionTreeRoot;

public class RuleResultDisplayComposite extends Composite {

	private TreeViewer treeViewer;
	private TableViewer tableViewer;
	private Text startTimeText;
	private Text endTimeText;

	private List<IRuleExecuteResult> ruleExecuteResults;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public RuleResultDisplayComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		SashForm sashForm = new SashForm(this, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		initTreeViewer(sashForm);
		initTableViewer(sashForm);

		sashForm.setWeights(new int[] { 1, 2 });
	}

	private void initTreeViewer(Composite sashForm) {
		treeViewer = new TreeViewer(sashForm, SWT.BORDER);
		RuleSessionTreeLabelContentProvider treeLabelContentProvider = new RuleSessionTreeLabelContentProvider();
		treeViewer.setLabelProvider(treeLabelContentProvider);
		treeViewer.setContentProvider(treeLabelContentProvider);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof TreeSelection) {
					TreeSelection treeSelection = (TreeSelection) selection;
					IResultTreeNode treeNode = (IResultTreeNode) treeSelection.getFirstElement();
					if (treeNode != null) {
						SessionRuleExecuteResult sessionRuleExecuteResult = treeNode.getSessionRuleExecuteResult();
						startTimeText.setText(String.format("%1$tF %1$tT", sessionRuleExecuteResult.getStartTime()));
						endTimeText.setText(String.format("%1$tF %1$tT", sessionRuleExecuteResult.getEndTime()));
						ruleExecuteResults = treeNode.getRuleExecResultList();
						tableViewer.setInput(ruleExecuteResults);
						tableViewer.refresh(true);
					}
				}
			}
		});

		refreshTreeViewerData();
	}

	public void removeAllResults() {
		SessionExecResultManager.getInstance().removeAllResults();
		refreshTreeViewerData();
		clearDetailComposite();
	}

//	public void removeSelectionResult() {
//		ISelection selection = treeViewer.getSelection();
//		if (selection instanceof TreeSelection) {
//			TreeSelection treeSelection = (TreeSelection) selection;
//			IResultTreeNode treeNode = (IResultTreeNode) treeSelection.getFirstElement();
//			if (treeNode != null) {
//				SessionRuleExecuteResult sessionRuleExecuteResult = treeNode.getSessionRuleExecuteResult();
//				SessionExecResultManager.getInstance().removeResult(sessionRuleExecuteResult);
//			}
//		}
//		refreshTreeViewerData();
//		clearDetailComposite();
//	}

	private void clearDetailComposite() {
		startTimeText.setText("");
		endTimeText.setText("");
		tableViewer.setInput(Collections.emptyList());
	}

	public void refreshTreeViewerData() {
		List<SoftReference<SessionRuleExecuteResult>> ruleExecResultList = SessionExecResultManager.getInstance()
				.getRuleExecResultList();
		List<IResultTreeNode> treeNodeList = new ArrayList<IResultTreeNode>();
		for (SoftReference<SessionRuleExecuteResult> sessionExecResult : ruleExecResultList) {
			if(sessionExecResult.get() != null){
				treeNodeList.add(new RuleSessionTreeRoot(sessionExecResult.get()));
			}
		}
		treeViewer.setInput(treeNodeList);
		treeViewer.refresh();
	}

	private void initTableViewer(Composite sashForm) {
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Group sessionDataGroup = new Group(composite, SWT.NONE);
		sessionDataGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		sessionDataGroup.setSize(64, 64);
		sessionDataGroup.setLayout(new GridLayout(4, false));
		sessionDataGroup.setText("Session");

		Label label = new Label(sessionDataGroup, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("\u5F00\u59CB\u65F6\u95F4:");

		startTimeText = new Text(sessionDataGroup, SWT.BORDER);
		startTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		startTimeText.setEditable(false);

		Label label1 = new Label(sessionDataGroup, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label1.setText("\u7ED3\u675F\u65F6\u95F4:");

		endTimeText = new Text(sessionDataGroup, SWT.BORDER);
		endTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		endTimeText.setEditable(false);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		List<String> columnNames = new ArrayList<String>();
		for (RuleResultColumnVO ruleResultColumnVo : RuleResultConstants.RULE_RESULT_COLUMNS) {
			TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
			tableColumn.setText(ruleResultColumnVo.getDisplayName());
			tableColumn.setWidth(ruleResultColumnVo.getColumnWidth());
			columnNames.add(ruleResultColumnVo.getDisplayName());
		}
		table.layout();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		RuleExecResultTableLabelContentProvider tableLabelContentProvider = new RuleExecResultTableLabelContentProvider();
		tableViewer.setColumnProperties(columnNames.toArray(new String[columnNames.size()]));
		tableViewer.setLabelProvider(tableLabelContentProvider);
		tableViewer.setContentProvider(tableLabelContentProvider);
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof StructuredSelection) {
					StructuredSelection selection = (StructuredSelection) event.getSelection();
					if (selection.getFirstElement() instanceof IRuleExecuteResult) {
						IRuleExecuteResult ruleExecResult = (IRuleExecuteResult) selection.getFirstElement();
						RuleExecResultDetailDialog dialog = new RuleExecResultDetailDialog(
								RuleResultDisplayComposite.this.getShell(), ruleExecResult, ruleExecuteResults);
						//仅打开，并不能做任何修改工作，不用判断是否OK/Cancel
						dialog.open();
					}
				}

			}
		});
	}

	@Override
	protected void checkSubclass() {
	}
}
