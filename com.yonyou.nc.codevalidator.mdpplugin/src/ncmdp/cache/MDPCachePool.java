package ncmdp.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import ncmdp.model.Constant;
import ncmdp.project.BuildFileTreeUtil;
import ncmdp.project.MDPExplorerTreeBuilder;
import ncmdp.project.MDPFileFilter;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ����һЩ��Ϣ
 * 
 * @author dingxm
 * 
 */
public class MDPCachePool {

	// ����ģ���µ�
	private EntityIDIndustryMap entityid_bmffile_map = null;

	// nchome�µ�
	private EntityIDIndustryMap entityid_bmffile_NCHOME_map = null;

	private static MDPCachePool instance = new MDPCachePool();
	// ThreadLocal
	ThreadLocal<HashMap<String, Object>> curLocal = null;

	public HashMap<String, Object> getLocalCache() {
		if (curLocal == null) {
			curLocal = new ThreadLocal<HashMap<String, Object>>() {
				@Override
				protected HashMap<String, Object> initialValue() {
					return new HashMap<String, Object>();
				}
			};
		}
		return curLocal.get();
	}

	private MDPCachePool() {
	}

	public static MDPCachePool getInstance() {// ����̬������ν�̰߳�ȫ
		return instance;
	}

	/**
	 * ����id�͵�ǰ��ҵ ����ļ�·��
	 * 
	 * @return
	 */
	public String getFilePathByIDAndIndustry(String entityID, String industry) {
		String filePath = getEntityIDMap().get(entityID, industry);
		if (StringUtil.isEmptyWithTrim(filePath)) {
			filePath = getEntityIDMapFromNCHOME().get(entityID, industry);
		}
		return filePath;
	}

	public EntityIDIndustryMap getEntityIDMapFromNCHOME() {
		if (entityid_bmffile_NCHOME_map == null) {
			initEntityIDMap();
		}
		return entityid_bmffile_NCHOME_map;
	}

	/**
	 * ��ù���ģ���µĻ���
	 * 
	 * @return
	 */
	public EntityIDIndustryMap getEntityIDMap() {
		if (entityid_bmffile_map == null) {
			initEntityIDMap();
		}
		return entityid_bmffile_map;
	}

	// ��ʼ�����棬ˢ�»���
	public void initEntityIDMap() {
		initEntityIDMapData();
	}

	/**
	 * ��ʼ�� entityid_bmffile_map
	 */
	private void initEntityIDMapData() {
		XPath xpath = XPathFactory.newInstance().newXPath();
		// 1)��������ģ���µ�����ʵ��
		initRefMDFiles(xpath);
		// 2)���������µ�����ʵ��
		initProjectFiles(xpath);

	}

