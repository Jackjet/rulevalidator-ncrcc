package ncmdp.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;

/**
 * 在提示的方形团中的两个横线之间的部分
 * @author wangxmn
 *
 */
public class PartmentFigure extends Figure{
	public PartmentFigure(){
		super();
		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(2);
		setLayoutManager(layout);
		setBorder(new PartmentBorder());
		setOpaque(false);
	}

	private class PartmentBorder extends AbstractBorder{
		public Insets getInsets(IFigure f) {
			return new Insets(1,0,0,0);
		}
		public void paint(IFigure f, Graphics g, Insets insets) {
			Point s = getPaintRectangle(f, insets).getTopLeft();
			Point e = tempRect.getTopRight();
			e.x -= 1;
			g.drawLine(s, e);
		}
	}
}
