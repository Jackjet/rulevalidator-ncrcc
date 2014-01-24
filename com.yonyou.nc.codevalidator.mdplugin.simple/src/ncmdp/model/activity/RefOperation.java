package ncmdp.model.activity;

import java.io.PrintWriter;
import java.io.Serializable;

import ncmdp.common.MDPConstants;
import ncmdp.model.Constant;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class RefOperation implements Serializable, Cloneable, Constant {
	private static final long serialVersionUID = -6772010010619821017L;

	private String id = NCMDPTool.generateID();

	private String opItfID = "";

	// todo Info
	private String name = "";

	private String displayName = "";

	private String opItfName = "";

	private boolean isAuthorization = true;

	private String industry = MDPConstants.BASE_INDUSTRY;

	private boolean isSource = false;
	private String versionType = MDPConstants.BASE_VERSIONTYPE;

	public RefOperation() {
		super();
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public boolean isSource() {
		return isSource;
	}

	public void setSource(boolean isSource) {
		this.isSource = isSource;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Element createElement(Document doc, boolean isEntityOperation,
			String ownerElementID) {
		Element ele = doc.createElement("refoperation");
		ele.setAttribute("ownerElementID", ownerElementID);
		this.setElementAttribute(ele);
		return ele;
	}

	private void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId());
		ele.setAttribute("opItfID", getOpItfID());
		ele.setAttribute("isAuthorization", isAuthorization() ? "true"
				: "false");
		ele.setAttribute("isSource", isSource() ? "true" : "false");
		ele.setAttribute("versiontype", getVersionType());
		ele.setAttribute("industry", getIndustry());
	}

	/**
	 * 输出bmf文件
	 * 
	 * @param pw
	 * @param tabStr
	 * @param isEntityOperation
	 * @param ownerElementID
	 */
	public void printXMLString(PrintWriter pw, String tabStr,
			boolean isEntityOperation, String ownerElementID) {
		pw.print(tabStr + "<refoperation ");
		pw.print(genXMLAttrString());
		// pw.print(" forBusinessEntity='" + (isEntityOperation ? "Y" : "N") +
		// "' ");
		pw.print(" ownerElementID='" + ownerElementID + "' ");
		pw.print(" isAuthorization='" + this.isAuthorization() + "' ");
		pw.print(" versiontype='" + this.getVersionType() + "' ");
		pw.print(" industry='" + this.getIndustry() + "' ");
		pw.print(" isSource='" + this.isSource() + "' ");
		pw.println(">");
		// pw.println(tabStr + "</operation>");
	}

	private String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' ");
		sb.append("opItfID='").append(this.getOpItfID()).append("' ");
		return sb.toString();
	}

	/**
	 * 根据 node获得 RefOperation对象
	 * 
	 * @param node
	 * @return
	 */
	public static RefOperation parseNode(Node node) {
		RefOperation operation = null;
		String name = node.getNodeName();
		if ("refoperation".equalsIgnoreCase(name)) {
			operation = new RefOperation();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				operation.setId(map.getNamedItem("id").getNodeValue());
				operation
						.setOpItfID(map.getNamedItem("opItfID").getNodeValue());
			}
			if (map.getNamedItem("isAuthorization") == null) {
				operation.setAuthorization(false);
			} else {
				operation.setAuthorization("true".equalsIgnoreCase(map
						.getNamedItem("isAuthorization").getNodeValue()));
			}

			if (map.getNamedItem("isSource") == null) {
				operation.setSource(false);
			} else {
				operation.setSource("true".equalsIgnoreCase(map.getNamedItem(
						"isSource").getNodeValue()));
			}

			if (map.getNamedItem("versiontype") != null) {
				operation.setVersionType(map.getNamedItem("versiontype")
						.getNodeValue());
			}
			if (map.getNamedItem("industry") != null) {
				operation.setIndustry(map.getNamedItem("industry")
						.getNodeValue());
			}

		}
		return operation;
	}

	public RefOperation copy() {
		RefOperation refOp = new RefOperation();
		try {
			refOp = (RefOperation) this.clone();
		} catch (CloneNotSupportedException e) {
			MDPLogger.error(e.getMessage(), e);
		}
		return refOp;
	}

	public String validate() {
		// 错误信息
		StringBuilder sb = new StringBuilder();
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpItfID() {
		return opItfID;
	}

	public void setOpItfID(String opItfID) {
		this.opItfID = opItfID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getOpItfName() {
		return opItfName;
	}

	public void setOpItfName(String opItfName) {
		this.opItfName = opItfName;
	}

	public boolean isAuthorization() {
		return isAuthorization;
	}

	public void setAuthorization(boolean isAuthorization) {
		this.isAuthorization = isAuthorization;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RefOperation) {
			if (obj != null) {
				return getId().equalsIgnoreCase(((RefOperation) obj).getId());
			}
		}
		return false;
	}
}
