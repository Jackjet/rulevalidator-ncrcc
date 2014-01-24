package ncmdp.actions;

import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.property.XMLElement;
import ncmdp.views.OperationPropertyView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

public class AddOperationPropertyAction extends Action {
	private class AddOperationPropertyCommand extends Command {
		private Object parentEle = null;

		private XMLElement childEle = null;

		public AddOperationPropertyCommand(Object parentEle, XMLElement childEle) {
			super();
			this.parentEle = parentEle;
			this.childEle = childEle;
		}

		@Override
		public void execute() {
			redo();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void redo() {
			if (parentEle instanceof XMLElement) {
				((XMLElement) parentEle).addXMLElement(childEle);
				view.getTreeViewer().refresh(parentEle);
			} else if (parentEle instanceof ArrayList) {
				((List<XMLElement>) parentEle).add(childEle);
				view.getTreeViewer().refresh();
			}
			view.getTreeViewer().expandAll();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void undo() {
			if (parentEle instanceof XMLElement) {
				((XMLElement) parentEle).removeXMLElement(childEle);
				view.getTreeViewer().refresh(parentEle);
			} else if (parentEle instanceof ArrayList) {
				((List<XMLElement>) parentEle).remove(childEle);
				view.getTreeViewer().refresh();
			}
			view.getTreeViewer().expandAll();
		}

	}

	private OperationPropertyView view = null;

	private String eleType = null;

	public AddOperationPropertyAction(OperationPropertyView view, String text, String eleType) {
		super(text);
		this.view = view;
		this.eleType = eleType;
	}

	@Override
	public void run() {
		TreeViewer tv = view.getTreeViewer();
		IStructuredSelection sel = (IStructuredSelection) tv.getSelection();
		Object parent = sel.getFirstElement();
		XMLElement child = new XMLElement(eleType);
		if (parent != null) {
			AddOperationPropertyCommand cmd = new AddOperationPropertyCommand(parent, child);
			if (NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}

	public String getEleType() {
		return eleType;
	}

}
