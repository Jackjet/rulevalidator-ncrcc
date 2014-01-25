package com.yonyou.nc.codevalidator.sdk.upm;

/**
 * upm组件对象，对应upm文件的组件
 * 
 * @author mazhqa
 * @since V1.0
 */
public class UpmComponentVO {

	private int priority;
	private boolean remote;
	private boolean singleton;
	private boolean accessProtected;
	private String cluster;
	private String name;
	private String tx;
	private String interfaceName;
	private String implementationName;
	private boolean supportAlias;

	public boolean isAccessProtected() {
		return accessProtected;
	}

	public void setAccessProtected(boolean accessProtected) {
		this.accessProtected = accessProtected;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public String getTx() {
		return tx;
	}

	public void setTx(String tx) {
		this.tx = tx;
	}

	/**
	 * @return
	 */
	public String getInterfaceName() {
		return interfaceName == null ? null : interfaceName.trim();
	}

	/**
	 * 由于有的规则需要判断是否存在空格，因此接口名称没有进行trim处理
	 * 
	 * @return
	 */
	public String getOriginInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * @return
	 */
	public String getImplementationName() {
		return implementationName == null ? null : implementationName.trim();
	}

	/**
	 * 由于有的规则需要判断是否存在空格，因此接口名称没有进行trim处理
	 * 
	 * @return
	 */
	public String getOriginImplementationName() {
		return implementationName;
	}

	public void setImplementationName(String implementationName) {
		this.implementationName = implementationName;
	}

	public boolean isSupportAlias() {
		return supportAlias;
	}

	public void setSupportAlias(boolean supportAlias) {
		this.supportAlias = supportAlias;
	}

}
