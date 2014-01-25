package ncmdp.actions;

import java.util.ArrayList;
import java.util.List;

import ncmdp.cache.MDPCachePool;
import ncmdp.model.Attribute;
import ncmdp.views.CellPropertiesView;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
/**
 * 复制属性的类有较严重问题，需要处理
 * @author wangxmn
 *
 */
public class AttrCopyAction extends Action {
	public static final String copyAttrCacheTag = "COPY_ATTR_CACHE_TAG ";

	private CellPropertiesView view = null;

	public AttrCopyAction(CellPropertiesView view) {
		super(Messages.AttrCopyAction_0);
		this.view = view;
	}

	private CellPropertiesView getPropertiesView() {
		return view;
	}

	@Override
	public void run() {
		Tree tree = getPropertiesView().getTv().getTree();
		TreeItem[] tis = tree.getSelection();
		if (tis != null && tis.length > 0) {
			List<Attribute> al = new ArrayList<Attribute>();
			for(TreeItem ti : tis){
				Object obj = ti.getData();
				if (obj instanceof Attribute) {
					Attribute attr = ((Attribute) obj).copy();
					al.add(attr);
				}
			}
			//暂时放到缓存中
			MDPCachePool.getInstance().getLocalCache().put(copyAttrCacheTag, al);
		}
	}
}
