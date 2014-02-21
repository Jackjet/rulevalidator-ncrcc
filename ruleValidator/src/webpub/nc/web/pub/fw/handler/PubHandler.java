package nc.web.pub.fw.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PubHandler implements Cloneable, Comparable<PubHandler> {
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	private String handler;
	private String url;
	private List<PubMapping> methods;

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public List<PubMapping> getMethods() {
		return methods;
	}

	public void setMethods(List<PubMapping> methods) {
		this.methods = methods;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PubHandler(String handler, String url, List<PubMapping> methods) {
		super();
		this.handler = handler;
		this.url = url;
		this.methods = methods;
	}

	public int getIndex(String url) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int i = 0;
		map.put("ecppub", i++);
		map.put("product", i++);
		map.put("order", i++);
		map.put("account", i++);
		map.put("channel", i++);
		map.put("orderplan", i++);
		map.put("bulletin", i++);
		map.put("esfw", i++);
		if (url.contains("/")) {
			url = url.split("/")[0];
		}
		Integer flag = map.get(url);
		if (flag == null) {
			flag = 0;
		}
		return flag;
	}

	@Override
	public int compareTo(PubHandler o) {
		if (this.getIndex(this.url) == o.getIndex(o.getUrl())) {
			return this.getUrl().compareTo(o.getUrl());
		} else {
			return this.getIndex(this.url) - o.getIndex(o.getUrl());
		}
	}

}
