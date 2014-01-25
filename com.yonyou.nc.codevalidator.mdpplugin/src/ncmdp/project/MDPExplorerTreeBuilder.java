package ncmdp.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.tree.mdptree.DirectoryTreeItem;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.ui.tree.mdptree.ProjectTreeItem;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;
import ncmdp.ui.tree.mdptree.RefMDFileDirTreeItem;
import ncmdp.ui.tree.mdptree.RefMDRootTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class MDPExplorerTreeBuilder {
	private static MDPExplorerTreeBuilder instance = new MDPExplorerTreeBuilder();

	public static final String METADATA_ROOT_DIR = "METADATA";

	private MDPExplorerTreeBuilder() {
		super();
	}

	public static MDPExplorerTreeBuilder getInstance() {
		return instance;
	}

	/**
	 * 加载所有的项目，包括完整的模块项目及组件项目
	 * @return
	 */
	public List<ProjectModel> loadProjectModels() {
		ArrayList<ProjectModel> al = new ArrayList<ProjectModel>();
		IProject[] projects = NCMDPTool.getOpenedJavaProjects();
		int count = projects == null ? 0 : projects.length;
		for (int i = 0; i < count; i++) {
			ProjectModel model = new ProjectModel(projects[i]);
			al.add(model);
		}
		return al;
	}

	/**
	 * 创建资源管理 树
	 * @param tree
	 */
	public void buildTree(Tree tree) {
		//获得当前工作空间中的项目
		List<ProjectModel> list = loadProjectModels();
		for (int i = 0; i < list.size(); i++) {
			ProjectModel pm = list.get(i);
			//创建根节点
			ProjectTreeItem ti = new ProjectTreeItem(tree, pm);
			//初始化树，相当于创建整个树，到叶子节点
			initProjectTree(ti);
			// project/component
			IProject project = pm.getJavaProject();
			//判断是否为mde工程
			if (ProjectUtil.hasCompoentProject(project)) {
				List<File> fileList = ProjectUtil.getMetadataDir(project);
				for (File file : fileList) {
					ProjectModel comPm = new ProjectModel(project);
					comPm.setComDirName(file.getName());
					ProjectTreeItem comTi = new ProjectTreeItem(tree, comPm);
					comTi.setText(project.getName() + "." + file.getName()
							+ "[" + comTi.getModuleName() + "]");
					initProjectTree(comTi);
				}
			}
		}
		//获得引用的树
		File[] refDir = BuildFileTreeUtil.getRefMDFiles();
		int count = refDir.length;
		if (count > 0) {
			RefMDRootTreeItem refRoot = new RefMDRootTreeItem(tree, SWT.NONE);
			for (int i = 0; i < count; i++) {
				File f = refDir[i];
				RefMDFileDirTreeItem item = new RefMDFileDirTreeItem(refRoot, f);
				String moduleName = f.getName();
				File mdDir = new File(f, METADATA_ROOT_DIR);
				if (mdDir.exists()) {
					initRefSubTree(item, mdDir, moduleName);
				}
			}
		}
	}

	/**
	 * 初始化引用的元数据文件
	 * @param parent
	 * @param f
	 * @param moduleName
	 */
	private void initRefSubTree(TreeItem parent, File f, String moduleName) {
		File[] childs = f.listFiles(new MDPFileFilter());
		int count = childs == null ? 0 : childs.length;
		for (int i = 0; i < count; i++) {
			File child = childs[i];
			TreeItem ti = new RefMDFileDirTreeItem(parent, child);
			if (child.isDirectory()) {
				initRefSubTree(ti, child, moduleName);
			} else {
				constructRefMDFileCellTreeItem(ti, moduleName);
			}
		}
	}

	/**
	 * 构造NCHome下的元数据文件
	 * @param parent
	 * @param moduleName
	 */
	private void constructRefMDFileCellTreeItem(TreeItem parent,
			String moduleName) {
		if (parent instanceof RefMDFileDirTreeItem) {
			RefMDFileDirTreeItem pItem = (RefMDFileDirTreeItem) parent;
			File mdFile = pItem.getFile();
			JGraph graph = JGraphSerializeTool.loadFromFile(mdFile, true);
			List<Cell> cells = graph.getCells();
			for (int i = 0; i < cells.size(); i++) {
				Cell cell = cells.get(i);
				if (cell.showInExplorerTree()) {
					String fPath = mdFile.getAbsolutePath();
					String ncHome = NCMDPTool.getNCHome();
					String mdFilePath = fPath.substring((ncHome
							+ File.separator + "modules/" + moduleName
							+ File.separator + "METADATA").length());
					RefMDCellTreeItem item = new RefMDCellTreeItem(pItem, cell);
					item.setModuleName(moduleName);
					item.setMdFilePath(mdFilePath);
				}
			}
		}
	}

	/**
	 * 构建item的信息，包括初始化其中的JGraph，即去读取信息
	 * @param item
	 */
	public void constructMDPFileChildNode(MDPFileTreeItem item) {
		item.removeAll();
		JGraph graph = item.getGraph();//读取bmf文件信息
		List<Cell> list = graph.getCells();
		for (int i = 0; i < list.size(); i++) {
			Cell cell = list.get(i);
			if (cell.showInExplorerTree()) {//如果在资源管理树中作为子节点显示
				RefMDCellTreeItem cellItem = new RefMDCellTreeItem(item, cell);//设为引用类型
				cellItem.setModuleName(item.getModuleName());
				cellItem.setMdFilePath(item.getMDFileRelativePath());
			}
		}
	}

	/**
	 * 初始化组件项目 
	 * @param projectRoot
	 */
	private void initProjectTree(ProjectTreeItem projectRoot) {
		IFolder folder = projectRoot.getRootFolder();//获得根节点的ifolder
		initProjectSubTree(projectRoot, folder, projectRoot.getProjectModel()
				.getComDirName());
	}

	/**
	 * 初始化根节点下子节点，全部加载，非懒加载
	 * @param parent
	 * @param res
	 * @param componentName
	 */
	private void initProjectSubTree(TreeItem parent, IResource res,
			String componentName) {
		if (res instanceof IFolder) {
			IResource[] reses = null;
			try {
				reses = ((IFolder) res).members();
			} catch (CoreException e) {
				MDPLogger.error(e.getMessage(), e);
			}
			int count = reses == null ? 0 : reses.length;
			for (int i = 0; i < count; i++) {
				IResource childRes = reses[i];
				TreeItem ti = null;
				if (childRes instanceof IFolder) {
					if (!((IFolder) childRes).getLocation().toFile().exists()) {
						continue;
					}
					ti = new DirectoryTreeItem(parent, (IFolder) childRes);
					initProjectSubTree(ti, childRes, componentName);
				} else if (childRes instanceof IFile) {
					IFile ifile = (IFile) childRes;
					if (!ifile.getLocation().toFile().exists()) {
						continue;
					}
					if (ifile.getFileExtension() == null
							|| !(ifile.getFileExtension().equalsIgnoreCase(
									Constant.MDFILE_SUFFIX_EXTENTION) || ifile
									.getFileExtension()
									.equalsIgnoreCase(
											Constant.MDFILE_BPF_SUFFIX_EXTENTION)))
						continue;
					ti = createFileTreeItem(parent, ifile, componentName);
					constructMDPFileChildNode((MDPFileTreeItem) ti);
				}
			}
		}
	}

	/**
	 * 创建mdpfile的叶子节点，并设置jgraph
	 * @param parent
	 * @param ifile
	 * @param componentName
	 * @return
	 */
	private MDPFileTreeItem createFileTreeItem(TreeItem parent, IFile ifile,
			String componentName) {
		MDPFileTreeItem ti = new MDPFileTreeItem(parent, ifile);
		File file = ti.getFile();
		JGraph graph = JGraphSerializeTool.loadFromFile(file, true);
		((MDPFileTreeItem) ti).setGraph(graph);
		return ti;
	}

}
