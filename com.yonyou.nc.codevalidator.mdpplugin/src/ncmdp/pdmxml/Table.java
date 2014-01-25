package ncmdp.pdmxml;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * ��Ӧ���ݿ�ı�
 * @author wangxmn
 *
 */
public class Table {
	private String name = "";
	private String displayName = "";
	private ArrayList<Field> fields =null;
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
	public ArrayList<Field> getFields() {
		return fields;
	}
	public void addField(Field[] thefields){
		if(fields == null)
			fields = new ArrayList<Field>();
		if(thefields != null)
			fields.addAll(Arrays.asList(thefields));
	}
	public void addField(Field field){
		if(fields == null)
			fields = new ArrayList<Field>();
		fields.add(field);
	}
	public String toString(){
		return getDisplayName();
	}
}
