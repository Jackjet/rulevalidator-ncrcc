package ncmdp.command;

public class CreateBendPointCommand extends AbstractBendPointCommand {

	@Override
	public boolean canExecute() {
		return conn != null && point != null;
	}
	@Override
	public void redo() {
		conn.addBendPoint(index, point);
	}

	@Override
	public void undo() {
		conn.removeBendPoint(index);
	}
    public String getLabel() {      
        return "create bendpoint";
     }  
}
