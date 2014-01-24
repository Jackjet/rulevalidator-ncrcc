package ncmdp.ui;

import java.util.List;

import ncmdp.model.StereoType;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class StereoTypeSelectDescriptor extends PropertyDescriptor {
	private List<StereoType> stereoTypeList= null;

	public StereoTypeSelectDescriptor(Object id, String displayName,List<StereoType> stereoTypeList ) {
		super(id, displayName);
		this.stereoTypeList = stereoTypeList;
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new StereoTypeSelectCellEditor(parent,stereoTypeList);
		if(getValidator()!=null){
			editor.setValidator(getValidator());
		}
		return editor;
	}
}
