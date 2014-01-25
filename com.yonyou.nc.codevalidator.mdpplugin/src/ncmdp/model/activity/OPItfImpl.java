package ncmdp.model.activity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;

import ncmdp.model.Constant;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class OPItfImpl implements Serializable, Cloneable, Constant {
	private static final long serialVersionUID = -6972333310619821017L;

	public static String OPITFIMPL_NODE_NAME = "opItfImpl";

	private String id = NCMDPTool.generateID();

	private String name = "name";

	private String displayName = "displayName";

	private String fullclassName = "fullclassName";

	private String description;

	private String tag;

	private String creator;

	private Date createTime;

	private String type;

	private String versionType = "0";

	public OPItfImpl() {
		super();
		setCreateTime(new Date());
	}

	/*********************************************/
	public Element createElement(Document doc, boolean isEntityOperation,
			String ownerElementID) {
		Element ele = doc.createElement(OPITFIMPL_NODE_NAME);
		this.setElementAttribute(ele);
		ele.setAttribute("ownerElementID", ownerElementID);
		return ele;
	}

	private void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId());
		ele.setAttribute("name", getName());
		ele.setAttribute("displayName", getDisplayName());
		ele.setAttribute("fullClassName", getFullClassName());
		ele.setAttribute("description", getDescription());
		ele.setAttribute("tag", getTag());
		ele.setAttribute("creator", getCreator());
		ele.setAttribute("createTime",
				NCMDPTool.formatDateString(getCreateTime()));
		ele.setAttribute("type", getType());
		ele.setAttribute("versionType", getVersionType());

	}

	public void printXMLString(PrintWriter pw, String tabStr,
			boolean isEntityOperation, String ownerElementID) {
		pw.print(tabStr + "<" + OPITFIMPL_NODE_NAME);
		pw.print(genXMLAttrString());
		pw.print(" ownerElementID='" + ownerElementID + "' ");
		pw.println("/" + OPITFIMPL_NODE_NAME + ">");
	}

	private String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' ");
		sb.append("name='").append(this.getName()).append("' ");
		sb.append("displayName='").append(this.getDisplayName()).append("' ");
		sb.append("fullClassName='").append(this.getFullClassName())
				.append("' ");
		sb.append("tag='").append(this.getTag()).append("' ");
		sb.append("creator='").append(this.getCreator()).append("' ");
		sb.append("createTime='")
				.append(NCMDPTool.formatDateString(getCreateTime()))
				.append("' ");
		sb.append("description='").append(this.getDescription()).append("' ");
		sb.append("versionType='").append(this.getVersionType()).append("' ");
		return sb.toString();
	}

	public static OPItfImpl parseNode(Node node) {
		OPItfImpl opImpl = null;
		String name = node.getNodeName();
		if (OPITFIMPL_NODE_NAME.equalsIgnoreCase(name)) {
			opImpl = new OPItfImpl();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				if (map.getNamedItem("id") != null) {
					opImpl.setId(map.getNamedItem("id").getNodeValue());
				}
				opImpl.setName(map.getNamedItem("name").getNodeValue());
				opImpl.setDisplayName(map.getNamedItem("displayName")
						.getNodeValue());
				opImpl.setFullclassName(map.getNamedItem("fullClassName")
						.getNodeValue());
				opImpl.setTag(map.getNamedItem("tag").getNodeValue());
				opImpl.setCreator(map.getNamedItem("creator").getNodeValue());
				opImpl.setCreateTime(NCMDPTool.parseStringToDate(map
						.getNamedItem("createTime").getNodeValue()));
				opImpl.setDescription(map.getNamedItem("description")
						.getNodeValue());
				if (map.getNamedItem("versionType") != null) {
					opImpl.setVersionType(map.getNamedItem("versionType")
							.getNodeValue());
				}
			}
		}
		return opImpl;
	}

	public OPItfImpl copy() {
		OPItfImpl opImpl = null;
		try {
			opImpl = (OPItfImpl) this.clone();
		} catch (CloneNotSupportedException e) {
			MDPLogger.error(e.getMessage(), e);
		}
		return opImpl;
	}

	public String validate() {
		return null;
	}

	/*********************************************/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullClassName() {
		return fullclassName;
	}

	public void setFullclassName(String fullclassName) {
		this.fullclassName = fullclassName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		if (id != null && obj != null && obj instanceof OPItfImpl) {
			return id.endsWith(((OPItfImpl) obj).getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
