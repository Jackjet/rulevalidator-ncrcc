package ncmdp.command;

public class DeleteBendPointCommand extends AbstractBendPointCommand {
	@Override
	public boolean canExecute() {
		return conn != null ;
	}
	@Override
	public void redo() {
		point = conn.removeBendPoint(index);
	}

	@Override
	public void undo() {
		conn.addBendPoint(index, point);
	}
    public String getLabel() {      
        return "delete bendpoint";
     }  
}
