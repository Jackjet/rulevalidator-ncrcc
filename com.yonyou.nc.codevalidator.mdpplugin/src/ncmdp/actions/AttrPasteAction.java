package ncmdp.actions;

import java.util.List;

import ncmdp.cache.MDPCachePool;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.ValueObject;
import ncmdp.views.CellPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
/**
 * 这个类与需要修改
 * @author wangxmn
 *
 */
public class AttrPasteAction extends Action {

	private class AttrPasteCommand extends Command {
		private List<Attribute> toCopyAttrList = null;
		private ValueObject vo = null;
		private int curIndex = -1;

		public AttrPasteCommand(ValueObject vo, 
				List<Attribute> toCopyAttrList, int curIndex) {
			super(Messages.AttrPasteAction_0);
			this.toCopyAttrList = toCopyAttrList;
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
			for (Attribute attr : toCopyAttrList) {
				if (curIndex != -1) {
					vo.addProp(++curIndex, attr);
				} else {
					vo.addProp(attr);
				}
			}
			view.getTv().refresh();
			view.getTv().expandAll();
		}

		@Override
		public void undo() {
			CellPropertiesView view = getPropertiesView();
			for (Attribute attr : toCopyAttrList) {
				vo.removeProp(attr);
			}
			view.getTv().refresh();
			view.getTv().expandAll();
		}

	}

	private CellPropertiesView view = null;

	public AttrPasteAction(CellPropertiesView view) {
		super(Messages.AttrPasteAction_0);
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
		int index = 0;
		if (tis != null && tis.length > 0) {
			TreeItem ti = tis[0];
			List<Object> ol = ((List<List<Object>>) tree.getData()).get(0);
			for(int i=0,j=ol.size();i<j;i++){
				if(ol.get(i).equals(ti.getData())){
					index = i;
					break;
				}
			}
		}
		pasteProp(index);
	}

	@SuppressWarnings("unchecked")
	private void pasteProp(int index) {
		//从缓存中获得数据
		List<Attribute> toCopyAttrList = (List<Attribute>) MDPCachePool
				.getInstance().getLocalCache()
				.get(AttrCopyAction.copyAttrCacheTag);

		CellPropertiesView view = getPropertiesView();
		if (toCopyAttrList != null && toCopyAttrList.size() > 0
				&& view.getCellPart() != null
				&& view.getCellPart().getModel() instanceof ValueObject) {
			ValueObject vo = (ValueObject) view.getCellPart().getModel();
			for (Attribute attr : toCopyAttrList) {
				attr.setClassID(vo.getId());
			}
			AttrPasteCommand cmd = new AttrPasteCommand(vo, toCopyAttrList,
					index);
			if (NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}
}
