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
 * 用于jms消息发送的客户端
 * 
 * @author mazhqa
 * @since V2.9
 */
public class JmsSender {

	/**
	 * 根据连接的url和发送的队列名称，向队列中发送一条MapMesssage
	 * 
	 * @param brokerUrl
	 *            - jms连接的url 类似"tcp://localhost:61616"格式
	 * @param queueName
	 *            - 待发送消息的队列名称
	 * @param ruleValidatorMsgObject
	 *            - 执行规则验证所需要的消息对象
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
			System.out.println(String.format("向:%s 队列：%s 发送消息成功!", brokerUrl, queueName));
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