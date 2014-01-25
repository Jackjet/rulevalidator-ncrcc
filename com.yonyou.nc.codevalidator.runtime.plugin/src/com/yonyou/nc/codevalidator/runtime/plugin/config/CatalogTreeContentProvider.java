package com.yonyou.nc.codevalidator.runtime.plugin.config;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

public class CatalogTreeContentProvider implements ITreeContentProvider {

	public static final String ROOT_STR = "È«²¿";
	public static final String ROOT_ID = "_root_";

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		return new String[] { ROOT_STR };
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof String) {
			return new CatalogEnum[] { CatalogEnum.JAVACODE, CatalogEnum.CREATESCRIPT, CatalogEnum.LANG,
					CatalogEnum.METADATA, CatalogEnum.OTHERCONFIGFILE, CatalogEnum.PRESCRIPT,
					CatalogEnum.THIRDPARTYJAR, CatalogEnum.UIFACTORY, CatalogEnum.WEBDEVELOP };
		} else if (parentElement instanceof CatalogEnum) {
			return SubCatalogEnum.getSubCatalogByCatalog((CatalogEnum) parentElement);
		} else {
			return null;
		}
	}

	public Object getParent(Object element) {
		if (element instanceof SubCatalogEnum) {
			return ((SubCatalogEnum) element).getCatalogEnum();
		} else if (element instanceof CatalogEnum) {
			return ROOT_ID;
		} else {
			return null;
		}
	}

	public boolean hasChildren(Object element) {
		Object[] obj = getChildren(element);
		return obj == null ? false : obj.length > 0;
	}

}
