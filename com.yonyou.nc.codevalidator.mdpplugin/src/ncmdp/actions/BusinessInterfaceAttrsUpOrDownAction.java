package ncmdp.actions;

import java.util.List;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.BusiItfAttr;
import ncmdp.views.BusinessInterfaceAttrsView;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * 业务接口属性上下移动操作
 * @author wangxmn
 *
 */
public class BusinessInterfaceAttrsUpOrDownAction extends Action {
	private class BusiItfAttrMoveUpOrDownCommand extends Command{
		private List<BusiItfAttr> al = null;
		private int index = -1;
		public BusiItfAttrMoveUpOrDownCommand(List<BusiItfAttr> al, int index) {
			super();
			if(isUp){
				setLabel(Messages.BusinessInterfaceAttrsUpOrDownAction_0);
			}else{
				setLabel(Messages.BusinessInterfaceAttrsUpOrDownAction_1);
			}
			this.al = al;
			this.index = index;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			TreeViewer tv = view.getTreeViewer();
			tv.cancelEditing();
			BusiItfAttr busiItfAttr = al.remove(index);
			if(isUp){
				al.add(index - 1, busiItfAttr);
			}else{
				al.add(index + 1, busiItfAttr);
			}
			tv.refresh(al);
		}
 
		@Override
		public void undo() {
			TreeViewer tv = view.getTreeViewer();
			tv.cancelEditing();
			BusiItfAttr busiItfAttr = null;
			if(isUp){
				busiItfAttr = al.remove(index-1);
			}else{
				busiItfAttr = al.remove(index+1);
			}
			al.add(index , busiItfAttr);
			tv.refresh(al);
		}

	}
	private boolean isUp = true;
	private BusinessInterfaceAttrsView view = null;
	public BusinessInterfaceAttrsUpOrDownAction(BusinessInterfaceAttrsView view,boolean isUp) {
		super();
		this.isUp = isUp;
		this.view = view;
		if(isUp){
			setText(Messages.BusinessInterfaceAttrsUpOrDownAction_2);
		}else{
			setText(Messages.BusinessInterfaceAttrsUpOrDownAction_3);
		}
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() {
		TreeViewer tv =view.getTreeViewer(); 
		tv.cancelEditing();
		TreeSelection sel =(TreeSelection) tv.getSelection();
		Object o = sel.getFirstElement();
		if(o instanceof BusiItfAttr){
			List<BusiItfAttr> al =(List<BusiItfAttr>)((List)tv.getInput()).get(0);
			int index = al.indexOf(o);
			if(isUp){
				if (index <= 0)
					return;
			}else{
				if (index == -1 || index >= al.size()-1)
					return;
			}
			BusiItfAttrMoveUpOrDownCommand cmd = new BusiItfAttrMoveUpOrDownCommand(al, index);
			if(NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}		
	}
}
