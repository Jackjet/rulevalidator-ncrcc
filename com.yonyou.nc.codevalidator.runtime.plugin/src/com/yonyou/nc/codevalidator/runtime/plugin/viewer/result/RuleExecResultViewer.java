package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class RuleExecResultViewer extends ViewPart {
	
	private RuleResultDisplayComposite composite;
	
	@Override
	public void createPartControl(Composite parent) {
		composite = new RuleResultDisplayComposite(parent, SWT.NONE);
		createToolbarButtons();
	}

	@Override
	public void setFocus() {
		composite.refreshTreeViewerData();
	}
	
	private void createToolbarButtons() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		initialToolBarActions(toolbarManager);
	}

	private void initialToolBarActions(IToolBarManager toolbarManager) {
		toolbarManager.add(new ActionContributionItem(new RefreshResultAction(composite)));
//		toolbarManager.add(new ActionContributionItem(new RemoveSelectResultAction(composite)));
		toolbarManager.add(new ActionContributionItem(new RemoveAllResultsAction(composite)));
	}
	
}
