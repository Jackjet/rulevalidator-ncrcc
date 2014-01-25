package com.yonyou.nc.codevalidator.runtime.plugin.celleditor;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 参照对话框中用于定义的界面处理接口，其中包括显示数据，获取最终结果值操作等
 * @author mazqa
 *
 */
public interface IDialogComposite {
	
	/**
	 * 创建对话框界面Area
	 * @param parent
	 * @param value
	 * @return
	 */
	public void createDialogArea(final Composite parent, Object value);
	
	/**
	 * 创建对话框内容方法，可以更改对话框的大小和显示标题
	 * @param parent
	 * @param value
	 * @return
	 */
	public void createContents(final Control parent, Object value);
	
	/**
	 * 得到对话框中显示的按钮
	 * @return
	 */
	public List<DialogButtonObj> getDialogButtons();
	
	/**
	 * 对按钮事件的处理方法
	 * @param buttonId
	 */
	public void buttonPressed(int buttonId);
	
	/**
	 * 获取最终的返回结果对象, 如果不是同一个对象，最好返回序列化复制后的对象，
	 * 以便能够在编辑完成后进行刷新
	 * @return
	 */
	public Object getResultValue();
	
	/**
	 * 在参照编辑状态中显示在左侧text中的值
	 * @param value
	 * @return
	 */
	public String getDisplayText(Object value);
	
	/**
	 * 从参照的text获得Object值，
	 * @param textValue
	 * @return
	 * @throws Exception 转换失败，格式不正确时
	 */
	public Object getObjectFromText(String textValue) throws Exception;
	
	/**
	 * 参照前面的文本框是否可用，如果不可用{@link #getObjectFromText(String)}方法也不会被调用到
	 * @return
	 */
	public boolean isTextEnable();
	
	/**
	 * 参照非编辑状态显示的值
	 * @param value
	 * @return
	 */
	public String getDisplayLabel(Object value);
	
	/**
	 * 得到TitleAreaDialog中的标题内容
	 * @return
	 */
	public String getTitle();
	
	/**
	 * 得到TitleAreaDialog中的描述内容
	 * @return
	 */
	public String getDescription();
	
	/**
	 * 显示在dialog中的button对象
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

