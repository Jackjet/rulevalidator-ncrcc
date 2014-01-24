package ncmdp.ruler;

import ncmdp.model.Cell;

import org.eclipse.gef.commands.Command;

public class GuideChangeCommand extends Command {
	private Cell cell = null;
	private boolean isHorizontal = false;
	private Guide oldGuide, newGuide;
	private Integer oldAlign, newAlign;

	public GuideChangeCommand(Cell cell, boolean isHorizontal) {
		super();
		this.cell = cell;
		this.isHorizontal = isHorizontal;
	}
	public void setNewGuide(Guide newGuide, Integer newAlign){
		this.newGuide = newGuide;
		this.newAlign = newAlign;
	}
	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute() {
		oldGuide = isHorizontal? cell.getHorizontalGuide():cell.getVerticalGuide();
		if(oldGuide != null){
			oldAlign = oldGuide.getAlignment(cell);
		}
		redo();
	}

	@Override
	public void redo() {
		changeGuide(oldGuide, newGuide, newAlign);
	}

	@Override
	public void undo() {
		changeGuide(newGuide, oldGuide, oldAlign);
	}
	private void changeGuide(Guide oldGuide, Guide newGuide, Integer newAlign){
		if(oldGuide != null && oldGuide != newGuide){
			oldGuide.detachCell(cell);
		}
		if(newGuide != null){
			newGuide.attachCell(cell, newAlign);
		}
	}
}
