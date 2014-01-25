package ncmdp.policy;

import ncmdp.command.ReconnectConnectionCommand;
import ncmdp.command.ConnectionCreateCommand;
import ncmdp.model.Cell;
import ncmdp.model.Connection;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

public class RelationGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand comd =(ConnectionCreateCommand) request.getStartCommand();
		
		Cell target = (Cell)getHost().getModel();
		comd.setTarget(target);
		return comd;
	}

	/**
	 * 创建连线命令
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		Cell sour = (Cell)getHost().getModel();//当鼠标移动到哪个组件时，会触发该方法，getHost就会获得相应的编辑part
		Class<Connection> conCls =(Class<Connection>) request.getNewObject();
		Command command = new ConnectionCreateCommand(sour, conCls);
		request.setStartCommand(command);
		return command;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Connection conn = (Connection)request.getConnectionEditPart().getModel();
		ReconnectConnectionCommand cmd = new ReconnectConnectionCommand(conn);
		cmd.setSource((Cell)getHost().getModel());
		return cmd;
	}
 
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Connection conn = (Connection)request.getConnectionEditPart().getModel();
		ReconnectConnectionCommand cmd = new ReconnectConnectionCommand(conn);
		cmd.setTarget((Cell)getHost().getModel());
		return cmd;
	}

}
