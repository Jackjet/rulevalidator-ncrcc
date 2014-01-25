package com.yonyou.nc.codevalidator.resparser;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.resource.IResource;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ��Դ��ѯ�����ӿڣ����ڷ����ѯ��Ӧ����Դ
 * @author mazhqa
 *
 * @param <T> ������Դ����
 * @param <S> �������Դ��ѯ����
 * @since V1.0
 */
public interface IResourceQueryFactory<T extends IResource, S extends IResourceQuery> {
	
	/**
	 * ��Դ����
	 * @return
	 */
	ResourceType getType();
	
	/**
	 * ������Դ��ѯquery�ӿڵõ���Ӧ�ľ�����Դ����
	 * @param resourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	List<T> getResource(S resourceQuery) throws RuleBaseException;

}
