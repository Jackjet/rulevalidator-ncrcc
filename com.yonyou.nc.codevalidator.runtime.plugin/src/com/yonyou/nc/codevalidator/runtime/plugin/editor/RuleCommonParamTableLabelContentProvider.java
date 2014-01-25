package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.yonyou.nc.codevalidator.rule.vo.CommonParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.ParamConfiguration;

public class RuleCommonParamTableLabelContentProvider implements IStructuredContentProvider, ITableLabelProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

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
		if (element instanceof ParamConfiguration) {
			ParamConfiguration paramConfiguration = (ParamConfiguration) element;
			switch (columnIndex) {
			case 0:
				return paramConfiguration.getParamName();
			case 1:
				return paramConfiguration.getParamValue();
			default:
				break;
			}
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof CommonParamConfiguration) {
			CommonParamConfiguration commonParamConfiguration = (CommonParamConfiguration) inputElement;
			List<ParamConfiguration> paramConfigurationList = commonParamConfiguration.getParamConfigurationList();
			return paramConfigurationList.toArray();
		}
		return null;
	}

}
