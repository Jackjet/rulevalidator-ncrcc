package ncmdp.model;

import java.util.ArrayList;

public class StereoType implements Cloneable {
	private String name = "";
	private String displayName = "";
	private ArrayList<Accessor> listAccessors = new ArrayList<Accessor>();
	
	public ArrayList<Accessor> getListAccessors() {
		return listAccessors;
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

	public StereoType() {
		super();
	}
	public Object clone(){
		StereoType st = new StereoType();
		st.setDisplayName(this.getDisplayName());
		st.setName(this.getName());
		st.setListAccessors((ArrayList<Accessor>)this.getListAccessors().clone());
		return st;

	}

	private void setListAccessors(ArrayList<Accessor> listAccessors) {
		this.listAccessors = listAccessors;
	}
	public String toString(){
		return getDisplayName();
	}
}
