package com.yonyou.nc.codevalidator.runtime.plugin.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class RulePerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		addViews(layout);
	}

	private void addViews(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		IFolderLayout leftfolder = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, editorArea);
		// ����������ͼ
		leftfolder.addView("com.yonyou.nc.codevalidator.runtime.plugin.ruleconfig");
		leftfolder.addView("org.eclipse.jdt.ui.PackageExplorer");
		leftfolder.addPlaceholder("org.eclipse.ui.navigator.ProjectExplorer");
		leftfolder.addPlaceholder("org.eclipse.ui.views.ResourceNavigator");

		//
		IFolderLayout bottomfolder = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75,
				editorArea);
		// property��ͼ
		// bottomfolder.addView(IPageLayout.ID_PROP_SHEET);
		// console ��ͼ
		bottomfolder.addView("com.yonyou.nc.codevalidator.runtime.plugin.ruleresult");
		// console ��ͼ
		bottomfolder.addView("org.eclipse.ui.console.ConsoleView");
		// problem ��ͼ
		// bottomfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottomfolder.addView("org.eclipse.pde.runtime.LogView");
		bottomfolder.addView("org.eclipse.ui.browser.view");

	}
}
