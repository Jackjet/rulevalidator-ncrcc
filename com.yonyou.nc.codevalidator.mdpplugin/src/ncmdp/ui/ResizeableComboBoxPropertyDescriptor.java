package ncmdp.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.ComboBoxLabelProvider;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ResizeableComboBoxPropertyDescriptor extends PropertyDescriptor {

    /**
     * The labels to display in the combo box
     */
    private String[] labels;

    /**
     * Creates an property descriptor with the given id, display name, and list
     * of value labels to display in the combo box cell editor.
     * 
     * @param id the id of the property
     * @param displayName the name to display for the property
     * @param labelsArray the labels to display in the combo box
     */
    public ResizeableComboBoxPropertyDescriptor(Object id, String displayName,
            String[] labelsArray) {
        super(id, displayName);
        labels = labelsArray;
    }

    /**
     * The <code>ComboBoxPropertyDescriptor</code> implementation of this 
     * <code>IPropertyDescriptor</code> method creates and returns a new
     * <code>ComboBoxCellEditor</code>.
     * <p>
     * The editor is configured with the current validator if there is one.
     * </p>
     */
    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new MyComboCellEditor(parent, labels,
                SWT.Resize|SWT.H_SCROLL);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

    /**
     * The <code>ComboBoxPropertyDescriptor</code> implementation of this 
     * <code>IPropertyDescriptor</code> method returns the value set by
     * the <code>setProvider</code> method or, if no value has been set
     * it returns a <code>ComboBoxLabelProvider</code> created from the 
     * valuesArray of this <code>ComboBoxPropertyDescriptor</code>.
     *
     * @see #setLabelProvider
     */
    public ILabelProvider getLabelProvider() {
        if (isLabelProviderSet()) {
			return super.getLabelProvider();
		} else {
			return new ComboBoxLabelProvider(labels);
		}
    }
}
