package com.yonyou.nc.codevalidator.sdk.datasource;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@SuppressWarnings("rawtypes")
public class XMLToObject {
	public XMLToObject() {
		m_AllowNoField = false;
	}

	private void fillFieldValue(Field f, Object o, String str) throws Exception {
		Class itemClass = f.getType();
		String value = str.trim();
		boolean isObjectPrimitiveClass = false;
		Class classA[] = { Boolean.class, Character.class, Integer.class,
				Long.class, String.class, Double.class, Float.class,
				BigDecimal.class };
		for (int i = 0; i < classA.length; i++)
			if (classA[i] == itemClass)
				isObjectPrimitiveClass = true;

		Object itemValue = null;
		if (!value.equals("null") && isObjectPrimitiveClass)
			if (itemClass == String.class)
				itemValue = value;
			else if (itemClass == Integer.class)
				itemValue = Integer.valueOf(value);
			else if (itemClass == Boolean.class)
				itemValue = Boolean.valueOf(value);
			else if (itemClass == Character.class)
				itemValue = new Character(value.charAt(0));
			else if (itemClass == Long.class)
				itemValue = Long.valueOf(value);
			else if (itemClass == Double.class)
				itemValue = Double.valueOf(value);
			else if (itemClass == Float.class)
				itemValue = Float.valueOf(value);
			else if (itemClass == BigDecimal.class)
				itemValue = new BigDecimal(value);
		if (isObjectPrimitiveClass) {
			f.set(o, itemValue);
			return;
		}
		if (itemClass == Integer.TYPE)
			f.setInt(o, Integer.parseInt(value));
		else if (itemClass == Boolean.TYPE)
			f.setBoolean(o, Boolean.valueOf(value).booleanValue());
		else if (itemClass == Character.TYPE)
			f.setChar(o, value.charAt(0));
		else if (itemClass == Long.TYPE)
			f.setLong(o, Long.parseLong(value));
		else if (itemClass == Double.TYPE)
			f.setDouble(o, Double.valueOf(value).doubleValue());
		else if (itemClass == Float.TYPE)
			f.setDouble(o, Float.valueOf(value).floatValue());
	}

	private Class getArrayItemClass(String className, ClassLoader loader)
			throws Exception {
		int key = className.indexOf("[L");
		if (key >= 0) {
			int lastLoc = className.indexOf(";");
			String classPureName = className.substring(key + 2, lastLoc);
			Class pureClass = null;
			if (loader != null)
				pureClass = loader.loadClass(classPureName);
			else
				pureClass = Class.forName(classPureName);
			if (key == 0)
				return pureClass;
			int arrayList[] = new int[key];
			for (int i = 0; i < arrayList.length; i++)
				arrayList[i] = 1;

			return Array.newInstance(pureClass, arrayList).getClass();
		}
		String id[] = { "[B", "[C", "[I", "[J" };
		Class type[] = { Byte.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE };
		for (int i = 0; i < id.length; i++) {
			key = className.indexOf(id[i]);
			if (key >= 0) {
				Class pureClass = type[i];
				if (key == 0)
					return pureClass;
				int arrayList[] = new int[key];
				for (int j = 0; j < arrayList.length; j++)
					arrayList[j] = 1;

				return Array.newInstance(pureClass, arrayList).getClass();
			}
		}

		if (loader != null)
			return loader.loadClass(className);
		else
			return Class.forName(className);
	}

	public static Object getJavaObjectFromFile(String fileName)
			throws Exception {
		return getJavaObjectFromFile(fileName, true);
	}

	public static Object getJavaObjectFromFile(File file, Class rootClass,
			boolean allowNoField) throws RuleBaseException {
		try{
			DocumentBuilder parser = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = parser.parse(file);
			return getJavaObjectFromDocument(doc, rootClass, allowNoField);
		} catch (Exception e) {
			throw new RuleBaseException(e);
		}
	}

	public static Object getJavaObjectFromStream(InputStream stream,
			Class rootClass, boolean allowNoField) throws Exception {
		DocumentBuilder parser = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = parser.parse(stream);
		return getJavaObjectFromDocument(doc, rootClass, allowNoField);
	}

