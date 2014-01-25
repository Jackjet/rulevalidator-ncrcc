package com.yonyou.nc.codevalidator.rule;

import java.util.List;

/**
 * ������ϲ�ε�ִ�е�Ԫ
 * @author mazhqa
 * @since V2.7
 */
public interface ICompositeExecuteUnit {
	
	/**
	 * �õ���ִ�����ȵ������ȣ�һ����ȫ�ּ���ģ�鼶ҵ������л��õ�
	 * @return
	 */
	List<BusinessComponent> getSubBusinessComponentList();

	void addSubBusinessComponentList(BusinessComponent businessComponent);

}
