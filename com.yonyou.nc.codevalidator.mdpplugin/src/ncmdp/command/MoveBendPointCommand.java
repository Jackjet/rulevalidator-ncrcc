package ncmdp.command;

import org.eclipse.draw2d.geometry.Point;

public class MoveBendPointCommand extends AbstractBendPointCommand {
	private Point oldPoint = null;
	@Override
	public boolean canExecute() {
		return conn != null && point != null;
	}
	@Override
	public void redo() {
		oldPoint = conn.removeBendPoint(index);
		conn.addBendPoint(index, point);
		
	}

	@Override
	public void undo() {
		conn.removeBendPoint(index);
		conn.addBendPoint(index, oldPoint);
	}
    public String getLabel() {      
        return "move bendpoint";
     }  
}
