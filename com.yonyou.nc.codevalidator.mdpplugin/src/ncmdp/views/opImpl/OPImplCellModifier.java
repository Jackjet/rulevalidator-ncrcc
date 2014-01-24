package ncmdp.views.opImpl;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.activity.OPItfImpl;
import ncmdp.tool.basic.StringUtil;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class OPImplCellModifier implements ICellModifier {
	public static String[] columnProiprtties = new String[] { "name", "displayName", "fullClassName", "type",
			"creator", "createTime", "description", "tag" };

	TableViewer tableViewer = null;

	public OPImplCellModifier(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		OPItfImpl opImpl = (OPItfImpl) element;
		if (property.equals(columnProiprtties[0])) {
			return opImpl.getName();
		} else if (property.equals(columnProiprtties[1])) {
			return opImpl.getDisplayName();
		} else if (property.equals(columnProiprtties[2])) {
			return opImpl.getFullClassName();
		} else if (property.equals(columnProiprtties[3])) {
			return opImpl.getType();
		} else if (property.equals(columnProiprtties[4])) {
			return opImpl.getCreator();
		} else if (property.equals(columnProiprtties[5])) {
			return opImpl.getCreateTime();
		} else if (property.equals(columnProiprtties[6])) {
			return opImpl.getDescription();
		} else if (property.equals(columnProiprtties[7])) { return opImpl.getTag(); }
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (value == null||StringUtil.isEmptyWithTrim(value.toString())) { return; }
		TableItem item = (TableItem) element;
		OPItfImpl opImpl = (OPItfImpl) item.getData();
		String oldValue = "";
		if (property.equals(columnProiprtties[0])) {
			oldValue = opImpl.getName();
			opImpl.setName((String) value);
		} else if (property.equals(columnProiprtties[1])) {
			oldValue = opImpl.getDisplayName();
			opImpl.setDisplayName((String) value);
		} else if (property.equals(columnProiprtties[2])) {
			oldValue = opImpl.getFullClassName();
			opImpl.setFullclassName((String) value);
		} else if (property.equals(columnProiprtties[3])) {
			oldValue = opImpl.getType();
			opImpl.setType((String) value);
		} else if (property.equals(columnProiprtties[4])) {
			oldValue = opImpl.getCreator();
			opImpl.setCreator((String) value);
		} else if (property.equals(columnProiprtties[6])) {
			oldValue = opImpl.getDescription();
			opImpl.setDescription((String) value);
		} else if (property.equals(columnProiprtties[7])) {
			oldValue = opImpl.getTag();
			opImpl.setTag((String) value);
		}
		tableViewer.update(opImpl, null);
		if (!value.equals(oldValue)) {
			NCMDPEditor.getActiveMDPEditor().setDirty(true);
		}
	}
}
