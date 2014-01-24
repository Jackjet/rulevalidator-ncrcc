package com.yonyou.nc.codevalidator.resparser;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.resource.IResource;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 资源查询工厂接口，用于分类查询对应的资源
 * @author mazhqa
 *
 * @param <T> 具体资源类型
 * @param <S> 具体的资源查询类型
 * @since V1.0
 */
public interface IResourceQueryFactory<T extends IResource, S extends IResourceQuery> {
	
	/**
	 * 资源类型
	 * @return
	 */
	ResourceType getType();
	
	/**
	 * 根据资源查询query接口得到对应的具体资源类型
	 * @param resourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	List<T> getResource(S resourceQuery) throws RuleBaseException;

}