	/**
	 * ��ʼ�����ã����м���µ����е�Ԫ�����ļ�
	 * @param xpath
	 */
	private void initRefMDFiles(XPath xpath) {// entityid_bmffile_NCHOME_map
		// 1�����ļ���ʼ������
		String ncHome = NCMDPTool.getNCHome();
		String fileCachePath = ncHome + "/ierp/metadata/mdp_entityid_Cache.obj";
		String bmfTSCachePath = ncHome + "/ierp/metadata/mdp_bmf_ts_Cache.obj";
		File entityIDCacheFile = new File(fileCachePath);
		File bmfTSCacheFile = new File(bmfTSCachePath);
//		boolean fileCacheSuccess = false;
//		if (entityIDCacheFile.exists() && bmfTSCacheFile.exists()) {
			if (entityIDCacheFile.exists()) {
			// ����ļ��汾
//			boolean hasUpdate = checkBmfFileTSChanged(bmfTSCacheFile);
//			if (!hasUpdate) {// ���ļ�ȡ����
				entityid_bmffile_NCHOME_map = (EntityIDIndustryMap) getObjectFromFile(entityIDCacheFile);
//			}
//			if (entityid_bmffile_NCHOME_map != null) {
//				fileCacheSuccess = true;
//			}
		}
		// 2����ʼ�����ɹ�������������ļ�,��д���ļ�����
//		if (!fileCacheSuccess) {
			if (!entityIDCacheFile.exists()) {
			// ���������ļ� ����ʼ��EntityID map
			entityid_bmffile_NCHOME_map = new EntityIDIndustryMap();
			File[] refDir = BuildFileTreeUtil.getRefMDFiles();
			int count = refDir.length;
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					File f = refDir[i];
					ProjectUtil.putInMapByPath(f, xpath, entityid_bmffile_NCHOME_map);
				}
			}
			// ��ʼ�� ts map
//			Map<String, String> fileTSMap = new HashMap<String, String>();
//			if (refDir.length > 0) {// ģ��ѭ��
//				for (int i = 0; i < refDir.length; i++) {
//					List<File> moduleBmfFiles = NCMDPTool.listFiles(refDir[i],
//							new FilenameFilter() {
//								@Override
//								public boolean accept(File dir, String name) {
//									return name
//											.endsWith(Constant.MDFILE_SUFFIX)
//											|| name.endsWith(Constant.MDFILE_BPF_SUFFIX);
//								}
//							});
//					for (File bmfFile : moduleBmfFiles) {
//						fileTSMap.put(bmfFile.getAbsolutePath(),
//								Long.toString(bmfFile.lastModified()));
//					}
//				}
//			}
			// д�뻺��
//			if (bmfTSCacheFile.exists()) {
//				bmfTSCacheFile.delete();
//			}
			if (entityIDCacheFile.exists()) {
				entityIDCacheFile.delete();
			}
			try {
//				bmfTSCacheFile.createNewFile();
				entityIDCacheFile.createNewFile();
			} catch (IOException e) {
				MDPLogger.error(e.getMessage(), e);
			}
			ObjectOutputStream obs1 = null;
			ObjectOutputStream obs2 = null;
			try {
//				obs1 = new ObjectOutputStream(new BufferedOutputStream(
//						new FileOutputStream(bmfTSCacheFile)));
				obs2 = new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(entityIDCacheFile)));
//				obs1.writeObject(fileTSMap);
				obs2.writeObject(entityid_bmffile_NCHOME_map);
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			} finally {
				if (obs1 != null) {
					try {
						obs1.close();
					} catch (IOException e) {}
				}
				if (obs2 != null) {
					try {
						obs2.close();
					} catch (IOException e) {}
				}
			}
		}
	}

	/**
	 * ����ļ�ϵͳ�Ƿ�ı�
	 */
