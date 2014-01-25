package ncmdp.figures;

import java.util.HashMap;
import java.util.List;

import ncmdp.figures.ui.AttributeLabel;
import ncmdp.figures.ui.OperationLable;
import ncmdp.model.Attribute;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.Operation;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * 具体绘制属性的地方
 * @author wangxmn
 *
 */
public class ValueObjectFigure extends CellFigure {
	private static Color bgColor = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	private HashMap<Attribute, AttributeLabel> hmAttrToPropLabel = new HashMap<Attribute, AttributeLabel>();
	private HashMap<Operation, OperationLable> hmOperationToLable = new HashMap<Operation, OperationLable>();
	private PartmentFigure operationFigure = new PartmentFigure();
	private PartmentFigure attrsFigure = null;
	
	public ValueObjectFigure(ValueObject vo){
		super(vo);
		setTypeLabText(Messages.ValueObjectFigure_0);
		setBackgroundColor(bgColor);

		add(getAttrsFigure());//绘制属性的图形
		add(operationFigure);//绘制操作的图形
		List<Attribute> props = vo.getProps();
		for (int i = 0; i < props.size(); i++) {
			Attribute attri = props.get(i);
			addAttribute(attri);
		}
		List<Operation> opers = vo.getOperations();
		for (int i = 0; i < opers.size(); i++) {
			Operation oper = opers.get(i);
			addOperation(oper);
		}
	}
	private PartmentFigure getAttrsFigure(){
		if(attrsFigure == null){
			attrsFigure = new PartmentFigure();
		}
		return attrsFigure;
	}

	/**
	 * 向图形的属性部分添加属性label
	 * @param attri
	 */
	public void addAttribute(Attribute attri){
		AttributeLabel lab = new AttributeLabel(attri);
		hmAttrToPropLabel.put(attri, lab);
		getAttrsFigure().add(lab);
	}
	/**
	 * 移除图形中的属性部分的属性label
	 * @param attri
	 */
	public void removeAttribute(Attribute attri){
		AttributeLabel figure = hmAttrToPropLabel.remove(attri);
		getAttrsFigure().remove(figure);
	}
	/**
	 * 更新图形中的属性标签
	 * @param attri
	 */
	public void updateAttribute(Attribute attri){
		AttributeLabel figure = hmAttrToPropLabel.get(attri);
		figure.updateFigure(attri);
	}
	/**
	 * 添加操作标签
	 * @param operation
	 */
	public void addOperation(Operation operation){
		OperationLable lbl = new OperationLable(operation);
		hmOperationToLable.put(operation, lbl);
		operationFigure.add(lbl);
	}
	/**
	 * 移除操作标签
	 * @param operation
	 */
	public void removeOperation(Operation operation){
		OperationLable lbl = hmOperationToLable.remove(operation);
		operationFigure.remove(lbl);
	}
	/**
	 * 更新操作标签
	 * @param operation
	 */
	public void updateOperation(Operation operation){
		OperationLable lbl = hmOperationToLable.get(operation);
		lbl.updateFigure(operation);
	}
	@SuppressWarnings("unchecked")
	public void updateAllAttribute(){
		List<Figure> children = getAttrsFigure().getChildren();
		for (int i = 0; i < children.size(); i++) {
			Figure child = children.get(i);
			if(child instanceof AttributeLabel){
				AttributeLabel label = (AttributeLabel)child;
				label.updateFigure(label.getProp());
			}
		}
	}
}
