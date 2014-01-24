package ncmdp.policy;
import ncmdp.command.DirectEditCommand;
import ncmdp.figures.ui.IDirectEditable;
import ncmdp.model.Cell;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;

public class CellDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		Cell cell = (Cell)getHost().getModel();
		CellEditor editor = request.getCellEditor();
		IDirectEditable f =(IDirectEditable) editor.getControl().getData("figure");
		Object id = ((IDirectEditable)f).getEditableObj();
		Object newValue = editor.getValue();
		DirectEditCommand command = new DirectEditCommand(cell,newValue ,id);
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {

	}

}
