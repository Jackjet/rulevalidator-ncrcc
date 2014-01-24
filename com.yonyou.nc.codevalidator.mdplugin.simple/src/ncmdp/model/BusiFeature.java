package ncmdp.model;

import java.util.List;

public class BusiFeature {

	private String name;
	private List<Attribute> startAttrs;
	private List<Attribute> endAttr;
	private List<BusiItfAttr> attrs;
	
	public BusiFeature(){
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Attribute> getStartAttrs() {
		return startAttrs;
	}

	public void setStartAttrs(List<Attribute> startAttrs) {
		this.startAttrs = startAttrs;
	}

	public List<Attribute> getEndAttr() {
		return endAttr;
	}

	public void setEndAttr(List<Attribute> endAttr) {
		this.endAttr = endAttr;
	}

	public List<BusiItfAttr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<BusiItfAttr> attrs) {
		this.attrs = attrs;
	}
	
	
	
}
