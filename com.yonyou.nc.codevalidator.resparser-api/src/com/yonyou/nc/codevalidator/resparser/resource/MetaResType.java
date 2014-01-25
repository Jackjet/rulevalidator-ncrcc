package com.yonyou.nc.codevalidator.resparser.resource;

/**
 * 元数据资源类型
 * 
 * @author mazhqa
 * @since V2.1
 */
public enum MetaResType {
	BMF("bmf"), BPF("bpf"), ALL(new String[]{"bmf", "bpf"});

	private String[] suffix;

	private MetaResType(String suffix) {
		this.suffix = new String[] { suffix };
	}

	private MetaResType(String[] suffix) {
		this.suffix = suffix;
	}

	/**
	 * 元数据资源类型的文件名后缀
	 * @return
	 */
	public String[] getMetaFileSuffix() {
		return suffix;
	}

}
