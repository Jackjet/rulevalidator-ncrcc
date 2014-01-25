package ncmdp.policy;

import java.util.ArrayList;
import java.util.List;

import ncmdp.command.CellConstraintUpdateCommand;
import ncmdp.command.CellCreateCommand;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.parts.CellPart;
import ncmdp.ruler.Guide;
import ncmdp.ruler.GuideChangeCommand;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.RelativeHandleLocator;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.rulers.RulerProvider;
/**
 * 创建command，
 * @author wangxmn
 *
 */
public class JGraphXYLayoutEditPolicy extends XYLayoutEditPolicy {

	private class MyResizeHandle extends ResizeHandle{
		public MyResizeHandle(GraphicalEditPart owner, int direction) {
			super(owner, direction);
			final int d = direction;
			setLocator(new RelativeHandleLocator(owner.getFigure(), direction){
			    public void relocate(IFigure target)
			    {
			    	int dv = Constant.CELL_CORNER_SIZE *getZoom() / 4;
			    	super.relocate(target);
			    	Rectangle rect = target.getBounds();
			    	switch(d){
			    	case PositionConstants.NORTH_EAST:
			    		rect.x -= dv;
			    		rect.y += dv;
			    		break;
			    	case PositionConstants.NORTH_WEST:
			    		rect.x += dv;
			    		rect.y +=dv;
			    		break;
			    	case PositionConstants.SOUTH_EAST:
			    		rect.x-=dv;
			    		rect.y -=dv;
			    		break;
			    	case PositionConstants.SOUTH_WEST:
			    		rect.x +=dv;
			    		rect.y -=dv;
			    		break;
//			    	default:
//			    		super.relocate(target);
			    	}
			    	target.setBounds(rect);
			    }
			});
		}
		
	}
	private class RoundResizableEditPolicy extends ResizableEditPolicy{
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected List createSelectionHandles() {
			
			List list = new ArrayList();
			GraphicalEditPart part=(GraphicalEditPart)getHost();
			list.add(new MoveHandle(part){
			    protected void initialize()
			    {
			       super.initialize();
			        setBorder(new LineBorder(1){
			            public void paint(IFigure figure, Graphics graphics, Insets insets)
			            {
			            	int corSize = Constant.CELL_CORNER_SIZE*getZoom();
			                tempRect.setBounds(getPaintRectangle(figure, insets));
			                if(getWidth() % 2 == 1)
			                {
			                    tempRect.width--;
			                    tempRect.height--;
			                }
			                tempRect.shrink(getWidth() / 2, getWidth() / 2);
			                graphics.setLineWidth(getWidth());
			                if(getColor() != null)
			                    graphics.setForegroundColor(getColor());
			                graphics.drawRoundRectangle(tempRect,corSize,corSize);
			            }
			        });
			    }

			});
			list.add(new MyResizeHandle(part, 16));
			list.add(new MyResizeHandle(part, 20));
			list.add(new MyResizeHandle(part,  4));
			list.add(new MyResizeHandle(part, 12));
			list.add(new MyResizeHandle(part,  8));
			list.add(new MyResizeHandle(part,  9));
			list.add(new MyResizeHandle(part,  1));
			list.add(new MyResizeHandle(part, 17));
			return list;
		}

