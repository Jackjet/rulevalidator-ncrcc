package com.yonyou.nc.codevalidator.resparser.md;

import java.util.List;

public interface IBusiInterface {
	
	/**
	 * ҵ��ӿ����������磺nc.vo.bd.meta.IBDObject
	 * @return
	 */
	String getFullClassName();
	
	/**
	 * ҵ��ӿ��е������б�
	 * @return
	 */
	List<IBusiInterfaceAttribute> getBusiInterfaceAttributes();
	
}
