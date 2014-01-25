package com.yonyou.nc.codevalidator.sdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;

public final class XmlUtils {

	private static final String CLASSPATH_PREFIX = "classpath:";
	private static final String IMPORT_TAG = "import";
	private static final String RESOURCE_TAG = "resource";
	private static final String FILE_SPLITER = "/";

	private XmlUtils() {

	}

	// /**
	// * XML�ļ��Ľ���
	// *
	// * @param fileName
	// * XML�ļ���
	// * @return XML�ĵ�����
	// */
	// public static Document parseXML(String fileName) throws RuleBaseException
	// {
	// Document doc = null;
	// try {
	// doc = getDocumentBuilder().parse(fileName);
	// } catch (Exception e) {
	// throw new RuleBaseException(e);
	// }
	// return doc;
	// }
	//
	// public static Document parseXML(File file) throws RuleBaseException {
	// Document doc = null;
	// try {
	// doc = getDocumentBuilder().parse(file);
	// } catch (Exception e) {
	// throw new RuleBaseException(e);
	// }
	// return doc;
	// }

	/**
	 * ������������Document�����ر�is
	 * 
	 * @param is
	 * @return
	 * @throws RuleBaseException
	 */
	public static Document parseNormalXml(InputStream is) throws RuleBaseException {
		Document doc = null;
		try {
			doc = getNormalDocumentBuilder().parse(is);
		} catch (SAXException e) {
			throw new RuleBaseException(e);
		} catch (IOException e) {
			throw new RuleBaseException(e);
		} catch (ParserConfigurationException e) {
			throw new RuleBaseException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return doc;
	}

	/**
	 * ����spring-beans.dtd������������SpringXmlDocument�������ܹ�������ǰdocument��
	 * �����Խ�����import��document
	 * 
	 * @param projectName
	 *            - �ӵ�ǰҵ��������ܹ����projectName
	 * @param xmlFilePath
	 *            - ��Ӧ��xml�ļ�·��
	 * @return
	 * @throws RuleBaseException
	 *             - ������xml��ȡ���쳣���Լ���ȡimport�ļ���Դ���쳣����ʱ
	 */
	public static SpringXmlDocument parseSpringXml(String projectName, String xmlFilePath) throws RuleBaseException {
		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
		InputStream is = null;
		try {
			String actualXmlFilePath = xmlFilePath.startsWith(FILE_SPLITER) ? xmlFilePath.substring(1) : xmlFilePath;
			is = classLoaderUtils.getResourceStream(projectName, actualXmlFilePath);
			if (is == null) {
				throw new RuleBaseException(String.format("�ڹ��̣�%s ��δ���ص���Ӧ��xml�ļ�: %s", projectName, xmlFilePath));
			}
			Document document = getSpringDocumentBuilder().parse(is);
			List<SpringXmlDocument> referenDocuments = getImportXmlDocument(projectName, document);
			return new SpringXmlDocument(document, referenDocuments);
		} catch (SAXException e) {
			throw new RuleBaseException(e);
		} catch (IOException e) {
			throw new RuleBaseException(e);
		} catch (ParserConfigurationException e) {
			throw new RuleBaseException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private static List<SpringXmlDocument> getImportXmlDocument(String projectName, Document document)
			throws RuleBaseException {
		List<SpringXmlDocument> result = new ArrayList<SpringXmlDocument>();
		NodeList childNodes = document.getElementsByTagName(IMPORT_TAG);
		if (null == childNodes || childNodes.getLength() == 0) {
			return result;
		}
		int childNodeLength = childNodes.getLength();
		for (int nodeIndex = 0; nodeIndex < childNodeLength; nodeIndex++) {
			Node childNode = childNodes.item(nodeIndex);
			if (childNode instanceof Element) {
				String filePath = childNode.getAttributes().getNamedItem(RESOURCE_TAG).getNodeValue();
				if (filePath.startsWith(CLASSPATH_PREFIX)) {
					filePath = filePath.substring(CLASSPATH_PREFIX.length());
					result.add(parseSpringXml(projectName, filePath));
				}
			}
		}
		return result;
	}

	private static DocumentBuilder getSpringDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		// factory.setSchema(null);
		factory.setValidating(false);
		DocumentBuilder db = factory.newDocumentBuilder();
		db.setEntityResolver(new EntityResolver() {
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				// if (publicId.equals("-//SPRING//DTD BEAN//EN")) {
				// /*
				// * InputStream in; try { in =
				// * ClassLoaderUtilsFactory.getClassLoaderUtils
				// * ().getResourceAsStream( "/spring-beans-2.0.dtd"); } catch
				// * (RuleClassLoadException e) { throw new IOException(e); }
				// */
				// }
				// return null;
				InputStream in = this.getClass().getClassLoader()
						.getResourceAsStream("com/yonyou/nc/codevalidator/sdk/utils/spring-beans.dtd");
				return new InputSource(in);
			}
		});
		return db;
	}

	private static DocumentBuilder getNormalDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		// factory.setSchema(null);
		factory.setValidating(false);
		DocumentBuilder db = factory.newDocumentBuilder();
		return db;
	}
}
