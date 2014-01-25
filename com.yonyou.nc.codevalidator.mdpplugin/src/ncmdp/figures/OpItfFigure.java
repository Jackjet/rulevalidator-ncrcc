package ncmdp.figures;

import java.util.HashMap;
import java.util.List;

import ncmdp.figures.ui.OperationLable;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;

import org.eclipse.swt.graphics.Color;

public class OpItfFigure extends CellFigure {
	private PartmentFigure opInterfaceFigure = new PartmentFigure();

	private HashMap<Operation, OperationLable> hmOperationToLable = new HashMap<Operation, OperationLable>();

	private static Color bgColor = new Color(null, 206, 221, 206);

	public OpItfFigure(OpInterface bo) {
		super(bo);
		setTypeLabText("<<Interface>>");
		add(opInterfaceFigure);
		// bgColor = new Color(null, 108, 157, 115);
		// bgColor = new Color(null, 130, 173, 136);
		// bgColor = new Color(null, 125, 179, 181);

		setBackgroundColor(bgColor);
		// setForegroundColor(new Color(null, 239, 255, 200));
		// setForegroundColor(new Color(null, 93, 256, 185));
		List<Operation> opers = bo.getOperations();
		for (int i = 0; i < opers.size(); i++) {
			Operation oper = opers.get(i);
			addOperation(oper);
		}
	}

	public void addOperation(Operation operation) {
		OperationLable lbl = new OperationLable(operation);
		hmOperationToLable.put(operation, lbl);
		opInterfaceFigure.add(lbl);
	}

	public void removeOperation(Operation operation) {
		OperationLable lbl = hmOperationToLable.remove(operation);
		opInterfaceFigure.remove(lbl);
	}

	public void updateOperation(Operation operation) {
		OperationLable lbl = hmOperationToLable.get(operation);
		lbl.updateFigure(operation);
	}
}
