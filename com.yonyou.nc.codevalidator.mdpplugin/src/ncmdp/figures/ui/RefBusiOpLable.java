package ncmdp.figures.ui;

import ncmdp.factory.ImageFactory;
import ncmdp.model.activity.RefBusiOperation;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class RefBusiOpLable extends Label implements IDirectEditable {
	private String msg = null;

	RefBusiOperation busiOp;

	public RefBusiOpLable(RefBusiOperation busiOp) {
		super(busiOp.getDisplayName());
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		this.busiOp = busiOp;
		markError();
	}

	public void updateFigure(RefBusiOperation busiOp) {
		this.busiOp = busiOp;
		setText(busiOp.getDisplayName());
		markError();
	}

	public RefBusiOperation getBusiOperation() {
		return busiOp;
	}

	private void markError() {
		msg = getBusiOperation().validate();
		if (msg != null) {
			setIcon(ImageFactory.getErrorImg());
		} else {
			setIcon(ImageFactory.getRefBusiOperationImg());
		}
	}

	public String getErrStr() {
		return msg;
	}

	public Object getEditableObj() {
		return getBusiOperation();
	}

}
