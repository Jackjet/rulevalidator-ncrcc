package ncmdp.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;
import ncmdp.model.JGraph;
import ncmdp.parts.CellPart;
import ncmdp.util.MDPUtil;
import ncmdp.views.EnumItemView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
/**
 * 新增枚举记录，没有任务快捷键哦~~~
 * @author wangxmn
 *
 */
public class AddEnumItemAction extends Action {
	private class AddEnumItemCommand extends Command {
		private EnumItem item = null;
		private Enumerate enumerate = null;

		public AddEnumItemCommand(Enumerate enumerate, EnumItem item) {
			super(Messages.AddEnumItemAction_0);
			this.enumerate = enumerate;
			this.item = item;
			doneItemValue();
		}

		private void doneItemValue() {
			List<EnumItem> list = enumerate.getEnumItems();
			Set<String> valSet = new HashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				valSet.add(list.get(i).getEnumValue());
			}
			String itemValue = "1"; 
			while (valSet.contains(itemValue)) {
				try {
					long l = Long.parseLong(itemValue) + 1;
					itemValue = l + ""; 
				} catch (Throwable t) {
				}
			}
			item.setEnumValue(itemValue);
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			enumerate.addEnumItem(item);
		}

		@Override
		public void undo() {
			enumerate.removeEnumItem(item);
		}

	}

	private EnumItemView view = null;

	public AddEnumItemAction(EnumItemView view) {
		super(Messages.AddEnumItemAction_3);
		this.view = view;
	}

	@Override
	public void run() {
		if(MDPUtil.getDevVersionType()==null){
			return;
		}
		CellPart part = view.getCellPart();
		Enumerate enumerate = (Enumerate) part.getModel();
		EnumItem item = new EnumItem();
		item.setClassID(enumerate.getId());
		
		//行业
		JGraph graph = NCMDPEditor.getActiveMDPEditor().getModel();
		if (graph.isIndustryIncrease()) {//行业扩展开发，则设置industryChanged字段
			item.setIndustryChanged(true);
			NCMDPEditor.getActiveMDPEditor().getModel()
					.setIndustryChanged(true);
			item.setSource(false);
			String endName = graph.getEndStrForIndustryElement();
			item.setDesc(endName);
		}
		
		item.setVersionType(MDPUtil.getDevVersionType());//设置开发维度
		
		AddEnumItemCommand cmd = new AddEnumItemCommand(enumerate, item);
		if (NCMDPEditor.getActiveMDPEditor() != null)
			NCMDPEditor.getActiveMDPEditor().executComand(cmd);
	}

}
