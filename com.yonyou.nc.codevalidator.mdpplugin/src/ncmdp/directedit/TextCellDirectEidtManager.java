package ncmdp.directedit;

import ncmdp.figures.ui.IDirectEditable;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;

public class TextCellDirectEidtManager extends DirectEditManager{
	private IDirectEditable lbl = null;
	public TextCellDirectEidtManager(GraphicalEditPart editPart, CellEditorLocator locator, IDirectEditable figure) {
		super(editPart, TextCellEditor.class, locator);
		this.lbl = figure;
	}

	protected void initCellEditor() {
		String str = lbl.getText();
		if(str != null)
			str = str.trim();
		getCellEditor().setValue(str);
		Text text = (Text)getCellEditor().getControl();
		getCellEditor().getControl().setData("figure", lbl);
		text.selectAll();
		
	}
	
}
