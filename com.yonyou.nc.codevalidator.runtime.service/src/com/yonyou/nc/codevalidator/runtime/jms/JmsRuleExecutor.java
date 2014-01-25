package com.yonyou.nc.codevalidator.runtime.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * JMS客户端，在此bundle启动时，需要开始执行监听操作，用来监听队列中的消息
 * 
 * @author mazhqa
 * @since V2.9
 */
public class JmsRuleExecutor {

	private ConnectionFactory connectionFactory;
	private Connection connection = null;
	// private Session session;

	private final String brokerUrl;
	private final String queueName;

	private int numberOfConsumers = 1;

	/**
	 * @param brokerUrl
	 *            - 消息队列的连接地址，格式类似"tcp://localhost:61616"
	 * @param queueName
	 *            - 消息队列名称
	 */
	public JmsRuleExecutor(String brokerUrl, String queueName) {
		this.brokerUrl = brokerUrl;
		this.queueName = queueName;
	}

	public void setNumberOfConsumers(int numberOfConsumers) {
		this.numberOfConsumers = numberOfConsumers;
	}

	private void establishConnection() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		connection = connectionFactory.createConnection();
		connection.start();
		for (int i = 0; i < numberOfConsumers; i++) {
			Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new RuleExecutorMessageListener());
		}
	}

	/**
	 * 建立起规则执行的消息监听，自动启动线程用于执行消息接收
	 * <P>
	 * 建立线程池用于并发执行监听的操作
	 * 
	 * @throws JMSException
	 * 
	 */
	public void startListen() throws JMSException {
		establishConnection();
		connection.setExceptionListener(new ExceptionListener() {

			@Override
			public void onException(JMSException exception) {
				int retryTimes = 5;
				String message = String.format("jms连接(%s)失败，尝试重新建立连接...", brokerUrl);
				Logger.error(message);
				System.err.println(message);
				exception.printStackTrace();
				for (int i = 1; i <= retryTimes; i++) {
					try {
						establishConnection();
						System.out.println(String.format("第%s次,连接成功", i));
						return;
					} catch (JMSException e) {
						message = String.format("第%s次,尝试重新连接(%s)失败，3分钟后重新尝试...", i, brokerUrl);
						Logger.error(message, e);
						System.err.println(message);
						e.printStackTrace();
						try {
							//三分钟后重试
							Thread.sleep(1000 * 60 * 3);
						} catch (InterruptedException e1) {
						}
					}
				}
				String errorMessage = String.format("已进行 %s 次重连尝试...", retryTimes);
				System.err.println(errorMessage);
				Logger.error(errorMessage);
			}
		});
	}

	public void stopListener() throws JMSException {
		connection.close();
	}
}