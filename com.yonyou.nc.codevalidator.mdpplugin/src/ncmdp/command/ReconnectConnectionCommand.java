package ncmdp.command;

import java.lang.reflect.Constructor;

import ncmdp.model.Cell;
import ncmdp.model.Connection;
import ncmdp.util.MDPLogger;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public class ReconnectConnectionCommand extends Command {
	private Connection conn = null;
	private Cell source = null;
	private Cell target = null;
	private Cell oldCell = null;
	private Constructor constructor = null;
	public ReconnectConnectionCommand(Connection conn){
		super();
		this.conn = conn;
		try {
			Class conCls = conn.getClass();
			constructor = conCls.getConstructor(new Class[]{Cell.class, Cell.class});
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		}
	}
	@Override
	public boolean canExecute() {
		return true;	
	}

	@Override
	public void execute() {
		redo();
	}

	@Override
	public void redo() {
		if(source != null){
			oldCell = conn.getSource();
			Cell t = conn.getTarget();
			conn.disConnect();
			conn = constructNewConnection(source, t);
		}else if(target != null){
			oldCell = conn.getTarget();
			Cell t = conn.getSource();
			conn.disConnect();
			conn = constructNewConnection(t, target);
		}
	}
	private Connection constructNewConnection(Cell sourCell, Cell targetCell) {
		try {
			Connection conn =(Connection)constructor.newInstance(new Object[]{sourCell, targetCell});
			if(source != null && source.equals(target)){
				Point p = source.getLocation();
				Dimension size = source.getSize();
				Point p1=new Point(p.x+size.width/2, p.y-20);
				Point p2 = new Point(p.x+size.width+20, p.y-20);
				Point p3 = new Point(p.x+size.width+20, p.y+size.height/2);
				conn.addBendPoint(0, p1);
				conn.addBendPoint(1, p2);
				conn.addBendPoint(2, p3);
				
				
			}
			return conn;
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
			return null;
		}
	}
	@Override
	public void undo() {
//		if(source != null){
//			Cell t = relation.getTarget();
//			relation.disConnect();
//			relation = new Relation(oldCell, t); 
//		}else if(target != null){
//			Cell t = relation.getSource();
//			relation.disConnect();
//			relation = new Relation(t, oldCell); 
//		}
	}

	public void setSource(Cell source) {
		this.source = source;
	}

	public void setTarget(Cell target) {
		this.target = target;
	}

}
