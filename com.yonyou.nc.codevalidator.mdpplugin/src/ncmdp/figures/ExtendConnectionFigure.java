package ncmdp.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;

public class ExtendConnectionFigure extends ConnectionFigure{
	public ExtendConnectionFigure(){
		super();
		PolygonDecoration pd = new PolygonDecoration();
		pd.setBackgroundColor(ColorConstants.white);
		PointList pl = new PointList();
		pl.addPoint(0, 0);
		pl.addPoint(-2, 2);
		pl.addPoint(-2, -2);
		pd.setTemplate(pl);
		pd.setFill(true);
		setTargetDecoration(pd);
//		setConnectionRouter(new BendpointConnectionRouter());
		Color bgColor = new Color(null, 4, 89, 158);
		setForegroundColor(bgColor);

	}
}
