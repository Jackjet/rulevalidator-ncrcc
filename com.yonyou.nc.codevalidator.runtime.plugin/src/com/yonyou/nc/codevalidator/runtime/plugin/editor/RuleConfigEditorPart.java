package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.yonyou.nc.codevalidator.config.IRuleConfig;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.vo.RuleCheckConfigurationImpl;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.viewer.config.RuleConfigEditorInput;

/**
 * 规则配置编辑器
 * 
 * @author mazhqa
 * 
 */
public class RuleConfigEditorPart extends EditorPart implements IRuleEditorListener {

	public static final String EDITOR_ID = "com.yonyou.nc.codevalidator.runtime.plugin.ruleconfigeditor";

	private boolean dirty;

	private RuleConfigEditorComposite ruleConfigEditorComposite;
	private RuleCheckConfigurationImpl ruleCheckConfiguration;

	public RuleConfigEditorPart() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IFileEditorInput fileEditorInput = null;
		if (getEditorInput() instanceof IFileEditorInput) {
			fileEditorInput = (IFileEditorInput) getEditorInput();
		}
		if (getEditorInput() instanceof RuleConfigEditorInput) {
			RuleConfigEditorInput ruleConfigEditorInput = (RuleConfigEditorInput) getEditorInput();
			fileEditorInput = ruleConfigEditorInput.getFileEditorInput();
		}
		if (fileEditorInput == null) {
			MessageDialog.openError(getSite().getShell(), "错误", "当前规则配置文件源未找到，请重新打开再保存!");
			return;
		}
		IFile file = fileEditorInput.getFile();
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			IRuleConfig ruleConfig = Activator.getRuleConfig();
			if (ruleConfig != null) {
				ruleConfig.exportConfig(os, ruleCheckConfiguration);
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				file.setContents(is, true, true, monitor);
				this.dirty = false;
				firePropertyChange(PROP_DIRTY);
			}
		} catch (CoreException e) {
			MessageDialog.openError(getSite().getShell(), "错误",
					"查看文件是否存在或资源同步问题，或刷新工程目录！" + e.getMessage() + "\n" + e.getStatus());
		} catch (RuleBaseException e) {
			MessageDialog.openError(getSite().getShell(), "错误", "具体信息: " + e.getMessage());
		}
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		InputStream inputStream = null;
		try {
			IRuleConfig ruleConfig = Activator.getRuleConfig();
			IFile file = null;
			if (getEditorInput() instanceof IFileEditorInput) {
				IFileEditorInput fileEditorInput = (IFileEditorInput) getEditorInput();
				file = fileEditorInput.getFile();
			}
			if (getEditorInput() instanceof RuleConfigEditorInput) {
				RuleConfigEditorInput ruleConfigEditorInput = (RuleConfigEditorInput) getEditorInput();
				IFileEditorInput fileEditorInput = ruleConfigEditorInput.getFileEditorInput();
				BusinessComponent businessComponent = ruleConfigEditorInput.getBusinessComponent();
				String ruleConfigFilePath = ruleConfig.getRuleConfigFilePath(businessComponent);
				File busiCompConfigFile = new File(ruleConfigFilePath);
				file = fileEditorInput.getFile();
				if (file.getLocalTimeStamp() != busiCompConfigFile.lastModified()) {
					file.refreshLocal(IResource.DEPTH_ZERO, null);
				}
				setPartName(String.format("规则配置:%s", businessComponent.getDisplayBusiCompName()));
			}
			inputStream = file.getContents();
			ruleConfigEditorComposite = new RuleConfigEditorComposite(parent, SWT.NONE, this);
			ruleConfigEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			ruleConfigEditorComposite.setLayout(new GridLayout(1, false));

			initRuleCheckConfiguration(inputStream);
			ruleConfigEditorComposite.loadRuleConfigData(ruleCheckConfiguration);
			ruleConfigEditorComposite.setRuleEditorListener(this);
		} catch (CoreException e) {
			MessageDialog.openError(getSite().getShell(), "错误", "查看文件是否存在或资源同步问题，或刷新工程目录！" + e.getMessage());
		} catch (RuleBaseException e) {
			MessageDialog.openWarning(getSite().getShell(), "警告", "文件格式可能为空，或格式不符合配置规范，以新文件方式打开！" + e.getMessage());
			ruleCheckConfiguration = new RuleCheckConfigurationImpl();
			ruleConfigEditorComposite.loadRuleConfigData(ruleCheckConfiguration);
			ruleConfigEditorComposite.setRuleEditorListener(this);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private void initRuleCheckConfiguration(InputStream inputStream) throws RuleBaseException {
		IRuleConfig ruleConfig = Activator.getRuleConfig();
		this.ruleCheckConfiguration = (RuleCheckConfigurationImpl)ruleConfig.loadConfiguration(inputStream);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void ruleEditorChanged() {
		this.dirty = true;
		firePropertyChange(PROP_DIRTY);
	}

}
