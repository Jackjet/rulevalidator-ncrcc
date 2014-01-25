package ncmdp.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ncmdp.cache.MDPCachePool;
import ncmdp.dnd.MDPExplorerTreeDragListener;
import ncmdp.dnd.MDPExplorerTreeDropListener;
import ncmdp.editor.NCMDPBpfEditor;
import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttopdm.wizard.ExportPDMFileWizard;
import ncmdp.exporttosql.wizard.GenSqlsWizard;
import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.model.Reference;
import ncmdp.project.action.DeleteAction;
import ncmdp.project.action.GenSqlsAction;
import ncmdp.project.action.GeneratePDMAction;
import ncmdp.project.action.InputFileAction;
import ncmdp.project.action.LocatorAction;
import ncmdp.project.action.NewBpfFileAction;
import ncmdp.project.action.NewDirAction;
import ncmdp.project.action.NewIncDevActivityFileAction;
import ncmdp.project.action.NewIncDevEntityFileAction;
import ncmdp.project.action.NewMDPFileAction;
import ncmdp.project.action.PublishModuleMetaDataAction;
import ncmdp.project.action.RefreshMDPExplorerTreeAction;
import ncmdp.project.action.SaveAllMDFilesAction;
import ncmdp.project.action.UpdateTreeItemStateAction;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.PublishMetaData;
import ncmdp.ui.tree.mdptree.DirectoryTreeItem;
import ncmdp.ui.tree.mdptree.IMDPTreeNode;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.ui.tree.mdptree.ProjectTreeItem;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;
import ncmdp.ui.tree.mdptree.RefMDFileDirTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

/**
 * ��Դ��������ͼ
 * 
 * @author wangxmn
 * 
 */
public class MDPExplorerTreeView extends ViewPart implements IMenuListener {

	private TreeViewer treeView = null;// �����
	private NewDirAction newDirAction = null;// �½�Ŀ¼
	private NewMDPFileAction newMDPFileAction = null;// �½�ʵ���ļ�
	private NewBpfFileAction newBpfFileAction = null;// �½���ļ�
	private NewIncDevEntityFileAction newIncDevFileAction = null;// �½�ʵ����������
	private NewIncDevActivityFileAction newIncDevActivityFileAction = null;// �½�������������
	private InputFileAction inputFileAction = null;//���빦�ܣ����Ե���һ��Ԫ�����ļ�
	private DeleteAction deleteAction = null;//ɾ������
	private PublishModuleMetaDataAction publishModuleMetaDataAction = null;// ����ģ��Ԫ����
	private PublishModuleMetaDataAction publishModuleMetaDataActionAllLowVesion = null;// ����Ԫ���ݺ��԰汾
	private GeneratePDMAction generatePDMAction = null;// ����pdm�ļ�
	private GenSqlsAction genSqlsAction1 = null;// ����sql�ļ�
	private GenSqlsAction genSqlsAction2 = null;// ����sql�ļ���ִ��
	private RefreshMDPExplorerTreeAction refreshMDPExplorerTreeAction = null;// ˢ��
	private SaveAllMDFilesAction saveAllMDFilesAction = null;// ����
	private UpdateTreeItemStateAction updateTreeItemStateAction = null;// ����״̬
	private LocatorAction locator = null;//��λ

	public MDPExplorerTreeView() {
		super();
		init();
	}

	private void init() {
		newDirAction = new NewDirAction();
		newMDPFileAction = new NewMDPFileAction();
		newBpfFileAction = new NewBpfFileAction();
		newIncDevFileAction = new NewIncDevEntityFileAction();
		newIncDevActivityFileAction = new NewIncDevActivityFileAction();
		inputFileAction = new InputFileAction();
		deleteAction = new DeleteAction();
		publishModuleMetaDataAction = new PublishModuleMetaDataAction(false);
		publishModuleMetaDataActionAllLowVesion = new PublishModuleMetaDataAction(
				true);
		generatePDMAction = new GeneratePDMAction();
		genSqlsAction1 = new GenSqlsAction(true);
		genSqlsAction2 = new GenSqlsAction(false);
		refreshMDPExplorerTreeAction = new RefreshMDPExplorerTreeAction();
		saveAllMDFilesAction = new SaveAllMDFilesAction();
		updateTreeItemStateAction = new UpdateTreeItemStateAction();
		locator = new LocatorAction();
	}

