package ncmdp.ui;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

public class EditableComboBoxCellEditor extends ComboBoxCellEditor {
	public EditableComboBoxCellEditor(Composite parent, String[] items) {
		super(parent, items);
	}
    protected void doSetValue(Object value) {
    	CCombo comboBox = (CCombo)getControl();
        if(value instanceof Integer){
        	super.doSetValue(value);
        }else if(comboBox != null && value instanceof String){
            comboBox.setText((String)value);
        }
    }
    protected Object doGetValue() {
    	CCombo comboBox = (CCombo)getControl();
        return comboBox.getText();
    }

}