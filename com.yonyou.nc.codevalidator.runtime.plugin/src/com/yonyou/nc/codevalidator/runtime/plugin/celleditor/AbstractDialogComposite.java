package com.yonyou.nc.codevalidator.runtime.plugin.celleditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * �����IDialogCompositeʵ��, ����Ĭ�ϵġ�ȷ�ϡ�ȡ������ť�� label����ʾ������ΪtoString������ʾ����
 * <p>���ṩ��һЩĬ�Ϲ��ܣ�����Ի�����ʾ���м�
 * 
 * @author mazqa
 * 
 */
public abstract class AbstractDialogComposite implements IDialogComposite {

	@Override
	public List<DialogButtonObj> getDialogButtons() {
		List<DialogButtonObj> result = new ArrayList<DialogButtonObj>();
		result.add(new DialogButtonObj(IDialogConstants.OK_ID, "OK", true));
		result.add(new DialogButtonObj(IDialogConstants.CANCEL_ID, "Cancel", false));
		return result;
	}

	@Override
	public String getDisplayLabel(Object value) {
		return value == null ? "" : value.toString(); //$NON-NLS-1$
	}
	
	@Override
	public String getDisplayText(Object value) {
		return value == null ? "" : (value.toString() == null ? "" : value.toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public boolean isTextEnable() {
		return false;
	}
	
	@Override
	public Object getObjectFromText(String textValue) throws Exception {
		return null;
	}

	/**
	 * ʵ�ʵĴ����������ݷ�����createContents�л���ã�
	 * ���Ը��ĶԻ���Ĵ�С����ʾ����
	 * @param parent
	 * @param value
	 */
	public abstract void createActualContents(Control parent, Object value) ;
	
	@Override
	public final void createContents(Control parent, Object value) {
		createActualContents(parent, value);
		//�����˶Ի�����ʾ����Ļ������߼�����
		Rectangle displayBounds = Display.getCurrent().getPrimaryMonitor().getBounds();
		Rectangle shellBounds = parent.getShell().getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		parent.getShell().setLocation(x, y);
	}
	
	@Override
	public String getTitle() {
		return "�����նԻ���";
	}
	
	@Override
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}

}
