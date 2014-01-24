package ncmdp.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;

public class ConvergeConnectionFigure extends ConnectionFigure {
	public ConvergeConnectionFigure() {
		super();
		PolygonDecoration targetPD = new PolygonDecoration();
		PointList pl = new PointList();
		pl.addPoint(0, 0);
		pl.addPoint(-2, 2);
		pl.addPoint(0, 0);
		pl.addPoint(-2, -2);
		targetPD.setTemplate(pl);
		setTargetDecoration(targetPD);
		PolygonDecoration sourPD = new PolygonDecoration();
		PointList pll = new PointList();
		pll.addPoint(0, 0);
		pll.addPoint(-1, 1);
		pll.addPoint(-2, 0);
		pll.addPoint(-1, -1);
		sourPD.setTemplate(pll);
		sourPD.setBackgroundColor(ColorConstants.white);
		sourPD.setFill(true);
		setSourceDecoration(sourPD);
		Color bgColor = new Color(null, 46, 129, 139);
		setForegroundColor(bgColor);

	}

	//	@Override
	//	protected void outlineShape(Graphics g) {
	//		g.setLineDash(new int[] { 8, 4 });
	//		super.outlineShape(g);
	//	}
}
