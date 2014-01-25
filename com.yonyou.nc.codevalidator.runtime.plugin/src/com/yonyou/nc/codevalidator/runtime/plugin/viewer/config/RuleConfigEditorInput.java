package com.yonyou.nc.codevalidator.runtime.plugin.viewer.config;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class RuleConfigEditorInput implements IEditorInput {

	private final IFileEditorInput fileEditorInput;
	private final BusinessComponent businessComponent;

	public RuleConfigEditorInput(IFileEditorInput fileEditorInput, BusinessComponent businessComponent) {
		this.fileEditorInput = fileEditorInput;
		this.businessComponent = businessComponent;
	}

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

	public IFileEditorInput getFileEditorInput() {
		return fileEditorInput;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return fileEditorInput.getAdapter(adapter);
	}

	@Override
	public boolean exists() {
		return fileEditorInput.exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return fileEditorInput.getImageDescriptor();
	}

	@Override
	public String getName() {
		return fileEditorInput.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return fileEditorInput.getPersistable();
	}

	@Override
	public String getToolTipText() {
		return fileEditorInput.getToolTipText();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileEditorInput == null) ? 0 : fileEditorInput.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleConfigEditorInput other = (RuleConfigEditorInput) obj;
		if (fileEditorInput == null) {
			if (other.fileEditorInput != null)
				return false;
		} else if (!fileEditorInput.equals(other.fileEditorInput))
			return false;
		return true;
	}

}