//	@SuppressWarnings("unchecked")
//	private boolean checkBmfFileTSChanged(File bmfTSCacheFile) {
//		boolean hasUpdated = false;
//		Map<String, String> fileTSMap = (Map<String, String>) getObjectFromFile(bmfTSCacheFile);
//		if (fileTSMap == null || fileTSMap.size() == 0) {
//			return true;
//		}
//		List<File> bmfFiles = new ArrayList<File>();
//		File[] refDir = BuildFileTreeUtil.getRefMDFiles();
//		if (refDir.length > 0) {// ģ��ѭ��
//			for (int i = 0; i < refDir.length; i++) {
//				List<File> moduleBmfFiles = NCMDPTool.listFiles(refDir[i],
//						new FilenameFilter() {
//							@Override
//							public boolean accept(File dir, String name) {
//								return name.endsWith(Constant.MDFILE_SUFFIX)
//										|| name.endsWith(Constant.MDFILE_BPF_SUFFIX);
//							}
//						});
//				bmfFiles.addAll(moduleBmfFiles);
//			}
//		}
//		if (bmfFiles.size() == fileTSMap.size()) {
//			for (File bmfFile : bmfFiles) {
//				String filePath = bmfFile.getAbsolutePath();
//				if (fileTSMap.containsKey(filePath)
//						&& fileTSMap.get(filePath).equalsIgnoreCase(
//								Long.toString(bmfFile.lastModified()))) {
//					// �ļ�δ�޸�
//					continue;
//				} else {
//					return true;
//				}
//			}
//		}
//		return hasUpdated;
//	}

	private Object getObjectFromFile(File bmfTSCacheFile) {
		ObjectInputStream obs = null;
		Object obj = null;
		try {
			obs = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(bmfTSCacheFile)));
			obj = obs.readObject();
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		} finally {
			if (obs != null) {
				try {
					obs.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(), e);
				}
			}
		}
		return obj;
	}

	private void initProjectFiles(XPath xpath) {
		entityid_bmffile_map = new EntityIDIndustryMap();
		IProject[] projects = NCMDPTool.getOpenedJavaProjects();
		int procount = projects == null ? 0 : projects.length;
		for (int i = 0; i < procount; i++) {
//			IProject project = projects[i];
//			// ��Eclipse�����²���
//			File tempFile = new File(project.getLocation().toFile(),
//					MDPExplorerTreeBuilder.METADATA_ROOT_DIR);
//			putInMapByPath(tempFile, xpath, entityid_bmffile_map);
//			if (ProjectUtil.hasCompoentProject(project)) {
//				// ����²���
//				List<File> comDirList = ProjectUtil.getMetadataDir(project);
//				for (File dir : comDirList) {
//					File comMeta = new File(dir,
//							MDPExplorerTreeBuilder.METADATA_ROOT_DIR);
//					if (comMeta.exists()) {
//						putInMapByPath(comMeta, xpath, entityid_bmffile_map);
//					}
//				}
//			}
			ProjectUtil.analysisProject(projects[i], xpath, entityid_bmffile_map);
		}
	}

	/**
	 * ��Ŀ¼�µ�����bmf�е�ʵ����뵽 entityid_bmffile_map
	 * @param mdDir
	 */
