package ncmdp.directedit;

import ncmdp.figures.ui.IDirectEditable;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MuliLineTextCellDirectEditManager extends DirectEditManager {
	private IDirectEditable direct = null;
	public MuliLineTextCellDirectEditManager(GraphicalEditPart editPart, CellEditorLocator locator, IDirectEditable direct) {
		super(editPart, TextCellEditor.class, locator);
		this.direct=direct;
	}

	@Override
	protected void initCellEditor() {
		String str = direct.getText();
		if(str != null)
			str = str.trim();
		getCellEditor().setValue(str);
		Text text = (Text)getCellEditor().getControl();
		getCellEditor().getControl().setData("figure", direct);
		text.selectAll();

	}
	protected CellEditor createCellEditorOn(Composite composite) {
		try {
			return new TextCellEditor(composite, SWT.WRAP|SWT.V_SCROLL);
//			Constructor constructor = editorType.getConstructor(new Class[]{Composite.class});
//			return (CellEditor)constructor.newInstance(new Object[]{composite});
		} catch (Exception e) {
			return null;
		}
	}

}
