package ncmdp.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import ncmdp.cache.MDPCachePool;
import ncmdp.common.MDPConstants;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.JGraph;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.internal.runtime.RuntimeLog;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 发布元数据的操作
 * 
 * @author wangxmn
 * 
 */
public class PublishMetaData {
	private boolean finished = false;
	private XPath xpath = null;
	private Exception exce = null;

	/**
	 * 发布元数据
	 * 
	 * @param file
	 * @param allowLowVersionPublish
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	public void publishMetaData(File file, final boolean allowLowVersionPublish)
			throws SecurityException, NoSuchMethodException,
			ClassNotFoundException {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		IProject project = view.getFileOwnProject(file);
		if (project != null) {
			finished = false;
			IWorkbenchPage page = view.getViewSite().getPage();
			IEditorPart[] dirtys = page.getDirtyEditors();
			int count = dirtys == null ? 0 : dirtys.length;
			for (int i = 0; i < count; i++) {
				IEditorPart part = dirtys[i];
				if (part instanceof NCMDPEditor) {
					if (!((NCMDPEditor) part).save()) {
						throw new RuntimeException(Messages.PublishMetaData_0
								+ part.getTitle() + Messages.PublishMetaData_1);
					}
				}
			}
			publishMetaData(file, project, allowLowVersionPublish);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void publishMetaData(File file, IProject project,
			final boolean allowLowVersionPublish) throws SecurityException,
			NoSuchMethodException, ClassNotFoundException{
		
		Job job = new PublishJob(Messages.PublishMetaData_4);
		job.setUser(true);
		job.schedule();
		String errormessage = null;
		List<File> list = new ArrayList<File>();
		try {
			list = new MDFileDependSearcher(true).getListDepends(file);
		} catch (Exception e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					Messages.PublishMetaData_12, e.getMessage());
			job.cancel();
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			File f = (File) list.get(i);
			JGraph graph = JGraphSerializeTool.loadFromFile(f);
			String validateStr = graph.validate();
			if (validateStr != null && validateStr.trim().length() > 0) {
				String message = Messages.PublishMetaData_2 + f.getPath()
						+ "\r\n\r\n" + validateStr;
				throw new RuntimeException(message);
			}
			String code = MDPUtil.getDevCode();
			String industry = graph.getIndustry().getCode();
			if (code == null) {
				errormessage = Messages.PublishMetaData_3;
				break;
			}
			if (!code.equals(industry)
					&& industry != MDPConstants.BASE_INDUSTRY) {
				errormessage = Messages.PublishMetaData_7;
				break;
			}
		}
		if (errormessage != null) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					Messages.PublishMetaData_12, errormessage);
			job.cancel();
			return;
		}
		String clsName = "nc.md.persist.designer.service.PublishServiceHelper";
		final Class cls = ClassTool.loadClass(clsName, project);
		final Method m = cls.getMethod("publishMetaDataForDesigner",
				new Class[] { List.class, boolean.class });
		final List<String> filePathList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			filePathList.add(((File) list.get(i)).getAbsolutePath());
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					exce = null;
					m.invoke(cls, new Object[] { filePathList,
							allowLowVersionPublish });
				} catch (Exception e) {
					finished = true;
					exce = e;
				} finally {
					finished = true;
					filePathList.clear();
				}
			}
		}).start();
	}

	private class MDFileDependSearcher extends DependSearcherSorter {

		public MDFileDependSearcher(boolean ignoreCircle) {
			super(ignoreCircle);
		}

		@Override
		public List<File> getDepend(File obj) throws Exception {
			File file = obj;
			return parseDependFiles(file);
		}

		@Override
		public String getIdentity(File obj) {
			return obj.getAbsolutePath();
		}
	}

	/**
	 * 获得该文件依赖的文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private List<File> parseDependFiles(File file) throws Exception {
		List<File> list = new ArrayList<File>();
		Document doc = getJGraphtDocument(file);
		if (doc != null) {
			NodeList nl = (NodeList) getXPath().evaluate(
					"/component/refdepends/dependfile", doc,// 获得bmf文件中的dependfile标签中记录的实体
					XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				NamedNodeMap map = node.getAttributes();
				// 根据实体的ID查找bmf文件，与文件路径无关 dingxm@2009-08-31
				if (map.getNamedItem(Messages.PublishMetaData_14) == null) {
					throw new RuntimeException(Messages.PublishMetaData_15
							+ file.getName() + Messages.PublishMetaData_16
							+ file.getAbsolutePath()
							+ Messages.PublishMetaData_17);
				}
				String entityID = map.getNamedItem("entityid").getNodeValue();// 获得实体的id
				if (StringUtil.isEmptyWithTrim(entityID)) {
					throw new RuntimeException(Messages.PublishMetaData_19
							+ file.getName() + Messages.PublishMetaData_20);
				}
				String dependFilePath = MDPCachePool.getInstance()// 又会从那可恶的缓存中获取数据
						.getFilePathByIDAndIndustry(entityID, null);
				if (StringUtil.isEmptyWithTrim(dependFilePath)) {
					throw new Exception(Messages.PublishMetaData_21
							+ file.getName() + Messages.PublishMetaData_22
							+ entityID + Messages.PublishMetaData_23);
				}
				File dependFile = new File(dependFilePath);
				if (!dependFile.exists()) {
					throw new Exception(Messages.PublishMetaData_24
							+ file.getName() + Messages.PublishMetaData_25
							+ entityID + Messages.PublishMetaData_26);
				}
				list.add(dependFile);
			}
		}
		return list;
	}

	private XPath getXPath() {
		if (xpath == null) {
			xpath = XPathFactory.newInstance().newXPath();
		}
		return xpath;
	}

	/**
	 * 获得该文件的doc
	 * 
	 * @param file
	 * @return
	 */
	private Document getJGraphtDocument(File file) {
		Document doc = null;
		FileInputStream fis = null;
		if (file.exists()) {
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				fis = new FileInputStream(file);
				doc = db.parse(fis);
			} catch (Exception e) {
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return doc;
	}

	/**
	 * 元数据发布任务
	 * 
	 * @author wangxmn
	 * 
	 */
	private class PublishJob extends Job {
		public PublishJob(String name) {
			super(name);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			int max = 30;
			monitor.beginTask(Messages.PublishMetaData_5, max);
			int count = 0;
			while (!finished && !monitor.isCanceled()) {
				count++;
				if (count > max) {
					monitor.beginTask(Messages.PublishMetaData_6, max);
					count = 0;
				}
				monitor.worked(1);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					MDPLogger.error(e.getMessage(), e);
				}
			}
			monitor.done();
			IStatus result = Status.OK_STATUS;
			if (!monitor.isCanceled()) {
				if (exce != null) {
					String msg = "后台发布元数据出现问题，具体错误如下："
							+ exce.getCause().getMessage();
					result = new Status(Status.ERROR, "NCMDP plugin",
							IStatus.ERROR, msg, exce);
					exce = null;
				} else {
					String msg = Messages.PublishMetaData_8;
					result = new Status(Status.INFO, "NCMDP plugin",
							IStatus.OK, msg, null);
//					RuntimeLog.log(result);
				}
			}
			return result;
		}
	}
}
