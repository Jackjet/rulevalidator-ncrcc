package ncmdp.serialize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import ncmdp.model.Attribute;
import ncmdp.model.BusinInterface;
import ncmdp.model.Feature;
import ncmdp.model.Reference;
import ncmdp.model.Type;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPCommonUtil;
import ncmdp.util.MDPLogger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FeatureConfig {
	private static Feature[] features = null;

	public synchronized static Feature[] getFeatures() {
		if (features == null) {
			features = parseFeatures();
		}
		return features;
	}

	public static Feature[] getFeatures(String moduleName) {
		features = parseFeatures(moduleName);
		return features;
	}

	private static Feature[] parseFeatures(String moduleName) {
		ArrayList<Feature> al = new ArrayList<Feature>();
		String ncHome = NCMDPTool.getNCHome();
		String filename = MDPCommonUtil.prefix + moduleName + MDPCommonUtil.suffix;
		File file = new File(ncHome, "/ierp/metadata/Features/" + filename);
		if (file != null && file.exists()) {
			parseFile(file, al);
		}
		// common
		String filename1 = "Features_common.xml";
		File file1 = new File(ncHome, "/ierp/metadata/Features/" + filename1);
		if (file1 != null && file1.exists()) {
			parseFile(file1, al);
		}
		return al.toArray(new Feature[0]);
	}

	private static Feature[] parseFeatures() {
		ArrayList<Feature> al = new ArrayList<Feature>();
		String ncHome = NCMDPTool.getNCHome();
		File dir = new File(ncHome, "/ierp/metadata");
		File[] featureFiles = dir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isFile()
						&& f.getName().toLowerCase().endsWith("features.xml");
			}
		});
		int count = featureFiles == null ? 0 : featureFiles.length;
		for (int i = 0; i < count; i++) {
			File file = featureFiles[i];
			parseFeatures(file, al);
		}
		return al.toArray(new Feature[0]);
	}

	private static void parseFile(File accessorConfigFile,
			ArrayList<Feature> al) {
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			fis = new FileInputStream(accessorConfigFile);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			String rootName = root.getNodeName();
			if ("features".equalsIgnoreCase(rootName)) {
				NodeList rootNL = root.getChildNodes();
				for (int i = 0; i < rootNL.getLength(); i++) {
					Node featureNode = rootNL.item(i);
					String featureName = featureNode.getNodeName();
					if ("feature".equals(featureName)) {
						Feature feature = new Feature();
						NamedNodeMap featureMap = featureNode.getAttributes();
						String fName = featureMap.getNamedItem("name")
								.getNodeValue();
						String fDisplayName = featureMap.getNamedItem(
								"displayName").getNodeValue();
						feature.setName(fName);
						feature.setDisplayName(fDisplayName);
						NodeList featureNL = featureNode.getChildNodes();
						for (int j = 0; j < featureNL.getLength(); j++) {
							Node childNode = featureNL.item(j);
							if ("propertys".equalsIgnoreCase(childNode
									.getNodeName())) {
								NodeList propsNL = childNode.getChildNodes();
								for (int k = 0; k < propsNL.getLength(); k++) {
									Node propNode = propsNL.item(k);
									if ("property".equalsIgnoreCase(propNode
											.getNodeName())) {
										Attribute attr = new Attribute();
										analyAttr(attr, propNode);
										/** 结束 wangxmn **/
										feature.addAttribute(attr);
									}
								}
							} else if ("refs".equalsIgnoreCase(childNode
									.getNodeName())) {
								NodeList propsNL = childNode.getChildNodes();
								for (int k = 0; k < propsNL.getLength(); k++) {
									Node propNode = propsNL.item(k);
									if ("ref".equalsIgnoreCase(propNode
											.getNodeName())) {
										NamedNodeMap propMap = propNode
												.getAttributes();
										String refName = propMap.getNamedItem(
												"refObject").getNodeValue();
										getRefFeature(refName, feature, rootNL);
									}
								}
							}
						}
						Node fdisplayshow = featureMap.getNamedItem("display");
						if (fdisplayshow != null) {
							if (fdisplayshow.getNodeValue().equals("true")) {
								al.add(feature);
							}
						} else {
							al.add(feature);
						}
					} else if ("Itfmappings".equals(featureName.trim())) {
						Feature feature = new Feature();
						NamedNodeMap featureMap = featureNode.getAttributes();
						feature.setName(featureMap.getNamedItem("name")
								.getNodeValue());
						feature.setDisplayName(featureMap.getNamedItem(
								"displayName").getNodeValue());
						NodeList busiItfNodes = featureNode.getChildNodes();
						for (int bi = 0, bj = busiItfNodes.getLength(); bi < bj; bi++) {
							Node node = busiItfNodes.item(bi);
							if ("attributeList".equals(node.getNodeName()
									.trim())) {
								NodeList nl = node.getChildNodes();
								for(int s=0,t=nl.getLength();s<t;s++){
									Node propNode = nl.item(s);
									if("attribute".equals(propNode.getNodeName())){
										Attribute att = new Attribute();
										analyAttr(att, propNode);
										feature.addAttribute(att);
									}
								}
							}
							if("busiItfList".equals(node.getNodeName()
									.trim())){
								NodeList nl = node.getChildNodes();
								for(int s=0,t=nl.getLength();s<t;s++){
									Node propNode = nl.item(s);
									if("busItf".equals(propNode.getNodeName())){
										NamedNodeMap propMap = propNode
										.getAttributes();
										Node prop = propMap.getNamedItem("busiItfId");
										feature.addBusiItfId(prop.getNodeValue());
									}
								}
							}
							if("refList".equals(node.getNodeName()
									.trim())){
								NodeList nl = node.getChildNodes();
								for(int s=0,t=nl.getLength();s<t;s++){
									Node propNode = nl.item(s);
									if("reference".equals(propNode.getNodeName())){
										Reference ref = new Reference();
										NamedNodeMap propMap = propNode
										.getAttributes();
										Node prop = propMap.getNamedItem("mdFilePath");
										if(prop!=null){
											ref.setMdFilePath(prop.getNodeValue());
										}
										if((prop = propMap.getNamedItem("moduleName"))!=null){
											ref.setModuleName(prop.getNodeValue());
										}
										if((prop = propMap.getNamedItem("refId"))!=null){
											ref.setRefId(prop.getNodeValue());
										}
										feature.addRefer(ref);
									}
									
								}
							}
							if("mappingList".equals(node.getNodeName()
									.trim())){
								NodeList nl = node.getChildNodes();
								for(int s=0,t=nl.getLength();s<t;s++){
									Node propNode = nl.item(s);
									if("mapping".equals(propNode.getNodeName())){
										NamedNodeMap propMap = propNode
										.getAttributes();
										Node propAtt = propMap.getNamedItem("attrName");
										Node propBusi = propMap.getNamedItem("busiAttrID");
										feature.putBusiAndAttrMapping(propAtt.getNodeValue(), propBusi.getNodeValue());
									}
									
								}
							}
						}
						al.add(feature);
					}
				}
			}
			al.add(null);// 添加null，在生成菜单时，会生成一个分隔条
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(),e);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
		}

	}

	private static void getRefFeature(String refName, Feature feature,
			NodeList rootNL) {
		for (int i = 0; i < rootNL.getLength(); i++) {
			Node featureNode = rootNL.item(i);
			String featureName = featureNode.getNodeName();
			if ("feature".equals(featureName)) {
				NamedNodeMap featureMap = featureNode.getAttributes();
				String fName = featureMap.getNamedItem("name").getNodeValue();
				if (fName.equals(refName)) {
					NodeList featureNL = featureNode.getChildNodes();
					for (int j = 0; j < featureNL.getLength(); j++) {
						Node childNode = featureNL.item(j);
						if ("propertys".equalsIgnoreCase(childNode
								.getNodeName())) {
							NodeList propsNL = childNode.getChildNodes();
							for (int k = 0; k < propsNL.getLength(); k++) {
								Node propNode = propsNL.item(k);
								if ("property".equalsIgnoreCase(propNode
										.getNodeName())) {
									Attribute attr = new Attribute();
									NamedNodeMap propMap = propNode
											.getAttributes();
									if (propMap.getNamedItem("name") != null)
										attr.setName(propMap.getNamedItem(
												"name").getNodeValue());
									if (propMap.getNamedItem("displayName") != null)
										attr.setDisplayName(propMap
												.getNamedItem("displayName")
												.getNodeValue());
									if (propMap.getNamedItem("dataType") != null) {
										String typeId = propMap.getNamedItem(
												"dataType").getNodeValue();
										Type type = NCMDPTool
												.getBaseTypeById(typeId);
										if (type == null) {
											type = new Type.SpecialType();
											((Type.SpecialType) type)
													.setSpecialTypeID(typeId);
										}
										attr.setType(type);
									}
									if (propMap.getNamedItem("length") != null)
										attr.setLength(propMap.getNamedItem(
												"length").getNodeValue());
									if (propMap.getNamedItem("isFixLength") != null)
										attr.setFixLen(propMap
												.getNamedItem("isFixLength")
												.getNodeValue()
												.equalsIgnoreCase("true"));
									if (propMap.getNamedItem("isKeyProperty") != null)
										attr.setKey(propMap
												.getNamedItem("isKeyProperty")
												.getNodeValue()
												.equalsIgnoreCase("true"));

									feature.addAttribute(attr);
								}
							}
						}
					}
				}
			}
		}
	}

	private static void parseFeatures(File accessorConfigFile,
			ArrayList<Feature> al) {
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			fis = new FileInputStream(accessorConfigFile);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			String rootName = root.getNodeName();
			if ("features".equalsIgnoreCase(rootName)) {
				NodeList rootNL = root.getChildNodes();
				for (int i = 0; i < rootNL.getLength(); i++) {
					Node featureNode = rootNL.item(i);
					String featureName = featureNode.getNodeName();
					if ("feature".equals(featureName)) {
						Feature feature = new Feature();
						NamedNodeMap featureMap = featureNode.getAttributes();
						String fName = featureMap.getNamedItem("name")
								.getNodeValue();
						String fDisplayName = featureMap.getNamedItem(
								"displayName").getNodeValue();
						feature.setName(fName);
						feature.setDisplayName(fDisplayName);
						NodeList featureNL = featureNode.getChildNodes();
						for (int j = 0; j < featureNL.getLength(); j++) {
							Node childNode = featureNL.item(j);
							if ("propertys".equalsIgnoreCase(childNode
									.getNodeName())) {
								NodeList propsNL = childNode.getChildNodes();
								for (int k = 0; k < propsNL.getLength(); k++) {
									Node propNode = propsNL.item(k);
									if ("property".equalsIgnoreCase(propNode
											.getNodeName())) {
										Attribute attr = new Attribute();
										NamedNodeMap propMap = propNode
												.getAttributes();
										if (propMap.getNamedItem("name") != null)
											attr.setName(propMap.getNamedItem(
													"name").getNodeValue());
										if (propMap.getNamedItem("displayName") != null)
											attr.setDisplayName(propMap
													.getNamedItem("displayName")
													.getNodeValue());
										if (propMap.getNamedItem("dataType") != null) {
											String typeId = propMap
													.getNamedItem("dataType")
													.getNodeValue();
											Type type = NCMDPTool
													.getBaseTypeById(typeId);
											if (type == null) {
												type = new Type.SpecialType();
												((Type.SpecialType) type)
														.setSpecialTypeID(typeId);
											}
											attr.setType(type);
										}
										if (propMap.getNamedItem("length") != null)
											attr.setLength(propMap
													.getNamedItem("length")
													.getNodeValue());
										if (propMap.getNamedItem("isFixLength") != null)
											attr.setFixLen(propMap
													.getNamedItem("isFixLength")
													.getNodeValue()
													.equalsIgnoreCase("true"));
										if (propMap
												.getNamedItem("isKeyProperty") != null)
											attr.setKey(propMap
													.getNamedItem(
															"isKeyProperty")
													.getNodeValue()
													.equalsIgnoreCase("true"));

										feature.addAttribute(attr);
									}
								}
							}
						}
						al.add(feature);
					}
				}
			}
			al.add(null);// 添加null，在生成菜单时，会生成一个分隔条
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {}
		}

	}

	public static boolean addFeature(File file, Feature fea) {
		FileInputStream in = null;
		Document doc = null;
		Node root = null;
		try {
			if(file.exists()){
				in = new FileInputStream(file);
				doc = getDoc(in);
				root = doc.getDocumentElement();
				String rootName = root.getNodeName();
				if ("features".equalsIgnoreCase(rootName)) {
					// 向原有文件中添加新的特性
					setElement(root, fea, doc);
				} else {
					MDPLogger.info("这不是一个特性文件，请查看");
					return false;
				}
			}else{
				doc = getDoc(null);
				MDPLogger.info("新建一个特性文件");
				root = doc.createElement("features");
				doc.appendChild(root);
				setElement(root, fea, doc);
			}
			toSave(file, root);
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(),e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

	private static Document getDoc(InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		if(in!=null){
			return db.parse(in);
		}else{
			return db.newDocument();
		}
		
	}

	private static void setElement(Node parent, Feature fea, Document doc) {
		Attribute[] attributes = fea.getAttrbuteCopys();
		if (attributes == null || attributes.length == 0) {
			return;
		}
		Element element = doc.createElement("feature");
		element.setAttribute("name", fea.getName());
		element.setAttribute("displayName", fea.getDisplayName());
		parent.appendChild(element);
		Element chiElement = doc.createElement("propertys");
		element.appendChild(chiElement);
		for (Attribute att : attributes) {
			Element propElement = doc.createElement("property");
			setElement(propElement, att);
			chiElement.appendChild(propElement);
		}
	}

	private static void toSave(File file, Node root)
			throws FileNotFoundException, TransformerException,
			UnsupportedEncodingException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
		DOMSource source = new DOMSource(root);
		transformer.transform(source, new StreamResult(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), "gb2312"))));
	}

	private static void setElement(Element propElement, Attribute att) {
		String ss = att.getName();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("name", ss.trim());
		}
		ss = att.getDisplayName();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("displayName", ss.trim());
		}
		ss = att.getAccessPowerGroup();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("accessPowerGroup", ss.trim());
		}
		ss = att.getTypeStyle();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("typeStyle", ss.trim());
		}
		Type dataType = att.getType();
		if (dataType != null) {
			propElement.setAttribute("dataType", dataType.getTypeId());
		}
		ss = att.getDefaultValue();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("defaultValue", ss.trim());
		}
		ss = att.getDesc();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("desc", ss.trim());
		}
		ss = att.getHelp();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("help", ss.trim());
		}
		ss = att.getAccessStrategy();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("accessStrategy", ss.trim());
		}
		ss = att.getVisibilityKind();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("visibilityKind", ss.trim());
		}
		boolean isKeyProperty = att.isKey();
		if (isKeyProperty) {
			propElement.setAttribute("isKeyProperty", "true");
		} else {
			propElement.setAttribute("isKeyProperty", "false");
		}
		boolean isAuthorization = att.isAuthorization();
		if (isAuthorization) {
			propElement.setAttribute("isAuthorization", "true");
		} else {
			propElement.setAttribute("isAuthorization", "false");
		}
		boolean isDefaultDimensionAttribute = att.isDefaultDimensionAttribute();
		if (isDefaultDimensionAttribute) {
			propElement.setAttribute("isDefaultDimensionAttribute", "true");
		} else {
			propElement.setAttribute("isDefaultDimensionAttribute", "false");
		}
		boolean isDefaultMeasureAttribute = att.isDefaultMeasureAttribute();
		if (isDefaultMeasureAttribute) {
			propElement.setAttribute("isDefaultMeasureAttribute", "true");
		} else {
			propElement.setAttribute("isDefaultMeasureAttribute", "false");
		}
		boolean isGlobalization = att.isGlobalization();
		if (isGlobalization) {
			propElement.setAttribute("isGlobalization", "true");
		} else {
			propElement.setAttribute("isGlobalization", "false");
		}
		boolean isHide = att.isHide();
		if (isHide) {
			propElement.setAttribute("isHide", "true");
		} else {
			propElement.setAttribute("isHide", "false");
		}
		boolean isShare = att.isShare();
		if (isShare) {
			propElement.setAttribute("isShare", "true");
		} else {
			propElement.setAttribute("isShare", "false");
		}
		boolean isKey = att.isKey();
		if (isKey) {
			propElement.setAttribute("isKey", "true");
		} else {
			propElement.setAttribute("isKey", "false");
		}
		boolean isNullable = att.isNullable();
		if (isNullable) {
			propElement.setAttribute("isNullable", "true");
		} else {
			propElement.setAttribute("isNullable", "false");
		}
		boolean accessPower = att.isAccessPower();
		if (accessPower) {
			propElement.setAttribute("accessPower", "true");
		} else {
			propElement.setAttribute("accessPower", "false");
		}
		boolean notSerialize = att.getIsSequence();
		if (notSerialize) {
			propElement.setAttribute("notSerialize", "true");
		} else {
			propElement.setAttribute("notSerialize", "false");
		}
		boolean nynamic = att.getIsDynamicAttr();
		if (nynamic) {
			propElement.setAttribute("nynamic", "true");
		} else {
			propElement.setAttribute("nynamic", "false");
		}
		ss = att.getDynamicTable();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("dynamicTable", ss.trim());
		}
		boolean isReadOnly = att.isReadOnly();
		if (isReadOnly) {
			propElement.setAttribute("isReadOnly", "true");
		} else {
			propElement.setAttribute("isReadOnly", "false");
		}
