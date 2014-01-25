package com.yonyou.nc.codevalidator.runtime.jms;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * jms��Ϣ�����������ڴ�����Щ���ܻ����·��͵���Ϣ�������ظ�����
 * @author mazhqa
 * @since V2.9
 */
public class JmsMessagesManager {
	
	/**
	 * ��ǰ����ִ�еĴ�����Ϣ
	 */
	private Set<String> jmsMessageIdSet;
	
//	private Set<String> historyMessages;
	
	private static JmsMessagesManager instance;
	
	public static JmsMessagesManager getInstance() {
		if(instance == null) {
			instance = new JmsMessagesManager();
		}
		return instance;
	}
	
	private JmsMessagesManager() {
		 jmsMessageIdSet = new CopyOnWriteArraySet<String>();
	}

	public void addProcessingMessage(String jmsMessageId) {
		jmsMessageIdSet.add(jmsMessageId);
	}
	
	public boolean isMessageIdExist(String jmsMessageId) {
		return jmsMessageIdSet.contains(jmsMessageId);
	}
	
	public void clearMessage(String jmsMessageId) {
		jmsMessageIdSet.remove(jmsMessageId);
	}
}
