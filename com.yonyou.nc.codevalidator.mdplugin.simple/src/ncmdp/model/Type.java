package ncmdp.model;

import java.io.Serializable;

import ncmdp.tool.NCMDPTool;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * 保存类型信息
 * 
 * @author wangxmn
 * 
 */
public class Type implements Cloneable, Serializable {

	private static final long serialVersionUID = -5485496899088138724L;

	private Cell cell = null;// 实体

	private String typeId = null;

	private String name = null;

	private String DisplayName = null;

	private String length = "";

	private String precise = "";

	private String dbType = "";// 对应数据库中的类型

	public static class SpecialType extends Type {
		private static final long serialVersionUID = 6564599401797663343L;
		private String specialTypeID = "";
		public static final String SelfType = "me";

		public SpecialType() {
			super();

		}

		public static Type getSelfType(Cell cell) {
			Type type = cell.converToType();
			return type;
		}

		public String getSpecialTypeID() {
			return specialTypeID;
		}

		public void setSpecialTypeID(String specialTypeID) {
			this.specialTypeID = specialTypeID;
		}
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public void setElementAttribute(Element ele) {
		ele.setAttribute("dataType", getTypeId());
		ele.setAttribute("typeName", getName());
		ele.setAttribute("typeDisplayName", getDisplayName());
		ele.setAttribute("dbtype", getDbType());

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("dataType='").append(getTypeId()).append("' ");
		sb.append("typeName='").append(getName()).append("' ");
		sb.append("typeDisplayName='").append(getDisplayName()).append("' ");
		sb.append("dbtype='").append(getDbType()).append("' ");
		return sb.toString();
	}

	public static Type parseType(NamedNodeMap map) {
		Type type = null;
		if (map.getNamedItem("dataType") != null) {
			String typeId = map.getNamedItem("dataType").getNodeValue();
			if (typeId != null && typeId.trim().length() > 0) {
				type = NCMDPTool.getBaseTypeById(typeId);
				if (type == null) {
					type = new Type();
					type.setTypeId(typeId);
					type.setName(map.getNamedItem("typeName").getNodeValue());
					type.setDisplayName(map.getNamedItem("typeDisplayName").getNodeValue());
					if (map.getNamedItem("dbtype") != null) {
						type.setDbType(map.getNamedItem("dbtype").getNodeValue());
					}
				}
			}
		}
		return type;
	}

	public String toString() {
		return getDisplayName();
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getPrecise() {
		return precise;
	}

	public void setPrecise(String precise) {
		this.precise = precise;
	}

	public Cell getCell() {
		if (cell == null) {
			// MDPExplorerTreeView view =
			// MDPExplorerTreeView.getMDPExploerTreeView(null);
			// if(view != null)
			// cell =view.findCellbyId(getTypeId());
			// cell = ProjectUtil.findCellbyId(getTypeId());
		}
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

}
