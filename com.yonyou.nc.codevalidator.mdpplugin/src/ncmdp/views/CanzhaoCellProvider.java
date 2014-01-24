package ncmdp.views;

import java.util.List;

import ncmdp.factory.ImageFactory;
import ncmdp.model.CanZhao;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 参照视图的内容和标签的提供者
 * @author wangxmn
 *
 */
public class CanzhaoCellProvider extends LabelProvider implements
ITableLabelProvider, ITreeContentProvider {
	public CanzhaoCellProvider() {
	}

	public Image getColumnImage(Object element, int columnIndex) {
		if(element instanceof CanZhao){
			CanZhao cz = (CanZhao)element;
			switch(columnIndex){
			//为毛只有一个呢，这个还有必要用switch?
			case 2: return ImageFactory.getCheckedImage(cz.isDefault());
			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof List){
			if(columnIndex == 0)
				return Messages.CanzhaoCellProvider_0;
		}else if(element instanceof CanZhao){
			CanZhao cz = (CanZhao)element;
			switch(columnIndex){
			case 0: return ""; 
			case 1: return cz.getName();
		
			}
		}
		return ""; 
	}

	@SuppressWarnings("rawtypes")
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof List){
			return ((List)parentElement).toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof List)
			return true;
		else{
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		return ((List)inputElement).toArray();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