//	private void putInMapByPath(File mdDir, XPath xpath,
//			EntityIDIndustryMap entityid_bmffile_map) {
//		if (mdDir.exists()) {
//			List<File> bmfFiles = NCMDPTool.listFiles(mdDir,
//					new FilenameFilter() {
//						@Override
//						public boolean accept(File dir, String name) {
//							return name.endsWith(Constant.MDFILE_SUFFIX)
//									|| name.endsWith(Constant.MDFILE_BPF_SUFFIX);
//						}
//					});
//			for (int j = 0; j < bmfFiles.size(); j++) {
//				Document doc = getJGraphtDocument(bmfFiles.get(j));
//				String industry = null;
//				if (doc != null) {
//					try {
//						/* ��� */
//						NodeList componentlist = (NodeList) xpath.evaluate(
//								"/component", doc, XPathConstants.NODESET);
//						for (int k = 0; k < componentlist.getLength();) {
//							Node node = componentlist.item(k);
//							industry = xpath.evaluate("industry", node);
//							break;
//						}
//						/* ʵ�� */
//						NodeList entitylist = (NodeList) xpath.evaluate(
//								"/component/celllist/entity", doc,
//								XPathConstants.NODESET);
//						for (int k = 0; k < entitylist.getLength(); k++) {
//							Node node = entitylist.item(k);
//							NamedNodeMap map = node.getAttributes();
//							if (map.getNamedItem("id") != null) {
//								entityid_bmffile_map.put(map.getNamedItem("id")
//										.getNodeValue(), industry, bmfFiles
//										.get(j).getAbsolutePath());
//							}
//						}
//						/* ҵ��ӿ� */
//						NodeList itfList = (NodeList) xpath.evaluate(
//								"/component/celllist/busiIterface", doc,
//								XPathConstants.NODESET);
//						for (int k = 0; k < itfList.getLength(); k++) {
//							Node node = itfList.item(k);
//							NamedNodeMap map = node.getAttributes();
//							if (map.getNamedItem("id") != null) {
//								String tempEntityID = map.getNamedItem("id")
//										.getNodeValue();
//								entityid_bmffile_map.put(tempEntityID,
//										industry, bmfFiles.get(j)
//												.getAbsolutePath());
//							}
//						}
//						/* ö�� */
//						NodeList enumList = (NodeList) xpath.evaluate(
//								"/component/celllist/Enumerate", doc,
//								XPathConstants.NODESET);
//						for (int k = 0; k < enumList.getLength(); k++) {
//							Node node = enumList.item(k);
//							NamedNodeMap map = node.getAttributes();
//							if (map.getNamedItem("id") != null) {
//								String tempEntityID = map.getNamedItem("id")
//										.getNodeValue();
//								entityid_bmffile_map.put(tempEntityID,
//										industry, bmfFiles.get(j)
//												.getAbsolutePath());
//							}
//						}
//						/* �����ӿ� */
//						NodeList opItfList = (NodeList) xpath.evaluate(
//								"/component/celllist/opinterface", doc,
//								XPathConstants.NODESET);
//						for (int k = 0; k < opItfList.getLength(); k++) {
//							Node node = opItfList.item(k);
//							NamedNodeMap map = node.getAttributes();
//							if (map.getNamedItem("id") != null) {
//								String tempEntityID = map.getNamedItem("id")
//										.getNodeValue();
//								entityid_bmffile_map.put(tempEntityID,
//										industry, bmfFiles.get(j)
//												.getAbsolutePath());
//							}
//						}
//						/* ҵ����� */
//						NodeList busiOperationList = (NodeList) xpath.evaluate(
//								"/component/celllist/busioperation", doc,
//								XPathConstants.NODESET);
//						for (int k = 0; k < busiOperationList.getLength(); k++) {
//							Node node = busiOperationList.item(k);
//							NamedNodeMap map = node.getAttributes();
//							if (map.getNamedItem("id") != null) {
//								String tempEntityID = map.getNamedItem("id")
//										.getNodeValue();
//								entityid_bmffile_map.put(tempEntityID,
//										industry, bmfFiles.get(j)
//												.getAbsolutePath());
//							}
//						}
//						/* ҵ�� */
//						NodeList busiActivityList = (NodeList) xpath.evaluate(
//								"/component/celllist/busiActivity", doc,
//								XPathConstants.NODESET);
//						for (int k = 0; k < busiActivityList.getLength(); k++) {
//							Node node = busiActivityList.item(k);
//							NamedNodeMap map = node.getAttributes();
//							if (map.getNamedItem("id") != null) {
//								String tempEntityID = map.getNamedItem("id")
//										.getNodeValue();
//								entityid_bmffile_map.put(tempEntityID,
//										industry, bmfFiles.get(j)
//												.getAbsolutePath());
//							}
//						}
//						/* ��������� */
//					} catch (XPathExpressionException e) {
//						MDPLogger.error(e.getMessage(), e);
//					}
//				}
//			}
//		}
//	}

//	private File[] getRefMDFiles() {
//		ArrayList<File> al = new ArrayList<File>();
//		String[] moduleNames = NCMDPTool.getExModuleNames();
//		ArrayList<String> alModules = new ArrayList<String>(
//				Arrays.asList(moduleNames));
//		String ncHomeStr = NCMDPTool.getNCHome();
//		File dir = new File(ncHomeStr + File.separator + "modules");
//		File[] childs = dir.listFiles(new MDPFileFilter());
//		int count = childs == null ? 0 : childs.length;
//		for (int i = 0; i < count; i++) {
//			File f = childs[i];
//			if (!alModules.contains(f.getName())) {
//				al.add(f);
//			}
//		}
//		return al.toArray(new File[0]);
//	}

//	private static Document getJGraphtDocument(File file) {
//		Document doc = null;
//		FileInputStream fis = null;
//		if (file.exists()) {
//			try {
//				DocumentBuilder db = DocumentBuilderFactory.newInstance()
//						.newDocumentBuilder();
//				fis = new FileInputStream(file);
//				doc = db.parse(fis);
//			} catch (Exception e) {
//				MDPLogger.error(e.getMessage(), e);
//			} finally {
//				if (fis != null) {
//					try {
//						fis.close();
//					} catch (IOException e) {
//						MDPLogger.error(e.getMessage(), e);
//					}
//				}
//			}
//		}
//		return doc;
//	}
}
