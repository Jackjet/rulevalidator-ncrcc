package com.yonyou.nc.codevalidator.rule.executor;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * 进行验证的监听器，在任务执行和任务结束时就会进行统治
 * <p>
 * V2.6加入新功能，可以请求cancel，在每次执行结束完一个规则后，都要进行cancel的判断
 * @author mazhqa
 * @since V1.0
 */
public interface IValidatorListener {

	/**
	 * 当执行单元上有相应的事件时，进行通知
	 * @param businessComponent - 执行单元
	 * @param event - 事件类型
	 */
	void notifyBusiCompEvent(BusinessComponent businessComponent, ValidatorEvent event);

	/**
	 * 当执行单元上某条规则的执行开始/结束时进行事件通知
	 * @param businessComponent
	 * @param ruleIdentifier
	 * @param event
	 */
	void notifyRuleEvent(BusinessComponent businessComponent, String ruleIdentifier, ValidatorEvent event);
	
	/**
	 * 判断是否用户进行了取消操作
	 * @return
	 */
	boolean requireCancel();
	
	/**
	 * 执行取消的操作
	 */
	void executeCancelOperation();
	
	

}
