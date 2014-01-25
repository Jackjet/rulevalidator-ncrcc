package ncmdp.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ObjectComboBoxPropertyDescriptor extends PropertyDescriptor {
	private Object[] items = null;
	public ObjectComboBoxPropertyDescriptor(Object id, String displayName,Object[] items) {
		super(id, displayName);
		this.items = items;
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new ObjectComboBoxCellEditor(parent, items);
	}

}
