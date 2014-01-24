package com.yonyou.nc.codevalidator.sdk.aop;

/**
 * 
 * @author mazhqa
 * @since V2.1
 */
public class AopAspectVO {

	/**
	 * aops-aspect-class
	 */
	private String implemenationClass;
	
	/**
	 * aops-aspect-component
	 */
	private String componentInterface;

	public String getImplemenationClass() {
		return implemenationClass;
	}

	public void setImplemenationClass(String implemenationClass) {
		this.implemenationClass = implemenationClass;
	}

	public String getComponentInterface() {
		return componentInterface;
	}

	public void setComponentInterface(String componentInterface) {
		this.componentInterface = componentInterface;
	}

}
