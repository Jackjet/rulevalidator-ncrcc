package com.yonyou.nc.codevalidator.runtime.plugin.celleditor;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ���նԻ��������ڶ���Ľ��洦��ӿڣ����а�����ʾ���ݣ���ȡ���ս��ֵ������
 * @author mazqa
 *
 */
public interface IDialogComposite {
	
	/**
	 * �����Ի������Area
	 * @param parent
	 * @param value
	 * @return
	 */
	public void createDialogArea(final Composite parent, Object value);
	
	/**
	 * �����Ի������ݷ��������Ը��ĶԻ���Ĵ�С����ʾ����
	 * @param parent
	 * @param value
	 * @return
	 */
	public void createContents(final Control parent, Object value);
	
	/**
	 * �õ��Ի�������ʾ�İ�ť
	 * @return
	 */
	public List<DialogButtonObj> getDialogButtons();
	
	/**
	 * �԰�ť�¼��Ĵ�����
	 * @param buttonId
	 */
	public void buttonPressed(int buttonId);
	
	/**
	 * ��ȡ���յķ��ؽ������, �������ͬһ��������÷������л����ƺ�Ķ���
	 * �Ա��ܹ��ڱ༭��ɺ����ˢ��
	 * @return
	 */
	public Object getResultValue();
	
	/**
	 * �ڲ��ձ༭״̬����ʾ�����text�е�ֵ
	 * @param value
	 * @return
	 */
	public String getDisplayText(Object value);
	
	/**
	 * �Ӳ��յ�text���Objectֵ��
	 * @param textValue
	 * @return
	 * @throws Exception ת��ʧ�ܣ���ʽ����ȷʱ
	 */
	public Object getObjectFromText(String textValue) throws Exception;
	
	/**
	 * ����ǰ����ı����Ƿ���ã����������{@link #getObjectFromText(String)}����Ҳ���ᱻ���õ�
	 * @return
	 */
	public boolean isTextEnable();
	
	/**
	 * ���շǱ༭״̬��ʾ��ֵ
	 * @param value
	 * @return
	 */
	public String getDisplayLabel(Object value);
	
	/**
	 * �õ�TitleAreaDialog�еı�������
	 * @return
	 */
	public String getTitle();
	
	/**
	 * �õ�TitleAreaDialog�е���������
	 * @return
	 */
	public String getDescription();
	
	/**
	 * ��ʾ��dialog�е�button����
	 * @author mazqa
	 *
	 */
	public static class DialogButtonObj{
		int id;
		String name;
		boolean defaultButton;
		public DialogButtonObj(int id, String name, boolean defaultButton) {
			super();
			this.id = id;
			this.name = name;
			this.defaultButton = defaultButton;
		}
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public boolean isDefaultButton() {
			return defaultButton;
		}
		
	}
}

