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
	 * �������е���Ŀ������������ģ����Ŀ�������Ŀ
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
	 * ������Դ���� ��
	 * @param tree
	 */
	public void buildTree(Tree tree) {
		//��õ�ǰ�����ռ��е���Ŀ
		List<ProjectModel> list = loadProjectModels();
		for (int i = 0; i < list.size(); i++) {
			ProjectModel pm = list.get(i);
			//�������ڵ�
			ProjectTreeItem ti = new ProjectTreeItem(tree, pm);
			//��ʼ�������൱�ڴ�������������Ҷ�ӽڵ�
			initProjectTree(ti);
			// project/component
			IProject project = pm.getJavaProject();
			//�ж��Ƿ�Ϊmde����
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
		//������õ���
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
	 * ��ʼ�����õ�Ԫ�����ļ�
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
	 * ����NCHome�µ�Ԫ�����ļ�
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
	 * ����item����Ϣ��������ʼ�����е�JGraph����ȥ��ȡ��Ϣ
	 * @param item
	 */
	public void constructMDPFileChildNode(MDPFileTreeItem item) {
		item.removeAll();
		JGraph graph = item.getGraph();//��ȡbmf�ļ���Ϣ
		List<Cell> list = graph.getCells();
		for (int i = 0; i < list.size(); i++) {
			Cell cell = list.get(i);
			if (cell.showInExplorerTree()) {//�������Դ����������Ϊ�ӽڵ���ʾ
				RefMDCellTreeItem cellItem = new RefMDCellTreeItem(item, cell);//��Ϊ��������
				cellItem.setModuleName(item.getModuleName());
				cellItem.setMdFilePath(item.getMDFileRelativePath());
			}
		}
	}

	/**
	 * ��ʼ�������Ŀ 
	 * @param projectRoot
	 */
	private void initProjectTree(ProjectTreeItem projectRoot) {
		IFolder folder = projectRoot.getRootFolder();//��ø��ڵ��ifolder
		initProjectSubTree(projectRoot, folder, projectRoot.getProjectModel()
				.getComDirName());
	}

	/**
	 * ��ʼ�����ڵ����ӽڵ㣬ȫ�����أ���������
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
	 * ����mdpfile��Ҷ�ӽڵ㣬������jgraph
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
