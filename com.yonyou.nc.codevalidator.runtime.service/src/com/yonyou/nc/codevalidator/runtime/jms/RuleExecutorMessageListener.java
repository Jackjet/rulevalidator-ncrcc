package com.yonyou.nc.codevalidator.runtime.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.runtime.webservice.Activator;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ����ִ�е���Ϣ���м�����
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
			// redelivered״̬���������Ϣʧ�ܵ��µ�����
			jmsMessageId = mapMessage.getJMSMessageID();
			Logger.info(String.format("��ʼ������Ϣ: %s ", jmsMessageId));
			boolean jmsRedelivered = mapMessage.getJMSRedelivered();
			boolean messageIdExist = JmsMessagesManager.getInstance().isMessageIdExist(jmsMessageId);
			Logger.info(String.format("��ǰ��Ϣ״̬: redelivered=%s, messageIdExist=%s", jmsRedelivered, messageIdExist));
			if (!jmsRedelivered || !messageIdExist) {
				// ���δ������ϴ��Ѿ����������ط����򲻽��д������
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
			// �����Ƿ���ɹ�������ִ�еĴ����Ѿ����
			if (jmsMessageId != null) {
				JmsMessagesManager.getInstance().clearMessage(jmsMessageId);
			}
		}
	}

}
