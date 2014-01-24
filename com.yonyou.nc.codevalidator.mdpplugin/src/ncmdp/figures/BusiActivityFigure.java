package ncmdp.figures;

import java.util.HashMap;
import java.util.List;

import ncmdp.figures.ui.RefBusiOpLable;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.RefBusiOperation;
import ncmdp.views.busioperation.BusiOperationUtil;

import org.eclipse.swt.graphics.Color;

public class BusiActivityFigure extends CellFigure {
	private PartmentFigure opInterfaceFigure = new PartmentFigure();

	private HashMap<RefBusiOperation, RefBusiOpLable> hmBusiOperationToLable = new HashMap<RefBusiOperation, RefBusiOpLable>();

	private static Color bgColor = new Color(null, 203, 192, 219);

	public BusiActivityFigure(BusiActivity ba) {
		super(ba);
		setTypeLabText(Messages.BusiActivityFigure_0);
		// bgColor = new Color(null, 159, 111, 133);
		// bgColor = new Color(null, 191, 151, 169);
		// bgColor = new Color(null, 221, 190, 198);

		add(opInterfaceFigure);
		setBackgroundColor(bgColor);

		List<RefBusiOperation> busiopers = ba.getRefBusiOperations();
		// init
		BusiOperationUtil.fillBusiOperationInfo(busiopers, true);
		for (int i = 0; i < busiopers.size(); i++) {
			RefBusiOperation oper = busiopers.get(i);
			addOperation(oper);
		}
	}

	public void addOperation(RefBusiOperation operation) {
		RefBusiOpLable lbl = new RefBusiOpLable(operation);
		hmBusiOperationToLable.put(operation, lbl);
		opInterfaceFigure.add(lbl);
	}

	public void removeOperation(RefBusiOperation operation) {
		RefBusiOpLable lbl = hmBusiOperationToLable.remove(operation);
		opInterfaceFigure.remove(lbl);
	}

	public void updateOperation(RefBusiOperation operation) {
		RefBusiOpLable lbl = hmBusiOperationToLable.get(operation);
		lbl.updateFigure(operation);
	}
}