		@Override
		protected IFigure createDragSourceFeedbackFigure() {
	        RoundedRectangle r = new RoundedRectangle();
	        FigureUtilities.makeGhostShape(r);
	        r.setLineStyle(3);
	        int corSize = Constant.CELL_CORNER_SIZE*getZoom();
	        r.setCornerDimensions(new Dimension(corSize,corSize));
	        r.setForegroundColor(ColorConstants.white);
	        r.setBounds(getInitialFeedbackBounds());
	        addFeedback(r);
	        return r;
		}
		
	}
	private int getZoom(){
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if(editor != null){
			return (int)((ScalableFreeformRootEditPart)editor.getGV().getRootEditPart()).getZoomManager().getZoom();
		}else{
			return 1;
		}
	}
	protected Command chainGuideDetachmentCommand(Request request, Cell cell,
			Command cmd, boolean horizontal) {
		Command result = cmd;
		Integer guidePos = (Integer)request.getExtendedData()
				.get(horizontal ? SnapToGuides.KEY_HORIZONTAL_GUIDE
				                : SnapToGuides.KEY_VERTICAL_GUIDE);
		if (guidePos == null)
			result = result.chain(new GuideChangeCommand(cell, horizontal));

		return result;
	}
	protected Command chainGuideAttachmentCommand(
			Request request, Cell cell, Command cmd, boolean horizontal) {
		Command result = cmd;
		Integer guidePos = (Integer)request.getExtendedData()
				.get(horizontal ? SnapToGuides.KEY_HORIZONTAL_GUIDE
				                : SnapToGuides.KEY_VERTICAL_GUIDE);
		if (guidePos != null) {
			int alignment = ((Integer)request.getExtendedData()
					.get(horizontal ? SnapToGuides.KEY_HORIZONTAL_ANCHOR
					                : SnapToGuides.KEY_VERTICAL_ANCHOR)).intValue();
			GuideChangeCommand gcc = new GuideChangeCommand(cell, horizontal);
			gcc.setNewGuide(findGuideAt(guidePos.intValue(), horizontal), alignment);
			result = result.chain(gcc);
		}

		return result;
	}
	protected Guide findGuideAt(int pos, boolean horizontal) {
		RulerProvider provider = ((RulerProvider)getHost().getViewer().getProperty(
				horizontal ? RulerProvider.PROPERTY_VERTICAL_RULER 
				: RulerProvider.PROPERTY_HORIZONTAL_RULER));
		return (Guide)provider.getGuideAt(pos);
	}
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart editPart, Object constraint) {
		if(editPart instanceof CellPart && constraint instanceof Rectangle){
			Cell cell = (Cell)editPart.getModel();
			Command cmd =new CellConstraintUpdateCommand(cell,(Rectangle) constraint); 
			if((request.getResizeDirection() & PositionConstants.NORTH_SOUTH )!=0){
				Integer guidePos =(Integer) request.getExtendedData().get(SnapToGuides.KEY_HORIZONTAL_GUIDE);
				if(guidePos != null){
					cmd = chainGuideAttachmentCommand(request, cell, cmd, true);
				}else if(cell.getHorizontalGuide() != null){
					int alignment = cell.getHorizontalGuide().getAlignment(cell);
					int edgeBeingResized = 0;
					if ((request.getResizeDirection() & PositionConstants.NORTH) != 0)
						edgeBeingResized = -1;
					else
						edgeBeingResized = 1;
					if (alignment == edgeBeingResized)
						cmd = cmd.chain(new GuideChangeCommand(cell, true));
				}
			}
			if((request.getResizeDirection() & PositionConstants.EAST_WEST ) != 0){
				Integer guidePos = (Integer) request.getExtendedData().get(SnapToGuides.KEY_VERTICAL_GUIDE);
				if(guidePos != null){
					cmd = chainGuideAttachmentCommand(request, cell, cmd, false);
				}else if(cell.getVerticalGuide() != null){
					int alignment = cell.getVerticalGuide().getAlignment(cell);
					int edgeBeingResized = 0;
					if((request.getResizeDirection() & PositionConstants.WEST )!= 0)
						edgeBeingResized = -1;
					else
						edgeBeingResized = 1;
					if(alignment == edgeBeingResized)
						cmd = cmd.chain(new GuideChangeCommand(cell , false));
				}
			}
			if (request.getType().equals(REQ_MOVE_CHILDREN)
					|| request.getType().equals(REQ_ALIGN_CHILDREN)) {
				cmd = chainGuideAttachmentCommand(request, cell, cmd, true);
				cmd = chainGuideAttachmentCommand(request, cell, cmd, false);
				cmd = chainGuideDetachmentCommand(request, cell, cmd, true);
				cmd = chainGuideDetachmentCommand(request, cell, cmd, false);
			}
			return cmd;
		}
		return super.createChangeConstraintCommand(request, editPart, constraint);
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart arg0, Object arg1) {
		return null;
	}

	/**
	 * 新建一个实体，或者枚举等类型触发的方法
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Object obj = request.getNewObject();
		if(Cell.class.isInstance(obj)){
			Cell  cell = (Cell)obj;
			Command cmd = new CellCreateCommand(cell, (JGraph)getHost().getModel(),(Rectangle)getConstraintFor(request));
			cmd = chainGuideAttachmentCommand(request, cell, cmd, true);
			return chainGuideAttachmentCommand(request, cell, cmd, false);
		}
		
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new RoundResizableEditPolicy();
	}

}
