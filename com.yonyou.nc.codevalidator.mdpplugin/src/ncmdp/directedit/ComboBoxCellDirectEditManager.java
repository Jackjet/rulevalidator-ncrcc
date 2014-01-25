package ncmdp.directedit;



import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

public class ComboBoxCellDirectEditManager extends DirectEditManager {
	private Label lbl = null;
	private String[] items = null;
	public ComboBoxCellDirectEditManager(GraphicalEditPart editPart, CellEditorLocator locator,Label lbl,String[] items) {
		super(editPart, ComboBoxCellEditor.class, locator);
		this.lbl = lbl;
		this.items = items;
	}

	@Override
	protected CellEditor createCellEditorOn(Composite comp) {
		ComboBoxCellEditor editor = new ComboBoxCellEditor(comp,items);
		((CCombo)editor.getControl()).addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
				setDirty(true);
			}
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
			
		});
		((CCombo)editor.getControl()).addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				setDirty(true);
			}

			public void keyReleased(KeyEvent e) {
				setDirty(true);
			}
			
		});

		return editor;
	
	}

	@Override
	protected void initCellEditor() {
		String str = lbl.getText();
		if(str != null)
			str = str.trim();
		((CCombo)getCellEditor().getControl()).setText(str);
		getCellEditor().getControl().setData("figure", lbl);
	}


}
