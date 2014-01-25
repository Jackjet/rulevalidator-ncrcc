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
 * ����ʵ������
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
	 * ���������
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
			MDPLogger.info("ģ�͵�������ͼΪ��");
			return;
		}
		Object object = part.getModel();// �������������ʵ��
		if (object != null && object instanceof ValueObject) {
			ValueObject vo = (ValueObject) object;
//			MDPLogger.info("AddCellPropAction����µ����ԣ�����ʵ���ID��" + vo.getId());
			prop.setClassID(vo.getId());// ����classIDΪ����ʵ���ID
			JGraph graph = NCMDPEditor.getActiveMDPEditor().getModel();
			// industry
			String curIndustry = graph.getIndustry().getCode();
			prop.setCreateIndustry(curIndustry);// ����������ҵ����
			prop.setModifyIndustry(curIndustry);// ����xiuga
			// ֻ������������ʱ����Ҫ�������Եĺ�׺��
			if (graph.isIndustryIncrease()) {
				vo.setIndustryChanged(true);
				graph.setIndustryChanged(true);
				prop.setSource(false);
				if (vo.isSource()) {// ������ʵ���ϲ����Ǻ�׺��
					String endName = graph.getEndStrForIndustryElement();
					prop.setName(endName);
				}
			}
			prop.setVersionType(MDPUtil.getDevVersionType());// ���ÿ���ά��
			// command
			TreeViewer treeView = view.getTv();
			TreeSelection sel = (TreeSelection) treeView.getSelection();
			Object o = sel.getFirstElement();
			int curIndex = 0;
			if (o instanceof Attribute) {
				curIndex = al.indexOf(o);
			}
			// �������ǿ������Ӷ������Ե�
			AddAttrCommand cmd = new AddAttrCommand(vo, al, prop, curIndex);
			if (NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}
}
