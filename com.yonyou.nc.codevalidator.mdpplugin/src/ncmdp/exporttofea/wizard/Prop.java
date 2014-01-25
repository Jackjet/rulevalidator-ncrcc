package ncmdp.exporttofea.wizard;


public class Prop {

	private Object content;
	private String name;
	private String id;
	
	public Prop(String name,String id,Object content){
		super();
		this.content = content;
		this.name = name;
		this.id = id;
	}

	public Object getConent(){
		return this.content;
	}
	
	public String getId(){
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
