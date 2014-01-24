package ncmdp.figures;

import java.util.HashMap;
import java.util.List;

import ncmdp.figures.ui.RefOperationLable;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.RefOperation;
import ncmdp.views.busioperation.BusiOperationUtil;

import org.eclipse.swt.graphics.Color;

public class BusiOperationFigure extends CellFigure {
	private PartmentFigure busiOperationFigure = new PartmentFigure();

	private HashMap<RefOperation, RefOperationLable> hmOperationToLable = new HashMap<RefOperation, RefOperationLable>();

	private static Color bgColor = new Color(null, 171, 210, 234);

	public BusiOperationFigure(BusiOperation bo) {
		super(bo);
		// bgColor = new Color(null, 127, 133, 213);
		// bgColor = new Color(null, 171, 210, 234);
		// bgColor = new Color(null, 236, 234, 175);
		setTypeLabText(Messages.BusiOperationFigure_0);
		add(busiOperationFigure);
		setBackgroundColor(bgColor);
		List<RefOperation> opers = bo.getOperations();
		// 第一次显示operation 初始化下
		BusiOperationUtil.fillOperationInfo(opers, true);
		for (int i = 0; i < opers.size(); i++) {
			RefOperation oper = opers.get(i);
			addOperation(oper);
		}
	}

	public void addOperation(RefOperation operation) {
		RefOperationLable lbl = new RefOperationLable(operation);
		hmOperationToLable.put(operation, lbl);
		busiOperationFigure.add(lbl);
	}

	public void removeOperation(RefOperation operation) {
		RefOperationLable lbl = hmOperationToLable.remove(operation);
		busiOperationFigure.remove(lbl);
	}

	public void updateOperation(RefOperation operation) {
		RefOperationLable lbl = hmOperationToLable.get(operation);
		lbl.updateFigure(operation);
	}
}
