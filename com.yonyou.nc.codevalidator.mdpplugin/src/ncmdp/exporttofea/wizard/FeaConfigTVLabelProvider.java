package ncmdp.exporttofea.wizard;

import ncmdp.model.Attribute;
import ncmdp.model.BusinInterface;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class FeaConfigTVLabelProvider extends LabelProvider 
implements ITableLabelProvider{

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof BusinInterface){
			if(columnIndex == 0){
				return null;
			}else if(columnIndex == 1){
				return ((BusinInterface)element).getDisplayName();
			}
		}
		if(element instanceof Attribute){
			if(columnIndex == 0){
				return null;
			}else if(columnIndex == 1){
				return ((Attribute)element).getDisplayName();
			}
		}
		return null;
	}

}
