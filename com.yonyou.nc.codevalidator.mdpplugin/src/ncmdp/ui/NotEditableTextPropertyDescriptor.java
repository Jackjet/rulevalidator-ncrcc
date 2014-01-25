package ncmdp.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class NotEditableTextPropertyDescriptor extends TextPropertyDescriptor {
	public NotEditableTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	public CellEditor createPropertyEditor(Composite parent) {
		return null;
	}
}
