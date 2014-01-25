package ncmdp.serialize;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ncmdp.model.Type;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 对应属性数据存入数据库中对应的类型，以及在系统中的属性
 * @author wangxmn
 *
 */
public class BaseTypeConfig {
	public static Type[] loadBaseTypes(){
		ArrayList<Type> al = new ArrayList<Type>();
		String ncHome = NCMDPTool.getNCHome();
		String baseTypeConfigFileStr = ncHome+"/ierp/metadata/baseType.xml";
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = new FileInputStream(baseTypeConfigFileStr);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			if("baseTypes".equals(root.getNodeName())){
				NodeList nl = root.getChildNodes();
				int len = nl.getLength();
				for (int i = 0; i < len; i++) {
					Node node = nl.item(i);
					String nodeName = node.getNodeName();
					if(nodeName.equals("baseType")){
						Type type= new Type();
						NodeList nl1 = node.getChildNodes();
						for (int j = 0; j < nl1.getLength(); j++) {
							Node child = nl1.item(j);
							if("id".equals(child.getNodeName())){
								type.setTypeId(child.getFirstChild().getNodeValue());
							}else if("name".equals(child.getNodeName())){
								type.setName(child.getFirstChild().getNodeValue());
							}else if("displayName".equals(child.getNodeName())){
								type.setDisplayName(child.getFirstChild().getNodeValue());
							}else if("dbType".equalsIgnoreCase(child.getNodeName())){
								type.setDbType(child.getFirstChild().getNodeValue());
							}else if("length".equalsIgnoreCase(child.getNodeName())){
								type.setLength(child.getFirstChild().getNodeValue());
							}else if("precise".equalsIgnoreCase(child.getNodeName())){
								type.setPrecise(child.getFirstChild().getNodeValue());
							}
						}
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
				} catch (IOException e) {}
		}

		return al.toArray(new Type[0]);
	}
}
