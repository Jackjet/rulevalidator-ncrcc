package ncmdp.ui;

import ncmdp.factory.ImageFactory;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class CheckboxLableProvider extends LabelProvider implements
		ILabelProvider {
	@Override
	public Image getImage(Object element) {
		boolean isNull = ((Boolean)element).booleanValue();
//		if(propId.equals(Service.SERVICE_REMOTE))
//			isNull = ((Service)element).isRemote();
//		else if(propId.equals(Service.SERVICE_SINGLETON))
//			isNull = ((Service)element).isSingleton();
		return ImageFactory.getCheckedImage(isNull);
	}

	@Override
	public String getText(Object element) {
		return null;//super.getText(element);
	}

}
