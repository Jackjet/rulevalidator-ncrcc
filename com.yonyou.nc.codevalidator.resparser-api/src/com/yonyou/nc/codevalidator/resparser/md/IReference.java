package com.yonyou.nc.codevalidator.resparser.md;

/**
 * Ԫ����-ʵ��-����
 * @author mazhqa
 * @since V2.3
 */
public interface IReference {
	
	/**
	 * ȱʡ
	 * @return
	 */
	boolean isDefault();
	
	/**
	 * ����
	 * @return
	 */
	String getName();
	
	/**
	 * ����ʵ��
	 * @return
	 */
	IEntity getEntity();

}
