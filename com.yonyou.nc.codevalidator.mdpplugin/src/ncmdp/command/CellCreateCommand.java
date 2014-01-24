package ncmdp.command;

import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.util.MDPLogger;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

/**
 * �½�ʵ�������
 * @author wangxmn
 *
 */
public class CellCreateCommand extends Command {
	private Cell cell = null;
	private boolean canUndo = true;
	private JGraph graph = null;
	private Rectangle rect = null;

	public CellCreateCommand(Cell cell, JGraph graph, Rectangle rect) {
		super();
		this.cell = cell;
		this.graph = graph;
		this.rect = rect;
		setLabel("create new cell");
	}

	@Override
	public boolean canExecute() {
		return cell != null && graph != null && rect != null;
	}

	@Override
	public void execute() {
//		MDPLogger.info("����ӵ�cell��idΪ��"+cell.getId());
		cell.setLocation(rect.getLocation());
		cell.setCreateIndustry(graph.getIndustry().getCode());//������ҵ����
		Dimension size = rect.getSize();
		if (size.width > 0 && size.height > 0)
			cell.setSize(size);
		redo();
	}

	@Override
	public void redo() {
		graph.addCell(cell);
	}

	@Override
	public void undo() {
		graph.removeCell(cell);
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	@Override
	public boolean canUndo() {
		return isCanUndo();
	}

}
