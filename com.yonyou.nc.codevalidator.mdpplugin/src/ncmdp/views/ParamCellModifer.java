package ncmdp.views;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.Type;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.model.activity.Parameter;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

public class ParamCellModifer implements ICellModifier {
	private class ParamCellModifyCommand extends Command {
		private Parameter param = null;

		private String property = ""; //$NON-NLS-1$

		private Object value = null;

		private Object old = null;

		public ParamCellModifyCommand(Parameter param, String property,
				Object value) {
			super();
			this.param = param;
			this.property = property;
			this.value = value;
		}

		@Override
		public void execute() {
			old = getValue(param, property);
			redo();
		}

		@Override
		public void redo() {
			modifyParameter(param, property, value);
		}

		@Override
		public void undo() {
			modifyParameter(param, property, old);
		}
	}

	public static final String[] colNames = { Messages.ParamCellModifer_1, Messages.ParamCellModifer_2, Messages.ParamCellModifer_3, Messages.ParamCellModifer_4,
			Messages.ParamCellModifer_5, Messages.ParamCellModifer_6, Messages.ParamCellModifer_7, Messages.ParamCellModifer_8, Messages.ParamCellModifer_9 };

	public boolean canModify(Object element, String property) {
		return true;
	}

	private OperationView view = null;

	public ParamCellModifer(OperationView view) {
		super();
		this.view = view;
	}

	public Object getValue(Object element, String property) {
		Parameter param = (Parameter) element;
		if (colNames[0].equals(property)) {
			return param.getName() == null ? "" : param.getName(); //$NON-NLS-1$
		} else if (colNames[1].equals(property)) {
			return param.getDisplayName() == null ? "" : param.getDisplayName(); //$NON-NLS-1$
		} else if (colNames[2].equals(property)) {
			return param.getParaType();
		} else if (colNames[3].equals(property)) {
			return param.isAggVO();
		} else if (colNames[4].equals(property)) {
			return param.getParamDefClassname() == null ? "" : param //$NON-NLS-1$
					.getParamDefClassname();
		} else if (colNames[5].equals(property)) {
			return param.getTypeStyle();
		} else if (colNames[6].equals(property)) {
			return param.getDesc() == null ? "" : param.getDesc(); //$NON-NLS-1$
		} else if (colNames[7].equals(property)) {
			return param.getHelp() == null ? "" : param.getHelp(); //$NON-NLS-1$
		} else if (colNames[8].equals(property)) {
			return param.getParamTypeForLog() == null ? "" : Integer //$NON-NLS-1$
					.parseInt(param.getParamTypeForLog());
		}

		return ""; //$NON-NLS-1$
	}

	public void modify(Object element, String property, Object value) {
		TableItem ti = (TableItem) element;
		Parameter param = (Parameter) ti.getData();
		Object old = getValue(param, property);
		if (old != null && old.equals(value))
			return;
		ParamCellModifyCommand cmd = new ParamCellModifyCommand(param,
				property, value);
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null)
			editor.executComand(cmd);
	}

	private void modifyParameter(Parameter param, String property, Object value) {
		if (colNames[0].equals(property)) {
			param.setName((String) value);
		} else if (colNames[1].equals(property)) {
			param.setDisplayName((String) value);
		} else if (colNames[2].equals(property)) {
			param.setParaType((Type) value);
		} else if (colNames[3].equals(property)) {
			param.setAggVO((Boolean) value);
		} else if (colNames[4].equals(property)) {
			param.setParamDefClassname((String) value);
		} else if (colNames[5].equals(property)) {
			param.setTypeStyle((String) value);
		} else if (colNames[6].equals(property)) {
			param.setDesc((String) value);
		} else if (colNames[7].equals(property)) {
			param.setHelp((String) value);
		} else if (colNames[8].equals(property)) {
			param.setParamTypeForLog(((Integer) value).toString());
		}

		view.getParamTableView().refresh(param);
		view.getTreeView().refresh();
		TreeItem[] tis = view.getTreeView().getTree().getSelection();
		if (tis != null && tis.length > 0) {
			Operation oper = (Operation) tis[0].getData();
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof ValueObject) {
				((ValueObject) cell).fireOperationUpdate(oper);
			} else if (cell instanceof OpInterface) {
				((OpInterface) cell).fireOperationUpdate(oper);
			}
		}

	}
}
