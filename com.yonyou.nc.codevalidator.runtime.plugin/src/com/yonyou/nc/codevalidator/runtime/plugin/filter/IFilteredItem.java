package com.yonyou.nc.codevalidator.runtime.plugin.filter;

public interface IFilteredItem extends Comparable<IFilteredItem> {

	String getIdentifier();

	String getItemText();

	String getItemDetailText();

	String getImagePath();

	String getDetailImagePath();
}
