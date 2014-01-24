package ncmdp.policy;

import ncmdp.command.ConnectionDeleteCommand;
import ncmdp.model.Connection;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class RelationEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		Connection conn = (Connection)((EditPart)request.getEditParts().get(0)).getModel();
		
		return new ConnectionDeleteCommand(conn);
	}

}
