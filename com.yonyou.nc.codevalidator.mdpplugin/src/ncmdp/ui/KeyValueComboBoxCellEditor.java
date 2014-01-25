package ncmdp.ui;

import java.text.MessageFormat;

import ncmdp.model.Attribute;
import ncmdp.model.Type;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.KeyValueComboBox.KeyValue;
import ncmdp.util.MDPLogger;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ȱʡֵcombox
 * 
 * @author wangxmn
 * 
 */
public class KeyValueComboBoxCellEditor extends CellEditor {

	private String[] items;

	int selection;

	KeyValueComboBox comboBox;

	private static final int defaultStyle = SWT.NONE;

	/**
	 * Creates a new cell editor with no control and no st of choices.
	 * Initially, the cell editor has no cell validator.
	 * 
	 * @since 2.1
	 * @see CellEditor#setStyle
	 * @see CellEditor#create
	 * @see ComboBoxCellEditor#setItems
	 * @see CellEditor#dispose
	 */
	public KeyValueComboBoxCellEditor() {
		setStyle(defaultStyle);
	}

	private TreeViewer tv = null;

	public KeyValueComboBoxCellEditor(TreeViewer tv) {
		this(tv.getTree());
		this.tv = tv;
	}

	public KeyValueComboBoxCellEditor(Composite parent) {
		this(parent, new String[] {}, defaultStyle);
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of
	 * choices and parented under the given control. The cell editor value is
	 * the zero-based index of the selected item. Initially, the cell editor has
	 * no cell validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 */
	public KeyValueComboBoxCellEditor(Composite parent, String[] items) {
		this(parent, items, defaultStyle);
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of
	 * choices and parented under the given control. The cell editor value is
	 * the zero-based index of the selected item. Initially, the cell editor has
	 * no cell validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 * @param style
	 *            the style bits
	 * @since 2.1
	 */
	public KeyValueComboBoxCellEditor(Composite parent, String[] items,
			int style) {
		super(parent, style);
		setItems(items);
	}

	/**
	 * Returns the list of choices for the combo box
	 * 
	 * @return the list of choices for the combo box
	 */
	public String[] getItems() {
		return this.items;
	}

	/**
	 * Sets the list of choices for the combo box
	 * 
	 * @param items
	 *            the list of choices for the combo box
	 */
	public void setItems(String[] items) {
		this.items = items;
		populateComboBoxItems();
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected Control createControl(Composite parent) {

		comboBox = new KeyValueComboBox(parent, getStyle());
		comboBox.setFont(parent.getFont());

		comboBox.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});

		comboBox.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

			public void widgetSelected(SelectionEvent event) {
				selection = comboBox.getSelectionIndex();
			}
		});

		comboBox.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		comboBox.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				KeyValueComboBoxCellEditor.this.focusLost();
			}
		});
		return comboBox;
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method returns the zero-based index of
	 * the current selection.
	 * 
	 * @return the zero-based index of the current selection wrapped as an
	 *         <code>Integer</code>
	 */
	protected Object doGetValue() {
		return comboBox.getText();
	}

	private void updateItems() {
		if (tv == null)
			return;
		Tree tree = tv.getTree();

		TreeItem[] items = tree.getSelection();
		if (items != null && items.length > 0) {
			TreeItem item = items[0];
			try {
				Object obj = item.getData();
				if (obj instanceof Attribute) {
					Attribute attr = (Attribute) obj;
					Type type = attr.getType();
					if (type != null)
						NCMDPTool.setDefaultValueScope(type);
				}

			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}

		}

	}

	protected void doSetFocus() {
		comboBox.setFocus();
		updateItems();
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method sets the minimum width of the
	 * cell. The minimum width is 10 characters if <code>comboBox</code> is not
	 * <code>null</code> or <code>disposed</code> eles it is 60 pixels to make
	 * sure the arrow button and some text is visible. The list of CCombo will
	 * be wide enough to show its longest item.
	 */
	public LayoutData getLayoutData() {
		LayoutData layoutData = super.getLayoutData();
		if ((comboBox == null) || comboBox.isDisposed()) {
			layoutData.minimumWidth = 60;
		} else {
			GC gc = new GC(comboBox);
			layoutData.minimumWidth = (gc.getFontMetrics()
					.getAverageCharWidth() * 10) + 10;
			gc.dispose();
		}
		return layoutData;
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method accepts a zero-based index of a
	 * selection.
	 * 
	 * @param value
	 *            the zero-based index of the selection wrapped as an
	 *            <code>Integer</code>
	 */
	protected void doSetValue(Object value) {
		if (value instanceof Integer) {
			selection = ((Integer) value).intValue();
			comboBox.select(selection);
		} else if (comboBox != null && value instanceof String) {
			comboBox.setText((String) value);
		}
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems() {
		if (comboBox != null && items != null) {
			comboBox.removeAll();
			for (int i = 0; i < items.length; i++) {
				comboBox.add(items[i], i);
			}

			setValueValid(true);
			selection = 0;
		}
	}

	/**
	 * Applies the currently selected value and deactiavates the cell editor
	 */
	void applyEditorValueAndDeactivate() {
		selection = comboBox.getSelectionIndex();
		Object newValue = doGetValue();
		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);

		if (!isValid) {
			if (items.length > 0 && selection >= 0 && selection < items.length) {
				setErrorMessage(MessageFormat.format(getErrorMessage(),
						new Object[] { items[selection] }));
			} else {
				setErrorMessage(MessageFormat.format(getErrorMessage(),
						new Object[] { comboBox.getText() }));
			}
		}

		fireApplyEditorValue();
		deactivate();
	}

	protected void focusLost() {
		if (isActivated()) {
			applyEditorValueAndDeactivate();
		}
	}

	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\t') { // tab key
			applyEditorValueAndDeactivate();
		}
	}

	public void setItems(KeyValue[] values) {
		comboBox.setItems(values);
	}
}