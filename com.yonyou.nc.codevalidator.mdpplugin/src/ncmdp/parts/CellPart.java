package ncmdp.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import ncmdp.directedit.CustomCellEditorLocator;
import ncmdp.directedit.MuliLineTextCellDirectEditManager;
import ncmdp.directedit.TextCellDirectEidtManager;
import ncmdp.editor.NCMDPEditor;
import ncmdp.figures.BusiActivityFigure;
import ncmdp.figures.BusiItfFigure;
import ncmdp.figures.BusiOperationFigure;
import ncmdp.figures.CellFigure;
import ncmdp.figures.EntityFigure;
import ncmdp.figures.EnumFigure;
import ncmdp.figures.NoteFigure;
import ncmdp.figures.OpItfFigure;
import ncmdp.figures.ReferFigure;
import ncmdp.figures.ServiceFigure;
import ncmdp.figures.ValueObjectFigure;
import ncmdp.figures.ui.AttributeLabel;
import ncmdp.figures.ui.BusiItfAttrLabel;
import ncmdp.figures.ui.IDirectEditable;
import ncmdp.figures.ui.NoteLabel;
import ncmdp.figures.ui.OperationLable;
import ncmdp.model.Attribute;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.model.Cell;
import ncmdp.model.Connection;
import ncmdp.model.Entity;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;
import ncmdp.model.Note;
import ncmdp.model.Reference;
import ncmdp.model.Service;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.model.activity.RefBusiOperation;
import ncmdp.model.activity.RefOperation;
import ncmdp.policy.CellComponentEditPolicy;
import ncmdp.policy.CellDirectEditPolicy;
import ncmdp.policy.CellSelectionPolicy;
import ncmdp.policy.RelationGraphicalNodeEditPolicy;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.request.CtrlMouseUpRequest;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.tree.mdptree.MDPCellTreeItem;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;
import ncmdp.ui.tree.mdptree.RefMDFileDirTreeItem;
import ncmdp.util.ProjectUtil;
import ncmdp.views.NCMDPViewPage;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

public class CellPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {
	/**
	 * create figures to be used as this part's visuals
	 */
	@Override
	protected IFigure createFigure() {
		Object model = getModel();
		IFigure figure = null;
		if (model instanceof Cell) {
			Cell cell = (Cell) model;
			figure = getFigureByModel(cell);
		}
		return figure;
	}

