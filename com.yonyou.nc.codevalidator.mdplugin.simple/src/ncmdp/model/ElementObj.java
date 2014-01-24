package ncmdp.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;

import ncmdp.common.MDPConstants;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPUtil;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * ����ʵ�塢ö�١����ߵ����͵ĸ��࣬���ڳ�ʼ��ʱ
 * ���ԡ��ӿڵȵ�Ҳ��������
 * ����һЩ��Ϣ��ʵ����IPropertySource,������������ͼ����ʾ����
 * @author wangxmn
 *
 */
public class ElementObj implements Cloneable, Serializable {
	private static final long serialVersionUID = 4422102235810928927L;
	public static final String PROP_ELEMENT_DISPLAY_NAME = "element_display_name"; 
	public static final String PROP_ELEMENT_NAME = "element_name"; 
	public static final String PROP_ID = "element_ID"; 
	public static final String PROP_DESCRIPTION = "element_description"; 
	public static final String PROP_CREATOR = "element_CREATOR"; 
	public static final String PROP_CREATE_TIME = "element_CREATE_TIME"; 
	public static final String PROP_MODIFIER = "element_MODIFIER"; 
	public static final String PROP_MODIFIER_TIME = "element_MODIFIER_TIME"; 
	public static final String ELEMENT_VALIDATE = "ELEMENT_VALIDATE"; 
	public static final String PROP_EXTEND_TAG = "ELEMENT_EXTEND_TAG"; 
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	private String name = "Element";//ģ�͵�����
	private String displayName = "displayname"; //ģ�͵���ʾ����
	private String id = ""; //ģ��id
	private String desc = null;//ģ������
	private String creator = null;//����
	private Date createTime = null;//����ʱ��
	private String modifier = null;//�޸���
	private Date modifyTime = null;//�޸�ʱ��
	private String versionType = null; //�汾��
	private String extendTag = null;//��չ��ǩ
	private boolean industryChanged = false;//��ҵ�����Ƿ����ı�
	private String createIndustry = MDPConstants.BASE_INDUSTRY;//Ĭ�����û�����ҵ
	private String modifyIndustry = MDPConstants.BASE_INDUSTRY;//�޸���ҵҲĬ�����û�����ҵ
	private boolean isSource = false;//�Ƿ�ΪԴ�ļ�,��Ҫ������������������������Ϊtrue������Ϊfalse

	public boolean isSource() {
		return isSource;
	}

	public void setSource(boolean isSource) {
		this.isSource = isSource;
	}

	/**
	 * �������ɫ���ϵ�����ʱ���ʼ��һ����Ӧ��model��������ʼ��������
	 */
	public ElementObj() {
		super();
		setCreateTime(new Date());//���õ�ǰʱ��
		setId(NCMDPTool.generateID());//����Ψһ�ı�־��
	}

	public String getModifyIndustry() {
		return modifyIndustry;
	}

	public void setModifyIndustry(String modifyIndustry) {
		this.modifyIndustry = modifyIndustry;
	}

	public ElementObj(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChange(PROP_ELEMENT_NAME, oldName, name);

	}

	public String getCreateIndustry() {
		return createIndustry;
	}

	public void setCreateIndustry(String createIndustry) {
		this.createIndustry = createIndustry;
		setModifyIndustry(createIndustry);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		String oldName = this.displayName;
		this.displayName = displayName;
		firePropertyChange(PROP_ELEMENT_DISPLAY_NAME, oldName, displayName);
	}

	public Date getCreateTime() {
		return createTime;
	}

