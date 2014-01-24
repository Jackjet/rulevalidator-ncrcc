package ncmdp.policy;

import ncmdp.command.CellDeleteCommand;
import ncmdp.model.Cell;
import ncmdp.model.JGraph;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
/**
 * 创建删除组件的操作
 * @author wangxmn
 *
 */
public class CellComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		Object parent = getHost().getParent().getModel();
		Object child = getHost().getModel();
		if (parent instanceof JGraph && child instanceof Cell) {
			return new CellDeleteCommand((JGraph) parent, (Cell) child);
		}
		return super.createDeleteCommand(request);
	}

}
