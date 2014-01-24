package ncmdp.views;

import java.util.ArrayList;

import ncmdp.factory.ImageFactory;
import ncmdp.model.Attribute;
import ncmdp.model.Type;
import ncmdp.tool.NCMDPTool;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 提供属性的内容
 * @author wangxmn
 *
 */
public class CellPropertiesViewProvider extends LabelProvider implements ITableLabelProvider,
		ITreeContentProvider {
	private CellPropertiesView view = null;

	public CellPropertiesViewProvider(CellPropertiesView view) {
		super();
		this.view = view;
	}

	public int convertDisplayIndexToColIndex(int disPlayIndex) {
		if (disPlayIndex < 0 || disPlayIndex >= view.getDisplayColNames().length) { return -1; }
		String colName = view.getDisplayColNames()[disPlayIndex];
		int col = -1;
		for (int i = 0; i < CellModifier.colNames.length; i++) {
			if (CellModifier.colNames[i].equals(colName)) {
				col = i;
				break;
			}
		}
		return col;
	}

	/**
	 * 获得某个字段的图形表示，主要是checkbox
	 */
	public Image getColumnImage(Object element, int colIndex) {
		int columnIndex = convertDisplayIndexToColIndex(colIndex);
		if (element instanceof Attribute) {
			Attribute attr = (Attribute) element;
			switch (columnIndex) {
				case 0: {
					String msg = attr.validate();
					if (msg != null)
						return ImageFactory.getErrorImg();
					else
						return NCMDPTool.getAttrImg(attr);
				}
				case 9:
					boolean hide = attr.isHide();
					return ImageFactory.getCheckedImage(hide);
				case 10:
					boolean isNull = attr.isNullable();
					return ImageFactory.getCheckedImage(isNull);
				case 11:
					boolean readonly = attr.isReadOnly();
					return ImageFactory.getCheckedImage(readonly);
				case 19:
					boolean fixlen = attr.isFixLen();
					return ImageFactory.getCheckedImage(fixlen);
				case 20:
					boolean cal = attr.isCalculateAttr();
					return ImageFactory.getCheckedImage(cal);
				case 21:
					boolean active = attr.isActive();
					return ImageFactory.getCheckedImage(active);
				case 22:
					boolean auth = attr.isAuthorization();
					return ImageFactory.getCheckedImage(auth);
				case 25:
					boolean isSequence = attr.getIsSequence();
					return ImageFactory.getCheckedImage(isSequence);
				case 26:
					boolean isDynamicAttr = attr.getIsDynamicAttr();
					return ImageFactory.getCheckedImage(isDynamicAttr);
				case 28:
					boolean isAccessPower = attr.isAccessPower();
					return ImageFactory.getCheckedImage(isAccessPower);
				case 30:
					boolean forLocal = attr.isForLocale();
					return ImageFactory.getCheckedImage(forLocal);
				default:
					break;
			}
		} else if (element instanceof ArrayList) {
			switch (columnIndex) {
				case 0:
					return ImageFactory.getAttrTreeRootImage();
				default:
					break;
			}
		}
		return null;
	}

	/**
	 * 获得每个字段的内容
	 */
	public String getColumnText(Object element, int colIndex) {
		int columnIndex = convertDisplayIndexToColIndex(colIndex);
		switch (columnIndex) {
			case 0:
				if (element instanceof ArrayList) { return Messages.CellPropertiesViewProvider_0; }
				break;
			case 1:
				if (element instanceof Attribute) { return ((Attribute) element).getName(); }
				break;
			case 2:
				if (element instanceof Attribute) { return ((Attribute) element).getDisplayName(); }
				break;
			case 3:
				if (element instanceof Attribute) { return ((Attribute) element).getTypeStyle(); }
				break;
			case 4:
				if (element instanceof Attribute) {
					Type type = ((Attribute) element).getType();
					return type == null ? "" : type.getDisplayName();
				}
				break;
			case 5:
				if (element instanceof Attribute) { return ((Attribute) element).getFieldName(); }
				break;
			case 6:
				if (element instanceof Attribute) { return ((Attribute) element).getFieldType(); }
				break;
			case 7:
				if (element instanceof Attribute) { return ((Attribute) element).getRefModuleName(); }
				break;
			case 8:
				if (element instanceof Attribute) { return ((Attribute) element).getDesc(); }
				break;
			case 12:
				if (element instanceof Attribute) { return ((Attribute) element).getAccessStrategy(); }
				break;
			case 13:
				if (element instanceof Attribute) { return ((Attribute) element).getVisibilityKind() + ""; } //$NON-NLS-1$
				break;
			case 14:
				if (element instanceof Attribute) { return ((Attribute) element).getDefaultValue(); }
				break;
			case 15:
				if (element instanceof Attribute) { return ((Attribute) element).getMaxValue(); }
				break;
			case 16:
				if (element instanceof Attribute) { return ((Attribute) element).getMinValue(); }
				break;
			case 17:
				if (element instanceof Attribute) { return ((Attribute) element).getLength(); }
				break;
			case 18:
				if (element instanceof Attribute) { return ((Attribute) element).getPrecision(); }
				break;
			case 23:
				if (element instanceof Attribute) { return ((Attribute) element).getResid(); }
				break;
			case 24:
				if (element instanceof Attribute) {
					//注：借用help字段来表示“扩展标签”
					return ((Attribute) element).getHelp();
				}
				break;
			case 27:
				if (element instanceof Attribute) { return ((Attribute) element).getDynamicTable(); }
				break;
			case 29:
				if (element instanceof Attribute) { return ((Attribute) element).getAccessPowerGroup(); }
				break;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ArrayList) {
			return ((ArrayList) parentElement).toArray(); 
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

	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		return ((ArrayList) inputElement).toArray();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
