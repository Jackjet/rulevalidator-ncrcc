package ncmdp.views;

import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.part.IPage;
/**
 * 用于展现元数据属性的page，由workbenchpart的getAdapter()方法调用
 * @author wangxmn
 *
 */
public interface INCMDPViewPage extends IPage, ISelectionListener{
	
}
