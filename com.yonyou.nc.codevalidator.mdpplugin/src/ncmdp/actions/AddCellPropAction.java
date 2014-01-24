package ncmdp.actions;

import java.util.List;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.JGraph;
import ncmdp.model.ValueObject;
import ncmdp.parts.CellPart;
import ncmdp.util.MDPLogger;
import ncmdp.util.MDPUtil;
import ncmdp.views.CellPropertiesView;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新增实体属性
 * 
 * @author wangxmn
 * 
 */
public class AddCellPropAction extends Action {
	private class AddAttrCommand extends Command {
		private List<Attribute> al = null;
		private Attribute attr = null;
		private ValueObject vo = null;
		private int curIndex = -1;

		public AddAttrCommand(ValueObject vo, List<Attribute> al,
				Attribute attr, int curIndex) {
			super(Messages.AddCellPropAction_0);
			this.al = al;
			this.attr = attr;
			this.vo = vo;
			this.curIndex = curIndex;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			CellPropertiesView view = getPropertiesView();
			if (curIndex != -1) {
				vo.addProp(attr);
			}
			view.getTv().refresh(al);
			view.getTv().expandAll();
		}

		@Override
		public void undo() {
			CellPropertiesView view = getPropertiesView();
			vo.removeProp(attr);
			view.getTv().refresh(al);
			view.getTv().expandAll();
		}
	}

	private CellPropertiesView view = null;

	public AddCellPropAction(CellPropertiesView view) {
		super(Messages.AddCellPropAction_1);
		this.view = view;
	}

	private CellPropertiesView getPropertiesView() {
		return view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Tree tree = getPropertiesView().getTv().getTree();
		TreeItem[] tis = tree.getSelection();
		if (tis != null && tis.length > 0) {
			TreeItem ti = tis[0];
			Object o = ti.getData();
			List<Attribute> al = null;
			if (o instanceof List) {
				al = (List<Attribute>) o;
			} else if (o instanceof Attribute) {
				al = (List<Attribute>) ti.getParentItem().getData();
			}
			if (al != null)
				insertNullProp(al);
		}
	}

	/**
	 * 插入空属性
	 * 
	 * @param al
	 */
	private void insertNullProp(List<Attribute> al) {
		if(MDPUtil.getDevVersionType()==null){
			return;
		}
		Attribute prop = new Attribute();
		CellPropertiesView view = getPropertiesView();
		CellPart part = view.getCellPart();
		if (part == null) {
			MDPLogger.info("模型的属性视图为空");
			return;
		}
		Object object = part.getModel();// 获得属性所属的实体
		if (object != null && object instanceof ValueObject) {
			ValueObject vo = (ValueObject) object;
//			MDPLogger.info("AddCellPropAction添加新的属性：所属实体的ID是" + vo.getId());
			prop.setClassID(vo.getId());// 设置classID为所属实体的ID
			JGraph graph = NCMDPEditor.getActiveMDPEditor().getModel();
			// industry
			String curIndustry = graph.getIndustry().getCode();
			prop.setCreateIndustry(curIndustry);// 设置生成行业编码
			prop.setModifyIndustry(curIndustry);// 设置xiuga
			// 只有在增量开发时，需要考虑属性的后缀名
			if (graph.isIndustryIncrease()) {
				vo.setIndustryChanged(true);
				graph.setIndustryChanged(true);
				prop.setSource(false);
				if (vo.isSource()) {// 新增子实体上不考虑后缀名
					String endName = graph.getEndStrForIndustryElement();
					prop.setName(endName);
				}
			}
			prop.setVersionType(MDPUtil.getDevVersionType());// 设置开发维度
			// command
			TreeViewer treeView = view.getTv();
			TreeSelection sel = (TreeSelection) treeView.getSelection();
			Object o = sel.getFirstElement();
			int curIndex = 0;
			if (o instanceof Attribute) {
				curIndex = al.indexOf(o);
			}
			// 理论上是可以增加多条属性的
			AddAttrCommand cmd = new AddAttrCommand(vo, al, prop, curIndex);
			if (NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}
}
