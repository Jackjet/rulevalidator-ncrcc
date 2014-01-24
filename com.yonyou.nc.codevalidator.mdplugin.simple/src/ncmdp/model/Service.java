package ncmdp.model;

import java.io.PrintWriter;
import java.util.List;

import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.serialize.XMLSerialize;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Service extends OpInterface {
	private static final long serialVersionUID = 3902799675232981051L;
	public Service() {
		super("service");
	}

//	@Override
//	public IPropertyDescriptor[] getPropertyDescriptors() {
//		return super.getPropertyDescriptors();
//	}

	@Override
	public Object getPropertyValue(Object id) {
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
	}
//	public String genXMLAttrString(){
//		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
//		sb.append("remote='").append(this.isRemote()? "true":"false").append("' ");
//		sb.append("singleton='").append(this.isSingleton()?"true":"false").append("' ");
//		sb.append("transprop='").append(this.getTransProp()).append("' ");
//		return sb.toString();
//
//	}
	public Element createElement(Document doc, JGraph graph){
		Element ele = doc.createElement("service");
		ele.setAttribute("componentID", graph.getId());
		setElementAttribute(ele);
		Element operlistEle = doc.createElement("operationlist");
		ele.appendChild(operlistEle);
//		pw.println(tabStr+"\t<operationlist>");
		List<Operation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			operlistEle.appendChild(opers.get(i).createElement(doc, false, getId()));
		}
		for (int i = 0; i < getAlPropertys().size(); i++) {
			ele.appendChild(getAlPropertys().get(i).createElement(doc));
		}
		return ele;

	}
	public void printXMLString(PrintWriter pw,String tabStr,JGraph graph){
		pw.print(tabStr+"<service ");
		pw.print(" componentID='"+graph.getId()+"' ");
		pw.print(genXMLAttrString());
		pw.println(">");
		pw.println(tabStr+"\t<operationlist>");
		List<Operation> opers = getOperations();
		for (int i = 0; i < opers.size(); i++) {
			opers.get(i).printXMLString(pw,tabStr+"\t\t",false, getId());
		}
		pw.println(tabStr+"\t</operationlist>");
		for (int i = 0; i < getAlPropertys().size(); i++) {
			pw.println(getAlPropertys().get(i).serializedXMLString(tabStr+"\t"));
		}
		pw.println(tabStr+"</service>");
	}
	protected static void parseNodeAttr(Node node, Service service){
		NamedNodeMap map = node.getAttributes();
		if(map != null){
			OpInterface.parseNodeAttr(node, service);
//			String strRemote = map.getNamedItem("remote").getNodeValue();
//			service.setRemote("true".equalsIgnoreCase(strRemote));
//			String strSingleton = map.getNamedItem("singleton").getNodeValue();
//			service.setSingleton("true".equalsIgnoreCase(strSingleton));
//			if(map.getNamedItem("transprop")!=null)
//				service.setTransProp(map.getNamedItem("transprop").getNodeValue());
		} 
	}
	public static Service parseNode(Node node){
		Service service = null;
		String name = node.getNodeName(); 
		if("service".equalsIgnoreCase(name)){
			service = new Service();
			Service.parseNodeAttr(node, service);
			OpInterface.parseOperation(node, service);
			parseOperationProperty(node, service);
			XMLSerialize.getInstance().register(service);
		}
		return service;
	}
	protected void copy0(Service service){
		super.copy0(service);
	}
	public Service copy(){
		Service ser = new Service();
		copy0(ser);
		return ser;
	}

}
