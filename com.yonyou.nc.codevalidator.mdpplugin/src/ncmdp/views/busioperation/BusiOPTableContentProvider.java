package ncmdp.views.busioperation;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class BusiOPTableContentProvider implements IStructuredContentProvider {

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
		// 关闭tableViewer时触发
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// 再次调用setInput时调用
	}
}
