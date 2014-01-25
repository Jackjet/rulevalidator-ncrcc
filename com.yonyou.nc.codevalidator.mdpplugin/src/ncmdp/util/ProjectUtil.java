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
	 * �жϵ�ǰ�����ǲ����������
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
	 * ��ù��������е������Ŀ�����manifest.xml
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
	 * �����Դ������������Դ��Ŀǰֻ��MDPExplorer��ͼʹ�á�
	 * �Ͻ�������ʹ��
	 * @param vf ���ֹ�����
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
	 * ��ʼ��tree��ˢ�º�ʹ��ʱʹ��
	 * @param tree
	 */
	public static void initMDPExplorerTree(final Tree tree) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MDPExplorerTreeBuilder.getInstance().buildTree(tree);// ִ�г���
				// ��ʼ���˻���
				MDPCachePool.getInstance().initEntityIDMap();
			}
		});
	}
	
	/**
	 * ����id����Cell��Դ
	 * @param id
	 * @return
	 */
	public static Cell findCellbyId(String id) {
		if(hasOpendMDP == false){
			new MDPExplorerTreeView();
		}
		RefMDCellTreeItem[] items = getAllCellTreeItem();
		int count = items == null ? 0 : items.length;
//		MDPLogger.info("MDPExplorerTreeView ���ҵ�������ʵ���cell��������" + count);
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
	 * ������е��������ͣ�����Դ��������
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
	 * ���item�µ����е�ʵ�壬����Դ��������ʵ�嶼����RefMDCellTreeItem��ʽ����
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
	 * ����Դ��������ͼ
	 */
	public static MDPExplorerTreeView openMDPExplorer(){
		String viewid = MDPExplorerTreeView.class.getCanonicalName();
		if(viewid==null){
			MDPLogger.error("�������˰�������Ҳ��������Ⱑ~~~~~~~~");
			return null;
		}
		IViewPart part = NCMDPTool.showView(viewid);
		if(!(part instanceof MDPExplorerTreeView)){
			MDPLogger.error("�ִ����ˣ�ת��ʧ������");
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
	 * ��λ��������λ����ǰ�༭�����ڽ��б༭��ģ���ļ�
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
	 * �������̣��������е����ݴ��뵽������
	 * @param project
	 * @param xpath
	 * @param map
	 */
	public static void analysisProject(IProject project,XPath xpath, EntityIDIndustryMap map){
		// ��Eclipse�����²���
		File tempFile = new File(project.getLocation().toFile(),
				MDPExplorerTreeBuilder.METADATA_ROOT_DIR);
		putInMapByPath(tempFile, xpath, map);
		if (ProjectUtil.hasCompoentProject(project)) {
			// ����²���
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
						/* ��� */
						NodeList componentlist = (NodeList) xpath.evaluate(
								"/component", doc, XPathConstants.NODESET);
						for (int k = 0; k < componentlist.getLength();) {
							Node node = componentlist.item(k);
							industry = xpath.evaluate("industry", node);
							break;
						}
						/* ʵ�� */
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
						/* ҵ��ӿ� */
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
						/* ö�� */
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
						/* �����ӿ� */
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
						/* ҵ����� */
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
						/* ҵ�� */
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
						/* ��������� */
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
	 * ����ļ���������Ŀ
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
	 * �жϸ�file�Ƿ��ڸ���Ŀ��
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
