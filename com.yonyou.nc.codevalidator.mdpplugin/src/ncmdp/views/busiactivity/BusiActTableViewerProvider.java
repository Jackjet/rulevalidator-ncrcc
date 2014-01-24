package ncmdp.views.busiactivity;

import ncmdp.factory.ImageFactory;
import ncmdp.model.activity.RefBusiOperation;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class BusiActTableViewerProvider implements ITableLabelProvider {

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// 由于显示文字
		RefBusiOperation refBusiOp = (RefBusiOperation) element;
		if (columnIndex == 0) { return refBusiOp.getName(); }
		if (columnIndex == 1) { return refBusiOp.getDisplayName(); }
		if (columnIndex == 2) { return refBusiOp.getName(); }
		if (columnIndex == 3) { return refBusiOp.getId(); }
		return null;
	}

	@Override
	public void dispose() {
		// TableViewer被关闭时触发，可以用来释放图片 Image.dispose();

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) { return ImageFactory.getRefBusiOperationImg(); }
		if (columnIndex == 4) {
			boolean isAuthorization = ((RefBusiOperation) element).isAuthorization();
			return ImageFactory.getCheckedImage(isAuthorization);
		}
		return null;
	}

	//以下方法很少用
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
