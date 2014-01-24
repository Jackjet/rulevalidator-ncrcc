package ncmdp.views;

import java.util.ArrayList;

import ncmdp.factory.ImageFactory;
import ncmdp.model.Type;
import ncmdp.model.activity.Operation;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 实体中的业务接属性provider
 * @author wangxmn
 *
 */
public class OperationCellProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0: {
			if (element instanceof ArrayList) {
				return ImageFactory.getOperationImg();
			} else if (element instanceof Operation) {
				Operation oper = (Operation) element;
				String msg = oper.validate();
				if (msg != null)
					return ImageFactory.getErrorImg();
				else {
					return ImageFactory.getOperationImg();
				}
			}
		}
		case 5: {
			if (element instanceof Operation) {
				boolean isAggVOReturn = ((Operation) element).isAggVOReturn();
				return ImageFactory.getCheckedImage(isAggVOReturn);
			}
		}
		case 9: {
			if (element instanceof Operation) {
				boolean isBusiActivity = ((Operation) element).isBusiActivity();
				return ImageFactory.getCheckedImage(isBusiActivity);
			}
		}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (element instanceof ArrayList) {
				return Messages.OperationCellProvider_0;
			}
			break;
		case 1:
			if (element instanceof Operation) {
				return ((Operation) element).getName();
			}
			break;
		case 2:
			if (element instanceof Operation) {
				return ((Operation) element).getDisplayName();
			}
			break;
		case 3:
			if (element instanceof Operation) {
				return ((Operation) element).getTypeStyle();
			}
			break;

		case 4:
			if (element instanceof Operation) {
				Type type = ((Operation) element).getReturnType();
				return type == null ? "" : type.getDisplayName(); //$NON-NLS-1$
			}
			break;
		case 6:
			if (element instanceof Operation) {
				return ((Operation) element).getDefClassName();
			}
			break;
		case 7:
			if (element instanceof Operation) {
				return ((Operation) element).getVisibility();
			}
			break;
		case 8:
			if (element instanceof Operation) {
				return ((Operation) element).getTransKind();
			}
			break;
		case 10:
			if (element instanceof Operation) {
				return ((Operation) element).getDescription();
			}
			break;
		case 11:
			if (element instanceof Operation) {
				return ((Operation) element).getHelp();
			}
			break;
		case 12:
			if (element instanceof Operation) {
				return ((Operation) element).getMethodException();
			}
			break;
		case 13:
			if (element instanceof Operation) {
				return ((Operation) element).getResid();
			}
			break;
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ArrayList) {
			return ((ArrayList<?>) parentElement).toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof ArrayList)
			return true;
		else {
			return false;
		}
	}

	public Object[] getElements(Object inputElement) {
		return ((ArrayList<?>) inputElement).toArray();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
