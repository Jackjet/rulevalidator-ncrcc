package ncmdp.command;

import ncmdp.model.Cell;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class CellConstraintUpdateCommand extends Command {
	private Cell cell = null;
	private Rectangle oldRect = null;
	private Rectangle newRect = null;
//	private ChangeBoundsRequest request = null;
	public CellConstraintUpdateCommand(Cell cell, Rectangle newRect){
		super();
//		this.request = request;
		this.cell = cell;
		this.newRect = newRect;
		setLabel("cell constraint update");
	}
	@Override
	public boolean canExecute() {
		return super.canExecute();
	}

	@Override
	public void execute() {
		oldRect = new Rectangle(newRect.getLocation(), newRect.getSize());
		redo();
	}

	@Override
	public void redo() {
		cell.setLocation(newRect.getLocation());
		cell.setSize(newRect.getSize());
	}

	@Override
	public void undo() {
		cell.setLocation(oldRect.getLocation());
		cell.setSize(oldRect.getSize());
	}

}
