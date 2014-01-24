package ncmdp.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
/**
 * 实现关系
 * @author wangxmn
 *
 */
public class BusiItfConnectionFigure extends ConnectionFigure {
	private static final Color bgcolor = new Color(null,35,132,56 );
	public BusiItfConnectionFigure() {
		super();
		PolygonDecoration pd = new PolygonDecoration();
		pd.setBackgroundColor(ColorConstants.white);
		PointList pl = new PointList();
		pl.addPoint(0, 0);
		pl.addPoint(-2, 2);
		pl.addPoint(-2, -2);
		pd.setTemplate(pl);
		pd.setFill(true);
		setLineStyle(SWT.LINE_DASH);
		setTargetDecoration(pd);
		setForegroundColor(bgcolor);
	}
	@Override
	protected void outlineShape(Graphics g) {
		g.setLineDash(new int[]{8,4});
		super.outlineShape(g);
	}
}
