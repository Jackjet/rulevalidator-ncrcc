package ncmdp.policy;

import ncmdp.command.ConnectionNameDirectEidtCommand;
import ncmdp.command.RelationDirectEditCommand;
import ncmdp.figures.ConnectionFigure;
import ncmdp.figures.RelationFigure;
import ncmdp.model.Connection;
import ncmdp.model.Relation;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.custom.CCombo;

public class RelationDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		ConnectionFigure connFigure = (ConnectionFigure) getHostFigure();
		Connection conn= (Connection)getHost().getModel();
		
		CellEditor editor = request.getCellEditor();
		IFigure figure = (IFigure)editor.getControl().getData("figure");
		if (connFigure.isCnnNameFigure(figure)) {
			return new ConnectionNameDirectEidtCommand(conn,(String)editor.getValue());
		} else if (connFigure instanceof RelationFigure) {
			RelationFigure relationFigure = (RelationFigure)connFigure;
			Relation relation = (Relation) conn;
			String newStr = ((CCombo)editor.getControl()).getText();
			if (relationFigure.isSourceConstraintFigure(figure)) {
				return new RelationDirectEditCommand(relation, newStr, Relation.SOURCE_CONSTRAINT);
			} else if (relationFigure.isTargetConstraintFigure(figure)) {
				return new RelationDirectEditCommand(relation, newStr, Relation.TARGET_CONSTRAINT);
			}
		}
		return null;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest arg0) {
		// TODO Auto-generated method stub

	}


}
