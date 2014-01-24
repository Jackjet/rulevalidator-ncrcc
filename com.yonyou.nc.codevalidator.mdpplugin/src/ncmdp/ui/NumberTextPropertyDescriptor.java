package ncmdp.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class NumberTextPropertyDescriptor extends PropertyDescriptor {

	public NumberTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new NumberTextCellEditor(parent);
	}
}
