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

/**
 * 获得可存入数据库类型的类型
 * @author wangxmn
 *
 */
public class DBTypesConfig {
	/**
	 * 获得可存入数据库中的基础类型，对应类型字段的类型
	 * @return
	 */
	public static String[] loadDBTypesConfigs(){
		ArrayList<String> al = new ArrayList<String>();
		String ncHome = NCMDPTool.getNCHome();
		String dbTypeConfigFileStr = ncHome+"/ierp/metadata/dbTypes.xml";
		FileInputStream fis = null;
		if(!new File(dbTypeConfigFileStr).exists()){
			return new String[0];
		}
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = new FileInputStream(dbTypeConfigFileStr);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			if("dbTypes".equals(root.getNodeName())){
				NodeList nl = root.getChildNodes();
				int len = nl.getLength();
				for (int i = 0; i < len; i++) {
					Node node = nl.item(i);
					String nodeName = node.getNodeName();
					if(nodeName.equals("type")){
						String type= node.getFirstChild().getNodeValue();
						if(type != null && type.trim().length() > 0)
							al.add(type);
					}
				}
			}
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		} finally{
			if(fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
		}

		return al.toArray(new String[0]);
	}

}
