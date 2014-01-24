package ncmdp.figures;

import ncmdp.factory.ImageFactory;
import ncmdp.parts.ConnectionPart;
import ncmdp.tool.NCMDPTool;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RoutingAnimator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class ConnectionFigure extends PolylineConnection {
	private Label lblCnnName = new Label();
	private Border border = new LineBorder(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
	private String errStr = null;
	public ConnectionFigure() {
		super();
		addRoutingListener(RoutingAnimator.getDefault());
		lblCnnName.setOpaque(true);
		add(lblCnnName, new ConnectionLocator(this));
		
	}
	private ConnectionPart part = null;
	@Override
	public void paintFigure(Graphics g) {
		if(NCMDPTool.isSupportGDI())
			g.setAntialias(SWT.ON);
		super.paintFigure(g);
	}

	public void setConnName(String newName){
		lblCnnName.setText(modifyString(newName));
		if(newName == null || newName.trim().length() == 0)
			lblCnnName.setOpaque(false);
		else
			lblCnnName.setOpaque(true);
	}
	public boolean isCnnNameFigure(IFigure figure) {
		if (figure.equals(lblCnnName)) {
			return true;
		} else {
			return false;
		}
	}
	protected String modifyString(String str) {
		if (str == null) {
			str = "";
		}
		int count = 10 - str.length();
		StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < count; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	public void setSelection(boolean sel){
		if(sel){
			lblCnnName.setBorder(border);
			setLineWidth(2);
			
		}else{
			lblCnnName.setBorder(null);
			setLineWidth(1);
		}
	}
	
	public void markError(String errMsg){
		errStr = errMsg;
		if(errStr != null){
			lblCnnName.setIcon(ImageFactory.getErrorImg());
		}else{
			lblCnnName.setIcon(null);
		}
	}
	public String getErrorStr(){
		return errStr;
	}
	public boolean isConnNameLabel(Label lbl){
		return lblCnnName.equals(lbl);
	}

	public ConnectionPart getPart() {
		return part;
	}

	public void setPart(ConnectionPart part) {
		this.part = part;
	}
}
