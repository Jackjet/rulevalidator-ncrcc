package ncmdp.figures.ui;

import ncmdp.factory.ImageFactory;
import ncmdp.model.EnumItem;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class EnumitemLabel extends Label implements IDirectEditable{

	private String msg = null;
	private EnumItem item = null;
	public EnumitemLabel(EnumItem item) {
		super(item.getEnumDisplay());
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		this.item = item;
		markError();
	}
	public void updateFigure(EnumItem item){
		this.item = item;
		setText(item.getEnumDisplay());
		markError();
	}
	public EnumItem getEnumItem(){
		return item;
	}
	private void markError(){
		msg = getEnumItem().validate();
		if(msg != null){
			setIcon(ImageFactory.getErrorImg());
		}else{
			setIcon(ImageFactory.getAttrImage());
		}
	}
	public String getErrStr(){
		return msg;
	}
	public Object getEditableObj() {
		// TODO Auto-generated method stub
		return getEnumItem();
	}

}
