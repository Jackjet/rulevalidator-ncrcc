package ncmdp.dnd;

import ncmdp.model.Reference;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;

/**
 * ����ק��Դ�������е���������ʱ�����ķ���
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
			ref.setReferencedCell(item.getCell());//���ö�Ӧ��ʵ��
			ref.setModuleName(item.getModuleName());//����ʵ������ģ����
			ref.setMdFilePath(item.getMdFilePath());//����ʵ�������ļ���·��
			ref.setRefId(item.getCell().getId());//�����µ�id
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
