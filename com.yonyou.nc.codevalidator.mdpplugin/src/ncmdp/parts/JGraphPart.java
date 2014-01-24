package ncmdp.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.figures.ConnectionFigure;
import ncmdp.figures.JGraphFigure;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.policy.JGraphXYLayoutEditPolicy;
import ncmdp.router.MyManhattanConnectionRouter;
import ncmdp.util.ProjectUtil;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.swt.SWT;

/**
 * 描述编辑器视图部分的外观
 * @author wangxmn
 *
 */
public class JGraphPart extends AbstractGraphicalEditPart implements PropertyChangeListener{
	private NCMDPEditor editor = null;
	public JGraphPart(NCMDPEditor editor) {
		super();
		this.editor = editor;
	}

	@Override
	protected IFigure createFigure() {
		JGraphFigure figure = new JGraphFigure();
		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new BendpointConnectionRouter());
		return figure;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(Class adapter) {
	    if (adapter == SnapToHelper.class) {
	        List snapStrategies = new ArrayList();
	        Boolean val = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
	        if (val != null && val.booleanValue())
	            snapStrategies.add(new SnapToGuides(this));
	        val = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
	        if (val != null && val.booleanValue())
	            snapStrategies.add(new SnapToGeometry(this));
	        val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
	        if (val != null && val.booleanValue())
	            snapStrategies.add(new SnapToGrid(this));
	        if (snapStrategies.size() == 0)
	            return null;
	        if (snapStrategies.size() == 1)
	            return (SnapToHelper)snapStrategies.get(0);
	        SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
	        for (int i = 0; i < snapStrategies.size(); i++)
	            ss[i] = (SnapToHelper)snapStrategies.get(i);
	        return new CompoundSnapToHelper(ss);
	    }
		return super.getAdapter(adapter);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JGraphXYLayoutEditPolicy());
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
	}

	@Override
	public void activate() {
		super.activate();
		((JGraph)getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((JGraph)getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		//新增删除触发事件
		if(JGraph.PROP_CHILD_ADD.equals(name)||JGraph.PROP_CHILD_REMOVE.equals(name)){
			refreshChildren();//会调用getModelChildren方法
//			MDPExplorerTreeView.getMDPExploerTreeView(null).fireJGraphEditor(name, editor,(Cell) event.getNewValue());
			ProjectUtil.openMDPExplorer().fireJGraphEditor(name, editor,(Cell) event.getNewValue());
		}else if(JGraph.CONN_ROUTER_TYPE.equals(name)){
			refreshVisuals();
		}
	}
	
	protected void refreshVisuals() {
		String newConnType = ((JGraph)getModel()).getConRouterType();
		Animation.markBegin();
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
	    if ((getViewer().getControl().getStyle() & SWT.MIRRORED ) == 0)
	        cLayer.setAntialias(SWT.ON);
		if (Constant.CONN_ROUTER_MANHATTAN.equals(newConnType)){
			cLayer.setConnectionRouter(new MyManhattanConnectionRouter());
		}else if(Constant.CONN_ROUTER_SHORTEST_PATH.equals(newConnType)){
			cLayer.setConnectionRouter(new ShortestPathConnectionRouter(getFigure()));
		}else{
			FanRouter router = new FanRouter();
			router.setSeparation(25);
			router.setNextRouter(new BendpointConnectionRouter());
			cLayer.setConnectionRouter(router);

		}
        for(Figure.FigureIterator iter = new Figure.FigureIterator(cLayer); iter.hasNext();)
        {
            IFigure figure = iter.nextFigure();
            if(figure instanceof ConnectionFigure){
            	((ConnectionFigure)figure).getPart().refreshVisuals();
            }
        }
		Animation.run(800);
	}
	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelChildren() {
		return ((JGraph)getModel()).getCells();	
	}

}
