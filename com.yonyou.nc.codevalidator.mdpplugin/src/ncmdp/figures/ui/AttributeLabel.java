package ncmdp.figures.ui;

import ncmdp.factory.ImageFactory;
import ncmdp.model.Attribute;
import ncmdp.tool.NCMDPTool;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

/**
 * ���Ա�ǩ�����ڱ༭���е�ʵ���е�
 * @author wangxmn
 *
 */
public class AttributeLabel extends Label implements IDirectEditable{
	private String msg = null;
	private Attribute prop = null;
	public AttributeLabel(Attribute prop) {
		super(prop.getDisplayName());
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		this.prop = prop;
		markError();
	}
	public void updateFigure(Attribute prop){
		this.prop = prop;
		setText(prop.getDisplayName());
		markError();
	}
	public Attribute getProp(){
		return prop;
	}
	private void markError(){
		msg = getProp().validate();
		if(msg != null){
			setIcon(ImageFactory.getErrorImg());
		}else{
			setIcon(NCMDPTool.getAttrImg(prop));
		}
	}
	public String getErrStr(){
		return msg;
	}
	public Object getEditableObj() {
		return getProp();
	}
}
