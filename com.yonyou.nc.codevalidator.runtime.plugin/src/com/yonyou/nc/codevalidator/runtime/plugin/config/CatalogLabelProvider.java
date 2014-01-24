package com.yonyou.nc.codevalidator.runtime.plugin.config;

import org.eclipse.jface.viewers.LabelProvider;

import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

public class CatalogLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof SubCatalogEnum) {
			return ((SubCatalogEnum) element).getName();
		} else if (element instanceof CatalogEnum) {
			return ((CatalogEnum) element).getName();
		} else {
			return element.toString();
		}
	}

}
