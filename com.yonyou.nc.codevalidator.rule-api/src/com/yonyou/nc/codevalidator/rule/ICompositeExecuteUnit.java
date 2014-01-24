package com.yonyou.nc.codevalidator.rule;

import java.util.List;

/**
 * 定义组合层次的执行单元
 * @author mazhqa
 * @since V2.7
 */
public interface ICompositeExecuteUnit {
	
	/**
	 * 得到该执行粒度的子粒度，一般在全局级和模块级业务组件中会用到
	 * @return
	 */
	List<BusinessComponent> getSubBusinessComponentList();

	void addSubBusinessComponentList(BusinessComponent businessComponent);

}
