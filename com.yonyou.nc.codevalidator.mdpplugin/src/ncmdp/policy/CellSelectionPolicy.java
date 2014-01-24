package ncmdp.policy;

import ncmdp.parts.CellPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

public class CellSelectionPolicy extends SelectionEditPolicy {
	@Override
	protected void hideSelection() {
	}

	@Override
	public void showSelection() {
		CellPart part = (CellPart) getHost();
		IFigure figure = part.getFigure();
		IFigure parent = figure.getParent();
		if(parent != null){
			parent.remove(figure);
			parent.add(figure);
		}
	}


}