	// �һ���ʾ�Ĳ˵���
	@Override
	public void menuAboutToShow(IMenuManager mm) {
		mm.add(newDirAction);
		mm.add(newMDPFileAction);
		mm.add(newBpfFileAction);
		mm.add(newIncDevFileAction);
		mm.add(newIncDevActivityFileAction);
		mm.add(inputFileAction);
		mm.add(new Separator());
		mm.add(deleteAction);
		mm.add(new Separator());
		mm.add(publishModuleMetaDataAction);
		mm.add(publishModuleMetaDataActionAllLowVesion);
		mm.add(new Separator());
		mm.add(generatePDMAction);
		mm.add(new Separator());
		mm.add(genSqlsAction1);
		mm.add(genSqlsAction2);
		mm.add(new Separator());
		mm.add(saveAllMDFilesAction);

		boolean isCanAddFile = canAddFile();
		newDirAction.setEnabled(canAddDir());
		newMDPFileAction.setEnabled(isCanAddFile);
		newBpfFileAction.setEnabled(isCanAddFile);
		newIncDevFileAction.setEnabled(isCanAddFile);
		newIncDevActivityFileAction.setEnabled(isCanAddFile);
		inputFileAction.setEnabled(isCanAddFile);
		boolean isIMDPTreeNode = isIMDPTreeNode();
		deleteAction.setEnabled(canDelete());
		publishModuleMetaDataAction.setEnabled(isIMDPTreeNode);
		publishModuleMetaDataActionAllLowVesion.setEnabled(isIMDPTreeNode);
		generatePDMAction.setEnabled(isIMDPTreeNode);
		genSqlsAction1.setEnabled(isIMDPTreeNode);
		genSqlsAction2.setEnabled(isIMDPTreeNode);
	}

	/**
	 * �ж��Ƿ����½��ļ�
	 * 
	 * @return
	 */
	private boolean canAddFile() {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			return false;
		TreeItem selTI = selTIs[0];
		if (!(selTI instanceof IMDPTreeNode))
			return false;
		File parentFile = ((IMDPTreeNode) selTI).getFile();
		if (parentFile.isFile())
			return false;
		return true;
	}
	
