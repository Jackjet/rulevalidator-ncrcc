package ncmdp.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * “¿¿µπÿ¡™≈‰÷√
 * @author wangxmn
 *
 */
public class DependConnectionFigure extends ConnectionFigure {
	public DependConnectionFigure() {
		super();
		PolygonDecoration pd = new PolygonDecoration();
		PointList pl = new PointList();
		pl.addPoint(0, 0);
		pl.addPoint(-2, 2);
		pl.addPoint(0, 0);
		pl.addPoint(-2, -2);
		pd.setTemplate(pl);
		setLineStyle(SWT.LINE_DASH);
		setTargetDecoration(pd);
		Color bgColor = new Color(null, 190, 154, 77);
		setForegroundColor(bgColor);
	}

	@Override
	protected void outlineShape(Graphics g) {
		g.setLineDash(new int[] { 8, 4 });
		super.outlineShape(g);
	}

}
