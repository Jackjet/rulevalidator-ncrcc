package ncmdp.figures;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * 聚合连线的图形
 * @author wangxmn
 *
 */
public class AggregationRelationFigure extends RelationFigure {

	public AggregationRelationFigure() {
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
		setSourceDecoration(sourPD);
		setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
	}

}