	private boolean canDelete(){
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0){
			return false;
		}else{
			TreeItem selTI = selTIs[0];
			//�����Ŀ�ͷ�Ԫ���ݽڵ㲻�ܽ���ɾ������
			if((selTI instanceof ProjectTreeItem)||(selTI instanceof RefMDCellTreeItem)){
				return false;
			}
		}
		return true;
	}

	/**
	 * �ж��ܷ��½�Ŀ¼
	 * 
	 * @return
	 */
	private boolean canAddDir() {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			return false;
		TreeItem selTI = selTIs[0];
		if (!(selTI instanceof IMDPTreeNode))
			return false;
		File parentFile = ((IMDPTreeNode) selTI).getFile();// (File)
		if (parentFile.isFile())
			return false;
		return true;
	}

	/**
	 * �ж��Ƿ�ΪԪ�������ڵ�
	 * 
	 * @return
	 */
	private boolean isIMDPTreeNode() {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			return false;
		TreeItem selTI = selTIs[0];
		if (selTI instanceof IMDPTreeNode) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �жϵ�ǰ�м���İ汾
	 */
	private void judgeVersion() {
		String ncHome = NCMDPTool.getNCHome();
		File file = new File(ncHome + "/ierp/metadata/version60.xml");
		if (!file.exists()) {
			MessageDialog.openError(null, Messages.MDPExplorerTreeView_1,
					Messages.MDPExplorerTreeView_2);
			throw new RuntimeException("judge:" + file.getAbsolutePath()
					+ Messages.MDPExplorerTreeView_4);
		}
	}

	// ������Դ������
	@Override
	public void createPartControl(Composite parent) {
		// �жϵ�ǰNCHOME�汾 �������ֻ������V6֮��İ汾
		judgeVersion();
		// validateLicense();
		// ��Դ��������ͼ����
		ViewForm vf = new ViewForm(parent, SWT.NONE);
		vf.setLayout(new FillLayout());
		// �������ϽǵĹ�����
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(updateTreeItemStateAction);
		tbm.add(refreshMDPExplorerTreeAction);
		tbm.add(deleteAction);
		tbm.add(locator);
		tbm.update(true);
		// ������
//		treeView = new TreeViewer(vf, SWT.MULTI | SWT.H_SCROLL | SWT.H_SCROLL);
		treeView = ProjectUtil.getMDPExplorerTreeViewer(vf);
		Tree tree = treeView.getTree();
		MenuManager mm = new MenuManager();
		mm.setRemoveAllWhenShown(true);
		mm.addMenuListener(this);// ��������ж����action
		Menu menu = mm.createContextMenu(tree);// ��Ϊ�һ������˵�
		tree.setMenu(menu);

		activePackageExplorer();// ��֪Ϊ����
		// ������
//		initTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				 boolean isDelete = canDelete();
				 deleteAction.setEnabled(isDelete);
			}

			// �ͷ������ʱ�Ĳ���
			@Override
			public void mouseUp(MouseEvent e) {
				if (NCMDPTool.isSetLocator()) {
					Tree tree = treeView.getTree();
					TreeItem[] tis = tree.getSelection();
					if (tis == null || tis.length == 0)
						return;
					TreeItem ti = tis[0];
					if (ti instanceof MDPFileTreeItem) {
						MDPFileTreeItem item = (MDPFileTreeItem) ti;
						if (EditorUtility.isOpenInEditor(item.getIFile()) != null) {
							try {
								EditorUtility.openInEditor(item.getIFile(),
										false);
							} catch (Exception e1) {
								MDPLogger.error(e1.getMessage(), e1);
							}
						}
					} else if (ti instanceof RefMDFileDirTreeItem) {
						openRefFileInEditor((RefMDFileDirTreeItem) ti);
					} else if (ti instanceof RefMDCellTreeItem) {
						RefMDCellTreeItem item = (RefMDCellTreeItem) ti;
						Cell cell = item.getCell();
						if (NCMDPEditor.getActiveMDPEditor() != null)
							NCMDPEditor.getActiveMDPEditor().setSelect(cell);
					}
				}
			}

			public void mouseDoubleClick(MouseEvent e) {// ˫����ҳ��
				Tree tree = treeView.getTree();
				TreeItem[] tis = tree.getSelection();
				if (tis == null || tis.length == 0)
					return;
				TreeItem ti = tis[0];
				if (ti instanceof MDPFileTreeItem) {
					openEditorTemp((MDPFileTreeItem) ti);
				} else if (ti instanceof RefMDFileDirTreeItem) {
					openRefFileInEditor((RefMDFileDirTreeItem) ti);
				} else if (ti instanceof RefMDCellTreeItem) {
					addRefCellToEidtor((RefMDCellTreeItem) ti);
				} else {
					ti.setExpanded(!ti.getExpanded());
				}
			}
		});
		vf.setTopRight(tb);
		vf.setContent(treeView.getControl());
		getSite().setSelectionProvider(treeView);
		DragSource dragSource = new DragSource(tree, DND.DROP_COPY
				| DND.DROP_MOVE);
		Transfer[] types = new Transfer[] { TemplateTransfer.getInstance() };
		dragSource.setTransfer(types);
		dragSource.addDragListener(new MDPExplorerTreeDragListener());
		DropTarget dropTarget = new DropTarget(tree, DND.DROP_COPY
				| DND.DROP_MOVE);
		dropTarget.setTransfer(types);
		dropTarget.addDropListener(new MDPExplorerTreeDropListener());
	}

	/**
	 * �������Դ��������λ���Ҳ�
	 */
	private void activePackageExplorer() {
		PackageExplorerPart part = PackageExplorerPart
				.getFromActivePerspective();
		if (part == null) {
			try {
				part = (PackageExplorerPart) getSite().getPage().showView(
						JavaUI.ID_PACKAGES, null, IWorkbenchPage.VIEW_CREATE);

			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		} else {
			IBaseLabelProvider provider = part.getTreeViewer()
					.getLabelProvider();
			try {
				Method addlistener = provider.getClass().getMethod(
						"addListener", ILabelProviderListener.class);
				if (addlistener != null) {
					ILabelProviderListener list = new ILabelProviderListener() {
						public void labelProviderChanged(
								LabelProviderChangedEvent event) {
							updateAllTreeItemState();
						}
					};
					addlistener.invoke(provider, list);
				}
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
	}

	protected void addRefCellToEidtor(RefMDCellTreeItem item) {
		IEditorPart editorPart = getViewSite().getPage().getActiveEditor();
		if (editorPart instanceof NCMDPEditor) {
			NCMDPEditor editor = (NCMDPEditor) editorPart;
			JGraph graph = editor.getModel();
			Reference ref = new Reference();
			ref.setReferencedCell(item.getCell());
			ref.setModuleName(item.getModuleName());
			ref.setMdFilePath(item.getMdFilePath());
			ref.setRefId(item.getCell().getId());
			graph.addCell(ref);
		}
	}

	/**
	 * �򿪽ڵ㣬ͬʱ�򿪱༭�����б༭
	 * 
	 * @param selItem
	 */
	public void openEditorTemp(MDPFileTreeItem selItem) {
		File file = selItem.getFile();
		if (file.isFile()) {
			IEditorInput input = selItem.getEditorInput();
			IWorkbenchPage page = JavaPlugin.getActivePage();
			if (page != null) {
				try {
					IEditorPart part = page.findEditor(input);
					if (part == null) {
						if (MDPUtil.isBpfInput(selItem.getFile().getName())) {
							// ��bpf�ļ�
							page.openEditor(input, NCMDPBpfEditor.EDITOR_ID,
									true);
						} else {
							// ��bmf�ļ�
							page.openEditor(input, NCMDPEditor.EDITOR_ID, true);
						}
					} else {
						page.activate(part);
					}
				} catch (PartInitException e) {
					MDPLogger.error(e.getMessage(), e);
					NCMDPTool.showErrDlg(NCMDPTool
							.getExceptionRecursionError(e));
				}
			}
		}
	}

	public void openRefFileInEditor(RefMDFileDirTreeItem item) {
		File file = item.getFile();
		if (file.isFile()) {
			IEditorInput input = new RefMDFileEditorInput(file);
			IWorkbenchPage page = JavaPlugin.getActivePage();
			if (page != null) {
				try {
					IEditorPart part = page.findEditor(input);
					if (part == null) {
						page.openEditor(input, NCMDPEditor.EDITOR_ID, true);
					} else {
						page.activate(part);
					}
				} catch (PartInitException e) {
					MDPLogger.error(e.getMessage(), e);
					NCMDPTool.showErrDlg(NCMDPTool
							.getExceptionRecursionError(e));
				}
			}
		}
	}

	@Override
	public void setFocus() {
		treeView.getControl().setFocus();
	}

	/**
	 * �½�Ŀ¼����
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addDirTreeNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0) {
			MDPLogger.info("the parent node is null");
			throw new Exception(Messages.MDPExplorerTreeView_6);
		}
		TreeItem parent = selTIs[0];// ��ǰѡ�еĽڵ�
		if (!(parent instanceof IMDPTreeNode)) {
			MDPLogger.info("the parent is not IMDPTreeNode");
			throw new Exception(Messages.MDPExplorerTreeView_7);
		}
		IResource parentRes = ((IMDPTreeNode) parent).getIResource();
		TreeItem ti = null;
		if (parentRes instanceof IFile) {
			throw new Exception(Messages.MDPExplorerTreeView_8);
		} else if (parentRes instanceof IFolder) {
			IFolder parentFolder = (IFolder) parentRes;// ��Ӧ��metaDATAĿ¼�ļ���
			IFolder childFolder = parentFolder.getFolder(dirName);
			parent = makeDirsAsNeed(parent, parentFolder);
			if (!childFolder.exists()) {
				childFolder.create(true, true, null);
				ti = new DirectoryTreeItem(parent, childFolder);
				parent.setExpanded(true);
			} else {
				throw new Exception(Messages.MDPExplorerTreeView_5);
			}
		}
		return ti;
	}

	private TreeItem addDirTreeNode(TreeItem parent, String dirName)
			throws Exception {
		if (!(parent instanceof IMDPTreeNode))
			throw new Exception(Messages.MDPExplorerTreeView_9);
		IResource parentRes = ((IMDPTreeNode) parent).getIResource();
		TreeItem ti = null;
		if (parentRes instanceof IFile)
			throw new Exception(Messages.MDPExplorerTreeView_10);
		else if (parentRes instanceof IFolder) {
			IFolder parentFolder = (IFolder) parentRes;
			IFolder childFolder = parentFolder.getFolder(dirName);
			if (!childFolder.exists()) {
				childFolder.create(true, true, null);
			}
			ti = new DirectoryTreeItem(parent, childFolder);
			parent.setExpanded(true);
		}
		return ti;
	}

	private TreeItem makeDirsAsNeed(TreeItem parentTI, IFolder folder) {
		List<IFolder> array = new ArrayList<IFolder>();
		while (folder != null && !folder.exists()) {
			array.add(0, folder);
			IContainer parent = folder.getParent();
			if (parent instanceof IFolder) {
				folder = (IFolder) parent;
			} else {
				folder = null;
			}
		}
		if (folder != null && folder.exists()) {
			parentTI = findTreeItemByFolder(parentTI, folder);
		}
		for (int i = 0; i < array.size(); i++) {
			try {
				folder = array.get(i);
				parentTI = addDirTreeNode(parentTI, folder.getName());
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		return parentTI;
	}

	/**
	 * �ҵ�����ļ�Ŀ¼��Ӧ�����ڵ�
	 * 
	 * @param item
	 * @param folder
	 * @return
	 */
	private TreeItem findTreeItemByFolder(TreeItem item, IFolder folder) {
		TreeItem ti = null;
		if (item instanceof IMDPTreeNode) {
			IResource res = ((IMDPTreeNode) item).getIResource();
			if (res instanceof IFolder
					&& res.getFullPath().toOSString()
							.equals(folder.getFullPath().toOSString())) {
				ti = item;
			} else if (item.getItemCount() > 0) {
				for (int i = 0; i < item.getItemCount(); i++) {
					ti = findTreeItemByFolder(item.getItem(i), folder);
					if (ti != null) {
						break;
					}
				}
			}
		}
		return ti;
	}

	/**
	 * ɾ��ѡ�еĽڵ㣬�����⣡���ر�
	 * 
	 * @throws Exception
	 */
	public void deleteSelectedTreeNode() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(Messages.MDPExplorerTreeView_11);
		TreeItem selTI = selTIs[0];
		if (selTI instanceof IMDPTreeNode) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell,
					Messages.MDPExplorerTreeView_12,
					Messages.MDPExplorerTreeView_13 + selTI.getText()
							+ Messages.MDPExplorerTreeView_14))
				return;
			closeOpenedEidtor(selTI);
			((IMDPTreeNode) selTI).deleteNode();
		}
	}

	private void closeOpenedEidtor(TreeItem ti) {
		if (ti instanceof MDPFileTreeItem) {
			IWorkbenchPage page = getViewSite().getPage();
			IEditorInput input = ((MDPFileTreeItem) ti).getEditorInput();
			IEditorPart editor = page.findEditor(input);
			if (editor != null) {
				page.closeEditor(editor, false);
			}
		} else {
			TreeItem[] childs = ti.getItems();
			int count = childs == null ? 0 : childs.length;
			for (int i = 0; i < count; i++) {
				closeOpenedEidtor(childs[i]);
			}
		}

	}

	/**
	 * ����sql�ļ�
	 * @param isExecSqls
	 * @return
	 * @throws Exception
	 */
	public String genSqls(boolean isExecSqls) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();

		if (selTIs != null && selTIs.length > 0) {
			IProject project = ((IMDPTreeNode) selTIs[0]).getIResource()
					.getProject();
			List<TreeItem> al = new ArrayList<TreeItem>();
			for (TreeItem item : selTIs) {
				IProject curProject = ((IMDPTreeNode) item).getIResource()
						.getProject();
				if (!project.getName().equalsIgnoreCase(curProject.getName())) {
					return Messages.MDPExplorerTreeView_15;
				}
				addItemFile(al, item);
			}
			GenSqlsWizard wizard = new GenSqlsWizard(al, project, isExecSqls);
			WizardDialog wd = new WizardDialog(Display.getCurrent()
					.getActiveShell(), wizard);
			wd.open();
		}
		return "";
	}

	/**
	 * ��������pdm�ļ�
	 * @return
	 * @throws Exception
	 */
	public String generatePDM() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs != null && selTIs.length > 0) {
			IProject project = ((IMDPTreeNode) selTIs[0]).getIResource()
					.getProject();
			List<TreeItem> al = new ArrayList<TreeItem>();
			for (TreeItem item : selTIs) {
				IProject curProject = ((IMDPTreeNode) item).getIResource()
						.getProject();
				if (!project.getName().equalsIgnoreCase(curProject.getName())) {
					return Messages.MDPExplorerTreeView_17;
				}
				addItemFile(al, item);
			}
			ExportPDMFileWizard wizard = new ExportPDMFileWizard(al, "",
					project);
			WizardDialog wd = new WizardDialog(getSite().getShell(), wizard);
			wd.open();
		}
		return "";
	}

	/**
	 * ��item�µ�ģ���ļ��ݹ���뵽al��
	 * @param al
	 * @param item
	 */
	private void addItemFile(List<TreeItem> al, TreeItem item) {
		if (item instanceof IMDPTreeNode) {
			//��һģ���ļ�������뵽al
			File parentFile = ((IMDPTreeNode) item).getFile();
			if (parentFile.isFile()) {
				al.add(item);
			} else {
				//�ݹ����
				TreeItem[] items = item.getItems();
				if (items != null && items.length > 0) {
					for (TreeItem cItem : items) {
						addItemFile(al, cItem);
					}
				}
			}
		}
	}

	/**
	 * ����Ԫ���ݣ����η�������Ԫ����
	 * 
	 * @param allowLowVersionPublish
	 *            �Ƿ�֧�ֺ��԰汾����
	 * @throws Exception
	 */
	public void publishMetaData(boolean allowLowVersionPublish)
			throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(Messages.MDPExplorerTreeView_20);
		try {
			for (TreeItem selTI : selTIs) {
				if (selTI instanceof IMDPTreeNode) {
					publishMetaData(selTI, allowLowVersionPublish);
				}
			}
		} catch (SecurityException e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"����", "�м���Ƿ�����");
		} catch (NoSuchMethodException e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"����", "δ�ҵ���Ӧ�ķ���");
		} catch (ClassNotFoundException e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"����", "û���ҵ���Ӧ����");
		}
	}

	/**
	 * ����ĳ���ڵ��Ԫ����
	 * 
	 * @param ti
	 * @param allowLowVersionPublish
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private void publishMetaData(TreeItem ti, boolean allowLowVersionPublish)
			throws SecurityException, NoSuchMethodException,
			ClassNotFoundException {
		if (ti instanceof MDPFileTreeItem) {
			IWorkbenchPage page = getViewSite().getPage();
			IEditorInput input = ((MDPFileTreeItem) ti).getEditorInput();
			IEditorPart editor = page.findEditor(input);
			File file = null;
			if (editor != null && editor instanceof NCMDPEditor) {
				NCMDPEditor mdpEditor = (NCMDPEditor) editor;
				if (mdpEditor.isDirty()) {
					MessageDialog.openConfirm(mdpEditor.getSite().getShell(),
							Messages.MDPExplorerTreeView_21,
							Messages.MDPExplorerTreeView_22);
					return;
				}
				JGraph graph = mdpEditor.getModel();
				String validateStr = graph.validate();
				if (validateStr != null && validateStr.trim().length() > 0) {
					String message = Messages.MDPExplorerTreeView_23
							+ validateStr;
					MessageDialog.openInformation(mdpEditor.getSite()
							.getShell(), Messages.MDPExplorerTreeView_24,
							message);
					return;
				}
				if (!MessageDialog.openConfirm(mdpEditor.getSite().getShell(),
						Messages.MDPExplorerTreeView_25,
						Messages.MDPExplorerTreeView_26))
					return;
				file = mdpEditor.getFile();
			} else {
				file = ((MDPFileTreeItem) ti).getFile();
			}
			if (file != null)
				new PublishMetaData().publishMetaData(file,
						allowLowVersionPublish);
		} else {
			TreeItem[] childs = ti.getItems();
			int count = childs == null ? 0 : childs.length;
			for (int i = 0; i < count; i++) {
				publishMetaData(childs[i], allowLowVersionPublish);
			}
		}
	}

	public MDPFileTreeItem addFileTreeNode(TreeItem selTI, String fileName,
			JGraph graph) throws Exception {
		MDPFileTreeItem ti = null;
		if (selTI instanceof IMDPTreeNode) {
			IResource parentRes = ((IMDPTreeNode) selTI).getIResource();
			if (parentRes instanceof IFolder) {
				IFolder folder = (IFolder) parentRes;
				IFile childFile = folder.getFile(fileName);
				TreeItem parent = makeDirsAsNeed(selTI, folder);
				if (!childFile.exists()) {
					String str = JGraphSerializeTool.serializeToString(graph);
					InputStream in = new ByteArrayInputStream(
							str.getBytes("UTF-8"));
					childFile.create(in, true, null);
					ti = new MDPFileTreeItem(parent, childFile);
					selTI.setExpanded(true);
					ti.setExpanded(true);
				}
			}
		}
		return ti;
	}

	public void creatTreControl(Composite parent) {

	}

//	private void initTree() {
//		Display.getDefault().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				MDPExplorerTreeBuilder.getInstance().buildTree(
//						treeView.getTree());// ִ�г���
//			}
//		});
//	}

	/**
	 * ˢ�����Ĳ���
	 */
	public void refreshTree() {
		// ���»���
		MDPCachePool.getInstance().initEntityIDMap();
		// ���¹�����
		Tree tree = treeView.getTree();
		tree.removeAll();
		ProjectUtil.initMDPExplorerTree(tree);
	}

	public void updateTreeItemState() {
		Tree tree = treeView.getTree();
		TreeItem[] items = tree.getSelection();
		int count = items == null ? 0 : items.length;
		for (int i = 0; i < count; i++) {
			updateItem(items[i]);
		}
	}

	public void updateAllTreeItemState() {
		Tree tree = treeView.getTree();
		if (tree.isDisposed())
			return;
		TreeItem[] items = tree.getItems();
		int count = items == null ? 0 : items.length;
		for (int i = 0; i < count; i++) {
			updateItem(items[i]);
		}
	}

	private void updateItem(TreeItem item) {
		if (item instanceof IMDPTreeNode) {
			((IMDPTreeNode) item).updateState();
		}
		int count = item.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem child = item.getItem(i);
			updateItem(child);
		}
	}

	public Tree getExplorerTree() {
		return treeView.getTree();
	}

