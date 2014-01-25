package ncmdp.command;

import ncmdp.model.Connection;

import org.eclipse.gef.commands.Command;

public class ConnectionNameDirectEidtCommand extends Command {
	private Connection conn = null;
	private String oldStr = null;
	private String newValue = null;
	
	public ConnectionNameDirectEidtCommand(Connection conn , String newValue) {
		super();
		this.conn = conn;
		this.newValue = newValue;
	}
	@Override
	public boolean canExecute() {
		return super.canExecute();
	}
	@Override
	public void execute() {
		oldStr = conn.getDisplayName();
		redo();
	}
	@Override
	public void redo() {
		conn.setDisplayName(newValue);
		
	}
	@Override
	public void undo() {
		conn.setDisplayName(oldStr);

	}
	
}
