package com.yonyou.nc.codevalidator.resparser.md;

import java.util.List;
import java.util.Map;

/**
 * Ԫ����-ʵ��
 * @author mazhqa
 *
 */
public interface IEntity {
	
	/**
	 * �õ�ʵ��ID 
	 * @return
	 */
	String getId();
	
	/**
	 * �õ�ʵ����ʾ����
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * ʵ���������
	 * @return
	 */
	String getFullClassName();
	
	/**
	 * �����ռ�+"."+ʵ������
	 * @return
	 */
	String getFullName();
	
	/**
	 * ʵ�����չ��ǩ
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * �õ�ʵ���е���������
	 * @return
	 */
	List<IAttribute> getAttributes();
	
	/**
	 * �õ�ʵ����ҵ��ӿ�
	 * @return
	 */
	List<IBusiInterface> getBusiInterfaces();

//	/**
//	 * �õ�ҵ���������ӳ���Ӧ��attributes
//	 * @return
//	 */
//	Map<String, IAttribute[]> getBusiattrAttrExtendMap();
	
	/**
	 * ����ҵ��ӿ����õ�����ֵ�б�
	 * @param busiInterfaceName
	 * @return �������ƶ�Ӧ����ֵ��ӳ��
	 */
	Map<String, IAttribute> getBusiInterfaceAttributes(String busiInterfaceName);
	
	/**
	 * ��ȡʵ���ȱʡ����
	 */
	String getTableName();
	
	/**
	 * ��ȡʵ��������
	 */
	IAttribute getKeyAttribute();
	
	/**
	 * ��ȡ�Ա�ʵ��ΪԴ�Ĺ����ͣ�IRelation����ϵ��Connection��
	 */
	List<IRelation> getSourceRelations();
	
	/**
	 * ��ȡ�Ա�ʵ��ΪĿ��Ĺ����ͣ�IRelation����ϵ��Connection��
	 */
	List<IRelation> getTargetRelations();
	
	/**
	 * �õ���ǰʵ��-���յ�ֵ
	 * @return
	 */
	List<IReference> getReferences();
	
	/**
	 * �õ�ʵ��ķ�����
	 * @return
	 */
	IAccessor getAccessor();
	
	/**
	 * �õ��ۺϹ�ϵ��target����
	 * @return
	 */
	List<IEntityTargetConnection> getAggreConnections();
}
