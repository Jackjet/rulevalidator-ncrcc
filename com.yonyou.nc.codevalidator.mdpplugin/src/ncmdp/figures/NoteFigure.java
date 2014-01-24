package ncmdp.figures;

import ncmdp.figures.ui.NoteLabel;
import ncmdp.figures.ui.NullBorder;
import ncmdp.model.Cell;
import ncmdp.model.Note;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.swt.graphics.Color;
/**
 * ×¢ÊÍ¹¦ÄÜ
 * @author wangxmn
 *
 */
public class NoteFigure extends CellFigure {
	private static Color bgColor = new Color(null, 239,255,200);
	private PartmentFigure remarkFigure = null;
	private NoteLabel tf = null;
	
	public NoteFigure(Note note) {
		super(note);
		setTypeLabText(Messages.NoteFigure_0);
		setBackgroundColor(bgColor);
		removeLableById(Cell.PROP_ELEMENT_DISPLAY_NAME);
		add(getRemarkFigure());
		getTf().setText(note.getRemark());
		BorderLayout layout = new BorderLayout();
		setLayoutManager(layout);
		layout.setConstraint(getRemarkFigure(), BorderLayout.CENTER);
		layout.setConstraint(getTitleFigure(), BorderLayout.TOP);
	}
	private PartmentFigure getRemarkFigure() {
		if(remarkFigure == null){
			remarkFigure = new PartmentFigure();
			BorderLayout layout = new BorderLayout();
			remarkFigure.setLayoutManager(layout);
			remarkFigure.add(getTf());
			layout.setConstraint(getTf(), BorderLayout.CENTER);
		}
		return remarkFigure;
	}
	public void updateRemark(String text){
		getTf().setText(text);
	}
	private NoteLabel getTf() {
		if(tf == null){
			tf = new NoteLabel(""); //$NON-NLS-1$
			tf.setBorder(new NullBorder());
		}
		return tf;
	}
}
