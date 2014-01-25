package ncmdp.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import ncmdp.NCMDPActivator;
import ncmdp.dnd.JGraphTransferDropTargetListener;
import ncmdp.factory.ElementEidtPartFactory;
import ncmdp.factory.ElementTreeEditPartFactory;
import ncmdp.factory.PaletteFactory;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.project.MDFileEditInput;
import ncmdp.project.MDPExplorerTreeBuilder;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.project.RefMDFileEditorInput;
import ncmdp.ruler.EditorRulerProvider;
import ncmdp.ruler.Ruler;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.ProjectUtil;
import ncmdp.views.INCMDPViewPage;
import ncmdp.views.MyFindReplaceTarget;
import ncmdp.views.NCMDPViewPage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class NCMDPEditor extends GraphicalEditorWithFlyoutPalette {
	public static final String EDITOR_ID = "ncmdp.editor.NCMDPEditor"; 

	private PaletteRoot paleteRoot = null;//调色板

	private JGraph jgraph = JGraph.getBMFJGraph();

	private KeyHandler shareKeyHandler = null;

	private boolean isOtherDirty = false;
	
	private Boolean isRefResEditor = null;
	
	private RulerComposite rulerComp = null;//网格

	/**
	 * 返回调色板，就是我们进行编辑的工具
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		if (paleteRoot == null) {
			paleteRoot = PaletteFactory.createPaletteRoot(false);
		}
		return paleteRoot;
	}

	public NCMDPEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain()
				.setDefaultTool(new PaletteFactory.CustomSelectionTool());
		
		//默认编辑器打开的时候自动打开mdpexplorer视图
		ProjectUtil.openMDPExplorer();
	}

	public void setTitleImage(Image titleImage) {
		super.setTitleImage(titleImage);

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		String msg = this.jgraph.validate();
		this.jgraph.dealBeforeSave();
		if (msg != null) {
			String message = Messages.NCMDPEditor_1 + msg;
			if (!MessageDialog.openConfirm(getSite().getShell(), Messages.NCMDPEditor_2
					+ getPartName(), message))
				return;
		}
		save();
		setDirty(false);
	}

	/**
	 * 保存编辑器内容
	 * @return
	 */
	public boolean save() {
		IEditorInput input = getEditorInput();
		File file = null;
		if (input instanceof MDFileEditInput) {
			MDFileEditInput fei = (MDFileEditInput) input;
			file = fei.getFile();
			IStatus statu = ResourcesPlugin.getWorkspace().validateEdit(
					new IFile[] { fei.getIFile() }, getSite().getShell());
			if (!statu.isOK()) {
				MessageDialog.openInformation(getSite().getShell(), Messages.NCMDPEditor_3
						+ getPartName(), statu.getMessage());
				return false;

			}
		} else if (input instanceof RefMDFileEditorInput) {
			getCommandStack().markSaveLocation();
			return true;
		}
		try {
			if (file != null) {
				this.jgraph.updateVersion();
				JGraphSerializeTool.saveToFile(this.jgraph, file);
			}
			getCommandStack().markSaveLocation();
			return true;
		} catch (Throwable th) {
			MessageDialog.openError(getSite().getShell(), Messages.NCMDPEditor_4 + getPartName(),
					NCMDPTool.getExceptionRecursionError(th));
			return false;
		}
	}
	
	/**
	 * 判断是否为引用模块下的内容，引用模块为nchome
	 * @return
	 */
	public boolean isRefMDFileEditor() {
		if (isRefResEditor == null) {
			isRefResEditor = Boolean.FALSE;
			IEditorInput input = getEditorInput();
			if (input instanceof RefMDFileEditorInput) {
				isRefResEditor = Boolean.TRUE;
			} else if (input instanceof MDFileEditInput) {
				MDFileEditInput finput = (MDFileEditInput) input;
				IFile file = finput.getIFile();
				IProject project = file.getProject();
				String componengName = finput.getComponentName();
				String relativePath = StringUtil.isEmptyWithTrim(componengName) ? MDPExplorerTreeBuilder.METADATA_ROOT_DIR
						: componengName + "//" //$NON-NLS-1$
								+ MDPExplorerTreeBuilder.METADATA_ROOT_DIR;
				IFolder mdFolder = project.getFolder(relativePath);
				if (!mdFolder.exists()) {
					isRefResEditor = Boolean.TRUE;
				} else {
					isRefResEditor = !file.getLocation().toOSString()
							.startsWith(mdFolder.getLocation().toOSString());
				}
			}
		}
		return isRefResEditor.booleanValue();
	}

	@Override
	public boolean isDirty() {
		if (isRefMDFileEditor()) {
			return false;
		}
		if (isOtherDirty) {
			return true;
		}
		return getCommandStack().isDirty();
	}

	@Override
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action;
		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.LEFT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.RIGHT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.TOP);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.BOTTOM);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.CENTER);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.MIDDLE);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		action = new MatchWidthAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MatchHeightAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

	}

	@Override
	protected ActionRegistry getActionRegistry() {
		return super.getActionRegistry();
	}

	public void setSelect(Cell cell) {
		EditPart ep = findEditPart(getGraphicalViewer().getRootEditPart(), cell);
		if (ep != null) {
			getGraphicalViewer().select(ep);
			getGraphicalViewer().reveal(ep);
		}
	}

	@SuppressWarnings("unchecked")
	private EditPart findEditPart(EditPart ep, Cell cell) {
		Object model = ep.getModel();
		EditPart part = null;
		if (model instanceof Cell
				&& ((Cell) model).getId().equals(cell.getId())) {
			part = ep;
		} else {
			List<EditPart> list = ep.getChildren();
			for (int i = 0; i < list.size(); i++) {
				part = findEditPart(list.get(i), cell);
				if (part != null)
					break;
			}
		}
		return part;
	}

	public static NCMDPEditor getActiveMDPEditor() {
		IWorkbenchPage page = NCMDPActivator.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		IEditorPart editor = null;
		if (page != null) {
			editor = page.getActiveEditor();
		}
		if (editor != null && editor instanceof NCMDPEditor) {
			return (NCMDPEditor) editor;
		} else {
			return null;
		}
	}

	private KeyHandler getShareKeyHandler() {
		if (shareKeyHandler == null) {
			shareKeyHandler = new KeyHandler();
			shareKeyHandler
					.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(
									ActionFactory.DELETE.getId()));
		}
		return shareKeyHandler;
	}

	

	@Override
	protected void createGraphicalViewer(Composite parent) {
		rulerComp = new RulerComposite(parent, SWT.NONE);
		super.createGraphicalViewer(rulerComp);
		rulerComp
				.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());

	}

	@Override
	protected Control getGraphicalControl() {
		return rulerComp;
	}

	/**
	 * 定制editpartViewer
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart rootEditpart = new ScalableFreeformRootEditPart();
		ZoomManager zm = rootEditpart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(zm));
		getActionRegistry().registerAction(new ZoomOutAction(zm));
		List<String> al = new ArrayList<String>();
		al.add(ZoomManager.FIT_WIDTH);
		al.add(ZoomManager.FIT_HEIGHT);
		al.add(ZoomManager.FIT_ALL);
		zm.setZoomLevelContributions(al);
		//设置底层的rootlayer
		getGraphicalViewer().setRootEditPart(rootEditpart);
		//创建editpartfactory，其实就是我们的编辑器视图
		getGraphicalViewer().setEditPartFactory(
				new ElementEidtPartFactory(this));
		//创建鼠标控制事件
		getGraphicalViewer().setKeyHandler(getShareKeyHandler());
		//创建右击popmenu
		getGraphicalViewer().setContextMenu(
				new NCMDPEditorContextMenuProvider(this, rootEditpart
						.getViewer()));
	}

	public GraphicalViewer getGV() {
		return getGraphicalViewer();
	}

	public JGraph getModel() {
		return this.jgraph;
	}

	/**
	 * 初始化
	 */
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(getModel());
		//给graphicaviewer添加作为选择工具拖动目标的事件监听
		getGraphicalViewer().addDropTargetListener(
				new JGraphTransferDropTargetListener(getGraphicalViewer()));
		Ruler leftRuler = getModel().getLeftRuler();
		EditorRulerProvider leftRulerProvider = new EditorRulerProvider(
				leftRuler);
		getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER,
				leftRulerProvider);
		Ruler topRuler = getModel().getTopRuler();
		EditorRulerProvider topRulerProvider = new EditorRulerProvider(topRuler);
		getGraphicalViewer().setProperty(
				RulerProvider.PROPERTY_HORIZONTAL_RULER, topRulerProvider);
		getGraphicalViewer().setProperty(
				RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.FALSE);
		getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED,
				Boolean.FALSE);
		NCMDPTool.showView(IPageLayout.ID_PROP_SHEET);
	}

	IEditorInput mdEditorInput = null;

	private IEditorInput getMDEditorInput(IEditorInput input) {
		if (mdEditorInput == null) {
			try {
				if (input instanceof FileEditorInput) {// from javaView
					FileEditorInput fei = (FileEditorInput) input;
					mdEditorInput = getJavaViewInput(fei);
				} else if (input instanceof MDFileEditInput) {
					mdEditorInput = input;
				} else if (input instanceof RefMDFileEditorInput) {
					mdEditorInput = input;
				}
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		return mdEditorInput;
	}

	private IEditorInput getJavaViewInput(FileEditorInput fileInput) {
		IEditorInput editorInput = null;
		String ncHome = NCMDPTool.getNCHome();
		String realFilePath = fileInput.getPath().toFile().getAbsolutePath();
		if (realFilePath.startsWith(ncHome)) {
			editorInput = new RefMDFileEditorInput(fileInput.getPath().toFile());
		} else {
			editorInput = new MDFileEditInput(fileInput.getPath().toFile(),
					fileInput.getFile(), getModuleName(fileInput));
		}
		return editorInput;
	}

	private String getModuleName(FileEditorInput fileInput) {
		String realFilePath = fileInput.getPath().toFile().getAbsolutePath();
		String proFilePath = fileInput.getFile().getProject().getLocation()
				.toFile().getAbsolutePath();
		if (realFilePath.startsWith(proFilePath)) {
			String tempStr = realFilePath.substring(proFilePath.length() + 1);
			if (tempStr.startsWith(MDPExplorerTreeBuilder.METADATA_ROOT_DIR)) {
				return null;
			} else {
				if (tempStr.indexOf(MDPExplorerTreeBuilder.METADATA_ROOT_DIR) != -1) {
					return tempStr
							.substring(
									0,
									tempStr.indexOf(MDPExplorerTreeBuilder.METADATA_ROOT_DIR) - 1);
				}
			}
		}
		return null;
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(getMDEditorInput(input));
		File file = getFile();
		if (file != null) {
			setPartName(file.getName());
			if (file.getName().trim().toLowerCase()
					.endsWith(Constant.MDFILE_SUFFIX)
					|| file.getName().trim().toLowerCase()
							.endsWith(Constant.MDFILE_BPF_SUFFIX)) {
				this.jgraph = JGraphSerializeTool.loadFromFile(file);
			} else {
				throw new RuntimeException(Messages.NCMDPEditor_6 + file.getPath());
			}
		} else {
			throw new RuntimeException(Messages.NCMDPEditor_7 + input);
		}
		//获得当前的工作页面
		IWorkbenchPage page = getSite().getPage();
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(page);
//		if (view != null) {
//			TreeItem item = MDPExplorerTreeView.getMDPExploerTreeView(page).
//			                findMDFileTreeItem(file);
			TreeItem item = ProjectUtil.findMDFileTreeItem(file);
			if (item instanceof MDPFileTreeItem) {
				MDPFileTreeItem selItem = (MDPFileTreeItem) item;
				String moduleName = selItem.getModuleName();
				if (jgraph.getOwnModule() == null
						|| !jgraph.getOwnModule().equalsIgnoreCase(moduleName)) {
					moduleName = StringUtil.isEmptyWithTrim(moduleName)?"":moduleName; 
					jgraph.setOwnModule(moduleName);
					try {
						JGraphSerializeTool.saveToFile(jgraph, file);
					} catch (Exception e) {
						MDPLogger.error(e.getMessage(), e);
					}
				}
				selItem.setGraph(getModel());
				//其它视图数据发生变动
				MDPExplorerTreeBuilder.getInstance().constructMDPFileChildNode(
						selItem);
			}
//		}
	}

	public File getFile() {
		IEditorInput input = getEditorInput();
		File file = null;
		if (input instanceof MDFileEditInput) {
			MDFileEditInput fei = (MDFileEditInput) input;
			file = fei.getFile();
		} else if (input instanceof RefMDFileEditorInput) {
			RefMDFileEditorInput rfi = (RefMDFileEditorInput) input;
			file = rfi.getFile();
		}
		return file;
	}

	/**
	 * 给调色板添加作为选择工具拖动源的事物监听
	 */
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
						viewer));
			}
		};
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class cls) {
		if (cls == IContentOutlinePage.class) {
			return new MDPJGraphOutlinePage(new TreeViewer());
		} else if (cls == ZoomManager.class) {
			return ((ScalableFreeformRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();
		} else if (cls == INCMDPViewPage.class) {
			return new NCMDPViewPage();
		} else if (cls == IFindReplaceTarget.class) {
			return new MyFindReplaceTarget();
		}
		return super.getAdapter(cls);
	}

	public void setDirty(boolean isDirty) {
		isOtherDirty = isDirty;
		if (isOtherDirty) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	public void executComand(Command cmd) {
		super.getCommandStack().execute(cmd);
	}

	/**
	 * 大纲视图
	 * @author wangxmn
	 *
	 */
	private class MDPJGraphOutlinePage extends ContentOutlinePage {
		private SashForm sash = null;

		private ScrollableThumbnail thumbnail = null;

		private DisposeListener dl = null;

		public MDPJGraphOutlinePage(EditPartViewer viewer) {
			super(viewer);
		}

		@Override
		public void createControl(Composite parent) {
			super.createControl(parent);
			sash = new SashForm(parent, SWT.VERTICAL);
			getViewer().createControl(sash);
			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new ElementTreeEditPartFactory());
			getSelectionSynchronizer().addViewer(getViewer());
			getViewer().setContents(getModel());
			Canvas canvas = new Canvas(sash, SWT.BORDER);
			LightweightSystem lws = new LightweightSystem(canvas);
			ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) getGraphicalViewer()
					.getRootEditPart();
			thumbnail = new ScrollableThumbnail(
					(Viewport) rootEditPart.getFigure());
			thumbnail.setSource(rootEditPart
					.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);
			dl = new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;
					}
				}
			};
			getGraphicalViewer().getControl().addDisposeListener(dl);
		}

		@Override
		public Control getControl() {
			return sash;
		}

		@Override
		public void dispose() {
			getSelectionSynchronizer().removeViewer(getViewer());
			if (getGraphicalViewer().getControl() != null
					&& !getGraphicalViewer().getControl().isDisposed()) {
				getGraphicalViewer().getControl().removeDisposeListener(dl);
			}
			super.dispose();
		}
	}
}
