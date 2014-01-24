package com.yonyou.nc.codevalidator.resparser.md;

/**
 * Ԫ����-����
 * @author mazhqa
 *
 */
public interface IAttribute {
	
	/**
	 * ���ʲ��ԣ���MDRuleConstants���У��ֳ����֣�<p>
	 * ACCESS_STRATEGY_POJO�� ACCESS_STRATEGY_NCBEAN�� ACCESS_STRATEGY_BODYOFAGGVO
	 * @return
	 */
	String getAccessStrategy();
	
	/**
	 * ������ʽ����MDRuleConstants���У��ֳ����֣�<p>
	 * TYPE_STYLE_ARRAY, TYPE_STYLE_SINGLE, TYPE_STYLE_LIST, TYPE_STYLE_REF
	 * @return
	 */
	String getTypeStyle();
	
	/**
	 * ��������
	 * @return
	 */
	String getName();
	
	/**
	 * �ֶ�����
	 * @return
	 */
	String getFieldName();
	
	/**
	 * �ֶ�����
	 * @return
	 */
	String getFieldType();
	
	/**
	 * �ֶγ��ȣ���Щ�ֶ����Ϳ���Ϊ�գ�
	 * @return
	 */
	String getLength();
	
	/**
	 * �ֶ���ʾ����
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * �õ����Ե�����
	 * @return
	 */
	IType getType();
	
	/**
	 * ʹ��Ȩ
	 * @return
	 */
	boolean isAccessPower();
	
	/**
	 * ʹ��Ȩ��
	 * @return
	 */
	String getAccessPowerGroup();

	
	/**
	 * ��ȡ���Բ������ƣ���Ϊ��
	 */
	String getRefModuleName();
	
	/**
	 * �Ƿ�̬����
	 * @return
	 */
	boolean isDynamic();
}
