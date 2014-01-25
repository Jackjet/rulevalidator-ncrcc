package ncmdp.model;

import java.io.PrintWriter;

import ncmdp.serialize.XMLSerialize;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * 聚合关联关系，比较复杂
 * @author wangxmn
 *
 */
public class AggregationRelation extends Relation {
	private static final long serialVersionUID = -6669273525821896444L;

	public AggregationRelation(Cell source, Cell target) {
		super(source, target);
		setName("Aggregation Relation");
		setDisplayName("");
		setSourceConstraint(Constant.MULTI_1);
		setTargetConstraint(Constant.MULTI_1_N);
	}
	public Element createElement(Document doc,String componetId){
		Element ele = doc.createElement("AggregationRelation");
		ele.setAttribute("componentID", componetId);
		setElementAttribute(ele);
		super.appendBendPointEle(doc, ele);
		return ele;
	}
	public void printXMLString(PrintWriter pw,String tabStr,String componentId){
		pw.print(tabStr+"<AggregationRelation ");
		pw.print(" componentID='"+componentId+"' ");
		pw.print(genXMLAttrString());
		pw.println(">");
		super.printBendPointXML(pw,tabStr+"\t");
		pw.println(tabStr+"</AggregationRelation>");
	}
	public static AggregationRelation parseNode(Node node){
		AggregationRelation con = null;
		String name = node.getNodeName();
		if("AggregationRelation".equalsIgnoreCase(name)){
			NamedNodeMap map = node.getAttributes();
			if(map != null){
				String srcId = map.getNamedItem("source").getNodeValue();
				String tarId = map.getNamedItem("target").getNodeValue();
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new AggregationRelation(src, tar);
				if(src instanceof ValueObject){
					Relation.parseSrcAttr(node, con, (ValueObject)src);
				}
				Relation.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}
}
