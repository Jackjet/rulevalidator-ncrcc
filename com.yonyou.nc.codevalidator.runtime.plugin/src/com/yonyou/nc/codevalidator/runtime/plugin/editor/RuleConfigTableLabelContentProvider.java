package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.yonyou.nc.codevalidator.rule.vo.RuleItemConfigVO;

public class RuleConfigTableLabelContentProvider implements IStructuredContentProvider, ITableLabelProvider {

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List) inputElement).toArray();
		}
		return null;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof RuleItemConfigVO) {
			IRuleConfigVOProperty ruleConfigVOProperty = RuleConfigConstants.RULE_CONFIG_MAIN_COLUMNS.get(columnIndex).getRuleConfigVOProperty();
			return ruleConfigVOProperty.getPropertyValue((RuleItemConfigVO) element);
		}
		return null;
	}

}
