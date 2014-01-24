package ncmdp.figures.ui;

import ncmdp.factory.ImageFactory;
import ncmdp.model.activity.Operation;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class OperationLable extends Label implements IDirectEditable{
	private String msg = null;
	Operation operation = null;
	public OperationLable(Operation operation) {
		super(operation.getDisplayName());
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(new NullBorder());
		this.operation = operation;
		markError();
	}
	public void updateFigure(Operation opration){
		this.operation = opration; 
		setText(opration.getDisplayName());
		markError();
	}
	public Operation getOperation(){
		return operation;
	}
	private void markError(){
		msg = getOperation().validate();
		if(msg != null){
			setIcon(ImageFactory.getErrorImg());
		}else{
			setIcon(ImageFactory.getOperationImg());
		}
	}
	public String getErrStr(){
		return msg;
	}
	public Object getEditableObj() {
		// TODO Auto-generated method stub
		return getOperation();
	}
}
