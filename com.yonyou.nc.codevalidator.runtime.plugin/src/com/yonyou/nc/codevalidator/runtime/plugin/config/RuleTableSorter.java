package com.yonyou.nc.codevalidator.runtime.plugin.config;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public final class RuleTableSorter extends ViewerSorter {

	private static final int ID = 1;
	private static final int CATALOG = 2;
	private static final int SUBCATALOG = 3;

	public static final RuleTableSorter ID_ASC = new RuleTableSorter(ID);
	public static final RuleTableSorter ID_DESC = new RuleTableSorter(-ID);
	public static final RuleTableSorter CATALOG_ASC = new RuleTableSorter(CATALOG);
	public static final RuleTableSorter CATALOG_DESC = new RuleTableSorter(-CATALOG);
	public static final RuleTableSorter SUBCATALOG_ASC = new RuleTableSorter(SUBCATALOG);
	public static final RuleTableSorter SUBCATALOG_DESC = new RuleTableSorter(-SUBCATALOG);

	private int sortType;

	private RuleTableSorter(int sortType) {
		this.sortType = sortType;
	}

	public int compare(Viewer viewer, Object obj1, Object obj2) {
		RuleDefinitionAnnotationVO o1 = (RuleDefinitionAnnotationVO) obj1;
		RuleDefinitionAnnotationVO o2 = (RuleDefinitionAnnotationVO) obj2;
		switch (sortType) {
		case ID: {
			String classname1 = o1.getRuleDefinitionIdentifier();
			String classname2 = o2.getRuleDefinitionIdentifier();

			String s1 = classname1.substring(classname1.lastIndexOf(".") + 1, classname1.length());
			String s2 = classname2.substring(classname2.lastIndexOf(".") + 1, classname2.length());
			// Long的compareTo方法返回值有三个可能值1,0,-1：
			// 如l1＞l2则返回1；如l1＝l2则返回0；如l1＜l2则返回-1
			return s1.compareTo(s2);
		}
		case -ID: {
			String classname1 = o1.getRuleDefinitionIdentifier();
			String classname2 = o2.getRuleDefinitionIdentifier();

			String s1 = classname1.substring(classname1.lastIndexOf(".") + 1, classname1.length());
			String s2 = classname2.substring(classname2.lastIndexOf(".") + 1, classname2.length());

			return s2.compareTo(s1);
		}
		case CATALOG: {
			String s1 = o1.getCatalog().getName();
			String s2 = o2.getCatalog().getName();
			return s1.compareTo(s2);
		}
		case -CATALOG: {
			String s1 = o1.getCatalog().getName();
			String s2 = o2.getCatalog().getName();
			return s2.compareTo(s1);
		}
		case SUBCATALOG: {
			String s1 = o1.getSubCatalog().getName();
			String s2 = o2.getSubCatalog().getName();
			return s1.compareTo(s2);
		}
		case -SUBCATALOG: {
			String s1 = o1.getSubCatalog().getName();
			String s2 = o2.getSubCatalog().getName();
			return s2.compareTo(s1);
		}
		}
		return 0;
	}

}
