package ncmdp.directedit;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;

public class RelationTextCellDirectEditManager extends DirectEditManager{
	private Label lbl = null;
	public RelationTextCellDirectEditManager(GraphicalEditPart editPart, CellEditorLocator locator, Label figure) {
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
