package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;

import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.impl.AbstractSimpleExecuteResult;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.common.RuleDefinitionVOComposite;

public class RuleExecuteResultDetailComposite extends Composite {

	// private Group ruleResultDetail;
	private Composite ruleResultComposite;
	private RuleDefinitionVOComposite ruleDefinitionVOComposite;

	private List<IRuleExecuteResult> ruleExecuteResults;
	private IRuleExecuteResult ruleExecuteResult;
	private StackLayout stackLayout;

	private List<ActionContributionItem> actionContributionItems = new ArrayList<ActionContributionItem>();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public RuleExecuteResultDetailComposite(Composite parent, int style, IRuleExecuteResult ruleExecuteResult,
			List<IRuleExecuteResult> ruleExecuteResults) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		ViewForm viewForm = new ViewForm(this, SWT.NONE);
		viewForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		initToolbarManager(viewForm);

		Composite composite = new Composite(viewForm, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		viewForm.setContent(composite);

		ruleDefinitionVOComposite = new RuleDefinitionVOComposite(composite, SWT.NONE);
		ruleDefinitionVOComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Group ruleResultDetail = new Group(composite, SWT.NONE);
		ruleResultDetail.setText("\u89C4\u5219\u8BE6\u60C5");
		ruleResultDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		ruleResultDetail.setLayout(new GridLayout(1, true));

		ruleResultComposite = new Composite(ruleResultDetail, SWT.NONE);
		stackLayout = new StackLayout();
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gridData.heightHint = 200;
		ruleResultComposite.setLayoutData(gridData);
		ruleResultComposite.setLayout(stackLayout);

		this.ruleExecuteResult = ruleExecuteResult;
		this.ruleExecuteResults = ruleExecuteResults;
		loadRuleExecuteResult(ruleExecuteResult);
	}

	private void initToolbarManager(ViewForm viewForm) {
		ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT | SWT.RIGHT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);
		actionContributionItems.add(new ActionContributionItem(new PrevResultAction()));
		actionContributionItems.add(new ActionContributionItem(new NextResultAction()));
		for (ActionContributionItem actionContributionItem : actionContributionItems) {
			toolBarManager.add(actionContributionItem);
		}
		toolBarManager.update(true);
		viewForm.setTopLeft(toolBar);
	}

	private void loadRuleExecuteResult(IRuleExecuteResult ruleExecuteResult) {
		this.ruleExecuteResult = ruleExecuteResult;
		String ruleDefinitionIdentifier = this.ruleExecuteResult.getRuleDefinitionIdentifier();
		ruleDefinitionVOComposite.loadRuleDefinitionVo(ruleDefinitionIdentifier);
		loadRuleExecuteDetailInfo(this.ruleExecuteResult);
		for (ActionContributionItem actionContributionItem : actionContributionItems) {
			actionContributionItem.update();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadRuleExecuteDetailInfo(IRuleExecuteResult ruleExecuteResult) {
		@SuppressWarnings("rawtypes")
		IResultDetailComposite detailComposite = null;
		if (ruleExecuteResult instanceof ScriptRuleExecuteResult) {
			detailComposite = new ScriptResultDetailComposite(ruleResultComposite, SWT.NONE);
		} else if (ruleExecuteResult instanceof ResourceRuleExecuteResult) {
			detailComposite = new ResourceResultDetailComposite(ruleResultComposite, SWT.NONE);
		} else if(ruleExecuteResult instanceof AbstractSimpleExecuteResult){
			detailComposite = new SimpleResultDetailComposite(ruleResultComposite, SWT.NONE);
		}
		if (detailComposite != null) {
			detailComposite.loadRuleExecuteResult(ruleExecuteResult);
			stackLayout.topControl = (Composite) detailComposite;
			ruleResultComposite.layout();
		} else {
			stackLayout.topControl = null;
			ruleResultComposite.layout();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private class PrevResultAction extends Action {

		public PrevResultAction() {
			setText("上一条");
			setToolTipText("上一条");
		}

		@Override
		public void run() {
			int currentIndex = ruleExecuteResults.indexOf(ruleExecuteResult);
			if (currentIndex > 0) {
				ruleExecuteResult = ruleExecuteResults.get(currentIndex - 1);
				loadRuleExecuteResult(ruleExecuteResult);
			}
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/back.jpg");
		}

		@Override
		public boolean isEnabled() {
			if (ruleExecuteResults == null) {
				return false;
			}
			int currentIndex = ruleExecuteResults.indexOf(ruleExecuteResult);
			return currentIndex > 0;
		}
	}

	private class NextResultAction extends Action {

		public NextResultAction() {
			setText("下一条");
			setToolTipText("下一条");
		}

		@Override
		public void run() {
			int currentIndex = ruleExecuteResults.indexOf(ruleExecuteResult);
			if (currentIndex < ruleExecuteResults.size() - 1) {
				ruleExecuteResult = ruleExecuteResults.get(currentIndex + 1);
				loadRuleExecuteResult(ruleExecuteResult);
			}
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/forward.jpg");
		}

		@Override
		public boolean isEnabled() {
			if (ruleExecuteResults == null) {
				return false;
			}
			int currentIndex = ruleExecuteResults.indexOf(ruleExecuteResult);
			return currentIndex < ruleExecuteResults.size() - 1;
		}
	}

}
