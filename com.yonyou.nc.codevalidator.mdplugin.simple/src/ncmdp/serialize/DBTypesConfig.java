package ncmdp.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ��ÿɴ������ݿ����͵�����
 * @author wangxmn
 *
 */
public class DBTypesConfig {
	/**
	 * ��ÿɴ������ݿ��еĻ������ͣ���Ӧ�����ֶε�����
	 * @return
	 */
	public static String[] loadDBTypesConfigs(){
		ArrayList<String> al = new ArrayList<String>();
		String dbTypeConfigFileStr = "/config/dbTypes.xml";
		InputStream fis = null;
//		if(!new File(dbTypeConfigFileStr).exists()){
//			return new String[0];
//		}
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = DBTypesConfig.class.getResourceAsStream(dbTypeConfigFileStr);
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
