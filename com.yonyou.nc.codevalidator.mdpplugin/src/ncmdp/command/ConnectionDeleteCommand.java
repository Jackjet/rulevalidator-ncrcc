package ncmdp.command;

import ncmdp.model.Connection;

import org.eclipse.gef.commands.Command;

public class ConnectionDeleteCommand extends Command {
	private Connection conn = null;

	public ConnectionDeleteCommand(Connection relation) {
		super();
		this.conn = relation;
		setLabel("delete relation");
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();

	}

	@Override
	public void execute() {
		redo();
		conn.deleteConn();
	}

	@Override
	public void redo() {
		conn.disConnect();
	}

	@Override
	public void undo() {
		conn.connect();
	}

}
