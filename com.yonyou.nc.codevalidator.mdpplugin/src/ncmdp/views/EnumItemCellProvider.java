package ncmdp.views;

import java.util.List;

import ncmdp.factory.ImageFactory;
import ncmdp.model.EnumItem;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 枚举的内容的view
 * @author wangxmn
 *
 */
public class EnumItemCellProvider implements IStructuredContentProvider, ITableLabelProvider {

	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) { 
			return ((List) inputElement).toArray(); 
		}
		return new Object[0];
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	public Image getColumnImage(Object element, int columnIndex) {
		EnumItem item = (EnumItem) element;
		switch (columnIndex) {
			case 4:
				boolean active = item.isHidden();
				return ImageFactory.getCheckedImage(active);
			default:
				break;
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		EnumItem item = (EnumItem) element;
		switch (columnIndex) {
			case 0:
				return item.getEnumDisplay();
			case 1:
				return item.getEnumValue();
			case 2:
				return item.getDesc();
			case 3:
				return item.getResId();
			default:
				break;
		}
		return "";
	}

	public void addListener(ILabelProviderListener listener) {}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {}

}
