package ncmdp.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import ncmdp.directedit.ComboBoxCellDirectEditManager;
import ncmdp.directedit.CustomCellEditorLocator;
import ncmdp.directedit.RelationTextCellDirectEditManager;
import ncmdp.figures.AggregationRelationFigure;
import ncmdp.figures.BusiItfConnectionFigure;
import ncmdp.figures.ConnectionFigure;
import ncmdp.figures.ConvergeConnectionFigure;
import ncmdp.figures.DependConnectionFigure;
import ncmdp.figures.ExtendConnectionFigure;
import ncmdp.figures.NoteConnectionFigure;
import ncmdp.figures.RelationFigure;
import ncmdp.model.AggregationRelation;
import ncmdp.model.Attribute;
import ncmdp.model.BusiItfImplConnection;
import ncmdp.model.Connection;
import ncmdp.model.Constant;
import ncmdp.model.ConvergeConnection;
import ncmdp.model.DependConnection;
import ncmdp.model.ExtendConnection;
import ncmdp.model.NoteConnection;
import ncmdp.model.Relation;
import ncmdp.policy.RelationBendpointEditPolicy;
import ncmdp.policy.RelationDirectEditPolicy;
import ncmdp.policy.RelationEditPolicy;
import ncmdp.policy.RelationSelectionPolicy;
import ncmdp.tool.NCMDPTool;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DirectEditManager;

/**
 * 连线part，绘制连线
 * @author wangxmn
 *
 */
public class ConnectionPart extends AbstractConnectionEditPart implements PropertyChangeListener {

	@Override
	protected IFigure createFigure() {
		Connection conn = (Connection) getModel();
		ConnectionFigure conFigure = null;
		if (conn instanceof ExtendConnection) {
			conFigure = new ExtendConnectionFigure();
		} else if (conn instanceof DependConnection) {
			conFigure = new DependConnectionFigure();//依赖关系
		} else if (conn instanceof BusiItfImplConnection) {
			conFigure = new BusiItfConnectionFigure();//实现关系
		} else if (conn instanceof ConvergeConnection) {
			conFigure = new ConvergeConnectionFigure();
		} else if (conn instanceof Relation) {//关联关系
			if (conn instanceof AggregationRelation) {
				conFigure = new AggregationRelationFigure();//聚合
			} else {
				conFigure = new RelationFigure();//关联
			}
			Relation relation = (Relation) conn;
			RelationFigure relationFigure = (RelationFigure) conFigure;
			relationFigure.setSourceConstraintFigureText(relation.getSourceConstraint());
			relationFigure.setTargetConstraintFigureText(relation.getTargetConstraint());

		} else if (conn instanceof NoteConnection) {//注释连接关系
			conFigure = new NoteConnectionFigure();
		}
		conFigure.setPart(this);
		conFigure.setConnName(conn.getDisplayName());
		conFigure.markError(conn.validate());
		return conFigure;
	}

	@Override
	public void performRequest(Request request) {
		if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType())) {
			performDirectEdit((DirectEditRequest) request);
		} else if (RequestConstants.REQ_OPEN.equals(request.getType())
				|| request instanceof SelectionRequest) {
			SelectionRequest sr = (SelectionRequest) request;
			Point p = sr.getLocation();
			ConnectionFigure fig = (ConnectionFigure) getFigure();
			IFigure figure = fig.findFigureAt(p);
			if (figure != null && figure instanceof Label) {
				Label lbl = (Label) fig.findFigureAt(p);
				if (fig.isCnnNameFigure(lbl)) {
					NCMDPTool.showErrDlg(fig.getErrorStr());
				}
			}
		} else {
			super.performRequest(request);
		}
	}

	private void performDirectEdit(DirectEditRequest request) {
		Point p = request.getLocation();
		ConnectionFigure connFigure = (ConnectionFigure) getFigure();
		connFigure.translateToRelative(p);
		IFigure figure = connFigure.findFigureAt(p);
		if (figure != null && figure instanceof Label) {
			Label lbl = (Label) connFigure.findFigureAt(p);
			DirectEditManager manager = null;
			if (connFigure.isCnnNameFigure(lbl)) {
				manager = new RelationTextCellDirectEditManager(this, new CustomCellEditorLocator(lbl), lbl);
			} else {
				manager = new ComboBoxCellDirectEditManager(this, new CustomCellEditorLocator(lbl), lbl,
						Constant.RELATION_MULTI_STRS);

			}
			manager.show();
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new RelationEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new RelationBendpointEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new RelationDirectEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new RelationSelectionPolicy());

	}

	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (Connection.PROP_BEND_POINT.equals(prop)) {
			refreshVisuals();
		} else if (Connection.PROP_ELEMENT_DISPLAY_NAME.equals(prop)) {
			String newName = (String) e.getNewValue();
			((ConnectionFigure) getFigure()).setConnName(newName);
		} else if (Relation.SOURCE_CONSTRAINT.equals(prop)) {
			((RelationFigure) getFigure()).setSourceConstraintFigureText((String) e.getNewValue());
		} else if (Relation.TARGET_CONSTRAINT.equals(prop)) {
			((RelationFigure) getFigure()).setTargetConstraintFigureText((String) e.getNewValue());
		} else if (Relation.PROP_SOURCE_ATTR.equals(prop)) {
			Attribute attr = (Attribute) e.getNewValue();
			if (attr != null) {
				NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().updateCanzhaoCellEditor(attr);
			}
		}

		Connection conn = (Connection) getModel();
		String validate = conn.validate();
		ConnectionFigure conFigure = (ConnectionFigure) getFigure();
		conFigure.markError(validate);
	}

	@Override
	public void refreshVisuals() {
		List<AbsoluteBendpoint> bendPoints = new ArrayList<AbsoluteBendpoint>();
		List<Point> list = ((Connection) getModel()).getBendPoints();
		for (int i = 0; i < list.size(); i++) {
			Point p = list.get(i);

			bendPoints.add(new AbsoluteBendpoint(p));
		}
		getConnectionFigure().setRoutingConstraint(bendPoints);
	}

	@Override
	public void activate() {
		super.activate();
		((Connection) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((Connection) getModel()).removePropertyChangeListener(this);
	}

}
