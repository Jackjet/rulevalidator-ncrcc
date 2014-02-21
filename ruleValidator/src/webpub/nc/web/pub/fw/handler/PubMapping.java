package nc.web.pub.fw.handler;

import org.springframework.web.bind.annotation.RequestMethod;

public class PubMapping implements Cloneable,  Comparable<PubMapping> {
	private String[] url;
	private RequestMethod[] method;
	private Class<?> returnType;
	private Class<?>[] params;
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public RequestMethod[] getMethod() {
		return method;
	}

	public void setMethod(RequestMethod[] method) {
		this.method = method;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public Class<?>[] getParams() {
		return params;
	}

	public void setParams(Class<?>[] params) {
		this.params = params;
	}

	public PubMapping(String[] strings, RequestMethod[] requestMethods, Class<?> class1,
			Class<?>[] classes) {
		super();
		this.url = strings;
		this.method = requestMethods;
		this.returnType = class1;
		this.params = classes;
	}

	@Override
	public int compareTo(PubMapping o) {
		int flag = 0;
		if(o.getUrl().length>0 && this.getUrl().length>0){
			flag = this.getUrl()[0].compareTo(o.getUrl()[0]);
		}
		return flag;
	}
	
}
