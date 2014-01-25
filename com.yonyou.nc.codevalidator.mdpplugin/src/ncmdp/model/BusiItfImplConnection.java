package ncmdp.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import ncmdp.serialize.XMLSerialize;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 实现关系
 * @author wangxmn
 *
 */
public class BusiItfImplConnection extends Connection {
	private static final long serialVersionUID = 4253127216358963769L;
	public static final String PROP_BUSIITFCONNIMPLCLSNAME= "prop_busiItfConnImplClsName"; 
	private String busiItfImplClsName = ""; 
	public BusiItfImplConnection(Cell source, Cell target) {
		super(source, target);
		setName("busiItf connection"); 
	}
	@Override
	public void connect() {
		super.connect();
		Cell target = getTarget();
		Cell source = getSource();
		if(target instanceof BusinInterface && source instanceof ValueObject){
			((ValueObject)source).addBusiItf((BusinInterface)target);
		}else if(target instanceof Reference && ((Reference)target).getReferencedCell() instanceof BusinInterface && source instanceof ValueObject ){
			BusinInterface itf =(BusinInterface) ((Reference)target).getReferencedCell();
			((ValueObject)source).addBusiItf(itf);
		}
	}

	@Override
	public void disConnect() {
		Cell target = getTarget();
		Cell source = getSource();
		if(target instanceof BusinInterface && source instanceof ValueObject){
			((ValueObject)source).removeBusiItf((BusinInterface)target);
		}else if(target instanceof Reference && ((Reference)target).getReferencedCell() instanceof BusinInterface && source instanceof ValueObject ){
			BusinInterface itf =(BusinInterface) ((Reference)target).getReferencedCell();
			((ValueObject)source).removeBusiItf(itf);
		}
		super.disConnect();
	}
	public Element createElement(Document doc,String componetId){
		Element ele = doc.createElement("busiitfconnection"); 
		ele.setAttribute("componentID", componetId); 
		setElementAttribute(ele);
		ele.setAttribute("bizItfImpClassName", getBusiItfImplClsName()); 
		super.appendBendPointEle(doc, ele);
		return ele;
		
	}
	public void printXMLString(PrintWriter pw,String tabStr,String componetId){
		pw.print(tabStr+"<busiitfconnection "); 
		pw.print(" componentID='"+componetId+"' ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		super.printBendPointXML(pw,tabStr+"\t"); 
		pw.println(tabStr+"</busiitfconnection>"); 
	}
	public static BusiItfImplConnection parseNode(Node node){
		BusiItfImplConnection con = null;
		String name = node.getNodeName();
		if("busiitfconnection".equalsIgnoreCase(name)){ 
			NamedNodeMap map = node.getAttributes();
			if(map != null){
				String srcId = map.getNamedItem("source").getNodeValue(); 
				String tarId = map.getNamedItem("target").getNodeValue(); 
				Cell src = XMLSerialize.getInstance().getCell(srcId);
				Cell tar = XMLSerialize.getInstance().getCell(tarId);
				con = new BusiItfImplConnection(src, tar);
				if(map.getNamedItem("bizItfImpClassName") != null){ 
					con.setBusiItfImplClsName(map.getNamedItem("bizItfImpClassName").getNodeValue()); 
				}
				Connection.parseNodeAttr(node, con);
				Connection.parseConnectionBendPoint(node, con);
			}
		}
		return con;
	}
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));

		PropertyDescriptor[] desc = new PropertyDescriptor[1];
		desc[0] = new TextPropertyDescriptor(PROP_BUSIITFCONNIMPLCLSNAME,Messages.BusiItfImplConnection_17);
		desc[0].setCategory(Messages.BusiItfImplConnection_18);
		al.addAll(Arrays.asList(desc));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	@Override
	public Object getPropertyValue(Object id) {
		if(PROP_BUSIITFCONNIMPLCLSNAME.equals(id)){
			return getBusiItfImplClsName();
		}else{
			return super.getPropertyValue(id);
		}
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {
		if(PROP_BUSIITFCONNIMPLCLSNAME.equals(id)){
			setBusiItfImplClsName((String)value);
		}else{
			super.setPropertyValue(id, value);
		}
	}
	public String getBusiItfImplClsName() {
		return busiItfImplClsName;
	}
	public void setBusiItfImplClsName(String busiItfImplClsName) {
		this.busiItfImplClsName = busiItfImplClsName;
	}
}
