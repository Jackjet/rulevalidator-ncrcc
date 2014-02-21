package com.web.pub.src.dao.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PubDaoUtils extends HibernateDaoSupport {

	public static String getUpdateSQL(String clsName, String[] filed,
			String filedValue, String conditrion) {
		StringBuffer sb = new StringBuffer(" udpate ");
		sb.append(clsName);
		sb.append(" set ");
		if (filed != null && filed.length > 0) {
			for (int i = 0; i < filed.length; i++) {
				sb.append(filed[i]);
				sb.append("=?,");
			}
		}
		sb.append(conditrion);
		return sb.toString();
	}
}