	public static Object getJavaObjectFromDocument(Document doc,
			Class rootClass, boolean allowNoField) throws Exception {
		XMLToObject xto = new XMLToObject();
		xto.setAllowNoField(allowNoField);
		Node node = doc.getDocumentElement();
		return xto.revertDocument(node, rootClass, null);
	}

	public static Object getJavaObjectFromNode(Node node, Class rootClass,
			boolean allowNoField) throws Exception {
		XMLToObject xto = new XMLToObject();
		xto.setAllowNoField(allowNoField);
		return xto.revertDocument(node, rootClass, null);
	}

	public static Object getJavaObjectFromFile(String fileName,
			boolean allowNoField) throws Exception {
		DocumentBuilder parser = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = parser.parse(fileName);
		XMLToObject xto = new XMLToObject();
		xto.setAllowNoField(allowNoField);
		Node node = doc.getDocumentElement();
		Class defaultClass = ((Class) ("(Java lang)Middleware depoly parameter"
				.equals(((Element) node).getAttribute("docType")) ? Class
				.forName("nc.bs.mw.pm.MiddleParameter") : Object.class));
		String classType = ((Element) node).getAttribute("ClassType");
		if (classType != null && classType.length() > 2)
			defaultClass = Class.forName(classType);
		Object oe = xto.revertDocument(node, defaultClass, null);
		return oe;
	}

	public static Object getJavaObjectFromFile(String fileName,
			Class rootClass, boolean allowNoField) throws RuleBaseException {
		return getJavaObjectFromFile(new File(fileName), rootClass,
				allowNoField);
	}

	private boolean isAllowNoField() {
		return m_AllowNoField;
	}

	private boolean isArrayClass(String classArrayName) throws Exception {
		return classArrayName.startsWith("[");
	}

	private boolean isNullNode(Node cNode) {
		Node valueNode = cNode.getAttributes().getNamedItem("value");
		if (valueNode == null)
			return false;
		return valueNode.getNodeValue().equals("null");
	}

	private boolean isNullNodeArray(Node cNode) {
		Node valueNode = cNode.getAttributes().getNamedItem("arrayValue");
		if (valueNode == null)
			return false;
		return valueNode.getNodeValue().equals("null");
	}

	private boolean isPrimitive(Class cl) {
		for (int i = 0; i < classA.length; i++)
			if (classA[i] == cl)
				return true;

		return false;
	}

	@SuppressWarnings("unchecked")
	private Object revertArray(Node cNode, Class defautClass, String nodeName)
			throws Exception {
		String arrayName = defautClass.getName();
		ClassLoader loader = defautClass.getClassLoader();
		if (defautClass.isArray())
			loader = defautClass.getComponentType().getClassLoader();
		Class arrayItemClass = getArrayItemClass(arrayName, loader);
		NodeList nl = cNode.getChildNodes();
		Vector vNl = new Vector();
		for (int i = 0; i < nl.getLength(); i++)
			if (nl.item(i).getNodeName().equals(nodeName))
				vNl.addElement(nl.item(i));

		if (vNl.size() == 1 && isNullNodeArray((Node) vNl.elementAt(0)))
			return null;
		Object o = Array.newInstance(arrayItemClass, vNl.size());
		if (isPrimitive(arrayItemClass)) {
			for (int i = 0; i < vNl.size(); i++) {
				Node item = (Node) vNl.elementAt(i);
				String str = item.getChildNodes().item(0).getNodeValue().trim();
				setArrayPrimitiveValue(o, i, arrayItemClass, str);
			}

		} else {
			for (int i = 0; i < vNl.size(); i++) {
				Node item = (Node) vNl.elementAt(i);
				Array.set(o, i, revertDocument(item, arrayItemClass, nodeName));
			}

		}
		return o;
	}

