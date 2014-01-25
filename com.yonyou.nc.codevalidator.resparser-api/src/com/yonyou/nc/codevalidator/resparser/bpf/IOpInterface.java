package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * ҵ�����-�ӿ�
 * @author mazhqa
 * @since V2.1
 */
public interface IOpInterface {
	
	String getID();
	
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
	 * �汾
	 * @return
	 */
	String getVersion();
	
	/**
	 * ������Դid
	 * @return
	 */
	String getResId();
	
	/**
	 * ��Ӧ�Ľӿ�����fullClassName
	 * @return
	 */
	String getInterfaceName();

	/**
	 * ��Ӧ��ʵ������implClsName
	 * @return
	 */
	String getImplementationClassName();
	
	/**
	 * ����ʵ��
	 * @return
	 */
	IType getOwnType();
	
	/**
	 * ��չ��ǩ
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * �Ƿ���
	 * @return
	 */
	boolean isSingleton();
	
	/**
	 * �Ƿ�ҵ��
	 * @return
	 */
	boolean isBusiActivity();
	
	/**
	 * �Ƿ�ҵ�����
	 * @return
	 */
	boolean isBusiOperation();
	
	/**
	 * �Ƿ���Ȩ
	 * @return
	 */
	boolean isAuthorization();
	
	/**
	 * �Ƿ�Զ��
	 * @return
	 */
	boolean isRemote();
	
	/**
	 * ҵ�������������
	 * @return
	 */
	List<IOperation> getOperations();
	
}
