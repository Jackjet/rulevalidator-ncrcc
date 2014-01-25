package ncmdp.figures.ui;

import ncmdp.factory.ImageFactory;
import ncmdp.model.BusiItfAttr;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class BusiItfAttrLabel extends Label implements IDirectEditable {
	private BusiItfAttr attr = null;
	private String msg = null;
	public BusiItfAttrLabel(BusiItfAttr attr) {
		super(attr.getDisplayName());
		this.attr = attr;
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		markError();
	}
	public Object getEditableObj() {
		return attr;
	}
	public void updateFigure(BusiItfAttr attr){
		this.attr =attr;
		setText(attr.getDisplayName());
		markError();
	}
	public BusiItfAttr getAttr(){
		return attr;
	}
	private void markError(){
		msg = getAttr().validate();
		if(msg != null){
			setIcon(ImageFactory.getErrorImg());
		}else{
			setIcon(ImageFactory.getAttrImage());
		}
	}
	public String getErrStr(){
		return msg;
	}

}
