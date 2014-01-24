package ncmdp.ruler;

import java.util.Iterator;

import ncmdp.model.Cell;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public class MoveGuideCommand extends Command {
	private Guide guide = null;

	private int delta = -1;

	public MoveGuideCommand(Guide guide, int delta) {
		super();
		this.guide = guide;
		this.delta = delta;
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
		guide.setPosition(guide.getPosition() + delta);
		Iterator<Cell> iter = guide.getCells().iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			Point p = cell.getLocation().getCopy();
			if(guide.isHorizontal()){
				p.y += delta;
			}else {
				p.x += delta;
			}
			cell.setLocation(p);
		}
	}

	@Override
	public void undo() {
		guide.setPosition(guide.getPosition() - delta);
		Iterator<Cell> iter = guide.getCells().iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			Point p = cell.getLocation().getCopy();
			if(guide.isHorizontal()){
				p.y -= delta;
			}else {
				p.x -= delta;
			}
			cell.setLocation(p);
		}
	}

}
