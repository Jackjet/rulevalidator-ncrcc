package ncmdp.views;

import java.util.ArrayList;

import ncmdp.model.BusiItfAttr;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
/**
 * 提供业务接口属性的
 * @author wangxmn
 *
 */
public class BusinessInterfaceAttrsViewProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider {

	public BusinessInterfaceAttrsViewProvider() {
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof BusiItfAttr){
			BusiItfAttr attr = (BusiItfAttr)element;
			switch(columnIndex){
				case 0 : break;
				case 1 : return attr.getName();
				case 2 : return attr.getDisplayName();
				case 3 : return attr.getType()==null?"":attr.getType().getDisplayName(); //$NON-NLS-1$
				case 4 : return attr.getTypeStyle();
				case 5 : return attr.getDesc();
			}
		}else if(element instanceof ArrayList){
			if(columnIndex == 0){
				return Messages.BusinessInterfaceAttrsViewProvider_1;
			}
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof ArrayList){
			return ((ArrayList<?>)parentElement).toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList)
			return true;
		else{
			return false;
		}
	}

	public Object[] getElements(Object inputElement) {
		return ((ArrayList<?>)inputElement).toArray();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
