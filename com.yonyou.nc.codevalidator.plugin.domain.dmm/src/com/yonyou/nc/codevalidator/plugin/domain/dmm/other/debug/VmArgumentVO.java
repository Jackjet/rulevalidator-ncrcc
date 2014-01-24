package com.yonyou.nc.codevalidator.plugin.domain.dmm.other.debug;

public class VmArgumentVO {
	private Double xms=new Double(0);
	private Double xmx=new Double(0);
	private Double maxPermSize=new Double(0);
	private Double permSize=new Double(0);
	public Double getXms() {
		return xms;
	}
	public void setXms(Double xms) {
		this.xms = xms;
	}
	public Double getXmx() {
		return xmx;
	}
	public void setXmx(Double xmx) {
		this.xmx = xmx;
	}
	public Double getMaxPermSize() {
		return maxPermSize;
	}
	public void setMaxPermSize(Double maxPermSize) {
		this.maxPermSize = maxPermSize;
	}
	public Double getPermSize() {
		return permSize;
	}
	public void setPermSize(Double permSize) {
		this.permSize = permSize;
	}
}
