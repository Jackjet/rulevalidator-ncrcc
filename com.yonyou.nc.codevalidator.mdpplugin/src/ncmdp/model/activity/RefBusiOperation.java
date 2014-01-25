package ncmdp.model.activity;

import java.io.PrintWriter;
import java.io.Serializable;

import ncmdp.common.MDPConstants;
import ncmdp.model.Constant;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class RefBusiOperation implements Serializable, Cloneable, Constant {
	private static final long serialVersionUID = 1L;

	private String id = NCMDPTool.generateID();

	private String busiActID = "";

	private String realBusiOpid = "";

	// todo Info
	private String name = "";

	private String displayName = "";

	private boolean isAuthorization = true;

	private String industry = MDPConstants.BASE_INDUSTRY;

	private boolean isSource = false;
	private String versionType = MDPConstants.BASE_VERSIONTYPE;

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
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

	public RefBusiOperation() {
		super();
	}

	public RefBusiOperation(BusiOperation refbusiOp, String busiActID) {
		super();
		this.busiActID = busiActID;
		if (refbusiOp != null) {
			name = refbusiOp.getName();
			realBusiOpid = refbusiOp.getId();
			displayName = refbusiOp.getDisplayName();
		}
	}

	public Element createElement(Document doc, boolean isEntityOperation,
			String ownerElementID) {
		Element ele = doc.createElement("refbusioperation");
		ele.setAttribute("ownerElementID", ownerElementID);
		this.setElementAttribute(ele);
		return ele;
	}

	private void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId());
		ele.setAttribute("realBusiOpid", getRealBusiOpid());
		ele.setAttribute("isAuthorization", isAuthorization() ? "true"
				: "false");
		ele.setAttribute("isSource", isSource() ? "true" : "false");
		ele.setAttribute("industry", getIndustry());
		ele.setAttribute("versiontype", getVersionType());
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
		pw.print(tabStr + "<refbusioperation ");
		pw.print(genXMLAttrString());
		// pw.print(" forBusinessEntity='" + (isEntityOperation ? "Y" : "N") +
		// "' ");
		pw.print(" ownerElementID='" + ownerElementID + "' ");
		pw.print(" isAuthorization='" + this.isAuthorization() + "' ");
		pw.print(" isSource='" + this.isSource() + "' ");
		pw.print(" versiontype='" + this.getVersionType() + "' ");
		pw.print(" industry='" + this.getIndustry() + "' ");
		pw.println(">");
		// pw.println(tabStr + "</refbusioperation>");
	}

	private String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' ");
		sb.append("realBusiOpid='").append(this.getRealBusiOpid()).append("' ");
		return sb.toString();
	}

	/**
	 * 根据 node获得 RefOperation对象
	 * 
	 * @param node
	 * @return
	 */
	public static RefBusiOperation parseNode(Node node) {
		RefBusiOperation operation = null;
		String name = node.getNodeName();
		if ("refbusioperation".equalsIgnoreCase(name)) {
			operation = new RefBusiOperation();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				operation.setId(map.getNamedItem("id").getNodeValue());
				operation.setRealBusiOpid(map.getNamedItem("realBusiOpid")
						.getNodeValue());
				operation.setBusiActID(map.getNamedItem("ownerElementID")
						.getNodeValue());

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
			if (map.getNamedItem("industry") != null) {
				operation.setIndustry(map.getNamedItem("industry")
						.getNodeValue());
			}
			if (map.getNamedItem("versiontype") != null) {
				operation.setVersionType(map.getNamedItem("versiontype")
						.getNodeValue());
			}
		}
		return operation;
	}

	public RefBusiOperation copy() {
		RefBusiOperation refOp = new RefBusiOperation();
		try {
			refOp = (RefBusiOperation) this.clone();
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

	public boolean isAuthorization() {
		return isAuthorization;
	}

	public void setAuthorization(boolean isAuthorization) {
		this.isAuthorization = isAuthorization;
	}

	public String getRealBusiOpid() {
		return realBusiOpid;
	}

	public void setRealBusiOpid(String realBusiOpid) {
		this.realBusiOpid = realBusiOpid;
	}

	public String getBusiActID() {
		return busiActID;
	}

	public void setBusiActID(String busiActID) {
		this.busiActID = busiActID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof RefBusiOperation)) {
			return false;
		}
		// return getId().equalsIgnoreCase(((RefBusiOperation) obj).getId());
		if (StringUtil.isEmptyWithTrim(busiActID)
				|| StringUtil.isEmptyWithTrim(realBusiOpid)) {
			return false;
		}
		RefBusiOperation ref = (RefBusiOperation) obj;
		return busiActID.endsWith(ref.getBusiActID())
				&& realBusiOpid.equalsIgnoreCase(ref.getRealBusiOpid());
	}

	@Override
	public int hashCode() {
		return (busiActID + realBusiOpid).hashCode();
	}
}