//	/**
//	 * ���¼��ع���ҳ�棬ˢ��ʱ������������
//	 * @param page
//	 * @return
//	 */
//	public static MDPExplorerTreeView getMDPExploerTreeView(IWorkbenchPage page) {
//		if (page == null) {
//			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//					.getActivePage() != null) {
//				page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//						.getActivePage();
//			}
//		}
//		if (page != null) {
//			MDPExplorerTreeView view = (MDPExplorerTreeView) page
//					.findView(MDPExplorerTreeView.class.getCanonicalName());
//			if (view == null) {
//				try {
//					view = (MDPExplorerTreeView) page
//							.showView(MDPExplorerTreeView.class
//									.getCanonicalName());
//				} catch (PartInitException e) {
//					MDPLogger.error(e.getMessage(), e);
//				}
//			}
//			return view;
//		} else
//			return null;
//	}

	/**
	 * ����ļ���������Ŀ
	 * 
	 * @param file
	 * @return
	 */
	public IProject getFileOwnProject(File file) {
		IProject project = null;
		Tree tree = treeView.getTree();
		TreeItem[] items = tree.getItems();
		int count = items == null ? 0 : items.length;
		for (int i = 0; i < count; i++) {
			if (items[i] instanceof ProjectTreeItem) {
				ProjectTreeItem item = (ProjectTreeItem) items[i];
				if (containsFile(item, file)) {
					project = item.getProjectModel().getJavaProject();
					break;
				}
			}
		}
		return project;
	}

	/**
	 * �жϸ�file�Ƿ��ڸ���Ŀ��
	 * 
	 * @param item
	 * @param file
	 * @return
	 */
	private boolean containsFile(TreeItem item, File file) {
		Object o = item.getData("localFile");
		if (o != null && o instanceof File
				&& ((File) o).getAbsolutePath().equals(file.getAbsolutePath())) {
			return true;
		} else {
			TreeItem[] items = item.getItems();
			int count = items == null ? 0 : items.length;
			for (int i = 0; i < count; i++) {
				if (containsFile(items[i], file)) {
					return true;
				}
			}
			return false;
		}
	}

