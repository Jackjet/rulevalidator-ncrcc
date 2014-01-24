package com.yonyou.nc.codevalidator.runtime.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.runtime.webservice.Activator;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 规则执行的消息队列监听器
 * 
 * @author mazhqa
 * @since V2.9
 */
public class RuleExecutorMessageListener implements MessageListener {

	public static final String CODEPATH = "CODEPATH";
	public static final String NCHOME = "NCHOME";
	public static final String DSNAME = "DSNAME";
	public static final String EXECUTEPERIOD = "EXECUTEPERIOD";
	public static final String PRODUCTCODE = "PRODUCTCODE";

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		String jmsMessageId = null;
		try {
			// redelivered状态代表如果消息失败导致的问题
			jmsMessageId = mapMessage.getJMSMessageID();
			Logger.info(String.format("开始处理消息: %s ", jmsMessageId));
			boolean jmsRedelivered = mapMessage.getJMSRedelivered();
			boolean messageIdExist = JmsMessagesManager.getInstance().isMessageIdExist(jmsMessageId);
			Logger.info(String.format("当前消息状态: redelivered=%s, messageIdExist=%s", jmsRedelivered, messageIdExist));
			if (!jmsRedelivered || !messageIdExist) {
				// 如果未处理或上次已经处理完后的重发，则不进行处理操作
				JmsMessagesManager.getInstance().addProcessingMessage(jmsMessageId);
				String codePath = mapMessage.getString(CODEPATH);
				String ncHome = mapMessage.getString(NCHOME);
				String dsName = mapMessage.getString(DSNAME);
				String executePeriod = mapMessage.getString(EXECUTEPERIOD);
				String productCode = mapMessage.getString(PRODUCTCODE);
				Activator.getRuleRuntimeExecutor().execute(ncHome, codePath, dsName, executePeriod, productCode);
				message.acknowledge();
			}
		} catch (JMSException e) {
			throw new RuleBaseRuntimeException(e);
		} finally {
			// 不管是否处理成功，本次执行的处理都已经完成
			if (jmsMessageId != null) {
				JmsMessagesManager.getInstance().clearMessage(jmsMessageId);
			}
		}
	}

}
