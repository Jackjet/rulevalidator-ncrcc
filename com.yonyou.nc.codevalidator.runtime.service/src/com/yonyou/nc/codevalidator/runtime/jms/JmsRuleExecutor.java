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
 * JMS�ͻ��ˣ��ڴ�bundle����ʱ����Ҫ��ʼִ�м����������������������е���Ϣ
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
	 *            - ��Ϣ���е����ӵ�ַ����ʽ����"tcp://localhost:61616"
	 * @param queueName
	 *            - ��Ϣ��������
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
	 * ���������ִ�е���Ϣ�������Զ������߳�����ִ����Ϣ����
	 * <P>
	 * �����̳߳����ڲ���ִ�м����Ĳ���
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
				String message = String.format("jms����(%s)ʧ�ܣ��������½�������...", brokerUrl);
				Logger.error(message);
				System.err.println(message);
				exception.printStackTrace();
				for (int i = 1; i <= retryTimes; i++) {
					try {
						establishConnection();
						System.out.println(String.format("��%s��,���ӳɹ�", i));
						return;
					} catch (JMSException e) {
						message = String.format("��%s��,������������(%s)ʧ�ܣ�3���Ӻ����³���...", i, brokerUrl);
						Logger.error(message, e);
						System.err.println(message);
						e.printStackTrace();
						try {
							//�����Ӻ�����
							Thread.sleep(1000 * 60 * 3);
						} catch (InterruptedException e1) {
						}
					}
				}
				String errorMessage = String.format("�ѽ��� %s ����������...", retryTimes);
				System.err.println(errorMessage);
				Logger.error(errorMessage);
			}
		});
	}

	public void stopListener() throws JMSException {
		connection.close();
	}
}