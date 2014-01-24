package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.yonyou.nc.codevalidator.runtime.plugin.Activator;

public class RuleConfigLabelTreeContentProvider extends LabelProvider implements ITreeContentProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IConfigTreeNode) {
			IConfigTreeNode treeNode = (IConfigTreeNode) element;
			return treeNode.getDisplayText();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IConfigTreeNode) {
			IConfigTreeNode treeNode = (IConfigTreeNode) element;
			String imageIcon = treeNode.getImageIcon();
			if (imageIcon != null && imageIcon.length() > 0) {
				return Activator.imageFromPlugin(imageIcon);
			}
		}
		return super.getImage(element);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List<?>) inputElement).toArray();
		}
		if (inputElement instanceof IConfigTreeNode) {
			IConfigTreeNode configTreeNode = (IConfigTreeNode) inputElement;
			return configTreeNode.actualGetChildrenNode().toArray();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IConfigTreeNode) {
			IConfigTreeNode result = (IConfigTreeNode) parentElement;
			return result.actualGetChildrenNode().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IConfigTreeNode) {
			IConfigTreeNode result = (IConfigTreeNode) element;
			return result.hasChildren();
		}
		return false;
	}

}
