package ncmdp.wizard.multiwizard;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class MultiTableHelper implements IStructuredContentProvider, ITableLabelProvider {

	public MultiTableHelper() {
	}

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

	/***********************************************************/
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element != null && element instanceof MultiContentVO) {
			MultiContentVO multiContent = (MultiContentVO) element;
			if (columnIndex == 0) {
				return multiContent.getResid();
			} else {
				return multiContent.getValue();
			}
			//			if (columnIndex == 1) { return multiContent.getSimpSource(); }
			//			if (columnIndex == 2) { return multiContent.getTradSource(); }
			//			if (columnIndex == 3) { return multiContent.getEngSource(); }
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

}
