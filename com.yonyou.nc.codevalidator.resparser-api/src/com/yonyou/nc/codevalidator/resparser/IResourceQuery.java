package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.RuntimeContext;

/**
 * ��Դ��ѯ�ӿ�
 * @author mazhqa
 * @since V1.0
 */
public interface IResourceQuery {
	
	/**
	 * ��Դ��ѯ��string��ʾ
	 * @return
	 */
	String getQueryString();
	 
	/**
	 * ��ǰ����ִ�е������ģ���ִ�в�ѯʱ��ע��ִ�е���������Ϣ
	 * @return
	 * @since V2.1
	 */
	RuntimeContext getRuntimeContext();
	
	void setRuntimeContext(RuntimeContext runtimeContext);
}
