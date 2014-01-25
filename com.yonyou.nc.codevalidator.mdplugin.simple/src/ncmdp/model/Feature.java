package ncmdp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示特性信息，即表中的内容
 * @author wangxmn
 *
 */
public class Feature {
	private String name = "";
	private String displayName = "";
	private List<String> busiItfId = new ArrayList<String>();
	private List<Attribute> alFeatures = new ArrayList<Attribute>();
	private List<BusinInterface> busiItfAttrs = new ArrayList<BusinInterface>();
	private List<Reference> refers = new ArrayList<Reference>();
	private Map<String,String> busiAndAttrMapping = new HashMap<String, String>();
	
	public Feature() {
		super();
	}
	
	public List<Reference> getRefers() {
		return refers;
	}
	public void addRefer(Reference refer) {
		this.refers.add(refer);
	}
	public void addRefer(List<Reference> refers){
		this.refers.addAll(refers);
	}
	public void addAttribute(Attribute attr){
		if(attr!=null){
			alFeatures.add(attr);
		}
	}	
	public Map<String, String> getBusiAndAttrMapping() {
		return busiAndAttrMapping;
	}
	public void putBusiAndAttrMapping(String attrId, String busiAttrId) {
		this.busiAndAttrMapping.put(attrId, busiAttrId);
	}
	public void putBusiAndAttrMapping(Map<String, String> mapping){
		this.busiAndAttrMapping.putAll(mapping);
	}

	public Attribute[] getAttrbuteCopys(){
		Attribute[] attrs = new Attribute[alFeatures.size()];
		for (int i = 0; i < alFeatures.size(); i++) {
			Attribute attr = alFeatures.get(i);
			if(attr!=null){
				attrs[i] = attr.copy();
			}
		}
		return attrs;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void addAttributes(List<Attribute> attribute){
		alFeatures.addAll(attribute);
	}
	
	public void addBusinInterface(BusinInterface att){
		busiItfAttrs.add(att);
	}
	
	public void addBusinInterface(List<BusinInterface> attrs){
		busiItfAttrs.addAll(attrs);
	}
	
	public List<BusinInterface> getBusinInterface(){
		return busiItfAttrs;
	}
	
	public List<String> getBusiItfIds(){
		return this.busiItfId;
	}
	public void addBusiItfId(String busiId){
		this.busiItfId.add(busiId);
	}
}
