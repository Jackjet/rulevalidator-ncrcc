package com.yonyou.nc.codevalidator.runtime.plugin.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.RuleConstants;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.PluginRuntimeContext;
import com.yonyou.nc.codevalidator.runtime.plugin.mde.MdeConfigInfoUtils;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class CodeValidatorAction extends Action implements IObjectActionDelegate, ISelectionChangedListener {

	private BusinessComponent businessComponent;

	public CodeValidatorAction() {
		setText("规则执行");
		setToolTipText("规则执行");
	}

	@Override
	public void run() {
		if (businessComponent == null) {
			Logger.error("未选定工程");
			MessageDialog.openWarning(Activator.getActiveWorkbenchShell(), "警告", "未选定工程!");
			return;
		}
		// 在执行操作之前询问是否需要保存
		IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.saveAllEditors(true);

		String mdeProjectName = businessComponent.getProjectName();

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String globalConfigFilePath = preferenceStore.getString(RuleConstants.GLOBAL_CONFIG_FILEPATH);
		String globalExportFilePath = preferenceStore.getString(RuleConstants.GLOBAL_EXPORT_FILEPATH);
		String globalLogFilePath = preferenceStore.getString(RuleConstants.GLOBAL_LOG_FILEPATH);
		String globalLogLevel = preferenceStore.getString(RuleConstants.GLOBAL_LOG_LEVEL) == null ? "ERROR"
				: preferenceStore.getString(RuleConstants.GLOBAL_LOG_LEVEL);
		String executeLevel = preferenceStore.getString(RuleConstants.EXECUTE_LEVEL);
		ExecutePeriod executePeriod = ExecutePeriod.getExecutePeriod(executeLevel) == null ? ExecutePeriod.DEPLOY
				: ExecutePeriod.getExecutePeriod(executeLevel);
		SystemRuntimeContext systemRuntimeContext = new SystemRuntimeContext(globalConfigFilePath,
				globalExportFilePath, globalLogFilePath, globalLogLevel, executePeriod);
		boolean runInNc5x = preferenceStore.getBoolean(RuleConstants.RUN_IN_NC_5X);
		systemRuntimeContext.setExecuteLevelIn5x(runInNc5x);

		PluginRuntimeContext runtimeContext = new PluginRuntimeContext(businessComponent, systemRuntimeContext,
				mdeProjectName);
		runtimeContext.setNcHome(MdeConfigInfoUtils.getMdeNcHome());
		runtimeContext.setMdeProjectName(mdeProjectName);
		RuleExecutorRuntimeJob codeValidateJob = new RuleExecutorRuntimeJob(runtimeContext);
		codeValidateJob.setRule(new CodeValidatorSchedulingRule(runtimeContext));
		codeValidateJob.schedule();
	}

	@Override
	public void run(IAction action) {
		run();
	}

//	private void loadBusinessComponents(IProject project) {
//		businessComponent = null;
//		if (project == null) {
//			Logger.error("未选定工程");
//			MessageDialog.openWarning(Activator.getActiveWorkbenchShell(), "警告", "需要在工程下执行验证!");
//			return;
//		}
//		// if (!PluginProjectUtils.isMdeProject(project.getProject())) {
//		// Logger.error(String.format("当前工程：%s 不是MDE工程",
//		// project.getProject().getName()));
//		// return;
//		// }
//		businessComponent = ProjectAnalyseUtils.getBusinessComponent(project.getProject().getLocation().toString(),
//				project.getName());
//	}
//
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
//		if (selection instanceof TreeSelection) {
//			TreeSelection treeSelection = (TreeSelection) selection;
//			Object selectObj = treeSelection.getFirstElement();
//			IProject project;
//			if (selectObj instanceof IJavaProject) {
//				project = ((IJavaProject) selectObj).getProject();
//				loadBusinessComponents(project.getProject());
//			} else if (selectObj instanceof IFolder) {
//				project = ((IFolder) selectObj).getProject();
//				loadBusinessComponents(project);
//			}
//		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection() instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) event.getSelection();
//			if (treeSelection.getFirstElement() instanceof IProject) {
//				IProject project = (IProject) treeSelection.getFirstElement();
//				loadBusinessComponents(project);
			if (treeSelection.getFirstElement() instanceof BusinessComponent) {
				BusinessComponent innerBusinessComponent = (BusinessComponent) treeSelection.getFirstElement();
				GlobalExecuteUnit globalExecuteUnit = new GlobalExecuteUnit(innerBusinessComponent.getProjectName());
				globalExecuteUnit.addSubBusinessComponentList(innerBusinessComponent);
				this.businessComponent = globalExecuteUnit;
				//TODO LOAD ALL PROJECTS WHEN SELECT GLOBAL!
//				businessComponent.getBusinessComponentPath()
//				businessComponentList = new ArrayList<BusinessComponent>();
//				businessComponentList.add(businessComponent);
			}
		}
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("/images/run.gif");
	}

}
