package ncmdp.views;

import ncmdp.common.MDPConstants;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.EnumItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import ncmdp.model.Enumerate;
import ncmdp.util.ProjectUtil;

/**
 * 修改枚举类型字段内容的修改器
 * @author wangxmn
 *
 */
public class EnumItemCellModifer implements ICellModifier {
	private class EnumItemUpdateCommand extends Command {
		Object old = ""; 

		Object newValue = ""; 

		EnumItem item = null;

		String property = ""; 

		public EnumItemUpdateCommand(EnumItem item, String property,
				Object newValue) {
			super(Messages.EnumItemCellModifer_3);
			this.item = item;
			this.property = property;
			this.newValue = newValue;
		}

		@Override
		public void execute() {
			old = getValue(item, property);
			String industry = NCMDPEditor.getActiveMDPEditor().getModel()
					.getIndustry().getCode();
			item.setCreateIndustry(industry);
			if (NCMDPEditor.getActiveMDPEditor().getModel().getIndustry() != null
					&& !MDPConstants.BASE_INDUSTRY.equalsIgnoreCase(industry)) {
				item.setIndustryChanged(true);
				item.setModifyIndustry(industry);
//				MDPExplorerTreeView view = MDPExplorerTreeView
//						.getMDPExploerTreeView(null);
//				if (view != null) {
//					view.findCellbyId(item.getClassID()).setIndustryChanged(
//							true);
				ProjectUtil.findCellbyId(item.getClassID()).setIndustryChanged(true);
				ProjectUtil.findCellbyId(item.getClassID()).setModifyIndustry(industry);
//					view.findCellbyId(item.getClassID()).setModifyIndustry(
//							industry);
//				}
				NCMDPEditor.getActiveMDPEditor().getModel()
						.setIndustryChanged(true);
				NCMDPEditor.getActiveMDPEditor().getModel()
						.setModifyIndustry(industry);
			}
			redo();
		}

		@Override
		public void redo() {
			modifyItem(item, property, newValue);//执行时修改为新值
		}

		@Override
		public void undo() {
			modifyItem(item, property, old);//撤销时修改为旧值
		}
	}

	private EnumItemView view = null;

	public EnumItemCellModifer(EnumItemView view) {
		super();
		this.view = view;
	}

	public static final String[] colNames = { Messages.EnumItemCellModifer_4, Messages.EnumItemCellModifer_5, Messages.EnumItemCellModifer_6, Messages.EnumItemCellModifer_7,
			Messages.EnumItemCellModifer_8 };

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		EnumItem item = (EnumItem) element;
		if (colNames[0].equals(property)) {
			return item.getEnumDisplay() == null ? "" : item.getEnumDisplay(); 
		} else if (colNames[1].equals(property)) {
			return item.getEnumValue() == null ? "" : item.getEnumValue(); 
		} else if (colNames[2].equals(property)) {
			return item.getDesc() == null ? "" : item.getDesc(); 
		} else if (colNames[3].equals(property)) {
			return item.getResId() == null ? "" : item.getResId(); 
		} else if (colNames[4].equals(property)) {
			return new Boolean(item.isHidden());
		}
		return ""; 
	}

	public void modify(Object element, String property, Object value) {
		TableItem ti = (TableItem) element;
		EnumItem item = (EnumItem) ti.getData();

		EnumItemUpdateCommand cmd = new EnumItemUpdateCommand(item, property,
				value);
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null)
			editor.executComand(cmd);

	}

	private void modifyItem(EnumItem item, String property, Object value) {
		if (colNames[0].equals(property)) {
			item.setEnumDisplay((String) value);
		} else if (colNames[1].equals(property)) {
			item.setEnumValue((String) value);
		} else if (colNames[2].equals(property)) {
			item.setDesc((String) value);
		} else if (colNames[3].equals(property)) {
			item.setResId((String) value);
		} else if (colNames[4].equals(property)) {
			item.setHidden(((Boolean) value).booleanValue());
		}
		((Enumerate) view.getCellPart().getModel()).fireEnumItemUpdate(item);
	}
}
