package ncmdp.model;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

import ncmdp.cache.MDPCachePool;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.serialize.XMLSerialize;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.NotEditableTextPropertyDescriptor;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * 引用类型
 * @author wangxmn
 *
 */
public class Reference extends Cell {
	private static final long serialVersionUID = -4778185111983694424L;

	public static final String PROP_MODULE_NAME = "module_name"; 

	public static final String PROP_MD_FILEPATH = "md_filepath"; 

	public static final String PROP_REF_ID = "ref_id"; 

	private String moduleName = null;

	private String mdFilePath = null;

	private String refId = null;

	private Cell referencedCell = null;

	public Reference() {
		super("reference"); 
		setSize(new Dimension(100, 100));
	}

	public Type converToType() {
		if (relationType == null) {
			Cell refCell = getReferencedCell();
			if (refCell != null) {
				relationType = new Type();
				relationType.setDisplayName(refCell.getDisplayName());
				relationType.setTypeId(refCell.getId());
				relationType.setName(refCell.getName());
			}
		}
		return relationType;
	}

	public String getMdFilePath() {
		return mdFilePath;
	}

	public void setMdFilePath(String mdFilePath) {
		this.mdFilePath = mdFilePath;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Cell getReferencedCell2() {
		if (referencedCell == null && moduleName != null && mdFilePath != null
				&& refId != null) {
			File file = getReferencedCellSourceFile(getGraph());
			if (file != null) {
				JGraph graph = JGraphSerializeTool.loadFromFile(file);
				referencedCell = graph.getCellByID(refId);
			}
		}
		return referencedCell;
	}

	public Cell getReferencedCell() {
		if (referencedCell == null && moduleName != null && mdFilePath != null
				&& refId != null) {
			File file = getReferencedCellSourceFile();
			if (file != null) {
				JGraph graph = JGraphSerializeTool.loadFromFile(file);
				referencedCell = graph.getCellByID(refId);
				if (referencedCell == null) {
					referencedCell = getReferencedCell(graph);
				}
			}
		}
		return referencedCell;
	}

	public Cell getReferencedCell(JGraph graph) {
		Cell referencedCelltemp = null;
		HashMap<String,String> map = graph.getMap();
		String path = (String) map.get(refId);
		File file = NCMDPTool.getMDPFileByModuleAndPath(this.moduleName, path);
		if (file != null) {
			JGraph graphtemp = JGraphSerializeTool.loadFromFile(file);
			referencedCelltemp = graphtemp.getCellByID(refId);
			if (referencedCelltemp == null) {
				referencedCelltemp = getReferencedCell(graphtemp);
			}
			setMdFilePath(path);
		}
		return referencedCelltemp;
	}

	public File getReferencedCellSourceFile(JGraph graph) {
		File file = NCMDPTool.getMDPFileByModuleAndPath(this.moduleName,
				this.mdFilePath);
		if (file != null) {
			HashMap<String,String> map = graph.getMap();
			String path = (String) map.get(refId);
			file = NCMDPTool.getMDPFileByModuleAndPath(this.moduleName, path);
			if (file == null) {
				JGraph graphtemp = JGraphSerializeTool.loadFromFile(file);
				Cell referencedCelltemp = graphtemp.getCellByID(refId);
				if (referencedCelltemp == null) {
					getReferencedCellSourceFile(graphtemp);
				}
			}

		}
		return file;
	}

	public File getReferencedCellSourceFile() {
		// 修改为根据refId查找 dingxm@2009-09-03
		String filePath = MDPCachePool.getInstance()
				.getFilePathByIDAndIndustry(this.getRefId(), null);
		return StringUtil.isEmptyWithTrim(filePath) ? null : new File(filePath);
	}

	public void setReferencedCell(Cell referencedCell) {
		this.referencedCell = referencedCell;
	}

	public void setElementAttribute(Element ele) {
		super.setElementAttribute(ele);
		ele.setAttribute("moduleName", getModuleName()); 
		ele.setAttribute("mdFilePath", getMdFilePath()); 
		ele.setAttribute("refId", getRefId()); 
	}

	public String genXMLAttrString() {
		StringBuilder sb = new StringBuilder(super.genXMLAttrString());
		sb.append("moduleName='").append(this.getModuleName()).append("' ");  
		sb.append("mdFilePath='").append(this.getMdFilePath()).append("' ");  
		sb.append("refId='").append(this.getRefId()).append("' ");  
		return sb.toString();

	}

	protected static void parseNodeAttr(Node node, Reference ref) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			Cell.parseNodeAttr(node, ref);
			ref.setModuleName(map.getNamedItem("moduleName").getNodeValue()); 
			ref.setMdFilePath(map.getNamedItem("mdFilePath").getNodeValue()); 
			ref.setRefId(map.getNamedItem("refId").getNodeValue()); 
		}
	}

	public static Reference parseNode(Node node) {
		Reference ref = null;
		String name = node.getNodeName();
		if ("Reference".equalsIgnoreCase(name)) { 
			ref = new Reference();
			Reference.parseNodeAttr(node, ref);
			XMLSerialize.getInstance().register(ref);
		}
		return ref;
	}

	public Element createElement(Document doc, JGraph graph) {
		Element ele = doc.createElement("Reference"); 
		ele.setAttribute("componentID", graph.getId()); 
		setElementAttribute(ele);
		return ele;
	}

	public void printXMLString(PrintWriter pw, String tabStr, JGraph graph) {
		pw.print(tabStr + "<Reference "); 
		pw.print(" componentID='" + graph.getId() + "' ");  
		pw.print(genXMLAttrString());
		pw.println(">"); 
		pw.println(tabStr + "</Reference>"); 
	}

	public String validate() {
		return null;
	}

	/**
	 * 引用类型亦提供属性视图查看的功能
	 */
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor[] pds = new PropertyDescriptor[3];
		pds[0] = new NotEditableTextPropertyDescriptor(PROP_MODULE_NAME, Messages.Reference_24);
		pds[1] = new NotEditableTextPropertyDescriptor(PROP_MD_FILEPATH,
				Messages.Reference_25);
		pds[2] = new NotEditableTextPropertyDescriptor(PROP_REF_ID, Messages.Reference_26);
		return pds;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROP_MODULE_NAME.equals(id)) {
			return this.getModuleName();
		} else if (PROP_MD_FILEPATH.equals(id)) {
			return this.getMdFilePath();
		} else if (PROP_REF_ID.equals(id)) {
			return this.getRefId();
		} else
			return super.getPropertyValue(id);
	}

	public void copy(Reference ref) {

	}

	public boolean showInExplorerTree() {
		return false;
	}
}
