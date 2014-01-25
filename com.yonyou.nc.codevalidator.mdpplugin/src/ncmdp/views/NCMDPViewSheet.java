package ncmdp.views;

import ncmdp.util.MDPLogger;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * 不知道这个是啥玩意
 * @author wangxmn
 *
 */
public class NCMDPViewSheet extends PageBookView implements ISelectionListener {

	public NCMDPViewSheet() {
		//调用父类
		super();
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		site.getPage().addSelectionListener(this);
		
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
		super.dispose();
	}

	@Override
	protected IPage createDefaultPage(PageBook book) {
		//当没有我们感兴趣的workbenchpart时，进行显示
		MessagePage page = new MessagePage();
		initPage(page);
		page.createControl(book);
		page.setMessage(Messages.NCMDPViewSheet_0);
		return page;
	}

	@Override
	protected void setPartName(String partName) {
		super.setPartName(partName);
	}

	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		Object obj = part.getAdapter(INCMDPViewPage.class);
		if (obj != null && obj instanceof INCMDPViewPage) {
			INCMDPViewPage page = (INCMDPViewPage) obj;
			if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
			page.createControl(getPageBook());
			return new PageRec(part, page);
		}
		return null;
	}

	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		IPage page = pageRecord.page;
		page.dispose();
		pageRecord.dispose();

	}

	@Override
	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if (page != null) {
			return page.getActivePart();
		}
		return null;
	}

	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		//说明该视图只对编辑器中的内容感兴趣
		return (part instanceof IEditorPart);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this || selection == null) {
			return;
		}
		if (!(getCurrentPage() instanceof INCMDPViewPage))
			return;
		INCMDPViewPage page = (INCMDPViewPage) getCurrentPage();
		if (page != null) {
			page.selectionChanged(part, selection);
		}

	}
	public static NCMDPViewSheet getNCMDPViewSheet(){

		NCMDPViewSheet sheet = null;
		if(sheet == null){
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if(page != null){
					sheet = (NCMDPViewSheet)page.showView(NCMDPViewSheet.class.getCanonicalName(),null,IWorkbenchPage.VIEW_VISIBLE);
				}
			} catch (PartInitException e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		return sheet;
	
	}
	public static NCMDPViewPage getNCMDPViewPage() {
		NCMDPViewSheet sheet = getNCMDPViewSheet() ;
		if (sheet != null) {
			IPage page = getNCMDPViewSheet().getCurrentPage();
			if (page instanceof NCMDPViewPage)
				return (NCMDPViewPage) page;
		}
		// else
		return null;
	}
}
