package ncmdp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import ncmdp.cache.EntityIDIndustryMap;
import ncmdp.cache.MDPCachePool;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.project.MDPExplorerTreeBuilder;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.project.Messages;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.tree.mdptree.IMDPTreeNode;
import ncmdp.ui.tree.mdptree.ProjectTreeItem;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;
import ncmdp.ui.tree.mdptree.RefMDFileDirTreeItem;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProjectUtil {

	public static String META_TAG = "nc.uap.mde.BizCompProjectNature";

	public static String COM_XML_FILE = "manifest.xml";
	
	private static TreeViewer treeViewer = null;
	
	private static boolean hasOpendMDP = false;

	/**
	 * 判断当前工程是不是组件工程
	 * @param project
	 * @return
	 */
	public static boolean hasCompoentProject(IProject project) {
		boolean hasCompoent = false;
		try {
			hasCompoent = project.hasNature(META_TAG);
		} catch (CoreException e) {
			MDPLogger.error(e.getMessage(), e);
		}
		return hasCompoent;
	}

	/**
	 * 获得工程下所有的组件项目，详见manifest.xml
	 * @param dir
	 * @return
	 */
	public static List<File> getMetadataDir(IProject project) {
		if (!hasCompoentProject(project)) { return null; }
		List<File> comDirList = new ArrayList<File>();
		File tempFile = project.getLocation().toFile();
		File maniFile = new File(tempFile, COM_XML_FILE);
		if (maniFile.exists()) {
			FileInputStream fis = null;
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				fis = new FileInputStream(maniFile);
				Document doc = db.parse(fis);
				Node root = doc.getDocumentElement();
				NodeList nl = root.getChildNodes();
				int len = nl.getLength();
				for (int i = 0; i < len; i++) {
					Node node = nl.item(i);
					if ("BusinessComponet".equalsIgnoreCase(node.getNodeName())) {
						NamedNodeMap stMap = node.getAttributes();
						String dirName = stMap.getNamedItem("name").getNodeValue();
						File file = new File(tempFile, dirName);
						if (file.exists()) {
							comDirList.add(file);
						}
					}
				}
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		return comDirList;
	}
	
	/**
	 * 获得资源管理器的树资源，目前只给MDPExplorer视图使用。
	 * 严禁其他人使用
	 * @param vf 布局管理器
	 * @return
	 */
	public static TreeViewer getMDPExplorerTreeViewer(ViewForm vf){
		if(treeViewer == null){
			hasOpendMDP = true;
			treeViewer = new TreeViewer(vf, SWT.MULTI | SWT.H_SCROLL | SWT.H_SCROLL);
			Tree tree = treeViewer.getTree();
			initMDPExplorerTree(tree);
		}
		return treeViewer;
	}
	
	/**
	 * 初始话tree，刷新和使用时使用
	 * @param tree
	 */
	public static void initMDPExplorerTree(final Tree tree) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MDPExplorerTreeBuilder.getInstance().buildTree(tree);// 执行程序
				// 初始化了缓存
				MDPCachePool.getInstance().initEntityIDMap();
			}
		});
	}
	
	/**
	 * 根据id查找Cell资源
	 * @param id
	 * @return
	 */
	public static Cell findCellbyId(String id) {
		if(hasOpendMDP == false){
			new MDPExplorerTreeView();
		}
		RefMDCellTreeItem[] items = getAllCellTreeItem();
		int count = items == null ? 0 : items.length;
//		MDPLogger.info("MDPExplorerTreeView 查找到的所有实体等cell的数量：" + count);
		Cell cell = null;
		for (int i = 0; i < count; i++) {
			String tempId = items[i].getCell().getId();
			if (tempId != null && tempId.equals(id)) {
				cell = items[i].getCell();
				break;
			}
		}
		return cell;
	}
	
	/**
	 * 获得所有的引用类型，在资源管理树中
	 * @return
	 */
	public static RefMDCellTreeItem[] getAllCellTreeItem() {
		if(hasOpendMDP == false){
			new MDPExplorerTreeView();
		}
		List<RefMDCellTreeItem> al = new ArrayList<RefMDCellTreeItem>();
		if(treeViewer !=  null){
			//TODO can't establish tree viewer.
			Tree tree = treeViewer.getTree();
			int count = tree.getItemCount();
			for (int i = 0; i < count; i++) {
				TreeItem ti = tree.getItem(i);
				collectCell(ti, al);
			}
		}
		return al.toArray(new RefMDCellTreeItem[0]);
	}
	
	/**
	 * 获得item下的所有的实体，在资源管理树中实体都是以RefMDCellTreeItem形式存在
	 * @param item
	 * @param al
	 */
	private static void collectCell(TreeItem item, List<RefMDCellTreeItem> al) {
		if (item instanceof RefMDCellTreeItem) {
			RefMDCellTreeItem refItem = (RefMDCellTreeItem) item;
			al.add(refItem);
		}
		int count = item.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem ti = item.getItem(i);
			collectCell(ti, al);
		}
	}
	
	/**
	 * 打开资源管理器视图
	 */
	public static MDPExplorerTreeView openMDPExplorer(){
		String viewid = MDPExplorerTreeView.class.getCanonicalName();
		if(viewid==null){
			MDPLogger.error("出大事了啊，这里也会出现问题啊~~~~~~~~");
			return null;
		}
		IViewPart part = NCMDPTool.showView(viewid);
		if(!(part instanceof MDPExplorerTreeView)){
			MDPLogger.error("粗大事了，转型失败了呢");
			return null;
		}
		return (MDPExplorerTreeView) part;
	}
	
	public static TreeItem findMDFileTreeItem(File file) {
		if(hasOpendMDP == false){
			new MDPExplorerTreeView();
		}
		return findMDFileTreeItem(treeViewer.getTree(), file, false);
	}

	public static TreeItem findMDFileTreeItem(File file, boolean includeRefItem) {
		if(hasOpendMDP == false){
			new MDPExplorerTreeView();
		}
		return findMDFileTreeItem(treeViewer.getTree(), file, includeRefItem);
	}

	public static TreeItem findMDFileTreeItem(Tree tree, File file,
			boolean includRefItem) {
		int count = tree.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem item = tree.getItem(i);
			TreeItem ti = findMDPFileTreeItem(item, file, includRefItem);
			if (ti != null)
				return ti;
		}
		return null;
	}
	
	public static TreeItem findMDPFileTreeItem(TreeItem item, File file,
			boolean includRefItem) {
		if (item instanceof IMDPTreeNode) {
			File itemFile = ((IMDPTreeNode) item).getFile();
			if (itemFile.equals(file)) {
				return item;
			} else {
				int count = item.getItemCount();
				for (int i = 0; i < count; i++) {
					TreeItem child = item.getItem(i);
					TreeItem ti = findMDPFileTreeItem(child, file,
							includRefItem);
					if (ti != null)
						return ti;
				}
			}
		} else if (includRefItem) {
			if (item instanceof RefMDFileDirTreeItem) {
				File itemFile = ((RefMDFileDirTreeItem) item).getFile();
				if (itemFile.equals(file)) {
					return item;
				} else {
					int count = item.getItemCount();
					for (int i = 0; i < count; i++) {
						TreeItem child = item.getItem(i);
						TreeItem ti = findMDPFileTreeItem(child, file,
								includRefItem);
						if (ti != null)
							return ti;
					}
				}
			} else if (item.getText().equals(Messages.MDPExplorerTreeView_33)) {
				int count = item.getItemCount();
				for (int i = 0; i < count; i++) {
					TreeItem child = item.getItem(i);
					TreeItem ti = findMDPFileTreeItem(child, file,
							includRefItem);
					if (ti != null)
						return ti;
				}
			}
		}
		return null;
	}
	
	/**
	 * 定位操作，定位到当前编辑器正在进行编辑的模型文件
	 */
	public static void locator() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if(hasOpendMDP == false){
			new MDPExplorerTreeView();
		}
		if (editor != null) {
			File file = editor.getFile();
			TreeItem ti = ProjectUtil.findMDFileTreeItem(file, true);
			if (ti != null) {
				treeViewer.getTree().setSelection(ti);
			}
		}
	}
	
	/**
	 * 解析工程，并将其中的内容存入到缓存中
	 * @param project
	 * @param xpath
	 * @param map
	 */
	public static void analysisProject(IProject project,XPath xpath, EntityIDIndustryMap map){
		// 在Eclipse工程下查找
		File tempFile = new File(project.getLocation().toFile(),
				MDPExplorerTreeBuilder.METADATA_ROOT_DIR);
		putInMapByPath(tempFile, xpath, map);
		if (ProjectUtil.hasCompoentProject(project)) {
			// 组件下查找
			List<File> comDirList = ProjectUtil.getMetadataDir(project);
			for (File dir : comDirList) {
				File comMeta = new File(dir,
						MDPExplorerTreeBuilder.METADATA_ROOT_DIR);
				if (comMeta.exists()) {
					putInMapByPath(comMeta, xpath, map);
				}
			}
		}
	}
	
	public static void putInMapByPath(File mdDir, XPath xpath,
			EntityIDIndustryMap entityid_bmffile_map) {
		if (mdDir.exists()) {
			List<File> bmfFiles = NCMDPTool.listFiles(mdDir,
					new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.endsWith(Constant.MDFILE_SUFFIX)
									|| name.endsWith(Constant.MDFILE_BPF_SUFFIX);
						}
					});
			for (int j = 0; j < bmfFiles.size(); j++) {
				Document doc = getJGraphtDocument(bmfFiles.get(j));
				String industry = null;
				if (doc != null) {
					try {
						/* 组件 */
						NodeList componentlist = (NodeList) xpath.evaluate(
								"/component", doc, XPathConstants.NODESET);
						for (int k = 0; k < componentlist.getLength();) {
							Node node = componentlist.item(k);
							industry = xpath.evaluate("industry", node);
							break;
						}
						/* 实体 */
						NodeList entitylist = (NodeList) xpath.evaluate(
								"/component/celllist/entity", doc,
								XPathConstants.NODESET);
						for (int k = 0; k < entitylist.getLength(); k++) {
							Node node = entitylist.item(k);
							NamedNodeMap map = node.getAttributes();
							if (map.getNamedItem("id") != null) {
								entityid_bmffile_map.put(map.getNamedItem("id")
										.getNodeValue(), industry, bmfFiles
										.get(j).getAbsolutePath());
							}
						}
						/* 业务接口 */
						NodeList itfList = (NodeList) xpath.evaluate(
								"/component/celllist/busiIterface", doc,
								XPathConstants.NODESET);
						for (int k = 0; k < itfList.getLength(); k++) {
							Node node = itfList.item(k);
							NamedNodeMap map = node.getAttributes();
							if (map.getNamedItem("id") != null) {
								String tempEntityID = map.getNamedItem("id")
										.getNodeValue();
								entityid_bmffile_map.put(tempEntityID,
										industry, bmfFiles.get(j)
												.getAbsolutePath());
							}
						}
						/* 枚举 */
						NodeList enumList = (NodeList) xpath.evaluate(
								"/component/celllist/Enumerate", doc,
								XPathConstants.NODESET);
						for (int k = 0; k < enumList.getLength(); k++) {
							Node node = enumList.item(k);
							NamedNodeMap map = node.getAttributes();
							if (map.getNamedItem("id") != null) {
								String tempEntityID = map.getNamedItem("id")
										.getNodeValue();
								entityid_bmffile_map.put(tempEntityID,
										industry, bmfFiles.get(j)
												.getAbsolutePath());
							}
						}
						/* 操作接口 */
						NodeList opItfList = (NodeList) xpath.evaluate(
								"/component/celllist/opinterface", doc,
								XPathConstants.NODESET);
						for (int k = 0; k < opItfList.getLength(); k++) {
							Node node = opItfList.item(k);
							NamedNodeMap map = node.getAttributes();
							if (map.getNamedItem("id") != null) {
								String tempEntityID = map.getNamedItem("id")
										.getNodeValue();
								entityid_bmffile_map.put(tempEntityID,
										industry, bmfFiles.get(j)
												.getAbsolutePath());
							}
						}
						/* 业务操作 */
						NodeList busiOperationList = (NodeList) xpath.evaluate(
								"/component/celllist/busioperation", doc,
								XPathConstants.NODESET);
						for (int k = 0; k < busiOperationList.getLength(); k++) {
							Node node = busiOperationList.item(k);
							NamedNodeMap map = node.getAttributes();
							if (map.getNamedItem("id") != null) {
								String tempEntityID = map.getNamedItem("id")
										.getNodeValue();
								entityid_bmffile_map.put(tempEntityID,
										industry, bmfFiles.get(j)
												.getAbsolutePath());
							}
						}
						/* 业务活动 */
						NodeList busiActivityList = (NodeList) xpath.evaluate(
								"/component/celllist/busiActivity", doc,
								XPathConstants.NODESET);
						for (int k = 0; k < busiActivityList.getLength(); k++) {
							Node node = busiActivityList.item(k);
							NamedNodeMap map = node.getAttributes();
							if (map.getNamedItem("id") != null) {
								String tempEntityID = map.getNamedItem("id")
										.getNodeValue();
								entityid_bmffile_map.put(tempEntityID,
										industry, bmfFiles.get(j)
												.getAbsolutePath());
							}
						}
						/* 其他情况？ */
					} catch (XPathExpressionException e) {
						MDPLogger.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	public static Document getJGraphtDocument(File file) {
		Document doc = null;
		FileInputStream fis = null;
		if (file.exists()) {
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				fis = new FileInputStream(file);
				doc = db.parse(fis);
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						MDPLogger.error(e.getMessage(), e);
					}
				}
			}
		}
		return doc;
	}
	
	/**
	 * 获得文件所属的项目
	 * 
	 * @param file
	 * @return
	 */
	public static IProject getFileOwnProject(File file) {
		IProject project = null;
		Tree tree = treeViewer.getTree();
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
	 * 判断该file是否在该项目下
	 * 
	 * @param item
	 * @param file
	 * @return
	 */
	private static boolean containsFile(TreeItem item, File file) {
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
}
