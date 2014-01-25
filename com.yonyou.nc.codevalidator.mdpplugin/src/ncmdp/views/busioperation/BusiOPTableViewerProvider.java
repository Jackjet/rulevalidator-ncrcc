package ncmdp.views.busioperation;

import ncmdp.factory.ImageFactory;
import ncmdp.model.activity.RefBusiOperation;
import ncmdp.model.activity.RefOperation;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class BusiOPTableViewerProvider implements ITableLabelProvider {

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// ������ʾ����
		RefOperation opration = (RefOperation) element;
		if (columnIndex == 0) { return opration.getName(); }
		if (columnIndex == 1) { return opration.getDisplayName(); }
		if (columnIndex == 2) { return opration.getOpItfName(); }
		if (columnIndex == 3) { return opration.getId(); }
		return null;
	}

	@Override
	public void dispose() {
		// TableViewer���ر�ʱ���������������ͷ�ͼƬ Image.dispose();

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) { return ImageFactory.getRefOperationImg(); }
		if (columnIndex == 4) {
			boolean isAuthorization = ((RefOperation) element).isAuthorization();
			return ImageFactory.getCheckedImage(isAuthorization);
		}
		return null;
	}

	//���·���������
	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
}
