package ncmdp.views;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.CanZhao;
import ncmdp.model.ValueObject;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;
/**
 * 参照视图的内容修改
 * @author wangxmn
 *
 */
public class CanzhaoCellModifer implements ICellModifier {
	public class CanzhaoModiferCommand extends Command {
		private ValueObject vo = null;

		private CanZhao cz = null;

		private String property = null;

		private Object value = null;

		private Object oldValue = null;

		public CanzhaoModiferCommand(ValueObject vo, CanZhao cz, String property, Object value) {
			super();
			this.vo = vo;
			this.cz = cz;
			this.property = property;
			this.value = value;
		}

		@Override
		public void execute() {
			oldValue = getValue(cz, property);
			redo();
		}

		@Override
		public void redo() {
			modify0(vo, cz, property, value);
		}

		@Override
		public void undo() {
			modify0(vo, cz, property, oldValue);
		}
	}

	public static final String[] colNames = { "", Messages.CanzhaoCellModifer_1, Messages.CanzhaoCellModifer_2 }; 

	private CanzhaoView view = null;

	public CanzhaoCellModifer(CanzhaoView view) {
		super();
		this.view = view;
	}

	public boolean canModify(Object element, String property) {
		if (property.equals(colNames[2]))
			return true;
		return false;
	}

	public Object getValue(Object element, String property) {
		CanZhao cz = (CanZhao) element;
		if (colNames[1].equals(property)) {
			return cz.getName();
		} else if (colNames[2].equals(property)) {
			return cz.isDefault();
		}
		return ""; 
	}

	public void modify(Object element, String property, Object value) {
		TreeItem ti = (TreeItem) element;
		Object obj = ti.getData();
		if (obj instanceof CanZhao) {
			ValueObject vo = (ValueObject) view.getCellPart().getModel();
			CanZhao cz = (CanZhao) obj;
			CanzhaoModiferCommand cmd = new CanzhaoModiferCommand(vo, cz, property, value);
			if(NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}

	private void modify0(ValueObject vo, CanZhao cz, String property, Object value) {
		if (colNames[2].equals(property)) {
			boolean isDefault = ((Boolean) value).booleanValue();
			cz.setDefault((Boolean) value);
			if (isDefault)
				vo.setDefaultCZ(cz);
		}
		view.getTreeViewer().refresh();
	}
}
