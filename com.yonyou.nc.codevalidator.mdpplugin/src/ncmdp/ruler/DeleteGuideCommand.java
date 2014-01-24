package ncmdp.ruler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ncmdp.model.Cell;

import org.eclipse.gef.commands.Command;

public class DeleteGuideCommand extends Command {
	private Guide guide = null;
	private Ruler ruler = null;
	private Map<Cell, Integer> map = null;
	public DeleteGuideCommand(Guide guide, Ruler ruler) {
		super();
		this.guide = guide;
		this.ruler = ruler;
		
	}
	@Override
	public boolean canExecute() {
		return true;
	}
	@Override
	public void execute() {
		map = new HashMap<Cell, Integer>(guide.getMap());
		redo();
	}
	@Override
	public void redo() {
		Iterator<Cell> iter= map.keySet().iterator();
		while (iter.hasNext()) {
			guide.detachCell(iter.next());
		}
		ruler.removeGuide(guide);
	}
	@Override
	public void undo() {
		ruler.addGuide(guide);
		Iterator<Cell> iter= map.keySet().iterator();
		while (iter.hasNext()) {
			Cell cell = iter.next();
			guide.attachCell(cell, map.get(cell));
		}
	}
	
	
}
