package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

/**
 * ����ҵ������ļ���ʹ�øýӿڽ��д���
 * @author mazhqa
 * @since V2.0
 */
public interface IMetaProcessFile {
	
	String getID();
	
	/**
	 * ҵ���������
	 * @return
	 */
	List<IBusiOperator> getBusiOperators();
	
	/**
	 * ҵ������
	 * @return
	 */
	List<IBusiActivity> getBusiActivities();
	
	/**
	 * ҵ��ӿڼ���
	 * @return
	 */
	List<IOpInterface> getOpInterfaces();
	
	/**
	 * ����
	 * @return
	 */
	String getName();
	
	/**
	 * ��ʾ����
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * ����ģ��
	 * @return
	 */
	String getOwnModule();
	
	/**
	 * ���ƿռ�
	 * @return
	 */
	String getNameSpace();
	
	/**
	 * ������Դģ����
	 * @return
	 */
	String getResModuleName();

	/**
	 * ������Դid
	 * @return
	 */
	String getResId();
	
	/**
	 * ��ҵ
	 * @return
	 */
	String getIndustry();
	
	/**
	 * ��չ��ǩ
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * �Ƿ�Ԥ����
	 * @return
	 */
	boolean isPreLoad();
	
	/**
	 * �Ƿ���������
	 * @return
	 */
	boolean isIndustryIncrease();
}
