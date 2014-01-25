package ncmdp.dnd;

import ncmdp.model.Reference;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;

/**
 * 当拖拽资源管理树中的引用类型时触发的方法
 * @author wangxmn
 *
 */
public class JGraphTransferDropTargetListener extends
		TemplateTransferDropTargetListener {
	private class RefItemFactory implements CreationFactory{
		RefMDCellTreeItem item = null;
		public RefItemFactory(RefMDCellTreeItem item) {
			super();
			this.item = item;
		}

		public Object getNewObject() {
			Reference ref = new Reference();
			ref.setReferencedCell(item.getCell());//引用对应的实体
			ref.setModuleName(item.getModuleName());//保存实体所属模块名
			ref.setMdFilePath(item.getMdFilePath());//保存实体所属文件的路径
			ref.setRefId(item.getCell().getId());//创建新的id
			return ref;
		}

		public Object getObjectType() {
			return null;
		}
		
	}
	public JGraphTransferDropTargetListener(EditPartViewer editPartViewer) {
		super(editPartViewer);
	}
	
    @SuppressWarnings("rawtypes")
	protected CreationFactory getFactory(Object template) {
    	if(template instanceof Class)
    		return new SimpleFactory((Class)template);
    	else if(template instanceof RefMDCellTreeItem){
    		return new RefItemFactory((RefMDCellTreeItem)template);
    	}else
    		return null;
    }





    
}
