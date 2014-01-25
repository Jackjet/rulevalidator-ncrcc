package com.yonyou.nc.codevalidator.anttask;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ����jms��Ϣ���͵Ŀͻ���
 * 
 * @author mazhqa
 * @since V2.9
 */
public class JmsSender {

	/**
	 * �������ӵ�url�ͷ��͵Ķ������ƣ�������з���һ��MapMesssage
	 * 
	 * @param brokerUrl
	 *            - jms���ӵ�url ����"tcp://localhost:61616"��ʽ
	 * @param queueName
	 *            - ��������Ϣ�Ķ�������
	 * @param ruleValidatorMsgObject
	 *            - ִ�й�����֤����Ҫ����Ϣ����
	 * @throws JMSException
	 */
	public static void sendMapMessage(String brokerUrl, String queueName,
			RuleValidatorMessageObject ruleValidatorMsgObject) throws JMSException {
		Connection connection = null;
		Session session = null;
		Destination destination;
		MessageProducer producer;
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, brokerUrl);
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.setTimeToLive(1000 * 60 * 60 * 24);
			MapMessage mapMessage = session.createMapMessage();
			mapMessage.setString(RuleValidatorMessageObject.CODEPATH, ruleValidatorMsgObject.getCodePath());
			mapMessage.setString(RuleValidatorMessageObject.NCHOME, ruleValidatorMsgObject.getNcHome());
			mapMessage.setString(RuleValidatorMessageObject.DSNAME, ruleValidatorMsgObject.getDataSourceName());
			mapMessage.setString(RuleValidatorMessageObject.EXECUTEPERIOD, ruleValidatorMsgObject.getExecutePeriod());
			mapMessage.setString(RuleValidatorMessageObject.PRODUCTCODE, ruleValidatorMsgObject.getProductCode());
			producer.send(mapMessage);
			System.out.println(String.format("��:%s ���У�%s ������Ϣ�ɹ�!", brokerUrl, queueName));
			session.commit();
		} catch (JMSException e) {
			try {
				if (session != null) {
					session.rollback();
				}
			} catch (JMSException e1) {
				throw e1;
			}
			throw e;
		} finally {
			try {
				if (null != connection) {
					connection.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}