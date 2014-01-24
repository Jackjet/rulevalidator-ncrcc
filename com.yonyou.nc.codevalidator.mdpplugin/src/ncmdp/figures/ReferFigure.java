package ncmdp.figures;

import ncmdp.figures.ui.NullBorder;
import ncmdp.model.Reference;
import ncmdp.parts.CellPart;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
/**
 * 引用类型的图形
 * @author wangxmn
 *
 */
public class ReferFigure extends CellFigure {
	private class MouseHandler implements MouseMotionListener{

		public void mouseDragged(MouseEvent arg0) {
			
		}

		public void mouseEntered(MouseEvent me) {
			if(me.getState() == MouseEvent.ALT){
				ReferFigure.this.setMouseHover(true);
			}else{
				ReferFigure.this.setMouseHover(false);
			}
		}

		public void mouseExited(MouseEvent me) {
			ReferFigure.this.setMouseHover(false);
			
		}

		public void mouseHover(MouseEvent me) {
			if(me.getState() == MouseEvent.ALT){
				ReferFigure.this.setMouseHover(true);
			}else{
				ReferFigure.this.setMouseHover(false);
			}
			
		}

		public void mouseMoved(MouseEvent me) {
			if(me.getState() == MouseEvent.ALT){
				ReferFigure.this.setMouseHover(true);
			}else{
				ReferFigure.this.setMouseHover(false);
			}
		}
		
	}
	private class ReferBorder extends AbstractBorder{
		public Insets getInsets(IFigure figure) {
			return new Insets(4,4,4,4);
		}

		public void paint(IFigure figure, Graphics g, Insets inset) {
			if(isMouseHover){
				g.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				g.drawRectangle(figure.getBounds().getCopy().crop(new Insets(2,2,3,3)));
			}
		}
	}
	IFigure refFigure = null;
	private boolean isMouseHover =false;
	public ReferFigure(Reference ref) {
		super(ref);
		setLayoutManager(null);
		remove(getTitleFigure());
		refFigure = CellPart.getFigureByModel(ref.getReferencedCell());
		if (refFigure != null) {
			if(refFigure instanceof CellFigure){
				CellFigure temp = (CellFigure)refFigure;
				String type = temp.getTypeLabText();
				type += Messages.ReferFigure_0;
				temp.setTypeLabText(type);
			}
		}else {
			refFigure = new PartmentFigure();
			refFigure.setBorder(new LineBorder());
			refFigure.add(new Label(Messages.ReferFigure_1));
			refFigure.add(getLabel(Messages.ReferFigure_2));
			refFigure.add(getLabel(Messages.ReferFigure_3));
			refFigure.add(getLabel("------------------------------")); 
			refFigure.add(getLabel(Messages.ReferFigure_5+ref.getModuleName()));
			refFigure.add(getLabel(Messages.ReferFigure_6+ref.getMdFilePath()));
			refFigure.add(getLabel(Messages.ReferFigure_7+ref.getRefId()));
			refFigure.add(getLabel("------------------------------")); 
			
		}
		add(refFigure);
		addMouseMotionListener(new MouseHandler());
		 Color bgColor = new Color(null, 170, 230, 170);
		setBackgroundColor(bgColor);
		setBorder(new ReferBorder());
	}

	private Label getLabel(String text) {
		Label tf = new Label(text);
		tf.setBorder(new NullBorder());
		tf.setLabelAlignment(PositionConstants.LEFT);
		return tf;
	}

	@Override
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		if (refFigure != null) {
			rect = this.getBounds();
			Insets inset =new Insets();;
			Border border =this.getBorder();
			if(border != null)
				inset = border.getInsets(this);
			Rectangle refRect = new Rectangle(rect.x+inset.left,rect.y+inset.top, rect.width - inset.left - inset.right, rect.height - inset.top - inset.bottom);
			refFigure.setBounds(refRect);
		}

	}
	
	public void setMouseHover(boolean hover){
		isMouseHover = hover;
		this.repaint();
	}
}