	private void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id != null && id.trim().length() > 0)
			this.id = id;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getExtendTag() {
		return extendTag;
	}

	public void setExtendTag(String extendTag) {
		this.extendTag = extendTag;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	/**
	 * ���Է����仯���¼�����������������
	 * @param prop
	 * @param old
	 * @param newValue
	 */
	protected void firePropertyChange(String prop, Object old, Object newValue) {
		setModifyTime(new Date());//�����޸�ʱ��
		listeners.firePropertyChange(prop, old, newValue);
	}

	/**
	 * ��������model�¼�
	 * @param prop
	 * @param newValue
	 */
	protected void fireStructureChange(String prop, Object newValue) {
		firePropertyChange(prop, null, newValue);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	public Object getEditableValue() {
		return this;
	}

	public Object getPropertyValue(Object id) {
		if (PROP_ELEMENT_NAME.equals(id))
			return name == null ? "" : name; 
		else if (PROP_ELEMENT_DISPLAY_NAME.equals(id)) {
			return getDisplayName() == null ? "" : getDisplayName(); 
		} else if (PROP_ID.equals(id)) {
			return getId() == null ? 0 : getId();
		} else if (PROP_DESCRIPTION.equals(id)) {
			return getDesc() == null ? "" : getDesc(); 
		} else if (PROP_CREATOR.equals(id)) {
			return getCreator() == null ? "" : getCreator(); 
		} else if (PROP_CREATE_TIME.equals(id)) {
			return NCMDPTool.formatDateString(getCreateTime());
		} else if (PROP_MODIFIER.equals(id)) {
			return getModifier() == null ? "" : getModifier(); 
		} else if (PROP_MODIFIER_TIME.equals(id)) {
			return NCMDPTool.formatDateString(getModifyTime());
		} else if (PROP_EXTEND_TAG.equals(id)) {
			return getExtendTag() == null ? "" : getExtendTag(); 
		}
		return ""; 
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {

	}

	public void setPropertyValue(Object id, Object value) {
		if (PROP_ELEMENT_NAME.equals(id)) {
			setName((String) value);
		} else if (PROP_ELEMENT_DISPLAY_NAME.equals(id)) {
			setDisplayName((String) value);
		} else if (PROP_DESCRIPTION.equals(id)) {
			setDesc((String) value);
		} else if (PROP_EXTEND_TAG.equals(id)) {
			setExtendTag((String) value);
		}

	}

	public void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId()); 
		ele.setAttribute("name", getName()); 
		ele.setAttribute("displayName", getDisplayName()); 
		ele.setAttribute("description", getDesc()); 
		ele.setAttribute("creator", getCreator()); 
		ele.setAttribute("createTime", 
				NCMDPTool.formatDateString(getCreateTime()));
		ele.setAttribute("modifier", getModifier()); 
		ele.setAttribute("modifyTime", 
				NCMDPTool.formatDateString(getModifyTime()));
		ele.setAttribute("versionType", getVersionType()); 
		ele.setAttribute("createIndustry", getCreateIndustry()); 
		ele.setAttribute("modifyIndustry", getModifyIndustry()); 
		ele.setAttribute("industryChanged", isIndustryChanged() ? "true"  //$NON-NLS-2$
				: "false"); 
		ele.setAttribute("isSource", isSource() ? "true" : "false");  //$NON-NLS-2$ //$NON-NLS-3$
		ele.setAttribute("help", getExtendTag());// ����cch��Ҫ�󣬶�����չ��ǩ���־û�ʱʹ��help�� 

	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id='").append(this.getId()).append("' ");  //$NON-NLS-2$
		sb.append("name='").append(this.getName()).append("' ");  //$NON-NLS-2$
		sb.append("displayName='").append(this.getDisplayName()).append("' ");  //$NON-NLS-2$
		sb.append("description='").append(this.getDesc()).append("' ");  //$NON-NLS-2$
		sb.append("creator='").append(this.getCreator()).append("' ");  //$NON-NLS-2$
		sb.append("createTime='") 
				.append(NCMDPTool.formatDateString(this.getCreateTime()))
				.append("' "); 
		sb.append("modifier='").append(this.getModifier()).append("' ");  //$NON-NLS-2$
		sb.append("modifyTime='") 
				.append(NCMDPTool.formatDateString(this.getModifyTime()))
				.append("' "); 
		sb.append("versionType='").append(this.getVersionType()).append("' ");  //$NON-NLS-2$
		sb.append("createIndustry='").append(this.getCreateIndustry()) 
				.append("' "); 
		sb.append("nodifyIndustry='").append(this.getModifyIndustry()) 
				.append("' "); 
		sb.append("industryChanged='") 
				.append(this.isIndustryChanged() ? "true" : "false")  //$NON-NLS-2$
				.append("' "); 
		sb.append("isSource='").append(this.isSource() ? "true" : "false")  //$NON-NLS-2$ //$NON-NLS-3$
				.append("' "); 
		return sb.toString();
	}

	public static void parseNodeAttr(Node node, ElementObj ele) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			ele.setId(map.getNamedItem("id").getNodeValue()); 
			ele.setName(map.getNamedItem("name").getNodeValue()); 
			ele.setDisplayName(map.getNamedItem("displayName").getNodeValue()); 
			ele.setDesc(map.getNamedItem("description").getNodeValue()); 
			ele.setCreator(map.getNamedItem("creator").getNodeValue()); 
			ele.setCreateTime(NCMDPTool.parseStringToDate(map.getNamedItem(
					"createTime").getNodeValue())); 
			ele.setModifier(map.getNamedItem("modifier").getNodeValue()); 
			ele.setModifyTime(NCMDPTool.parseStringToDate(map.getNamedItem(
					"modifyTime").getNodeValue())); 
			if (map.getNamedItem("versionType") != null) { 
				ele.setVersionType(map.getNamedItem("versionType") 
						.getNodeValue());
			}
			if (map.getNamedItem("createIndustry") != null) { 
				ele.setCreateIndustry(map.getNamedItem("createIndustry") 
						.getNodeValue());
			}
			if (map.getNamedItem("mofifyIndustry") != null) { 
				ele.setModifyIndustry(map.getNamedItem("modifyIndustry") 
						.getNodeValue());
			}
			if (map.getNamedItem("industryChanged") != null) { 
				ele.setIndustryChanged("true".equalsIgnoreCase(map 
						.getNamedItem("industryChanged").getNodeValue())); 
			}
			if (map.getNamedItem("isSource") != null) { 
				ele.setSource("true".equalsIgnoreCase(map.getNamedItem(
						"isSource").getNodeValue())); 
			}
			if (map.getNamedItem("help") != null) { 
				ele.setExtendTag(map.getNamedItem("help").getNodeValue()); 
			}
		}
	}

	protected boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (isNull(getName()))
			sb.append(Messages.ElementObj_101);
		if (isNull(getDisplayName()))
			sb.append(Messages.ElementObj_102);
		if (sb.length() > 0)
			return sb.toString();
		else
			return null;
	}

	public String getVersionType() {
		if(versionType==null){
			versionType = MDPUtil.getDevVersionType();
		}
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public boolean isIndustryChanged() {
		return industryChanged;
	}

	public void setIndustryChanged(boolean industryChanged) {
		this.industryChanged = industryChanged;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Object o = super.clone();
		return o;
	}

}