	private Object revertDocument(Node item, Class defaultClass, String nodeName)
			throws Exception {
		if (isNullNode(item))
			return null;
		String className = defaultClass.getName();
		if (isArrayClass(className))
			return revertArray(item, defaultClass, nodeName);
		Class classType = defaultClass.getClassLoader().loadClass(className);
		Object o = newInstance(classType);
		if (isPrimitive(classType))
			throw new Exception("Parse Error");
		NodeList nl = item.getChildNodes();
		Field fa[] = o.getClass().getDeclaredFields();
		for (int i = 0; i < fa.length; i++) {
			boolean isAccessible = fa[i].isAccessible();
			fa[i].setAccessible(true);
			if (!Modifier.isFinal(fa[i].getModifiers())) {
				Class c = fa[i].getType();
				Node cNode = null;
				for (int j = 0; j < nl.getLength(); j++) {
					if (!fa[i].getName().equals(nl.item(j).getNodeName()))
						continue;
					cNode = nl.item(j);
					break;
				}

				if (cNode == null && !fa[i].getType().isArray()) {
					if (!isAllowNoField())
						throw new Exception((new StringBuilder(
								"\u7F3A\u5C11\uFF1A")).append(fa[i].getName())
								.append(" \u7684\u63CF\u8FF0").toString());
				} else {
					if (fa[i].getType().isArray()) {
						Object oa = revertArray(item, fa[i].getType(),
								fa[i].getName());
						fa[i].set(o, oa);
					} else if (isPrimitive(c)) {
						NodeList nlc = cNode.getChildNodes();
						if (nlc.item(0) == null)
							fillFieldValue(fa[i], o, "");
						else
							fillFieldValue(fa[i], o, nlc.item(0).getNodeValue());
					} else {
						Object os = revertDocument(cNode, fa[i].getType(),
								nodeName);
						fa[i].set(o, os);
					}
					fa[i].setAccessible(isAccessible);
				}
			}
		}

		return o;
	}

	private Object newInstance(Class clazz) throws Exception {
		@SuppressWarnings("unchecked")
		Constructor cnst = clazz.getDeclaredConstructor(new Class[0]);
		cnst.setAccessible(true);
		return cnst.newInstance(new Object[0]);
	}

	public void setAllowNoField(boolean newAllowNoField) {
		m_AllowNoField = newAllowNoField;
	}

	private void setArrayPrimitiveValue(Object arrayObject, int location,
			Class itemClass, String value) throws Exception {
		boolean isObjectPrimitiveClass = false;
		Class classA[] = { Boolean.class, Character.class, Integer.class,
				Long.class, String.class, Double.class, Float.class,
				BigDecimal.class };
		for (int i = 0; i < classA.length; i++)
			if (classA[i] == itemClass)
				isObjectPrimitiveClass = true;

		Object itemValue = null;
		if (!value.equals("null") && isObjectPrimitiveClass)
			if (itemClass == String.class)
				itemValue = value;
			else if (itemClass == Integer.class)
				itemValue = Integer.valueOf(value);
			else if (itemClass == Boolean.class)
				itemValue = Boolean.valueOf(value);
			else if (itemClass == Character.class)
				itemValue = new Character(value.charAt(0));
			else if (itemClass == Long.class)
				itemValue = Long.valueOf(value);
			else if (itemClass == Double.class)
				itemValue = Double.valueOf(value);
			else if (itemClass == Float.class)
				itemValue = Float.valueOf(value);
			else if (itemClass == BigDecimal.class)
				itemValue = new BigDecimal(value);
		if (isObjectPrimitiveClass) {
			Array.set(arrayObject, location, itemValue);
			return;
		}
		if (itemClass == Integer.TYPE)
			Array.setInt(arrayObject, location, Integer.parseInt(value));
		else if (itemClass == Boolean.TYPE)
			Array.setBoolean(arrayObject, location, Boolean.valueOf(value)
					.booleanValue());
		else if (itemClass == Character.TYPE)
			Array.setChar(arrayObject, location, value.charAt(0));
		else if (itemClass == Long.TYPE)
			Array.setLong(arrayObject, location, Long.parseLong(value));
		else if (itemClass == Double.TYPE)
			Array.setDouble(arrayObject, location, Double.valueOf(value)
					.doubleValue());
		else if (itemClass == Float.TYPE)
			Array.setDouble(arrayObject, location, Float.valueOf(value)
					.floatValue());
	}

	public static Class classA[];
	private boolean m_AllowNoField;

	static {
		classA = (new Class[] { Boolean.class, Character.class, Integer.class,
				Long.class, Double.class, Float.class, String.class,
				BigDecimal.class, Integer.TYPE, Character.TYPE, Boolean.TYPE,
				Long.TYPE, Double.TYPE, Float.TYPE });
	}

}
