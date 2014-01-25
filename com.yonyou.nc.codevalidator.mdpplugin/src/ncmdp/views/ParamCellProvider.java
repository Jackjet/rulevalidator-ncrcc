package ncmdp.views;

import java.util.List;

import ncmdp.common.ParamTypeEnum;
import ncmdp.factory.ImageFactory;
import ncmdp.model.activity.Parameter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class ParamCellProvider implements IStructuredContentProvider,
		ITableLabelProvider {

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List) inputElement).toArray();
		}
		return new Object[0];
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	// ///////////////////////////////////////////////////////////////////
	public Image getColumnImage(Object element, int columnIndex) {
		// if (columnIndex == 0) {
		// return ImageFactory.getParameterImgDescriptor();
		// }
		// else
		if (columnIndex == 3) {
			if (element instanceof Parameter) {
				boolean isAggVO = ((Parameter) element).isAggVO();
				return ImageFactory.getCheckedImage(isAggVO);
			}
		}

		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		Parameter param = (Parameter) element;
		switch (columnIndex) {
		case 0:
			return param.getName();
		case 1:
			return param.getDisplayName();
		case 2:
			return param.getParaType() == null ? "" : param.getParaType()
					.getDisplayName();
		case 4:
			return param.getParamDefClassname();
		case 5:
			return param.getTypeStyle();
		case 6:
			return param.getDesc();
		case 7:
			return param.getHelp();
		case 8:
			return ParamTypeEnum.getEnum(
					Integer.parseInt(param.getParamTypeForLog())).toString();
		default:
			break;
		}
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
