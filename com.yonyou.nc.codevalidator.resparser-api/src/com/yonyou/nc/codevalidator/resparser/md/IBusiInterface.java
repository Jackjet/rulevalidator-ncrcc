package com.yonyou.nc.codevalidator.resparser.md;

import java.util.List;

public interface IBusiInterface {
	
	/**
	 * 业务接口类名，比如：nc.vo.bd.meta.IBDObject
	 * @return
	 */
	String getFullClassName();
	
	/**
	 * 业务接口中的属性列表
	 * @return
	 */
	List<IBusiInterfaceAttribute> getBusiInterfaceAttributes();
	
}
