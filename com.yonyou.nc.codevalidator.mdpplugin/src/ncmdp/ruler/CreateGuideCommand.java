package ncmdp.ruler;

import org.eclipse.gef.commands.Command;

public class CreateGuideCommand extends Command {
	private Ruler ruler = null;

	private int position = -1;

	private Guide guide = null;

	public CreateGuideCommand(Ruler ruler, int position) {
		super("create guide");
		this.ruler = ruler;
		this.position = position;
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void execute() {
		guide = new Guide(!ruler.isHorizontal());
		guide.setPosition(position);
		redo();
	}

	@Override
	public void redo() {
		ruler.addGuide(guide);
	}

	@Override
	public void undo() {
		ruler.removeGuide(guide);
	}

}
