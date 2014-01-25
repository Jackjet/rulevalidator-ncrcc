package ncmdp.figures.ui;

import org.eclipse.draw2d.Label;

public class NameLabel extends Label implements IDirectEditable {
	private String propId = null;
	public NameLabel(String text,String propId) {
		super(text);
		this.propId = propId;
	}

	public Object getEditableObj() {
		return propId;
	}

}
