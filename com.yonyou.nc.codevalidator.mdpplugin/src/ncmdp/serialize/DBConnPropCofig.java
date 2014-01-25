package ncmdp.serialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DBConnPropCofig {
	
	/**
	 * 获得数据连接属性
	 * @return
	 */
	public static DBConnProp[] loadDBConnProps(){
		ArrayList<DBConnProp> al = new ArrayList<DBConnProp>();
		String ncHome = NCMDPTool.getNCHome();
		String baseTypeConfigFileStr = ncHome+"/ierp/metadata/dbConnectProp.xml";
		if(!new File(baseTypeConfigFileStr).exists())
			return null;
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = new FileInputStream(baseTypeConfigFileStr);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			if("dbprops".equals(root.getNodeName())){
				NodeList nl = root.getChildNodes();
				int len = nl.getLength();
				for (int i = 0; i < len; i++) {
					Node node = nl.item(i);
					String nodeName = node.getNodeName();
					if(nodeName.equals("dbprop")){
						DBConnProp prop= new DBConnProp();
						NodeList nl1 = node.getChildNodes();
						for (int j = 0; j < nl1.getLength(); j++) {
							Node child = nl1.item(j);
							if("url".equalsIgnoreCase(child.getNodeName())){
								prop.setUrl(child.getFirstChild().getNodeValue());
							}else if("user".equalsIgnoreCase(child.getNodeName())){
								prop.setUser(child.getFirstChild().getNodeValue());
							}else if("password".equalsIgnoreCase(child.getNodeName())){
								prop.setPassword(child.getFirstChild().getNodeValue());
							}else if("driverClassName".equalsIgnoreCase(child.getNodeName())){
								prop.setDbDriver(child.getFirstChild().getNodeValue());
							}else if("databaseType".equalsIgnoreCase(child.getNodeName())){
								prop.setDbType(child.getFirstChild().getNodeValue());
							}
						}
						al.add(prop);
					}
				}
			}
		} catch (Exception e) {
		} finally{
			if(fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(), e);
				}
		}

		return al.toArray(new DBConnProp[0]);
	}

}
