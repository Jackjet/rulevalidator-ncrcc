package ncmdp.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class NoteConnectionFigure extends ConnectionFigure {

	public NoteConnectionFigure() {
		super();
		setLineStyle(SWT.LINE_DASH);
		setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
//		setConnectionRouter(new BendpointConnectionRouter());

	}

	protected void outlineShape(Graphics g) {
		g.setLineDash(new int[] { 8, 4 });
		super.outlineShape(g);
	}
}
