package ncmdp.pdmxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
/**
 * 解析pdm，只能解析pdm版本为12的
 * @author wangxmn
 *
 */
public class PDMParser {
	private File pdmFile = null;
	private String pdmVersion = "12";
	public PDMParser(File pdmFile) {
		super();
		this.pdmFile = pdmFile;
	}

	private String getTableTag() {
		if (pdmVersion.startsWith("7") || pdmVersion.startsWith("8")) {
			return "o:SPdoObjTabl";
		} else {
			return "o:Table";
		}
	}

	private String getColTag() {
		if (pdmVersion.startsWith("7") || pdmVersion.startsWith("8")) {
			return "o:SPdoObjColn";
		} else {
			return "o:Column";
		}
	}

	private String getDataTypeTag() {
		if (pdmVersion.startsWith("7") || pdmVersion.startsWith("8")) {
			return "a:Dttp";
		} else {
			return "a:DataType";
		}
	}
	private Table[] analyzeTable(Element root) {
		ArrayList<Table> al = new ArrayList<Table>();
		NodeList tables = root.getElementsByTagName(getTableTag());
		if (tables.getLength() == 0) {
			tables = root.getElementsByTagName(getTableTag());
		}
		for (int i = 0; i < tables.getLength(); i++) {
			Element tb = (Element) tables.item(i);
			Table table = defTable(tb);
			ArrayList<String> alKeyIds = findKeysID(tb);
			// 如果返回为空,说明是表引用不是定义
			if (table == null)
				continue;
			defField(tb, table, alKeyIds);
			al.add(table);
		}
		return al.toArray(new Table[0]);
	}



	private ArrayList<String> findKeysID(Element tb) {
		ArrayList<String> al = new ArrayList<String>();
		NodeList nl = tb.getElementsByTagName("c:PrimaryKey");
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element)nl.item(i);
			Element node = (Element)e.getElementsByTagName("o:Key").item(0);
			String ref = node.getAttribute("Ref");
			NodeList nl1 = tb.getElementsByTagName("o:Key");
			for (int j = 0; j < nl1.getLength(); j++) {
				Element keyEle = (Element)nl1.item(j);
				String id = keyEle.getAttribute("Id");
				if(ref.equals(id)){
					NodeList keyCollist = keyEle.getElementsByTagName("o:Column");
					for (int k = 0; k < keyCollist.getLength(); k++) {
						Element col = (Element)keyCollist.item(k);
						al.add(col.getAttribute("Ref"));
					}
				}
			}
		}
		return al;
	}

	private String getTagValue(Element e, String tag) {
		NodeList list = e.getElementsByTagName(tag);
		Element item = (Element) list.item(0);
		Text t = (Text) item.getFirstChild();
		return t.getNodeValue();
	}

	private Table defTable(Element tb) {
		String idTb = tb.getAttribute("Id");
		if (idTb == null || idTb.equals(""))
			return null;
		String name = getTagValue(tb, "a:Code");
		String displayName = getTagValue(tb, "a:Name");
		Table td = new Table();
		td.setName(name);
		td.setDisplayName(displayName);
		return td;
	}

	private void defField(Element tb, Table table, ArrayList<String> alKeyIds) {
		NodeList fieldList = null;
		fieldList = tb.getElementsByTagName(getColTag());
		for (int j = 0; j < fieldList.getLength(); j++) {
			Element e = (Element) fieldList.item(j);
			String id = e.getAttribute("Id");
			if (id == null || id.equals(""))
				continue;
			Field fd = new Field();
			fd.setKey(alKeyIds.contains(id));
			String name = getTagValue(e, "a:Code");
			String displayName = getTagValue(e, "a:Name");
			fd.setName(name);
			fd.setDisplayName(displayName);
			String strdttp = null;
			strdttp = getTagValue(e, getDataTypeTag());
			String[] dttps = getDttp(strdttp);
			fd.setType(dttps[0]);
			fd.setLength(dttps[1]);
			fd.setPrecision(dttps[2]);
			table.addField(fd);
		}
	}

	public String getStringInBracket(String strLine) {
		int iIndex1 = strLine.indexOf("(");
		int iIndex2 = strLine.indexOf(")");
		if (iIndex1 == -1 || iIndex2 == -1)
			return null;
		String str = strLine.substring(iIndex1 + 1, iIndex2);
		return str.trim();
	}

	private String[] getDttp(String strdttp) {
		String[] dttp = { "", "", "" };
		int index = strdttp.indexOf("(");
		String type = null;
		if (index == -1) {
			dttp[0] = strdttp;
		} else {
			type = strdttp.substring(0, index);
			dttp[0] = type;
			String lenAndPre = getStringInBracket(strdttp);
			int index2 = lenAndPre.indexOf(",");
			if (index2 == -1) {
				dttp[1] = lenAndPre.trim();
			} else {
				String len = lenAndPre.substring(0, index2);
				dttp[1] = len.trim();
				String pre = lenAndPre.substring(index2 + 1);
				dttp[2] = pre.trim();
			}
		}
		return dttp;
	}
	public Table[] parse() {
		Table[] tables = null;
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = new FileInputStream(pdmFile);
			Document document = db.parse(fis);
			Element root = document.getDocumentElement();
			tables = analyzeTable(root);
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
		return tables;
	}
}