//	public TreeItem findMDFileTreeItem(File file) {
//		return findMDFileTreeItem(treeView.getTree(), file, false);
//	}
//
//	public TreeItem findMDFileTreeItem(File file, boolean includeRefItem) {
//		return findMDFileTreeItem(treeView.getTree(), file, includeRefItem);
//	}
//
//	private TreeItem findMDFileTreeItem(Tree tree, File file,
//			boolean includRefItem) {
//		int count = tree.getItemCount();
//		for (int i = 0; i < count; i++) {
//			TreeItem item = tree.getItem(i);
//			TreeItem ti = findMDPFileTreeItem(item, file, includRefItem);
//			if (ti != null)
//				return ti;
//		}
//		return null;
//	}

//	private TreeItem findMDPFileTreeItem(TreeItem item, File file,
//			boolean includRefItem) {
//		if (item instanceof IMDPTreeNode) {
//			File itemFile = ((IMDPTreeNode) item).getFile();
//			if (itemFile.equals(file)) {
//				return item;
//			} else {
//				int count = item.getItemCount();
//				for (int i = 0; i < count; i++) {
//					TreeItem child = item.getItem(i);
//					TreeItem ti = findMDPFileTreeItem(child, file,
//							includRefItem);
//					if (ti != null)
//						return ti;
//				}
//			}
//		} else if (includRefItem) {
//			if (item instanceof RefMDFileDirTreeItem) {
//				File itemFile = ((RefMDFileDirTreeItem) item).getFile();
//				if (itemFile.equals(file)) {
//					return item;
//				} else {
//					int count = item.getItemCount();
//					for (int i = 0; i < count; i++) {
//						TreeItem child = item.getItem(i);
//						TreeItem ti = findMDPFileTreeItem(child, file,
//								includRefItem);
//						if (ti != null)
//							return ti;
//					}
//				}
//			} else if (item.getText().equals(Messages.MDPExplorerTreeView_33)) {
//				int count = item.getItemCount();
//				for (int i = 0; i < count; i++) {
//					TreeItem child = item.getItem(i);
//					TreeItem ti = findMDPFileTreeItem(child, file,
//							includRefItem);
//					if (ti != null)
//						return ti;
//				}
//			}
//		}
//		return null;
//	}

