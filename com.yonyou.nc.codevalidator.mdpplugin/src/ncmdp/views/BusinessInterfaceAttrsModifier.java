package ncmdp.views;

import java.util.ArrayList;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.model.Type;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 实体的业务接口属性编辑
 * @author wangxmn
 *
 */
public class BusinessInterfaceAttrsModifier  implements ICellModifier {
	private BusinessInterfaceAttrsView view = null;
	public BusinessInterfaceAttrsModifier(BusinessInterfaceAttrsView view) {
		super();
		this.view = view;
	}

	private class BusiItfAttrsModifyCommand extends Command{
		private String property = ""; 
		private Object value = null;
		private Object oldValue = null;
		private BusiItfAttr attr = null;
		public BusiItfAttrsModifyCommand(String property, Object value, BusiItfAttr attr) {
			super(Messages.BusinessInterfaceAttrsModifier_1);
			this.property = property;
			this.value = value;
			this.attr = attr;
		}
		@Override
		public void execute() {
			oldValue = getValue(attr, property);
			redo();
		}
		@Override
		public void redo() {
			modifyAttr(attr, property, value);
		}
		@Override
		public void undo() {
			modifyAttr(attr, property, oldValue);
		}
	}
	
	public static final String[] colNames = {"", 
		Messages.BusinessInterfaceAttrsModifier_3,Messages.BusinessInterfaceAttrsModifier_4,Messages.BusinessInterfaceAttrsModifier_5,Messages.BusinessInterfaceAttrsModifier_6,Messages.BusinessInterfaceAttrsModifier_7
		};
	
	public boolean canModify(Object element, String property) {
		if(element instanceof ArrayList){
			return false;
		}else if(element instanceof BusiItfAttr){
			return true;
		}
		return false;
	}

	public void modifyAttr(BusiItfAttr attr, String property, Object value) {
		if(colNames[1].equals(property)){
			attr.setName((String)value);
		}else if(colNames[2].equals(property)){
			attr.setDisplayName((String)value);
		}else if(colNames[3].equals(property)){
			attr.setType((Type)value);
		}else if(colNames[4].equals(property)){
			attr.setTypeStyle((String)value);
		}else if(colNames[5].equals(property)){
			attr.setDesc((String)value);
		}
		view.getTreeViewer().refresh(attr);
		((BusinInterface)view.getCellPart().getModel()).fireUpdateBusiAttr(attr);
	}

	public Object getValue(Object element, String property) {
		if(element instanceof BusiItfAttr){
			BusiItfAttr attr = (BusiItfAttr)element;
			if(colNames[1].equals(property)){
				return attr.getName();
			}else if(colNames[2].equals(property)){
				return attr.getDisplayName();
			}else if(colNames[3].equals(property)){
				return attr.getType();
			}else if(colNames[4].equals(property)){
				return attr.getTypeStyle();
			}else if(colNames[5].equals(property)){
				return attr.getDesc();
			}
		}
		return ""; 
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object o = item.getData();
		if(o instanceof BusiItfAttr){
			BusiItfAttr prop = (BusiItfAttr)o;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			BusiItfAttrsModifyCommand cmd = new BusiItfAttrsModifyCommand(property, value, prop);
			NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

}
