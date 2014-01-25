package ncmdp.serialize;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;

public class DomTreeToXML {
	public static void domTreeToXML(Document doc, File file){
		try {
			domTreeToStream(doc, new FileOutputStream(file));
//			String str = "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0' " + 
//			" xmlns:xalan='http://xml.apache.org/xslt' " +
//			" exclude-result-prefixes='xalan'>" + 
//			"  <xsl:output method='xml' indent='yes' xalan:indent-amount='4'/>" +
//			"  <xsl:strip-space elements='*'/>" + 
//			"  <xsl:template match='/'>" + 
//			"    <xsl:apply-templates/>" + 
//			"  </xsl:template>" + 
//			"  <xsl:template match='node() | @*'>" + 
//			"        <xsl:copy>" + 
//			"          <xsl:apply-templates select='node() | @*'/>" + 
//			"        </xsl:copy>" + 
//			"  </xsl:template>" + 
//			"</xsl:stylesheet>";
//			Source s = new StreamSource(new ByteArrayInputStream(str.getBytes()));
//			Transformer t = TransformerFactory.newInstance().newTransformer(s);
//			t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(file)));
//
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MDPLogger.error(e.getMessage(), e);
		}
		
	}
	public static void domTreeToStream(Document doc, OutputStream out){
		try {
			String str = "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0' " + 
			" xmlns:xalan='http://xml.apache.org/xslt' " +
			" exclude-result-prefixes='xalan'>" + 
			"  <xsl:output method='xml' indent='yes' xalan:indent-amount='4'/>" +
			"  <xsl:strip-space elements='*'/>" + 
			"  <xsl:template match='/'>" + 
			"    <xsl:apply-templates/>" + 
			"  </xsl:template>" + 
			"  <xsl:template match='node() | @*'>" + 
			"        <xsl:copy>" + 
			"          <xsl:apply-templates select='node() | @*'/>" + 
			"        </xsl:copy>" + 
			"  </xsl:template>" + 
			"</xsl:stylesheet>";
			Source s = new StreamSource(new ByteArrayInputStream(str.getBytes()));
			Transformer t = TransformerFactory.newInstance().newTransformer(s);
			t.transform(new DOMSource(doc), new StreamResult(out));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			MDPLogger.error(e.getMessage(), e);
		}
	}
}