//	/**
//	 * ���item�µ����е�ʵ�壬����Դ��������ʵ�嶼����RefMDCellTreeItem��ʽ����
//	 * @param item
//	 * @param al
//	 */
//	private void collectCell(TreeItem item, ArrayList<RefMDCellTreeItem> al) {
//		if (item instanceof RefMDCellTreeItem) {
//			RefMDCellTreeItem refItem = (RefMDCellTreeItem) item;
//			al.add(refItem);
//		}
//		int count = item.getItemCount();
//		for (int i = 0; i < count; i++) {
//			TreeItem ti = item.getItem(i);
//			collectCell(ti, al);
//		}
//	}

//	/**
//	 * ������е��������ͣ�nchonme�µ�ʵ������
//	 * @return
//	 */
//	public RefMDCellTreeItem[] getAllCellTreeItem() {
//		ArrayList<RefMDCellTreeItem> al = new ArrayList<RefMDCellTreeItem>();
//		Tree tree = treeView.getTree();
//		int count = tree.getItemCount();
//		for (int i = 0; i < count; i++) {
//			TreeItem ti = tree.getItem(i);
//			collectCell(ti, al);
//		}
//		return al.toArray(new RefMDCellTreeItem[0]);
//	}

	/**
	 * ������Դ������
	 * @param property
	 * @param editor
	 * @param cell
	 */
	public void fireJGraphEditor(String property, NCMDPEditor editor, Cell cell) {
		File file = ((NCMDPEditor) editor).getFile();
		TreeItem ti = ProjectUtil.findMDFileTreeItem(treeView.getTree(), file, false);
		if (ti != null && ti instanceof MDPFileTreeItem) {
			MDPFileTreeItem fileItem = (MDPFileTreeItem) ti;
			if (JGraph.PROP_CHILD_ADD.equals(property)) {
				if (cell.showInExplorerTree()) {
					RefMDCellTreeItem cellItem = new RefMDCellTreeItem(
							fileItem, cell);
					cellItem.setModuleName(fileItem.getModuleName());
					cellItem.setMdFilePath(fileItem.getMDFileRelativePath());
				}
			} else if (JGraph.PROP_CHILD_REMOVE.equals(property)) {
				if (cell.showInExplorerTree()) {
					int count = fileItem.getItemCount();
					for (int i = 0; i < count; i++) {
						RefMDCellTreeItem child = (RefMDCellTreeItem) fileItem
								.getItem(i);
						if (child.getCell().getId().equals(cell.getId())) {
							child.dispose();
							break;
						}
					}
				}
			}
		}
	}

