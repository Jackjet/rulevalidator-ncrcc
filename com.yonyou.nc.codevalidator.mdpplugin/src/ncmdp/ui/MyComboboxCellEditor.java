package ncmdp.ui;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

/**
 * 用于组合框编辑，
 * @author wangxmn
 *
 */
public class MyComboboxCellEditor extends ComboBoxCellEditor {

	public MyComboboxCellEditor() {
	}

	public MyComboboxCellEditor(Composite parent, String[] items) {
		super(parent, items);
	}

	public MyComboboxCellEditor(Composite parent, String[] items, int style) {
		super(parent, items, style);
	}

	@Override
	protected Object doGetValue() {
		return ((CCombo)getControl()).getText();
	}

	@Override
	protected void doSetValue(Object value) {
		((CCombo)getControl()).setText((String)value);
	}

}
