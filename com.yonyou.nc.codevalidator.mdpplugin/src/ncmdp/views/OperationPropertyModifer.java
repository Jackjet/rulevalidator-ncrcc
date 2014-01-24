package ncmdp.views;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Constant;
import ncmdp.model.property.XMLAttribute;
import ncmdp.model.property.XMLElement;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

public class OperationPropertyModifer implements ICellModifier {

	private class OperationPropertyModifiCommand extends Command {
		private Object data = null;

		private String property = null;

		private Object value = null;

		private Object oldValue = null;

		public OperationPropertyModifiCommand(Object data, String property, Object value) {
			super();
			this.data = data;
			this.property = property;
			this.value = value;
		}

		@Override
		public void execute() {
			oldValue = getValue(data, property);
			redo();
		}

		@Override
		public void redo() {
			modify0(data, property, value);
		}

		@Override
		public void undo() {
			modify0(data, property, oldValue);
		}

	}

	public static String[] colNames = { "", Messages.OperationPropertyModifer_1 }; //$NON-NLS-1$

	private OperationPropertyView view = null;

	public OperationPropertyModifer(OperationPropertyView view) {
		super();
		this.view = view;
		// TODO Auto-generated constructor stub
	}

	public boolean canModify(Object element, String property) {
		if (property.equals(colNames[1])) {
			return shouldModify(element);
			// if (element instanceof XMLElement) {
			// String type = ((XMLElement) element).getElementType();
			// if (type.equalsIgnoreCase(Constant.XML_TYPE_REF) ||
			// type.equalsIgnoreCase(Constant.XML_TYPE_VALUE)) {
			// return true;
			// } else {
			// return false;
			// }
			// }else if(element instanceof XMLAttribute){
			// return true;
			// }else{
			// return false;
			// }
		}
		return false;
	}

	public static boolean shouldModify(Object element) {
		if (element instanceof XMLElement) {
			String type = ((XMLElement) element).getElementType();
			if (type.equalsIgnoreCase(Constant.XML_TYPE_REF) || type.equalsIgnoreCase(Constant.XML_TYPE_VALUE)) {
				return true;
			} else {
				return false;
			}
		} else if (element instanceof XMLAttribute) {
			return true;
		} else {
			return false;
		}
	}

	public Object getValue(Object element, String property) {
		if (element instanceof XMLElement) {
			XMLElement ele = (XMLElement) element;
			if (colNames[0].equals(property)) {
				return ele.getElementType();
			} else if (colNames[1].equals(property)) {
				return ele.getText();
			}

		} else if (element instanceof XMLAttribute) {
			XMLAttribute attr = (XMLAttribute) element;
			if (colNames[0].equals(property)) {
				return attr.getKey();
			} else if (colNames[1].equals(property)) {
				return attr.getValue();
			}
		}
		return ""; //$NON-NLS-1$
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem) element;
		Object data = item.getData();
		OperationPropertyModifiCommand cmd = new OperationPropertyModifiCommand(data, property, value);
		if (NCMDPEditor.getActiveMDPEditor() != null)
			NCMDPEditor.getActiveMDPEditor().executComand(cmd);
	}

	public void modify0(Object data, String property, Object value) {
		if (data instanceof XMLElement) {
			XMLElement ele = (XMLElement) data;
			if (colNames[1].equals(property)) {
				ele.setText((String) value);
			}
		} else if (data instanceof XMLAttribute) {
			XMLAttribute attr = (XMLAttribute) data;
			if (colNames[1].equals(property)) {
				attr.setValue((String) value);
			}
		}
		view.getTreeViewer().refresh(data);
	}

}
