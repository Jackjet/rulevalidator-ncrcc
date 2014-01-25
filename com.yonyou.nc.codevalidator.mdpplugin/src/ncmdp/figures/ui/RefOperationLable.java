package ncmdp.figures.ui;

import ncmdp.factory.ImageFactory;
import ncmdp.model.activity.RefOperation;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class RefOperationLable extends Label implements IDirectEditable {
	private String msg = null;

	RefOperation operation = null;

	public RefOperationLable(RefOperation operation) {
		//		super(operation.getDisplayName()+"("+operation.getOpItfName()+")");
		super(operation.getDisplayName());
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		this.operation = operation;
		markError();
	}

	public void updateFigure(RefOperation opration) {
		this.operation = opration;
		setText(opration.getDisplayName() + "(" + operation.getOpItfName() + ")");
		markError();
	}

	public RefOperation getOperation() {
		return operation;
	}

	private void markError() {
		msg = getOperation().validate();
		if (msg != null) {
			setIcon(ImageFactory.getErrorImg());
		} else {
			setIcon(ImageFactory.getRefOperationImg());
		}
	}

	public String getErrStr() {
		return msg;
	}

	public Object getEditableObj() {
		// TODO Auto-generated method stub
		return getOperation();
	}
}
