package ncmdp.views;

import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.Entity;
import ncmdp.model.ValueObject;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 映射属性的修改
 * @author wangxmn
 *
 */
public class BusinessItfAttrsMapModifier implements ICellModifier {
	private BusinessItfAttrsMapView view = null;

	public BusinessItfAttrsMapModifier(BusinessItfAttrsMapView view) {
		super();
		this.view = view;
	}

	private class BusinessItfAttrsMapCommand extends Command {
		private BusiItfAttr busiitfattr = null;

		private String property = null;

		private Attribute[] attr = null;

		private Attribute[] oldattr = null;

		private ValueObject vo = null;

		public BusinessItfAttrsMapCommand(BusiItfAttr busiitfattr, String property, Attribute attr[],
				ValueObject vo) {
			super(Messages.BusinessItfAttrsMapModifier_0);
			this.busiitfattr = busiitfattr;
			this.property = property;
			this.attr = attr;
			this.vo = vo;
		}

		@Override
		public void execute() {
			oldattr = vo.getBusiattrAtrrExtendMap(busiitfattr);
			redo();
		}

		@Override
		public void redo() {
			modifyMap(busiitfattr, property, attr, vo);
		}

		@Override
		public void undo() {
			modifyMap(busiitfattr, property, oldattr, vo);
		}
	}

	public static final String[] colNames = { Messages.BusinessItfAttrsMapModifier_1, Messages.BusinessItfAttrsMapModifier_2, Messages.BusinessItfAttrsMapModifier_3, Messages.BusinessItfAttrsMapModifier_4 };

	public boolean canModify(Object element, String property) {
		if (property.equals(colNames[2]) || property.equals(colNames[3])) {
			return true;
		} else {
			return false;
		}
	}

	public Object getValue(Object element, String property) {
		if (property.equals(colNames[2])) {
			if (element instanceof BusiItfAttr) {
				BusiItfAttr attr = (BusiItfAttr) element;
				ValueObject vo = view.getVauleObejct();
				if (vo != null) {
					Attribute[] a = vo.getBusiattrAtrrExtendMap(attr);
					if (a != null) {
						return a[0];
					} else {
						return ""; 
					}
				}
			}
		}
		if (property.equals(colNames[3])) {
			if (element instanceof BusiItfAttr) {
				/**初始化下拉框的数据*/
				BusiItfAttr attr = (BusiItfAttr) element;
				boolean isCombo = initObjectEditor((BusiItfAttr) attr);
				if (!isCombo)
					return null;
				ValueObject vo = view.getVauleObejct();
				if (vo != null) {
					Attribute[] a = vo.getBusiattrAtrrExtendMap(attr);
					if (a != null) {
						return a[1];
					} else {
					}
				}
			}
		}
		return null;
	}

	public boolean initObjectEditor(BusiItfAttr element) {
		BusiItfAttr attr = (BusiItfAttr) element;
		ValueObject vo = view.getVauleObejct();
		if (vo != null) {
			Attribute[] attrs = vo.getBusiattrAtrrExtendMap(attr);
			if (attrs != null && attrs.length > 0 && attrs[0] != null && attrs[0].getType() != null
					&& attrs[0].getType().getCell() != null && attrs[0].getType().getCell() instanceof Entity) {
				List<Attribute> listProp = ((Entity) attrs[0].getType().getCell()).getProps();
				this.view.setObjectEditorValue(listProp);
				return true;
			} else {
				this.view.setObjectEditorValue(null);
				return false;
			}
		} else {
			return false;
		}
	}

	public void modify(Object element, String property, Object value) {
		Attribute[] attrs = new Attribute[2];
		if (property.equals(colNames[2])) {
			Object obj = ((TreeItem) element).getData();
			if (obj instanceof BusiItfAttr) {
				BusiItfAttr busiitfattr = (BusiItfAttr) obj;
				ValueObject vo = view.getVauleObejct();
				if (vo != null) {
					attrs[0] = (Attribute) value;
					BusinessItfAttrsMapCommand cmd = new BusinessItfAttrsMapCommand(busiitfattr, property,
							attrs, vo);
					NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
					if (editor != null)
						editor.executComand(cmd);
				}
			}
		}
		if (property.equals(colNames[3])) {
			Object obj = ((TreeItem) element).getData();
			if (obj instanceof BusiItfAttr) {
				BusiItfAttr busiitfattr = (BusiItfAttr) obj;
				ValueObject vo = view.getVauleObejct();
				if (vo != null) {
					attrs[1] = (Attribute) value;
					BusinessItfAttrsMapCommand cmd = new BusinessItfAttrsMapCommand(busiitfattr, property,
							attrs, vo);
					NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
					if (editor != null)
						editor.executComand(cmd);
				}
			}
		}
	}

	private void modifyMap(BusiItfAttr busiitfattr, String property, Attribute[] attrs, ValueObject vo) {
		vo.setBusiattrAttrExtendMap(busiitfattr, attrs, property);
		view.getTreeViewer().refresh(busiitfattr);
	}

}
