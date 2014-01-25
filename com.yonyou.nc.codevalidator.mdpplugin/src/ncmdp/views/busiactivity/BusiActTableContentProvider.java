package ncmdp.views.busiactivity;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class BusiActTableContentProvider  implements IStructuredContentProvider {

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
		// �ر�tableViewerʱ����
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// �ٴε���setInputʱ����
	}
}