//	/**
//	 * ����������������ͣ������ø÷�����ͨ��Type.getCell()
//	 * @param id
//	 * @return
//	 */
//	public Cell findCellbyId(String id) {
//		RefMDCellTreeItem[] items = getAllCellTreeItem();
//		int count = items == null ? 0 : items.length;
//		MDPLogger.info("MDPExplorerTreeView ���ҵ�������ʵ���cell��������" + count);
//		Cell cell = null;
//		for (int i = 0; i < count; i++) {
//			String tempId = items[i].getCell().getId();
//			if (tempId != null && tempId.equals(id)) {
//				cell = items[i].getCell();
//				break;
//			}
//		}
//		return cell;
//	}

	public void updateCellNode(Cell cell) {
		IEditorPart editor = getViewSite().getPage().getActiveEditor();
		if (editor instanceof NCMDPEditor) {
			File file = ((NCMDPEditor) editor).getFile();
			TreeItem ti = ProjectUtil.findMDFileTreeItem(treeView.getTree(), file, false);
			if (ti != null && ti instanceof MDPFileTreeItem) {
				MDPFileTreeItem fileItem = (MDPFileTreeItem) ti;
				if (!(cell instanceof Reference)) {
					int count = fileItem.getItemCount();
					for (int i = 0; i < count; i++) {
						RefMDCellTreeItem child = (RefMDCellTreeItem) fileItem
								.getItem(i);
						if (child.getCell().getId().equals(cell.getId())) {
							child.update(cell);
							break;
						}
					}

				}
			}
		}
	}

//	/**
//	 * ��λ��������λ����ǰ�༭�����ڽ��б༭��ģ���ļ�
//	 */
//	public void locator() {
//		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
//		if (editor != null) {
//			File file = editor.getFile();
//			TreeItem ti = ProjectUtil.findMDFileTreeItem(file, true);
//			if (ti != null) {
//				treeView.getTree().setSelection(ti);
//			}
//		}
//	}
}
