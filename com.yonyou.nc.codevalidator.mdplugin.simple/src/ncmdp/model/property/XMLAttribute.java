package ncmdp.model.property;

public class XMLAttribute {
	private String key = "";
	private String value = "";
	public XMLAttribute(String key) {
		super();
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String genXMLStr(){
		return key+"='"+value+"' ";
	}
	public String getKey() {
		return key;
	}
}
