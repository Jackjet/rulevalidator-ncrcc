package com.yonyou.nc.codevalidator.resparser.md;

import java.util.List;

/**
 * ÿ��Ԫ�����ļ�(bmf)�������ɸö���
 * <p>
 * @author mazhqa
 * @since V1.0
 */
public interface IMetadataFile {

	/**
	 * �õ�Ԫ�����ļ�����ʵ�壬���Ԫ�����ļ����ǽӿڣ����ܷ���null
	 * @return
	 */
	IEntity getMainEntity();
	
	/**
	 * �õ�Ԫ�����ļ��е�����Ԫ����ʵ��
	 * @return
	 */
	List<IEntity> getAllEntities();
	
	/**
	 * �����ռ�
	 * @return
	 */
	String getNamespace();
	
	/**
	 * ������
	 * @return
	 */
	String getCodeStyle();
	
	/**
	 * ������Դid
	 * @return
	 */
	String getResId();
	
	/**
	 * ������Դģ������
	 * @return
	 */
	String getResModuleName();
	
	/**
	 * ����
	 * @return
	 */
	String getName();
	
	/**
	 * �Ƿ�Ԥ����
	 * @return
	 */
	boolean isPreLoad();
	
	/**
	 * ����ģ��
	 * @return
	 */
	String getOwnModule();
	
	/**
	 * ��ʾ����
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * ��ҵ
	 * @return
	 */
	String getHyName();
	
	/**
	 * �Ƿ���������ģ��
	 * @return
	 */
	boolean isIncrementDevelopMode();
	
	/**
	 * ��չ��ǩ
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * ��Ԫ�����ļ��Ƿ�Ϊ����(��չ��ǩBDMODE�ж�)
	 * @return
	 */
	boolean isBill();
	
	/**
	 * ��Ԫ�����ļ��Ƿ�Ϊ����(��չ��ǩBDMODE�ж�)
	 * @return
	 */
	boolean isDoc();
	
	/**
	 * ��ЩԪ�����ļ���ֻ����һЩ�ӿں�ö�٣���û����ʵ��
	 * @return
	 */
	boolean containsMainEntity();
}