	public static IFigure getFigureByModel(Cell cell) {
		CellFigure figure = null;
		if (cell instanceof Reference) {//引用类型
			Reference ref = (Reference) cell;
			figure = new ReferFigure(ref);
		} else if (cell instanceof Entity) {//实体
			Entity entity = (Entity) cell;
			figure = new EntityFigure(entity);
		} else if (cell instanceof ValueObject) {
			ValueObject vo = (ValueObject) cell;
			figure = new ValueObjectFigure(vo);
		} else if (cell instanceof Service) {//暂不提供
			Service service = (Service) cell;
			figure = new ServiceFigure(service);
		} else if (cell instanceof OpInterface) {
			OpInterface bo = (OpInterface) cell;
			figure = new OpItfFigure(bo);
		} else if (cell instanceof BusiOperation) {
			BusiOperation bo = (BusiOperation) cell;
			figure = new BusiOperationFigure(bo);
		} else if (cell instanceof BusiActivity) {
			BusiActivity bo = (BusiActivity) cell;
			figure = new BusiActivityFigure(bo);
		} else if (cell instanceof Enumerate) {//枚举
			figure = new EnumFigure((Enumerate) cell);
		} else if (cell instanceof Note) {//注释
			figure = new NoteFigure((Note) cell);
		} else if (cell instanceof BusinInterface) {//业务接口
			figure = new BusiItfFigure((BusinInterface) cell);
		} else if (cell instanceof BusiActivity) {
			figure = new BusiActivityFigure((BusiActivity) cell);
		}
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new CellComponentEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new RelationGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CellDirectEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new CellSelectionPolicy());

	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (Cell.PROP_CELL_SIZE.equals(name)
				|| Cell.PROP_CELL_LOCATION.equals(name)) {
			refreshVisuals();
		} else if (ValueObject.PROP_ADD_CELL_PROPS.equals(name)) {
			Attribute attri = (Attribute) event.getNewValue();
			((ValueObjectFigure) getFigure()).addAttribute(attri);
		} else if (ValueObject.PROP_UPDATE_CELL_PROPS.equals(name)) {
			Attribute attri = (Attribute) event.getNewValue();
			((ValueObjectFigure) getFigure()).updateAttribute(attri);
		} else if (ValueObject.PROP_REMOVE_CELL_PROPS.equals(name)) {
			Attribute attri = (Attribute) event.getNewValue();
			((ValueObjectFigure) getFigure()).removeAttribute(attri);
		} else if (Cell.PROP_SOURCE_CONNECTION.equals(name)) {
			refreshSourceConnections();
		} else if (Cell.PROP_TARGET_CONNECTION.equals(name)) {
			refreshTargetConnections();
		} else if (Cell.PROP_ELEMENT_DISPLAY_NAME.equals(name)) 
		{  ((CellFigure) getFigure()).setLblTextById(name,(String) event.getNewValue());
		} else if (ValueObject.ADD_OPERATION.equals(name)) {
			Operation operation = (Operation) event.getNewValue();
			((ValueObjectFigure) getFigure()).addOperation(operation);
		} else if (ValueObject.REMOVE_OPERATION.equals(name)) {
			Operation operation = (Operation) event.getNewValue();
			((ValueObjectFigure) getFigure()).removeOperation(operation);
		} else if (ValueObject.UPDATE_OPERATION.equals(name)) {
			Operation operation = (Operation) event.getNewValue();
			((ValueObjectFigure) getFigure()).updateOperation(operation);
		} else if (OpInterface.OPERATION_ADD.equals(name)) {
			Operation operation = (Operation) event.getNewValue();
			((OpItfFigure) getFigure()).addOperation(operation);
		} else if (OpInterface.OPERATION_REMOVE.equals(name)) {
			Operation operation = (Operation) event.getNewValue();
			((OpItfFigure) getFigure()).removeOperation(operation);
		} else if (OpInterface.OPERATION_UPDATE.equals(name)) {
			Operation operation = (Operation) event.getNewValue();
			((OpItfFigure) getFigure()).updateOperation(operation);
		} else if (BusiOperation.OPERATION_ADD.equals(name)) {
			RefOperation operation = (RefOperation) event.getNewValue();
			((BusiOperationFigure) getFigure()).addOperation(operation);
		} else if (BusiOperation.OPERATION_REMOVE.equals(name)) {
			RefOperation operation = (RefOperation) event.getNewValue();
			((BusiOperationFigure) getFigure()).removeOperation(operation);
		} else if (BusiActivity.BUSIOPERATION_ADD.equals(name)) {
			RefBusiOperation operation = (RefBusiOperation) event.getNewValue();
			((BusiActivityFigure) getFigure()).addOperation(operation);
		} else if (BusiActivity.BUSIOPERATION_REMOVE.equals(name)) {
			RefBusiOperation operation = (RefBusiOperation) event.getNewValue();
			((BusiActivityFigure) getFigure()).removeOperation(operation);
		} else if (BusiActivity.BUSIOPERATION_UPDATE.equals(name)) {
			RefBusiOperation operation = (RefBusiOperation) event.getNewValue();
			((BusiActivityFigure) getFigure()).updateOperation(operation);
		} else if (Enumerate.ENUMITEM_ADD.equals(name)) {
			EnumItem ei = (EnumItem) event.getNewValue();
			((EnumFigure) getFigure()).addEnumItem(ei);
			NCMDPViewSheet.getNCMDPViewPage().getEnumItemView().refresh();
		} else if (Enumerate.ENUMITEM_REMOVE.equals(name)) {
			EnumItem ei = (EnumItem) event.getNewValue();
			((EnumFigure) getFigure()).removeEnumItem(ei);
			NCMDPViewSheet.getNCMDPViewPage().getEnumItemView().refresh();
		} else if (Enumerate.ENUMITEM_UPDATE.equals(name)) {
			EnumItem ei = (EnumItem) event.getNewValue();
			((EnumFigure) getFigure()).updateEnumItem(ei);
			NCMDPViewSheet.getNCMDPViewPage().getEnumItemView()
					.getEnumItemTableViewer().refresh(ei);
		} else if (Note.PROP_REMARK.equals(name)) {
			((NoteFigure) getFigure()).updateRemark((String) event
					.getNewValue());
		} else if (BusinInterface.PROP_ADD_BUSIATTR.equals(name)) {
			BusiItfAttr attr = (BusiItfAttr) event.getNewValue();
			((BusiItfFigure) getFigure()).addBusiItfAttr(attr);
		} else if (BusinInterface.PROP_REMOVE_BUSIATTR.equals(name)) {
			BusiItfAttr attr = (BusiItfAttr) event.getNewValue();
			((BusiItfFigure) getFigure()).removeBusiItfAttr(attr);
		} else if (BusinInterface.PROP_UPDATE_BUSIATTR.equals(name)) {
			BusiItfAttr attr = (BusiItfAttr) event.getNewValue();
			((BusiItfFigure) getFigure()).updateBusiItfAttr(attr);
		} else if (ValueObject.ADD_BUSIITF.equals(name)
				|| ValueObject.REMOVE_BUSIITF.equals(name)) {
			NCMDPViewSheet.getNCMDPViewPage().getBusinessItfAttrsMapView()
					.getTreeViewer().refresh();
		} else if (Entity.PROP_KEY_ATTRIBUTE.equals(name)) {
			Attribute attr = (Attribute) event.getNewValue();
			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv()
					.refresh(attr);
		} else if (Entity.PROP_UPDATE_ALL_ATTR.equals(name)) {
			((EntityFigure) getFigure()).updateAllAttribute();
			NCMDPViewPage page = NCMDPViewSheet.getNCMDPViewPage();
			if (page != null) {
				page.getCellPropertiesView().getTv().refresh();
			}
		}
		Cell cell = (Cell) getModel();
		String validate = cell.validate();
		CellFigure cellFigure = (CellFigure) getFigure();
		cellFigure.markError(validate);
//		MDPExplorerTreeView explorer = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView explorer = ProjectUtil.openMDPExplorer();
		if (explorer != null) {
			explorer.updateCellNode(cell);
		}
	}

	@Override
	public void activate() {
		super.activate();
		((Cell) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((Cell) getModel()).removePropertyChangeListener(this);
	}

	@Override
	protected void refreshVisuals() {
		Cell cell = (Cell) getModel();
		Rectangle rect = new Rectangle(cell.getLocation(), cell.getSize());

		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), rect);

	}

	@Override
	public void performRequest(Request request) {
		if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType())) {
			performDirectEdit((DirectEditRequest) request);
		} else if (RequestConstants.REQ_OPEN.equals(request.getType())
				|| request instanceof SelectionRequest) {
			SelectionRequest sr = (SelectionRequest) request;
			Point p = sr.getLocation();
			CellFigure fig = (CellFigure) getFigure();
			Label lbl = fig.getLblByPoint(p);

			if (fig.isTypeLabel(lbl)) {
				NCMDPTool.showErrDlg(fig.getErrStr());
			} else if (lbl instanceof AttributeLabel) {
				NCMDPTool.showErrDlg(((AttributeLabel) lbl).getErrStr());
			} else if (lbl instanceof OperationLable) {
				NCMDPTool.showErrDlg(((OperationLable) lbl).getErrStr());
			} else if (lbl instanceof BusiItfAttrLabel) {
				NCMDPTool.showErrDlg(((BusiItfAttrLabel) lbl).getErrStr());
			}
		} else if (request instanceof CtrlMouseUpRequest) {
			openReferedCell();
		} else {
			super.performRequest(request);
		}
	}

	private void openReferedCell() {
		Reference refer = (Reference) getModel();
		File f = refer.getReferencedCellSourceFile();
		if (f != null && f.exists()) {
//			MDPExplorerTreeView treeView = MDPExplorerTreeView
//					.getMDPExploerTreeView(PlatformUI.getWorkbench()
//							.getActiveWorkbenchWindow().getActivePage());
//			TreeItem item = treeView.findMDFileTreeItem(f, true);
			MDPExplorerTreeView treeView = ProjectUtil.openMDPExplorer();
			TreeItem item = ProjectUtil.findMDFileTreeItem(f, true);
			// treeView.getExplorerTree().setSelection(item);
			TreeItem selItem = item;
			if (item instanceof MDPFileTreeItem) {
				MDPFileTreeItem fileItem = (MDPFileTreeItem) item;
				treeView.openEditorTemp(fileItem);
				Cell cell = refer.getReferencedCell();
				if (cell != null && NCMDPEditor.getActiveMDPEditor() != null) {
					NCMDPEditor.getActiveMDPEditor().setSelect(cell);
				}
			} else if (item instanceof RefMDFileDirTreeItem) {
				RefMDFileDirTreeItem refmdfileItem = (RefMDFileDirTreeItem) item;
				treeView.openRefFileInEditor(refmdfileItem);
				Cell cell = refer.getReferencedCell();
				if (cell != null && NCMDPEditor.getActiveMDPEditor() != null) {
					NCMDPEditor.getActiveMDPEditor().setSelect(cell);
				}
			}
			String refId = refer.getRefId();
			int count = item.getItemCount();
			for (int i = 0; i < count; i++) {
				TreeItem tmp = item.getItem(i);
				Cell cell = null;
				if (tmp instanceof RefMDCellTreeItem) {
					cell = ((RefMDCellTreeItem) tmp).getCell();
				} else if (tmp instanceof MDPCellTreeItem) {
					cell = ((MDPCellTreeItem) tmp).getCell();
				}
				if (cell != null && cell.getId().equals(refId)) {
					selItem = tmp;
					break;
				}
			}
			treeView.getExplorerTree().setSelection(selItem);

		} else {
			MessageDialog.openInformation(
					Display.getCurrent().getActiveShell(), "info",
					"The referenced MDFile lost:" + refer.getMdFilePath());// 引用的模型文件不存在:
		}
	}

	private void performDirectEdit(DirectEditRequest request) {
		if (getModel() instanceof Reference) {
			return;
		}
		Point p = request.getLocation();
		IDirectEditable lbl = ((CellFigure) getFigure())
				.getIDirectEditableByPoint(p);
		if (lbl != null) {

			DirectEditManager manager = null;
			if (lbl instanceof NoteLabel) {
				manager = new MuliLineTextCellDirectEditManager(this,
						new CustomCellEditorLocator((IFigure) lbl), lbl);
			} else {
				manager = new TextCellDirectEidtManager(this,
						new CustomCellEditorLocator((IFigure) lbl), lbl);
			}
			if (manager != null)
				manager.show();
		}
	}

	// //////////////////////////
	private ConnectionAnchor anchor;

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			anchor = new ChopboxAnchor(getFigure());
		}
		return anchor;
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart arg0) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request arg0) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return getConnectionAnchor();
	}

	protected List<Connection> getModelSourceConnections() {
		return ((Cell) this.getModel()).getSourceConnections();
	}

	protected List<Connection> getModelTargetConnections() {
		return ((Cell) this.getModel()).getTargetConnections();
	}
}
