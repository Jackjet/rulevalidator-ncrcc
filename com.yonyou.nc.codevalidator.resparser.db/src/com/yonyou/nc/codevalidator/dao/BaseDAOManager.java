package com.yonyou.nc.codevalidator.dao;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * BaseDAO�Ĺ����������̺߳�����Դ����ķ�ʽ����basedao
 * @author mazhqa
 * @since V2.3
 */
public class BaseDAOManager {
	
	private static BaseDAOManager INSTANCE;
	private final ThreadLocal<Map<String, BaseDAO>> dataSourceToDaoMapTl;
	
	private BaseDAOManager(){
		dataSourceToDaoMapTl = new ThreadLocal<Map<String, BaseDAO>>() {
			protected java.util.Map<String, BaseDAO> initialValue() {
				return new WeakHashMap<String, BaseDAO>();
			}
		};
	}
	
	public static BaseDAOManager getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new BaseDAOManager();
		}
		return INSTANCE;
	}
	
	/**
	 * �ӵ�ǰ�߳����õ���������Դ��baseDAO
	 * @param dataSource
	 * @return
	 */
	public BaseDAO getBaseDAO(String dataSource){
		Map<String, BaseDAO> dataSourceToDaoMap = dataSourceToDaoMapTl.get();
		if(dataSourceToDaoMap == null){
			dataSourceToDaoMap = new WeakHashMap<String, BaseDAO>();
			dataSourceToDaoMapTl.set(dataSourceToDaoMap);
		}
		BaseDAO baseDao = dataSourceToDaoMap.get(dataSource);
		if(baseDao == null){
			baseDao = new BaseDAO(dataSource);
			dataSourceToDaoMap.put(dataSource, baseDao);
		}
		return baseDao;
	}

}