//		ss = att.getVersionType();
//		if (ss != null && !("".equals(ss.trim()))) {
//			propElement.setAttribute("versionType", ss.trim());
//		}
		boolean industryChanged = att.isIndustryChanged();
		if (industryChanged) {
			propElement.setAttribute("industryChanged", "true");
		} else {
			propElement.setAttribute("industryChanged", "false");
		}
		boolean isSource = att.isSource();
		if (isSource) {
			propElement.setAttribute("isSource", "true");
		} else {
			propElement.setAttribute("isSource", "false");
		}
//		propElement.setAttribute("modifyIndustry", MDPUtil.getCurIndustry());
//		propElement.setAttribute("createIndustry", MDPUtil.getCurIndustry());
		ss = att.getFieldName();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("fieldName", ss.trim());
		}
		ss = att.getFieldType();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("fieldType", ss.trim());
		}
		ss = att.getMinValue();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("minValue", ss.trim());
		}
		ss = att.getMaxValue();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("maxValue", ss.trim());
		}
		boolean isFixLen = att.isFixLen();
		if (isFixLen) {
			propElement.setAttribute("isFixLength", "true");
		} else {
			propElement.setAttribute("isFixLength", "false");
		}
		ss = att.getLength();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("length", ss.trim());
		}
		ss = att.getPrecision();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("precision", ss.trim());
		}
		ss = att.getRefModuleName();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("refModelName", ss.trim());
		}
		boolean isCalculateAttr = att.isCalculateAttr();
		if (isCalculateAttr) {
			propElement.setAttribute("isCalculateAttr", "true");
		} else {
			propElement.setAttribute("isCalculateAttr", "false");
		}
		boolean isActive = att.isActive();
		if (isActive) {
			propElement.setAttribute("isActive", "true");
		} else {
			propElement.setAttribute("isActive", "false");
		}
		ss = att.getResid();
		if (ss != null && !("".equals(ss.trim()))) {
			propElement.setAttribute("resid", ss.trim());
		}
		boolean isFeature = att.getIsFeature();
		if (isFeature) {
			propElement.setAttribute("isFeature", "true");
		} else {
			propElement.setAttribute("isFeature", "false");
		}
		boolean forLocale = att.isForLocale();
		if (forLocale) {
			propElement.setAttribute("forLocale", "true");
		} else {
			propElement.setAttribute("forLocale", "false");
		}
	}

	public static boolean addBusiItf(Feature feature,File file) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		List<BusinInterface> inters = feature.getBusinInterface();
		List<Reference> refs = feature.getRefers();
		Attribute[] attrs = feature.getAttrbuteCopys();
		Map<String, String> mapping = feature.getBusiAndAttrMapping();
		return addBusiItf(inters, refs, attrs, mapping, feature.getName(),
				feature.getDisplayName(),file);
	}
	/**
	 * 新添加一个序列化业务接口映射
	 * 
	 * @param feature
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	private static boolean addBusiItf(List<BusinInterface> inters,
			List<Reference> refs, Attribute[] attrs,Map<String, String> mapping, 
			String name,String displayName,File file) throws ParserConfigurationException, 
			SAXException, IOException, TransformerException {
		FileInputStream in = null;
		Document doc = null;
		Node root = null;
		try {
			if(file.exists()){
				in = new FileInputStream(file);
				doc = getDoc(in);
				root = doc.getDocumentElement();
				String rootName = root.getNodeName();
				if ("features".equalsIgnoreCase(rootName)) {
					MDPLogger.info("向原有的特性文件中添加新的映射关系——————————");
					// 向原有文件中添加新的特性
					setBusItf(root, inters, refs, attrs, mapping,  name, displayName, doc);
				} else {
					MDPLogger.info("这不是一个特性文件，请查看");
					return false;
				}
			}else{
				doc = getDoc(null);
				root = doc.getDocumentElement();
				MDPLogger.info("新建一个特性文件");
				root = doc.createElement("features");
				doc.appendChild(root);
				setBusItf(root, inters, refs, attrs, mapping, name, displayName, doc);
			}
			toSave(file, root);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {}
			}
		}
		return true;
	}

	/**
	 * 具体存储Busitf
	 * 
	 * @param root
	 * @param busitf
	 */
	private static void setBusItf(Node parent, List<BusinInterface> inters,
			List<Reference> refs, Attribute[] attrs, Map<String, String> mapping,String name,
			String displayName,Document doc) {
		if (inters == null || inters.isEmpty()) {
			MDPLogger.info("the business Interface is null;");
			return;
		}
		Element element = doc.createElement("Itfmappings");
		 element.setAttribute("name", name);
		 element.setAttribute("displayName", displayName);
		parent.appendChild(element);
		Element chiElement = doc.createElement("attributeList");
		Element itfElement = doc.createElement("busiItfList");
		Element refElement = doc.createElement("refList");
		Element mapElement = doc.createElement("mappingList");
		element.appendChild(chiElement);
		element.appendChild(itfElement);
		element.appendChild(refElement);
		element.appendChild(mapElement);
		for(int i=0,j=attrs.length;i<j;i++){
			Element ele = doc.createElement("attribute");
			setElement(ele, attrs[i]);
			chiElement.appendChild(ele);
		}
		for (BusinInterface att : inters) {
			Element propElement = doc.createElement("busItf");
			propElement.setAttribute("busiItfId", att.getId());
			itfElement.appendChild(propElement);
		}
		for (Reference ref : refs) {
			Element refer = doc.createElement("reference");
			refer.setAttribute("moduleName", ref.getModuleName());
			refer.setAttribute("mdFilePath", ref.getMdFilePath());
			refer.setAttribute("refId", ref.getRefId());
			refer.setAttribute("referencedCell", ref.getReferencedCell()
					.getId());
			refElement.appendChild(refer);
		}
		Iterator<String> keys = mapping.keySet().iterator();
		while (keys.hasNext()) {
			Element map = doc.createElement("mapping");
			String attrId = keys.next();
			map.setAttribute("attrName", attrId);
			map.setAttribute("busiAttrID", mapping.get(attrId));
			mapElement.appendChild(map);
		}
	}

	private static void analyAttr(Attribute attr, Node propNode) {
		NamedNodeMap propMap = propNode.getAttributes();
		if (propMap.getNamedItem("name") != null)
			attr.setName(propMap.getNamedItem("name").getNodeValue());
		if (propMap.getNamedItem("displayName") != null)
			attr.setDisplayName(propMap.getNamedItem("displayName")
					.getNodeValue());
		if (propMap.getNamedItem("dataType") != null) {
			String typeId = propMap.getNamedItem("dataType").getNodeValue();
			Type type = NCMDPTool.getBaseTypeById(typeId);
			if (type == null) {
				type = new Type.SpecialType();
				((Type.SpecialType) type).setSpecialTypeID(typeId);
			}
			attr.setType(type);
		}
		if (propMap.getNamedItem("length") != null)
			attr.setLength(propMap.getNamedItem("length").getNodeValue());
		if (propMap.getNamedItem("isFixLength") != null)
			attr.setFixLen(propMap.getNamedItem("isFixLength").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isKeyProperty") != null)
			attr.setKey(propMap.getNamedItem("isKeyProperty").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("refModelName") != null)
			attr.setRefModuleName(propMap.getNamedItem("refModelName")
					.getNodeValue());

		/** 增加其他属性 wangxmn **/
		Node node = null;
		// 添加类型样式特性
		if ((node = propMap.getNamedItem("typeStyle")) != null) {
			attr.setTypeStyle(node.getNodeValue());
		}
		// 默认值
		if ((node = propMap.getNamedItem("defaultValue")) != null) {
			attr.setDefaultValue(node.getNodeValue());
		}
		// 描述
		if ((node = propMap.getNamedItem("desc")) != null) {
			attr.setDesc(node.getNodeValue());
		}
		// 帮助
		if ((node = propMap.getNamedItem("help")) != null) {
			attr.setHelp(node.getNodeValue());
		}
		// 访问策略
		if ((node = propMap.getNamedItem("accessStrategy")) != null) {
			attr.setAccessStrategy(node.getNodeValue());
		}
		if ((node = propMap.getNamedItem("visibilityKind")) != null) {
			attr.setVisibilityKind(node.getNodeValue());
		}
		if (propMap.getNamedItem("isAuthorization") != null)
			attr.setAuthorization(propMap.getNamedItem("isAuthorization")
					.getNodeValue().equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isDefaultDimensionAttribute") != null)
			attr.setDefaultDimensionAttribute(propMap
					.getNamedItem("isDefaultDimensionAttribute").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isDefaultMeasureAttribute") != null)
			attr.setDefaultMeasureAttribute(propMap
					.getNamedItem("isDefaultMeasureAttribute").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isGlobalization") != null)
			attr.setGlobalization(propMap.getNamedItem("isGlobalization")
					.getNodeValue().equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isHide") != null)
			attr.setHide(propMap.getNamedItem("isHide").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isShare") != null)
			attr.setShare(propMap.getNamedItem("isShare").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isKey") != null)
			attr.setKey(propMap.getNamedItem("isKey").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("isNullable") != null)
			attr.setNullable(propMap.getNamedItem("isNullable").getNodeValue()
					.equalsIgnoreCase("true"));
		if (propMap.getNamedItem("accessPower") != null)
			attr.setAccessPower(propMap.getNamedItem("accessPower")
					.getNodeValue().equalsIgnoreCase("true"));
		if (propMap.getNamedItem("notSerialize") != null)
			attr.setIsSequence(propMap.getNamedItem("notSerialize")
					.getNodeValue().equalsIgnoreCase("true"));
		if (propMap.getNamedItem("nynamic") != null)
			attr.setIsDynamicAttr(propMap.getNamedItem("nynamic")
					.getNodeValue().equalsIgnoreCase("true"));
		if ((node = propMap.getNamedItem("dynamicTable")) != null) {
			attr.setDynamicTable(node.getNodeValue());
		}
		if ((node = propMap.getNamedItem("accessPowerGroup")) != null) {
			attr.setAccessPowerGroup(node.getNodeValue());
		}
		if (propMap.getNamedItem("isReadOnly") != null)
			attr.setReadOnly(propMap.getNamedItem("isReadOnly").getNodeValue()
					.equalsIgnoreCase("true"));
		if ((node = propMap.getNamedItem("fieldName")) != null) {
			attr.setFieldName(node.getNodeValue());
		}
		if ((node = propMap.getNamedItem("fieldType")) != null) {
			attr.setFieldType(node.getNodeValue());
		}
		if ((node = propMap.getNamedItem("minValue")) != null) {
			attr.setMinValue(node.getNodeValue());
		}
		if ((node = propMap.getNamedItem("maxValue")) != null) {
			attr.setMaxValue(node.getNodeValue());
		}
		if ((node = propMap.getNamedItem("length")) != null) {
			attr.setLength(node.getNodeValue());
		}
		if ((node = propMap.getNamedItem("precision")) != null) {
			attr.setPrecision(node.getNodeValue());
		}
	}
}
