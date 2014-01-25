package ncmdp.command;

import java.util.List;

import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.model.Connection;

import org.eclipse.gef.commands.Command;

/**
 * É¾³ýcellµÄÃüÁî
 * @author wangxmn
 *
 */
public class CellDeleteCommand extends Command {
	private JGraph graph = null;
	private Cell cell = null;
	private boolean canUndo = true;
	private List<Connection> sourceConnections = null;
	private List<Connection> targetConnections = null;
	public CellDeleteCommand(JGraph graph, Cell cell){
		super();
		this.graph = graph;
		this.cell = cell;
		setLabel("delete cell");
	}
	@Override
	public boolean canExecute() {
		return super.canExecute();
	}

	@Override
	public void execute() {
		sourceConnections = cell.getSourceConnections();
		targetConnections = cell.getTargetConnections();
		redo();
	}
	private void removeConnections(List<Connection> conn){
		int count = conn.size();
		for (int i = 0; i < count; i++) {
			Connection relation = conn.get(i);
			relation.disConnect();
		}
	}
	private void addConnections(List<Connection> conn){
		int count = conn.size();
		for (int i = 0; i < count; i++) {
			Connection relation = conn.get(i);
			relation.connect();
		}
	}
	@Override
	public void redo() {
		if(graph.removeCell(cell)){
			removeConnections(sourceConnections);
			removeConnections(targetConnections);
		}
	}

	@Override
	public boolean canUndo() {
		return isCanUndo();
	}
	@Override
	public void undo() {
		if(graph.addCell(cell)){
			addConnections(sourceConnections);
			addConnections(targetConnections);
		}
	}
	public boolean isCanUndo() {
		return canUndo;
	}
	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

}
