package ncmdp.ui;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.StereoType;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class StereoTypeSelectCellEditor extends DialogCellEditor {
	Composite parent = null;

	private List<StereoType> stereoTypeList = null;

	public StereoTypeSelectCellEditor(Composite parent, List<StereoType> stereoTypeList) {
		super(parent);
		this.parent = parent;
		this.stereoTypeList = stereoTypeList;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		StereoTypeSelectDialog dialog = new StereoTypeSelectDialog(parent.getShell(), stereoTypeList);
		int result = dialog.open();
		List<StereoType> stereoList = null;
		if (result == IDialogConstants.OK_ID) {
			stereoList = dialog.getSelectedTypeList();
		}
		return stereoList;
	}

	@Override
	protected Button createButton(Composite parent) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText("..."); 
		return result;
	}

	@Override
	protected Object doGetValue() {
		return super.doGetValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetValue(Object value) {
		if(value==null){
			return;
		}
		if(value instanceof List){
			List<StereoType> types = (List<StereoType>) value;
			if(types.size()==0){
				return;
			}else{
				List<StereoType> doGetValue = new ArrayList<StereoType>();
				for (StereoType stereoType : (List<StereoType>) value) {
					doGetValue.add((StereoType) stereoType.clone());
				}
				super.doSetValue(doGetValue);
			}
		}
	}
}
