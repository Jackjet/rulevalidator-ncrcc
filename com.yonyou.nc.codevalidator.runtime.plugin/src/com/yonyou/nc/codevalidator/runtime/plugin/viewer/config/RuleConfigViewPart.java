package com.yonyou.nc.codevalidator.runtime.plugin.viewer.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;

import com.yonyou.nc.codevalidator.config.IRuleConfig;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.RulePersistenceConstants;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.PluginProjectUtils;
import com.yonyou.nc.codevalidator.runtime.plugin.actions.CodeValidatorAction;
import com.yonyou.nc.codevalidator.runtime.plugin.editor.RuleConfigEditorPart;
import com.yonyou.nc.codevalidator.sdk.project.ProjectAnalyseUtils;

public class RuleConfigViewPart extends ViewPart {

	private RuleConfigComposite ruleConfigComposite;

	public RuleConfigViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		ruleConfigComposite = new RuleConfigComposite(parent, SWT.NONE);
		addDoubleClickListener();
		createToolBarActions();
	}

	private void addDoubleClickListener() {
		ruleConfigComposite.getTreeViewer().addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof TreeSelection) {
					TreeSelection treeSelection = (TreeSelection) event.getSelection();
					Object element = treeSelection.getFirstElement();
					if (element instanceof BusinessComponent && !(element instanceof GlobalExecuteUnit)) {
						BusinessComponent businessComponent = (BusinessComponent) element;
						String projectName = businessComponent.getProjectName();
						IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
						
						IRuleConfig ruleConfig = Activator.getRuleConfig();
						String filePath = businessComponent.getBusinessComp() + ruleConfig.getRuleConfigRelativePath();
						IFile file = project.getFile(filePath);
						if (file != null && file.exists()) {
							openFileEditor(file, businessComponent);
						} else {
							boolean okToCreate = MessageDialog.openQuestion(getSite().getShell(), "该业务组件配置文件不存在",
									"是否需要添加配置文件?");
							if (okToCreate) {
								try {
									createRuleConfigFile(businessComponent, ruleConfig);
									file = project.getFile(filePath);
									openFileEditor(file, businessComponent);
								} catch (RuleBaseException e) {
									MessageDialog.openError(getSite().getShell(), "错误", "初始化规则配置时出现错误! " + e.getMessage());
								}
							}
						}
					}

				}
			}

			private void createRuleConfigFile(BusinessComponent businessComponent, IRuleConfig rulePersistence) throws RuleBaseException {
				String businessComponentPath = businessComponent.getBusinessComponentPath();
				File file = new File(businessComponentPath);
				File ruleCaseFolder = new File(file, RulePersistenceConstants.RULECASE_FOLDER_NAME);
				if (!ruleCaseFolder.exists()) {
					ruleCaseFolder.mkdir();
				}
				rulePersistence.configFolderInitialize(ruleCaseFolder);
				File ruleConfigFile = new File(ruleCaseFolder, rulePersistence.getRuleConfigFileName());
				try {
					boolean created = ruleConfigFile.createNewFile();
					if(created) {
						rulePersistence.configFileInitialize(ruleConfigFile);
					}
				} catch (IOException e) {
					throw new RuleBaseException(e);
				}
			}
		});
	}

	private void createToolBarActions() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		CodeValidatorAction ruleValidatorAction = new CodeValidatorAction();
		ruleConfigComposite.getTreeViewer().addSelectionChangedListener(ruleValidatorAction);
		toolbarManager.add(new ActionContributionItem(ruleValidatorAction));
		toolbarManager.add(new ActionContributionItem(new RefreshConfigResourceAction()));
	}

	@Override
	public void setFocus() {

	}

	public void openFileEditor(IFile file, BusinessComponent businessComponent) {
		try {
			openEditor(file, businessComponent, RuleConfigEditorPart.EDITOR_ID);
		} catch (PartInitException e) {
			MessageDialog.openError(getSite().getShell(), "打开编辑器错误", "具体信息：" + e.getMessage());
		}
	}

	public IEditorPart openEditor(IFile file, BusinessComponent businessComponent, String editorid)
			throws PartInitException {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		RuleConfigEditorInput ruleConfigEditorInput = new RuleConfigEditorInput(new FileEditorInput(file),
				businessComponent);
		IEditorPart openedEditorPart = activePage.findEditor(ruleConfigEditorInput);
		if (openedEditorPart != null) { // 已经打开,需要激活？
			activePage.activate(openedEditorPart);
		} else {
			openedEditorPart = IDE.openEditor(activePage, ruleConfigEditorInput, editorid);
		}
		return openedEditorPart;
	}

	private class RefreshConfigResourceAction extends Action {

		public RefreshConfigResourceAction() {
			setText("刷新");
			setToolTipText("刷新");
		}

		@Override
		public void run() {
			List<IProject> mdeProjectsInCurrentWorkspace = PluginProjectUtils.getMdeProjectsInCurrentWorkspace();
			List<BusinessComponent> inputElements = new ArrayList<BusinessComponent>();
			for (IProject iProject : mdeProjectsInCurrentWorkspace) {
				inputElements.add(ProjectAnalyseUtils.getInnerBusinessComponent(iProject.getLocation().toString(),
						iProject.getName(), GlobalExecuteUnit.DEFAULT_GLOBAL_NAME));
			}
			ruleConfigComposite.getTreeViewer().setInput(inputElements);
			if (mdeProjectsInCurrentWorkspace != null && mdeProjectsInCurrentWorkspace.size() > 0) {
				for (IProject iProject : mdeProjectsInCurrentWorkspace) {
					try {
						iProject.refreshLocal(IResource.DEPTH_ZERO, null);
					} catch (CoreException e) {
						MessageDialog.openError(RuleConfigViewPart.this.getViewSite().getShell(), "错误!", "刷新时产生错误! "
								+ e.getMessage());
					}
				}
			}
			ruleConfigComposite.getTreeViewer().refresh();
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Activator.imageDescriptorFromPlugin("/images/refresh.gif");
		}
	}
}
