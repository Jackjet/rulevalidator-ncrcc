package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree.IResultTreeNode;

public class RuleSessionTreeLabelContentProvider extends LabelProvider implements ITreeContentProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IResultTreeNode) {
			IResultTreeNode treeNode = (IResultTreeNode) element;
			return treeNode.getDisplayText();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IResultTreeNode) {
			IResultTreeNode treeNode = (IResultTreeNode) element;
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
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IResultTreeNode) {
			IResultTreeNode result = (IResultTreeNode) parentElement;
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
		if (element instanceof IResultTreeNode) {
			IResultTreeNode result = (IResultTreeNode) element;
			return result.hasChildren();
		}
		return false;
	}

}
