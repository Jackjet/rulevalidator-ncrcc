package ncmdp.policy;

import ncmdp.figures.ConnectionFigure;

import org.eclipse.gef.editpolicies.SelectionEditPolicy;

public class RelationSelectionPolicy extends SelectionEditPolicy {

	@Override
	protected void hideSelection() {
		
		ConnectionFigure figure = (ConnectionFigure)getHostFigure();
		figure.setSelection(false);


	}

	@Override
	protected void showSelection() {
		ConnectionFigure figure = (ConnectionFigure)getHostFigure();
		figure.setSelection(true);

	}

}
