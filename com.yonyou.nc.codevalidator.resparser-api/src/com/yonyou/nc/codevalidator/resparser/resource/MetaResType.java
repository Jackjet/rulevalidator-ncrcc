package com.yonyou.nc.codevalidator.resparser.resource;

/**
 * Ԫ������Դ����
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
	 * Ԫ������Դ���͵��ļ�����׺
	 * @return
	 */
	public String[] getMetaFileSuffix() {
		return suffix;
	}

}
