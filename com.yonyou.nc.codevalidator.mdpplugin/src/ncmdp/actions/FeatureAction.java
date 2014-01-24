package ncmdp.actions;

import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.Cell;
import ncmdp.model.Entity;
import ncmdp.model.Feature;
import ncmdp.model.Type;
import ncmdp.model.ValueObject;
import ncmdp.model.Type.SpecialType;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

public class FeatureAction extends Action {

	private class AddFeatureCommand extends Command {
		private ValueObject vo = null;
		private Attribute[] attrs = null;
		private Attribute oldKeyAttr = null;

		public AddFeatureCommand(ValueObject vo, Attribute[] attrs) {
			super();
			this.vo = vo;
			this.attrs = attrs;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			if (vo instanceof Entity) {
				oldKeyAttr = ((Entity) vo).getKeyAttribute();
			}
			String industry = MDPUtil.getDevCode();
			if(industry==null){
				return;
			}
			String versionType = vo.getGraph().getVersionType();
			int count = attrs == null ? 0 : attrs.length;
			for (int i = 0; i < count; i++) {
				Type type = attrs[i].getType();
				attrs[i].setClassID(vo.getId());
				attrs[i].setParentCell(vo);
				attrs[i].setVersionType(versionType);
				attrs[i].setCreateIndustry(industry);
				attrs[i].setModifyIndustry(industry);
				if (type instanceof SpecialType) {
					SpecialType st = (SpecialType) type;
					if (SpecialType.SelfType.equals(st.getSpecialTypeID())) {
						Type t = SpecialType.getSelfType(vo);
						attrs[i].setType(t);
						attrs[i].setTypeStyle("REF"); 
					} else {
//						Cell cell = MDPExplorerTreeView.getMDPExploerTreeView(
//								null).findCellbyId(st.getSpecialTypeID());
						Cell cell = ProjectUtil.findCellbyId(st.getSpecialTypeID());
						if (cell != null) {
							Type t = cell.converToType();
							attrs[i].setType(t);
						}
						if (cell instanceof Entity) {
							attrs[i].setTypeStyle("REF"); 
						}
					}
				}
				vo.addProp(attrs[i]);
				if (attrs[i].isKey() && vo instanceof Entity) {
					((Entity) vo).setKeyAttribute(attrs[i]);
				}
				// 设置特性属性 dingxm@2009-08-31
				attrs[i].setIsFeature(true);
			}

			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv()
					.refresh();
		}

		@Override
		public void undo() {
			int count = attrs == null ? 0 : attrs.length;
			for (int i = 0; i < count; i++) {
				vo.removeProp(attrs[i]);
			}
			if (vo instanceof Entity) {
				((Entity) vo).setKeyAttribute(oldKeyAttr);
			}
			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv()
					.refresh();
		}
	}

	private Feature feature = null;

	public FeatureAction(Feature feature) {
		super();
		this.feature = feature;
		setText(feature.getDisplayName());
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			List<?> list = editor.getGV().getSelectedEditParts();
			ValueObject vo = null;
			if (list.size() > 0) {
				EditPart ep = (EditPart) list.get(0);
				//获得选中的实体的数据模型
				Object o = ep.getModel();
				if (o instanceof ValueObject) {
					vo = (ValueObject) o;
				}
			}
			if (vo != null) {
				Attribute[] attrs = feature.getAttrbuteCopys();
				AddFeatureCommand cmd = new AddFeatureCommand(vo, attrs);
				editor.executComand(cmd);
			} else {
				MessageDialog.openConfirm(editor.getSite().getShell(), Messages.FeatureAction_2,
						Messages.FeatureAction_3);
			}

		}

	}

}
