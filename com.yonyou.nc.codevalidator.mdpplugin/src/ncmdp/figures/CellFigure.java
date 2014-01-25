package ncmdp.figures;

import java.util.HashMap;

import ncmdp.factory.ImageFactory;
import ncmdp.figures.ui.IDirectEditable;
import ncmdp.figures.ui.NameLabel;
import ncmdp.figures.ui.NullBorder;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.tool.NCMDPTool;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

public class CellFigure extends RectangleFigure {

	private Label typeLab = new Label(Messages.CellFigure_0);
	private Color bgColor = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	private Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	private HashMap<String, IFigure> hmPropIdToFigure = new HashMap<String, IFigure>();
	private String errStr = null;
	private PartmentFigure titleFigure = null;

	public CellFigure(Cell cell) {
		super();
		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(10);
		setLayoutManager(layout);

		setBackgroundColor(bgColor);

		addPropertity(Cell.PROP_ELEMENT_DISPLAY_NAME, cell.getDisplayName());
		add(getTitleFigure());//绘制实体头
		markError(cell.validate());//进行验证

	}

	/**
	 * 更改外观，感谢liuyuan提供技术支持 @dingxm 2010-08-04
	 */
	@Override
	public void paint(Graphics graphics) {
		Rectangle rect = getBounds().getCopy();
		Pattern pattern = new Pattern(null, rect.x, rect.y + rect.height, rect.x, rect.y,
				ColorConstants.white, getBackgroundColor());
		Image img = null;
		GC gc = null;
		if (graphics instanceof SWTGraphics) {
			graphics.setBackgroundPattern(pattern);
			graphics.fillRectangle(rect);
		} else {
			pattern = new Pattern(Display.getCurrent(), 0, 0, rect.width, rect.height, white,
					getBackgroundColor());

			img = new Image(Display.getCurrent(), rect.width, rect.height);
			gc = new GC(img);
			gc.setBackgroundPattern(pattern);
			gc.fillRectangle(rect.x, rect.y + rect.height, rect.x, rect.y);
			graphics.drawImage(img, rect.x, rect.y);
		}

		if (getLocalForegroundColor() != null)
			graphics.setForegroundColor(getLocalForegroundColor());
		if (getLocalFont() != null)
			graphics.setFont(getLocalFont());

		graphics.pushState();
		try {
			paintFigure(graphics);
			graphics.restoreState();
			paintClientArea(graphics);
			paintBorder(graphics);
		} finally {
			if (gc != null)
				gc.dispose();
			if (img != null)
				img.dispose();
			graphics.popState();
		}
	}

	@Override
	protected void fillShape(Graphics g) {
		int corSize = Constant.CELL_CORNER_SIZE;
		if (NCMDPTool.isSupportGDI()) {
			g.setAntialias(SWT.ON);
			Rectangle rect = getBounds().getCopy();
			Image img = null;
			GC gc = null;
			Pattern pattern = null;
			try {
				rect.width -= 1;
				rect.height -= 1;
				if (rect.width <= 0)
					rect.width = 1;
				if (rect.height <= 0)
					rect.height = 1;
				if (g instanceof SWTGraphics) {
					pattern = new Pattern(Display.getCurrent(), rect.x, rect.y, rect.x + rect.width, rect.y
							+ rect.height, white, getBackgroundColor());
					g.setBackgroundPattern(pattern);
					g.fillRoundRectangle(rect, corSize, corSize);
				} else {// ScaledGraphics
					pattern = new Pattern(Display.getCurrent(), 0, 0, rect.width, rect.height, white,
							getBackgroundColor());

					img = new Image(Display.getCurrent(), rect.width, rect.height);
					gc = new GC(img);
					gc.setBackgroundPattern(pattern);
					gc.fillRoundRectangle(0, 0, rect.width, rect.height, corSize, corSize);
					g.drawImage(img, rect.x, rect.y);
				}
			} finally {
				if (pattern != null)
					pattern.dispose();
				if (gc != null)
					gc.dispose();
				if (img != null)
					img.dispose();
			}
		} else {
			g.fillRoundRectangle(getBounds(), corSize, corSize);
		}
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		int corSize = Constant.CELL_CORNER_SIZE;
		Rectangle r = getBounds();
		int x = r.x + lineWidth / 2;
		int y = r.y + lineWidth / 2;
		int w = r.width - Math.max(1, lineWidth);
		int h = r.height - Math.max(1, lineWidth);
		Rectangle newR = new Rectangle(x, y, w, h);
		graphics.drawRoundRectangle(newR, corSize, corSize);// Rectangle(x, y, w, h);
	}

	protected void setTypeLabText(String text) {
		typeLab.setText(text);
	}

	protected String getTypeLabText() {
		return typeLab.getText();
	}
	
	public Label addPropertity(String id, String prop) {
		NameLabel lbl = new NameLabel(prop, id);
		lbl.setBorder(new NullBorder());
		lbl.setLabelAlignment(PositionConstants.CENTER);
		hmPropIdToFigure.put(id, lbl);
		getTitleFigure().add(lbl);
		return lbl;
	}

	public void removeLableById(String propId) {
		Label lbl = (Label) hmPropIdToFigure.get(propId);
		getTitleFigure().remove(lbl);
	}

	public void setLblTextById(String propId, String text) {
		Label lbl = (Label) hmPropIdToFigure.get(propId);
		lbl.setText(text);
	}

	public IDirectEditable getIDirectEditableByPoint(Point p) {
		translateToRelative(p);
		IFigure fi = findFigureAt(p);
		while (fi != null && !(fi instanceof IDirectEditable)) {
			fi = fi.getParent();
		}
		if (fi instanceof IDirectEditable) {
			return (IDirectEditable) fi;
		} else {
			return null;
		}
	}

	public Label getLblByPoint(Point p) {
		translateToRelative(p);
		IFigure fi = findFigureAt(p);
		if (fi instanceof Label) {
			return (Label) fi;
		} else {
			return null;
		}
	}

	protected PartmentFigure getTitleFigure() {
		if (titleFigure == null) {
			titleFigure = new PartmentFigure();
			titleFigure.add(typeLab);
			titleFigure.setBorder(new AbstractBorder() {
				public Insets getInsets(IFigure ifigure) {
					return new Insets(2, 0, 0, 0);
				}
				public void paint(IFigure ifigure, Graphics g, Insets insets) {
				}
			});
		}
		return titleFigure;
	}

	public void markError(String errMsg) {
		errStr = errMsg;
		if (errStr != null) {
			typeLab.setIcon(ImageFactory.getErrorImg());
		} else {
			typeLab.setIcon(null);
		}
	}

	public String getErrStr() {
		return errStr;
	}

	public boolean isTypeLabel(Label lbl) {
		return typeLab.equals(lbl);
	}
}
