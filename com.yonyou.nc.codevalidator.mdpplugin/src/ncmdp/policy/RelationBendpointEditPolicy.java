package ncmdp.policy;

import ncmdp.command.CreateBendPointCommand;
import ncmdp.command.DeleteBendPointCommand;
import ncmdp.command.MoveBendPointCommand;
import ncmdp.model.Connection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

public class RelationBendpointEditPolicy extends BendpointEditPolicy {

	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		CreateBendPointCommand cmd = new CreateBendPointCommand();
		cmd.setIndex(request.getIndex());
		Point p = request.getLocation();
		getHostFigure().translateToRelative(p);
		cmd.setPoint(p);
		cmd.setConnection((Connection)getHost().getModel());
		return cmd;
	}

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		DeleteBendPointCommand cmd = new DeleteBendPointCommand();
		cmd.setIndex(request.getIndex());
		cmd.setConnection((Connection)getHost().getModel());
		return cmd;
	}

	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendPointCommand cmd = new MoveBendPointCommand();
		cmd.setIndex(request.getIndex());
		Point p = request.getLocation();
		getHostFigure().translateToRelative(p);
		cmd.setPoint(p);
		cmd.setConnection((Connection)getHost().getModel());
		return cmd;
	
	}
	

}
