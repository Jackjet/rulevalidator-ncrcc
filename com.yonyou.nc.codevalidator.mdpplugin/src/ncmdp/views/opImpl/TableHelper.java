package ncmdp.views.opImpl;

import java.util.List;

import ncmdp.factory.ImageFactory;
import ncmdp.model.activity.OPItfImpl;
import ncmdp.tool.NCMDPTool;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class TableHelper implements IStructuredContentProvider, ITableLabelProvider {
	/**IContentProvider*/

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List) inputElement).toArray();
		} else {
			return new Object[0];
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	/**ITableLabelProvider*/
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element != null && element instanceof OPItfImpl) {
			OPItfImpl opImpl = (OPItfImpl) element;
			if (columnIndex == 0) { return opImpl.getName(); }
			if (columnIndex == 1) { return opImpl.getDisplayName(); }
			if (columnIndex == 2) { return opImpl.getFullClassName(); }
			if (columnIndex == 3) { return opImpl.getType(); }
			if (columnIndex == 4) { return opImpl.getCreator(); }
			if (columnIndex == 5) { return NCMDPTool.formatDateString(opImpl.getCreateTime()); }
			if (columnIndex == 6) { return opImpl.getDescription(); }
			if (columnIndex == 7) { return opImpl.getTag(); }

		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) { return ImageFactory.getOPItfImplImg(); }
		return null;
	}

}
