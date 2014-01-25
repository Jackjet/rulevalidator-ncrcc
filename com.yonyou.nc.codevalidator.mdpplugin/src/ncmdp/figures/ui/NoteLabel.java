package ncmdp.figures.ui;

public class NoteLabel extends MultiLineLabel implements IDirectEditable {
	
	public NoteLabel() {
		super();
	}

	public NoteLabel(String text) {
		super(text);
	}

	public Object getEditableObj() {
		return null;
	}

}
