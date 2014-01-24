package ncmdp.views;

import java.util.ArrayList;

import ncmdp.model.Attribute;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;

import ncmdp.model.ValueObject;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
/**
 * 业务接口属性映射视图
 * @author wangxmn
 *
 */
public class BusinessItfAttrsMapViewProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider {
	private BusinessItfAttrsMapView view = null;
	public BusinessItfAttrsMapViewProvider(BusinessItfAttrsMapView view) {
		super();
		this.view = view;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int column) {

  		if(element instanceof BusinInterface){
			if(column == 0){
				return ((BusinInterface)element).getDisplayName();
			}
		}else if(element instanceof BusiItfAttr){
			BusiItfAttr attr = (BusiItfAttr)element;
			switch (column) {
			case 1:
				return attr.getDisplayName();
			case 2:
				  ValueObject vo = view.getVauleObejct();
				  if(vo != null){
					Attribute[] a = vo.getBusiattrAtrrExtendMap(attr);
					if(a != null){
					   return a[0] == null ? "" : a[0].getDisplayName();
					}
					else
					{
					   return "";
					}
				  }else{
					return "";
				  }
			case 3:
				ValueObject vos = view.getVauleObejct();
				if(vos != null){
					Attribute[] a = vos.getBusiattrAtrrExtendMap(attr);
					if(a != null){
					   return a[1] == null ? "" : a[1].getDisplayName();	
					}
					else
					{
						return "";
					}
				}else{
					return "";
				}
			default:
				break;
			}
		}
		return "";
	}
	@SuppressWarnings("rawtypes")
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof ArrayList){
			return ((ArrayList)parentElement).toArray();
		}else 
		if(parentElement instanceof BusinInterface){
			BusinInterface itf= (BusinInterface)parentElement;
			return itf.getBusiItAttrs().toArray(new BusiItfAttr[0]);
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList){
			return true;
		}else 
		if(element instanceof BusinInterface){
			return true;
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
