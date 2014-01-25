package ncmdp.command;

import ncmdp.model.Attribute;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.model.Cell;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;
import ncmdp.model.Note;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.views.BusinessInterfaceAttrsView;
import ncmdp.views.CellPropertiesView;
import ncmdp.views.EnumItemView;
import ncmdp.views.NCMDPViewPage;
import ncmdp.views.NCMDPViewSheet;
import ncmdp.views.OperationView;

import org.eclipse.gef.commands.Command;

public class DirectEditCommand extends Command {
	private Cell cell = null;

	private String oldValue = null;

	private Object newValue = null;

	private Object propId = null;

	public DirectEditCommand(Cell cell, Object newValue, Object propId) {
		super();
		this.cell = cell;
		this.newValue = newValue;
		this.propId = propId;
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();
	}

	@Override
	public void execute() {
		if (cell instanceof Note) {
			oldValue = ((Note) cell).getRemark();
		} else if (Cell.PROP_ELEMENT_DISPLAY_NAME.equals(propId)) {
			oldValue = cell.getDisplayName();
		} else if (propId != null && propId instanceof Attribute) {
			oldValue = ((Attribute) propId).getDisplayName();
		} else if (propId != null && propId instanceof Operation) {
			oldValue = ((Operation) propId).getDisplayName();
		} else if (propId != null && propId instanceof EnumItem) {
			oldValue = ((EnumItem) propId).getEnumDisplay();
		} else if (propId != null && propId instanceof BusiItfAttr) {
			oldValue = ((BusiItfAttr) propId).getDisplayName();
		}
		redo();
	}

	@Override
	public void redo() {
		if (cell instanceof Note) {
			((Note) cell).setRemark((String) newValue);
		} else if (Cell.PROP_ELEMENT_DISPLAY_NAME.equals(propId)) {
			cell.setDisplayName((String) newValue);
		} else if (propId != null && propId instanceof Attribute) {
			Attribute prop = (Attribute) propId;
			prop.setDisplayName((String) newValue);
			((ValueObject) cell).firePropUpdate(prop);
			//			CellPropertiesView.getPropertiesView().getTv().refresh(prop);
			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv().refresh(prop);
		} else if (propId != null && propId instanceof Operation) {
			Operation oper = (Operation) propId;
			oper.setDisplayName((String) newValue);
			if (cell instanceof ValueObject) {
				((ValueObject) cell).fireOperationUpdate(oper);
			} else if (cell instanceof OpInterface) {
				((OpInterface) cell).fireOperationUpdate(oper);
			}
			//			OperationView.getOperationView().getTreeView().refresh(oper);
			NCMDPViewSheet.getNCMDPViewPage().getOperationView().getTreeView().refresh(oper);

		} else if (propId != null && propId instanceof EnumItem) {
			EnumItem item = (EnumItem) propId;
			item.setEnumDisplay((String) newValue);
			((Enumerate) cell).fireEnumItemUpdate(item);
			//			EnumItemView.getEnumItemView().getEnumItemTableViewer().refresh(item);
			NCMDPViewSheet.getNCMDPViewPage().getEnumItemView().getEnumItemTableViewer().refresh(item);
		} else if (propId != null && propId instanceof BusiItfAttr) {
			BusiItfAttr attr = (BusiItfAttr) propId;
			attr.setDisplayName((String) newValue);
			((BusinInterface) cell).fireUpdateBusiAttr(attr);
			BusinessInterfaceAttrsView.refresh(attr);
		}
	}

	@Override
	public void undo() {
		if (cell instanceof Note) {
			((Note) cell).setRemark(oldValue);
		} else if (Cell.PROP_ELEMENT_DISPLAY_NAME.equals(propId)) {
			cell.setDisplayName(oldValue);
		} else if (propId != null && propId instanceof Attribute) {
			Attribute prop = (Attribute) propId;
			prop.setDisplayName(oldValue);
			((ValueObject) cell).firePropUpdate(prop);
			//			CellPropertiesView.getPropertiesView().getTv().refresh(prop);
			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv().refresh(prop);
		} else if (propId != null && propId instanceof Operation) {
			Operation oper = (Operation) propId;
			oper.setDisplayName(oldValue);
			if (cell instanceof ValueObject) {
				((ValueObject) cell).fireOperationUpdate(oper);
			} else if (cell instanceof OpInterface) {
				((OpInterface) cell).fireOperationUpdate(oper);
			}
			//			OperationView.getOperationView().getTreeView().refresh(oper);
			NCMDPViewSheet.getNCMDPViewPage().getOperationView().getTreeView().refresh(oper);
		} else if (propId != null && propId instanceof EnumItem) {
			EnumItem item = (EnumItem) propId;
			item.setEnumDisplay(oldValue);
			((Enumerate) cell).fireEnumItemUpdate(item);
			//			EnumItemView.getEnumItemView().getEnumItemTableViewer().refresh(item);
			NCMDPViewSheet.getNCMDPViewPage().getEnumItemView().getEnumItemTableViewer().refresh(item);
		} else if (propId != null && propId instanceof BusiItfAttr) {
			BusiItfAttr attr = (BusiItfAttr) propId;
			attr.setDisplayName((String) oldValue);
			((BusinInterface) cell).fireUpdateBusiAttr(attr);
			BusinessInterfaceAttrsView.refresh(attr);
			//			BusinessInterfaceAttrsView.getBusiItfAttrsView().getTreeViewer().refresh(attr);
		}

	}

}
