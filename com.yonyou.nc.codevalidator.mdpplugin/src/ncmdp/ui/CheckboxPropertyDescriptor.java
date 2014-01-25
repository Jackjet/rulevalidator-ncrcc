package ncmdp.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class CheckboxPropertyDescriptor extends PropertyDescriptor {

	public CheckboxPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new CheckboxCellEditor(parent);
	}

}
