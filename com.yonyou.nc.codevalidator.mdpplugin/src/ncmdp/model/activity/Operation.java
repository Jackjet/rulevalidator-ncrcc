package ncmdp.model.activity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

import ncmdp.model.Constant;
import ncmdp.model.Type;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;
import ncmdp.wizard.multiwizard.util.IMultiElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Operation implements Serializable, Cloneable, Constant,
		IMultiElement {
	private static final long serialVersionUID = 1277012240611457746L;

	private String id = NCMDPTool.generateID();

	private String name = "operationName"; //$NON-NLS-1$

	// private String returnType = Constant.TYPE_STRING;
	private Type type = null;

	private String typeStyle = TYPE_STYLES[0];

	private String visibility = Constant.VISIBILITY_PUBLIC;

	private String description = null;

	private String methodException = "nc.vo.pub.BusinessException"; //$NON-NLS-1$

	private String resid = ""; //$NON-NLS-1$
	// private boolean isAuthorization = true;

	private boolean isAggVOReturn = false;

	private boolean isBusiActivity = false;

	private String displayName = "operationDisplay"; //$NON-NLS-1$

	private String defClassName = null;

	private String help = null;

	private String transKind = Constant.TRANS_NEVER;

	private String versionType = "0"; //$NON-NLS-1$

	private String opItfID = ""; //$NON-NLS-1$

	private ArrayList<Parameter> paras = new ArrayList<Parameter>();

	// /** industry */
	// private boolean isSource = false;
	//
	// public boolean isSource() {
	// return isSource;
	// }
	//
	// public void setSource(boolean isSource) {
	// this.isSource = isSource;
	// }

	public String getDescription() {
		return description;
	}

	public String getMethodException() {
		return methodException;
	}

	public void setMethodException(String methodException) {
		this.methodException = methodException;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAggVOReturn() {
		return isAggVOReturn;
	}

	public void setAggVOReturn(boolean isAggVOReturn) {
		this.isAggVOReturn = isAggVOReturn;
	}

	// public boolean isAuthorization() {
	// return isAuthorization;
	// }
	//
	// public void setAuthorization(boolean isAuthorization) {
	// this.isAuthorization = isAuthorization;
	// }

	public boolean isBusiActivity() {
		return isBusiActivity;
	}

	public void setBusiActivity(boolean isBusiActivity) {
		this.isBusiActivity = isBusiActivity;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDefClassName() {
		return defClassName;
	}

	public void setDefClassName(String defClassName) {
		this.defClassName = defClassName;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getReturnType() {
		return type;
	}

	public void setReturnType(Type returnType) {
		this.type = returnType;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getTransKind() {
		return transKind;
	}

	public void setTransKind(String transKind) {
		this.transKind = transKind;
	}

	public ArrayList<Parameter> getParas() {
		return paras;
	}

	private void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId()); //$NON-NLS-1$
		ele.setAttribute("name", getName()); //$NON-NLS-1$
		ele.setAttribute("dataTypeStyle", getTypeStyle()); //$NON-NLS-1$

		ele.setAttribute("opItfID", getOpItfID()); //$NON-NLS-1$
		if (getReturnType() != null) {
			getReturnType().setElementAttribute(ele);
		}
		ele.setAttribute("visibility", getVisibility()); //$NON-NLS-1$
		ele.setAttribute("methodException", getMethodException()); //$NON-NLS-1$
		ele.setAttribute("resid", getResid()); //$NON-NLS-1$
		ele.setAttribute("isAggVOReturn", isAggVOReturn() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// ele.setAttribute("isAuthorization", isAuthorization() ? "true":
		// "false");
		ele.setAttribute("isBusiActivity", isBusiActivity() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ele.setAttribute("displayName", getDisplayName()); //$NON-NLS-1$
		ele.setAttribute("help", getHelp()); //$NON-NLS-1$
		ele.setAttribute("defclassname", getDefClassName()); //$NON-NLS-1$
		ele.setAttribute("transKind", getTransKind()); //$NON-NLS-1$
		ele.setAttribute("versionType", getVersionType()); //$NON-NLS-1$
		// ele.setAttribute("isSource", isSource() ? "true" : "false");

	}

	private String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("name='").append(this.getName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("dataTypeStyle='").append(this.getTypeStyle()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$

		if (getReturnType() != null) {
			sb.append(getReturnType().genXMLAttrString());
		}
		sb.append("visibility='").append(this.getVisibility()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("description='").append(this.getDescription()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("methodException='").append(this.getMethodException()) //$NON-NLS-1$
				.append("' "); //$NON-NLS-1$
		sb.append("resid='").append(this.getResid()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		// sb.append("isAuthorization='").append(this.isAuthorization())
		// .append("' ");
		sb.append("isAggVOReturn='").append(this.isAggVOReturn()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("isBusiActivity='").append(this.isBusiActivity()) //$NON-NLS-1$
				.append("' "); //$NON-NLS-1$
		sb.append("displayName='").append(this.getDisplayName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("help='").append(this.getHelp()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("defclassname='").append(this.getDefClassName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("transKind='").append(this.getTransKind()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("opItfID='").append(this.getOpItfID()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("versionType='").append(this.getVersionType()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		// sb.append("isSource='").append(this.isSource()).append("' ");
		return sb.toString();
	}

	public Element createElement(Document doc, boolean isEntityOperation,
			String ownerElementID) {
		Element ele = doc.createElement("operation"); //$NON-NLS-1$
		this.setElementAttribute(ele);
		ele.setAttribute("forBusinessEntity", isEntityOperation ? "Y" : "N"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ele.setAttribute("ownerElementID", ownerElementID); //$NON-NLS-1$
		ArrayList<Parameter> params = getParas();

		Element paramEle = doc.createElement("paramlist"); //$NON-NLS-1$
		ele.appendChild(paramEle);

		for (int i = 0; i < params.size(); i++) {
			Parameter param = params.get(i);
			paramEle.appendChild(param.createElement(doc, getId(), i));
		}

		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr,
			boolean isEntityOperation, String ownerElementID) {
		pw.print(tabStr + "<operation "); //$NON-NLS-1$
		pw.print(genXMLAttrString());
		pw.print(" forBusinessEntity='" + (isEntityOperation ? "Y" : "N") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "' "); //$NON-NLS-1$
		pw.print(" ownerElementID='" + ownerElementID + "' "); //$NON-NLS-1$ //$NON-NLS-2$
		pw.println(">"); //$NON-NLS-1$
		ArrayList<Parameter> params = getParas();
		pw.println(tabStr + "\t<paramlist>"); //$NON-NLS-1$
		for (int i = 0; i < params.size(); i++) {
			Parameter param = params.get(i);
			param.printXMLString(pw, getId(), i);
		}
		pw.println(tabStr + "\t</paramlist>"); //$NON-NLS-1$
		pw.println(tabStr + "</operation>"); //$NON-NLS-1$
	}

	public static Operation parseNode(Node node) {
		Operation operation = null;
		String name = node.getNodeName();
		if ("operation".equalsIgnoreCase(name)) { //$NON-NLS-1$
			operation = new Operation();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				if (map.getNamedItem("id") != null) { //$NON-NLS-1$
					operation.setId(map.getNamedItem("id").getNodeValue()); //$NON-NLS-1$
				}
				operation.setName(map.getNamedItem("name").getNodeValue()); //$NON-NLS-1$
				Type type = Type.parseType(map);
				if (type != null)
					operation.setReturnType(type);
				if (map.getNamedItem("dataTypeStyle") != null) //$NON-NLS-1$
					operation.setTypeStyle(map.getNamedItem("dataTypeStyle") //$NON-NLS-1$
							.getNodeValue());

				// operation.setReturnType(map.getNamedItem("returnType").getNodeValue());
				operation.setVisibility(map.getNamedItem("visibility") //$NON-NLS-1$
						.getNodeValue());
				if (map.getNamedItem("description") != null) { //$NON-NLS-1$
					operation.setDescription(map.getNamedItem("description") //$NON-NLS-1$
							.getNodeValue());
				}
				// if (map.getNamedItem("isAuthorization") == null) {
				// operation.setAuthorization(false);
				// } else {
				// operation.setAuthorization("true".equalsIgnoreCase(map
				// .getNamedItem("isAuthorization").getNodeValue()));
				// }
				if (map.getNamedItem("isAggVOReturn") == null) { //$NON-NLS-1$
					operation.setAggVOReturn(false);
				} else {
					operation.setAggVOReturn("true".equalsIgnoreCase(map //$NON-NLS-1$
							.getNamedItem("isAggVOReturn").getNodeValue())); //$NON-NLS-1$
				}
				if (map.getNamedItem("isBusiActivity") == null) { //$NON-NLS-1$
					operation.setBusiActivity(false);
				} else {
					operation.setBusiActivity("true".equalsIgnoreCase(map //$NON-NLS-1$
							.getNamedItem("isBusiActivity").getNodeValue())); //$NON-NLS-1$
				}
				operation.setDisplayName(map.getNamedItem("displayName") //$NON-NLS-1$
						.getNodeValue());
				operation.setHelp(map.getNamedItem("help").getNodeValue()); //$NON-NLS-1$
				if (map.getNamedItem("defclassname") != null) { //$NON-NLS-1$
					operation.setDefClassName(map.getNamedItem("defclassname") //$NON-NLS-1$
							.getNodeValue());
				}
				if (map.getNamedItem("resid") != null) { //$NON-NLS-1$
					operation
							.setResid(map.getNamedItem("resid").getNodeValue()); //$NON-NLS-1$
				}
				if (map.getNamedItem("methodException") != null) { //$NON-NLS-1$
					operation.setMethodException(map.getNamedItem(
							"methodException").getNodeValue()); //$NON-NLS-1$
				}
				operation.setTransKind(map.getNamedItem("transKind") //$NON-NLS-1$
						.getNodeValue());
				operation
						.setOpItfID(map.getNamedItem("opItfID").getNodeValue()); //$NON-NLS-1$
				if (map.getNamedItem("versionType") != null) { //$NON-NLS-1$
					operation.setVersionType(map.getNamedItem("versionType") //$NON-NLS-1$
							.getNodeValue());
				}
				// if (map.getNamedItem("isSource") == null) {
				// operation.setSource(false);
				// } else {
				// operation.setSource("true".equalsIgnoreCase(map
				// .getNamedItem("isSource").getNodeValue()));
				// }
			}
			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node child = nl.item(i);
				if ("paramlist".equalsIgnoreCase(child.getNodeName())) { //$NON-NLS-1$
					NodeList paraNL = child.getChildNodes();
					for (int j = 0; j < paraNL.getLength(); j++) {
						Node paraNode = paraNL.item(j);
						Parameter para = Parameter.parseNode(paraNode);
						if (para != null)
							operation.getParas().add(para);
					}
				}
			}
		}
		return operation;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (getName() == null || getName().trim().length() == 0) {
			sb.append(Messages.Operation_99);
		}
		if (getDisplayName() == null || getDisplayName().trim().length() == 0) {
			sb.append(Messages.Operation_100);
		}
		// if (getReturnType() == null) {
		// sb.append("操作：" + getDisplayName() + "返回值类型不能为空\r\n");
		// }
		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		for (int i = 0; i < getParas().size(); i++) {
			Parameter para = getParas().get(i);
			String msg = para.validate();
			if (msg != null)
				sb.append(msg + "\r\n"); //$NON-NLS-1$
			String name = para.getName();
			if (name != null && !al1.contains(name)) {
				al1.add(name);
			} else {
				sb.append(Messages.Operation_102 + name + "\r\n"); //$NON-NLS-2$
			}
			String displayName = para.getDisplayName();
			if (displayName != null && !al2.contains(displayName)) {
				al2.add(displayName);
			} else {
				sb.append(Messages.Operation_104 + displayName + "\r\n"); //$NON-NLS-2$
			}
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeStyle() {
		return typeStyle;
	}

	public void setTypeStyle(String typeStyle) {
		this.typeStyle = typeStyle;
	}

	public Operation copy() {
		Operation operation = new Operation();
		operation.setDescription(this.getDescription());
		operation.setMethodException(this.getMethodException());
		operation.setResid(this.getResid());
		operation.setAggVOReturn(this.isAggVOReturn());
		// operation.setAuthorization(this.isAuthorization());
		operation.setBusiActivity(this.isBusiActivity());
		operation.setDisplayName(this.getDisplayName());
		operation.setHelp(this.getHelp());
		operation.setDefClassName(this.getDefClassName());
		operation.setName(this.getName());
		operation.setReturnType(this.getReturnType());
		operation.setTransKind(this.getTransKind());
		operation.setTypeStyle(this.getTypeStyle());
		operation.setOpItfID(this.getOpItfID());
		operation.setVersionType(this.getVersionType());
		// operation.setSource(operation.isSource());
		operation.setVisibility(this.getVisibility());
		ArrayList<Parameter> al = this.getParas();
		for (int i = 0; i < al.size(); i++) {
			try {
				operation.getParas().add((Parameter) al.get(i).clone());
			} catch (CloneNotSupportedException e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		return operation;
	}

	public String getOpItfID() {
		return opItfID;
	}

	public void setOpItfID(String opItfID) {
		this.opItfID = opItfID;
	}

	//
	// public void dealIncDevForIndustry() {
	// setSource(true);
	// }

	@Override
	public String getElementType() {
		return Messages.Operation_106;
	}
}
