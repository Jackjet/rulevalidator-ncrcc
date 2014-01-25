package ncmdp.model.activity;

import java.io.PrintWriter;
import java.io.Serializable;

import ncmdp.model.Constant;
import ncmdp.model.Type;
import ncmdp.tool.NCMDPTool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Parameter implements Serializable, Cloneable, Constant {
	private static final long serialVersionUID = 1932209369734869446L;

	private String name = "paraName"; //$NON-NLS-1$

	private String displayName = "paraDisplayName"; //$NON-NLS-1$

	private String paramDefClassname = null;

	private boolean isAggVO = false;
	
	private String paramTypeForLog = "7";//业务日志使用的参数类型，对应ParamTypeEnum //$NON-NLS-1$

	private Type paraType = null;

	private String typeStyle = TYPE_STYLES[0];

	private String desc = null;

	private String help = null;

	private String versionType = "0"; //$NON-NLS-1$

	public Parameter() {
		super();
		if (NCMDPTool.getBaseTypes().length > 0) {
			setParaType(NCMDPTool.getBaseTypes()[0]);
		}
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isAggVO() {
		return isAggVO;
	}

	public void setAggVO(boolean isAggVO) {
		this.isAggVO = isAggVO;
	}

	public String getParamTypeForLog() {
		return paramTypeForLog;
	}

	public void setParamTypeForLog(String paramTypeForLog) {
		this.paramTypeForLog = paramTypeForLog;
	}

	public String getParamDefClassname() {
		return paramDefClassname;
	}

	public void setParamDefClassname(String paramDefClassname) {
		this.paramDefClassname = paramDefClassname;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public Type getParaType() {
		return paraType;
	}

	public void setParaType(Type paraType) {
		this.paraType = paraType;
	}

	public void setElementAttribute(Element ele) {
		ele.setAttribute("name", getName()); //$NON-NLS-1$
		ele.setAttribute("displayName", getDisplayName()); //$NON-NLS-1$
		ele.setAttribute("dataTypeStyle", getTypeStyle()); //$NON-NLS-1$
		if (getParaType() != null) {
			getParaType().setElementAttribute(ele);
		}
		ele.setAttribute("description", getDesc()); //$NON-NLS-1$
		ele.setAttribute("paramdefclassname", getParamDefClassname()); //$NON-NLS-1$
		ele.setAttribute("paramTypeForLog", getParamTypeForLog()); //$NON-NLS-1$
		ele.setAttribute("isAggVO", isAggVO() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ele.setAttribute("versionType", getVersionType()); //$NON-NLS-1$

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name='").append(this.getName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("displayName='").append(this.getDisplayName()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("dataTypeStyle='").append(getTypeStyle()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		if (getParaType() != null) {
			sb.append(getParaType().genXMLAttrString());
		}
		sb.append("description='").append(this.getDesc()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("help='").append(this.getHelp()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("isAggVO='").append(this.isAggVO()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append("paramdefclassname='").append(this.getParamDefClassname()) //$NON-NLS-1$
				.append("' "); //$NON-NLS-1$
		sb.append("paramTypeForLog='").append(this.getParamTypeForLog()) //$NON-NLS-1$
		.append("' "); //$NON-NLS-1$
		
		sb.append("versionType='").append(this.getVersionType()).append("' "); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public Element createElement(Document doc, String OperationId, int index) {
		Element ele = doc.createElement("parameter"); //$NON-NLS-1$
		ele.setAttribute("operationID", OperationId); //$NON-NLS-1$
		ele.setAttribute("sequence", index + ""); //$NON-NLS-1$ //$NON-NLS-2$
		this.setElementAttribute(ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String OperationId, int index) {
		pw.print("<parameter "); //$NON-NLS-1$
		pw.print(" operationID='" + OperationId + "' "); //$NON-NLS-1$ //$NON-NLS-2$
		pw.print(" sequence='" + index + "' "); //$NON-NLS-1$ //$NON-NLS-2$
		pw.print(genXMLAttrString());
		pw.println("/>"); //$NON-NLS-1$
	}

	public static Parameter parseNode(Node node) {
		Parameter param = null;
		String name = node.getNodeName();
		if ("parameter".equalsIgnoreCase(name)) { //$NON-NLS-1$
			param = new Parameter();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				param.setName(map.getNamedItem("name").getNodeValue()); //$NON-NLS-1$
				param.setDisplayName(map.getNamedItem("displayName") //$NON-NLS-1$
						.getNodeValue());
				if (map.getNamedItem("paramdefclassname") != null) { //$NON-NLS-1$
					param.setParamDefClassname(map.getNamedItem(
							"paramdefclassname").getNodeValue()); //$NON-NLS-1$
				}
				if (map.getNamedItem("paramTypeForLog") != null) { //$NON-NLS-1$
					param.setParamTypeForLog(map.getNamedItem(
							"paramTypeForLog").getNodeValue()); //$NON-NLS-1$
				}
				if (map.getNamedItem("isAggVO") == null) { //$NON-NLS-1$
					param.setAggVO(false);
				} else {
					param.setAggVO("true".equalsIgnoreCase(map.getNamedItem( //$NON-NLS-1$
							"isAggVO").getNodeValue())); //$NON-NLS-1$
				}

				if (map.getNamedItem("dataTypeStyle") != null) { //$NON-NLS-1$
					param.setTypeStyle(map.getNamedItem("dataTypeStyle") //$NON-NLS-1$
							.getNodeValue());
				}
				Type type = Type.parseType(map);
				if (type != null)
					param.setParaType(type);
				// param.setParaType(map.getNamedItem("paraType").getNodeValue());
				param.setDesc(map.getNamedItem("description").getNodeValue()); //$NON-NLS-1$
				if (map.getNamedItem("help") != null) { //$NON-NLS-1$
					param.setHelp(map.getNamedItem("help").getNodeValue()); //$NON-NLS-1$
				}
				if (map.getNamedItem("versionType") != null) { //$NON-NLS-1$
					param.setVersionType(map.getNamedItem("versionType") //$NON-NLS-1$
							.getNodeValue());
				}

			}
		}
		return param;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (getName() == null || getName().trim().length() == 0) {
			sb.append(Messages.Parameter_59);
		}
		if (getDisplayName() == null || getDisplayName().trim().length() == 0) {
			sb.append(Messages.Parameter_60);
		}
		if (getParaType() == null) {
			sb.append(Messages.Parameter_61);
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

	public String getTypeStyle() {
		return typeStyle;
	}

	public void setTypeStyle(String typeStyle) {
		this.typeStyle = typeStyle;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Object o = super.clone();
		Parameter param = (Parameter) o;
		param.setDesc(this.getDesc());
		param.setDisplayName(this.getDisplayName());
		param.setHelp(this.getHelp());
		param.setParamDefClassname(this.getParamDefClassname());
		param.setParamTypeForLog(this.getParamTypeForLog());
		param.setAggVO(this.isAggVO());
		param.setName(this.getName());
		param.setParaType(this.getParaType());
		param.setTypeStyle(this.getTypeStyle());
		param.setVersionType(this.getVersionType());
		return param;
	}

}
