package ncmdp.ui;

import ncmdp.model.Type;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class TypeSelectePropertyDescriptor extends PropertyDescriptor {
	private Type[] items = null;
	private boolean showBtn = true;
	public TypeSelectePropertyDescriptor(Object id, String displayName,Type[] items) {
		super(id, displayName);
		this.items = items; 
	}
	public TypeSelectePropertyDescriptor(Object id, String displayName,Type[] items,boolean showBtn) {
		super(id, displayName);
		this.items = items; 
		this.showBtn = showBtn;
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new TypeSelectCellEditor(parent, items,showBtn);
	}

}
