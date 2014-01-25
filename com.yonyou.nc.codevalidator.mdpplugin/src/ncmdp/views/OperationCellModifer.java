package ncmdp.views;

import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.Type;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.tool.NCMDPTool;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

public class OperationCellModifer implements ICellModifier {
	public static final String[] colNames = { "", Messages.OperationCellModifer_1, Messages.OperationCellModifer_2, Messages.OperationCellModifer_3, Messages.OperationCellModifer_4, //$NON-NLS-1$
			Messages.OperationCellModifer_5, Messages.OperationCellModifer_6, Messages.OperationCellModifer_7, Messages.OperationCellModifer_8, Messages.OperationCellModifer_9, Messages.OperationCellModifer_10, Messages.OperationCellModifer_11 ,Messages.OperationCellModifer_12,"resid"}; //$NON-NLS-9$

	private class OperationModifyCommand extends Command {
		private Cell cell = null;
		private Operation oper = null;
		private String property = null;
		private Object oldValue = null;
		private Object newValue = null;

		public OperationModifyCommand(Cell cell, Operation oper,
				String property, Object newValue) {
			super();
			this.cell = cell;
			this.oper = oper;
			this.property = property;
			this.newValue = newValue;
		}

		@Override
		public void execute() {
			oldValue = getValue(oper, property);
			redo();
		}

		@Override
		public void redo() {
			modifyOperation(cell, oper, property, newValue);
		}

		@Override
		public void undo() {
			modifyOperation(cell, oper, property, oldValue);
		}

	}

	public boolean canModify(Object element, String property) {
		if (element instanceof ArrayList)
			return false;
		else
			return true;
	}

	public Object getValue(Object element, String property) {
		if (element instanceof Operation) {
			Operation oper = (Operation) element;
			if (colNames[1].equals(property)) {
				return oper.getName() == null ? "" : oper.getName(); //$NON-NLS-1$
			} else if (colNames[2].equals(property)) {
				return oper.getDisplayName() == null ? "" : oper //$NON-NLS-1$
						.getDisplayName();
			} else if (colNames[3].equals(property)) {
				return oper.getTypeStyle();
			} else if (colNames[4].equals(property)) {
				return oper.getReturnType();
			}
			else if (colNames[5].equals(property)) {
				return oper.isAggVOReturn();
			}
			else if (colNames[6].equals(property)) {
				return oper.getDefClassName() == null ? "" : oper //$NON-NLS-1$
						.getDefClassName();
			} else if (colNames[7].equals(property)) {
				String visi = oper.getVisibility();
				return NCMDPTool.getVisibilityIndex(visi);
			} else if (colNames[8].equals(property)) {
				String trans = oper.getTransKind();
				for (int i = 0; i < Constant.TRANSKINDS.length; i++) {
					if (Constant.TRANSKINDS[i].equals(trans)) {
						return Integer.valueOf(i);
					}
				}
				return Integer.valueOf(0);
			} else if (colNames[9].equals(property)) {
				return oper.isBusiActivity();
			} else if (colNames[10].equals(property)) {
				return oper.getDescription() == null ? "" : oper //$NON-NLS-1$
						.getDescription();
			} else if (colNames[11].equals(property)) {
				return oper.getHelp() == null ? "" : oper.getHelp(); //$NON-NLS-1$
			} else if (colNames[12].equals(property)) {
				return oper.getMethodException() == null ? "" : oper.getMethodException(); //$NON-NLS-1$
			}else if (colNames[13].equals(property)) {
				return oper.getResid() == null ? "" : oper.getResid(); //$NON-NLS-1$
			}
		}
		return null;
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem) element;
		Object o = item.getData();
		if (o instanceof Operation) {
			Operation oper = (Operation) o;
			Object old = getValue(oper, property);
			if (old != null && old.equals(value)) {
				return;
			}
			Cell cell = (Cell) view.getCellPart().getModel();
			OperationModifyCommand cmd = new OperationModifyCommand(cell, oper,
					property, value);
			NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
			if (editor != null)
				editor.executComand(cmd);

		}

	}

	private OperationView view = null;

	public OperationCellModifer(OperationView view) {
		super();
		this.view = view;
	}

	private void modifyOperation(Cell cell, Operation oper, String property,
			Object value) {

		if (colNames[1].equals(property)) {
			oper.setName((String) value);
		} else if (colNames[2].equals(property)) {
			oper.setDisplayName((String) value);
		} else if (colNames[3].equals(property)) {
			oper.setTypeStyle((String) value);
		} else if (colNames[4].equals(property)) {
			oper.setReturnType((Type) value);
		} else if (colNames[5].equals(property)) {
			oper.setAggVOReturn((Boolean) value);
		} else if (colNames[6].equals(property)) {
			oper.setDefClassName((String) value);
		} else if (colNames[7].equals(property)) {
			Integer i = (Integer) value;
			oper.setVisibility(Constant.VISIBILITYS[i.intValue()]);
		} else if (colNames[8].equals(property) && cell instanceof OpInterface) {
			Integer i = (Integer) value;
			oper.setTransKind(Constant.TRANSKINDS[i.intValue()]);
		}  else if (colNames[9].equals(property)) {
			oper.setBusiActivity((Boolean) value);
		} else if (colNames[10].equals(property)) {
			oper.setDescription((String) value);
		} else if (colNames[11].equals(property)) {
			oper.setHelp((String) value);
		} else if (colNames[12].equals(property)) {
			oper.setMethodException((String) value);
		}else if (colNames[13].equals(property)) {
			oper.setResid((String) value);
		}
		view.getTreeView().refresh(oper);
		if (cell instanceof ValueObject) {
			((ValueObject) cell).fireOperationUpdate(oper);
		} else if (cell instanceof OpInterface) {
			((OpInterface) cell).fireOperationUpdate(oper);
		}

	}
}
