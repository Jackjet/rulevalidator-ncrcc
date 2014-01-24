package ncmdp.serialize;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ncmdp.model.Accessor;
import ncmdp.model.StereoType;
import ncmdp.model.Accessor.AccProp;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StereoTypeConfig {
	private static StereoType[] stereoTypes = null;

	private static Accessor[] accessors = null;

	private static boolean parsed = false;

	public static StereoType[] getStereoTypes() {
		if (stereoTypes == null) {
			parse();
		}
		return stereoTypes;
	}

	public static Accessor[] getAccessors() {
		if (accessors == null) {
			parse();
		}
		return accessors;
	}

	private static void parse() {
		if (!parsed) {
			String ncHome = NCMDPTool.getNCHome();
			String accessorConfigFileStr = ncHome + "/ierp/metadata/StereoType.xml";
			FileInputStream fis = null;
			try {
				
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				fis = new FileInputStream(accessorConfigFileStr);
				Document doc = db.parse(fis);
				accessors = parseAccessors(doc);
				stereoTypes = parseStereoTypes(doc);
				parsed = true;
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}finally{
				if(fis != null)
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						MDPLogger.error(e.getMessage(), e);
					}
			}
		}
	}

	private static StereoType getDefaultStereoType() {
		StereoType st = new StereoType();
		st.setName("");
		st.setDisplayName("");
		Accessor[] acces = getAccessors();
		int count = acces == null ? 0 : acces.length;
		for (int i = 0; i < acces.length; i++) {
			st.getListAccessors().add(acces[i].clone());
		}
		return st;
	}

	private static StereoType[] parseStereoTypes(Document doc) {
		ArrayList<StereoType> al = new ArrayList<StereoType>();
		al.add(getDefaultStereoType());
		Node root = doc.getDocumentElement();
		NodeList nl = root.getChildNodes();
		int len = nl.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nl.item(i);
			String nodeName = node.getNodeName();
			if (nodeName.equals("accessorStereoType")) {
				NodeList stNL = node.getChildNodes();
				for (int j = 0; j < stNL.getLength(); j++) {
					Node stNode = stNL.item(j);
					String stNodeName = stNode.getNodeName();
					if ("StereoType".equalsIgnoreCase(stNodeName)) {
						NamedNodeMap stMap = stNode.getAttributes();
						String stName = stMap.getNamedItem("name").getNodeValue();
						String stDisplayName = stMap.getNamedItem("displayName").getNodeValue();
						StereoType st = new StereoType();
						st.setName(stName);
						st.setDisplayName(stDisplayName);
						NodeList accNL = stNode.getChildNodes();
						for (int k = 0; k < accNL.getLength(); k++) {
							Node accNode = accNL.item(k);
							String accNodeName = accNode.getNodeName();
							if ("accessor".equalsIgnoreCase(accNodeName)) {
								NamedNodeMap accMap = accNode.getAttributes();
								String accName = accMap.getNamedItem("name").getNodeValue();
								Accessor acc = getAccessorByName(accName);
								if (acc != null) {
									NodeList accNodeList = accNode.getChildNodes();
									for (int l = 0; l < accNodeList.getLength(); l++) {
										Node accChildNode = accNodeList.item(l);
										String accChildNodeName = accChildNode.getNodeName();
										if ("properties".equalsIgnoreCase(accChildNodeName)) {
											NodeList propsNL = accChildNode.getChildNodes();
											for (int m = 0; m < propsNL.getLength(); m++) {
												Node accPropNode = propsNL.item(m);
												String accPropNodeName = accPropNode.getNodeName();
												if ("property".equalsIgnoreCase(accPropNodeName)) {
													NamedNodeMap propMap = accPropNode.getAttributes();
													String propName = propMap.getNamedItem("name").getNodeValue();
													String propDisplayName = null;
													String propValue = null;
													if (propMap.getNamedItem("displayName") != null) {
														propDisplayName = propMap.getNamedItem("displayName").getNodeValue();
													}
													if (propMap.getNamedItem("value") != null) {
														propValue = propMap.getNamedItem("value").getNodeValue();
													}
													AccProp prop = acc.getPropmap().get(propName);
													if (prop == null) {
														prop = new Accessor.AccProp();
														prop.setName(propName);
														acc.getAlProps().add(propName);
														acc.getPropmap().put(propName, prop);
													}
													if (propDisplayName != null)
														prop.setDisplayName(propDisplayName);
													if (propValue != null)
														prop.setValue(propValue);

												}
											}
										}
									}
								}
								st.getListAccessors().add(acc);
							}
						}
						// /////////
						al.add(st);
					}
				}

			}
		}
		return al.toArray(new StereoType[0]);
	}

	private static Accessor getAccessorByName(String name) {
		int count = accessors == null ? 0 : accessors.length;
		for (int i = 0; i < count; i++) {
			if (accessors[i].getName().equals(name)) {
				return accessors[i].clone();
			}
		}
		return null;
	}

	private static Accessor[] parseAccessors(Document doc) {
		ArrayList<Accessor> al = new ArrayList<Accessor>();
		Node root = doc.getDocumentElement();
		// if ("StereoType".equals(root.getNodeName())) {
		NodeList nl = root.getChildNodes();
		int len = nl.getLength();
		for (int i = 0; i < len; i++) {
			Node stChildnode = nl.item(i);
			String stNodeName = stChildnode.getNodeName();
			if ("accessorTemplates".equals(stNodeName)) {
				NodeList nlAccessors = stChildnode.getChildNodes();
				for (int n = 0; n < nlAccessors.getLength(); n++) {
					Node node = nlAccessors.item(n);
					String nodeName = node.getNodeName();
					if (nodeName.equals("accessor")) {
						Accessor accessor = new Accessor();
						NodeList nl1 = node.getChildNodes();
						for (int j = 0; j < nl1.getLength(); j++) {
							Node child = nl1.item(j);
							if ("name".equals(child.getNodeName())) {
								accessor.setName(child.getFirstChild().getNodeValue());
							} else if ("displayName".equals(child.getNodeName())) {
								accessor.setDisplayName(child.getFirstChild().getNodeValue());
							} else if ("classFullname".equalsIgnoreCase(child.getNodeName())) {
								accessor.setClsFullName(child.getFirstChild().getNodeValue());
							} else if ("properties".equals(child.getNodeName())) {
								NodeList propsnl = child.getChildNodes();
								for (int k = 0; k < propsnl.getLength(); k++) {
									Node propchild = propsnl.item(k);
									if ("property".equals(propchild.getNodeName())) {
										NamedNodeMap map = propchild.getAttributes();
										String propdisplayName = map.getNamedItem("displayName").getNodeValue();
										String propName = map.getNamedItem("name").getNodeValue();

										AccProp accProp = new Accessor.AccProp();
										accProp.setDisplayName(propdisplayName);
										accProp.setName(propName);
										accessor.getPropmap().put(propName, accProp);
										accessor.getAlProps().add(propName);
									}
								}
							}
						}
						al.add(accessor);
					}
				}
			}
			// }
		}
		return al.toArray(new Accessor[0]);
	}

}
