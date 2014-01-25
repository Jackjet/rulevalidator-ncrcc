package com.yonyou.nc.codevalidator.runtime.plugin.celleditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * 抽象的IDialogComposite实现, 带有默认的“确认、取消”按钮， label中显示的名称为toString方法显示名称
 * <p>并提供了一些默认功能，比如对话框显示在中间
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
	 * 实际的创建窗体内容方法，createContents中会调用，
	 * 可以更改对话框的大小和显示标题
	 * @param parent
	 * @param value
	 */
	public abstract void createActualContents(Control parent, Object value) ;
	
	@Override
	public final void createContents(Control parent, Object value) {
		createActualContents(parent, value);
		//加入了对话框显示在屏幕中央的逻辑处理
		Rectangle displayBounds = Display.getCurrent().getPrimaryMonitor().getBounds();
		Rectangle shellBounds = parent.getShell().getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		parent.getShell().setLocation(x, y);
	}
	
	@Override
	public String getTitle() {
		return "表格参照对话框";
	}
	
	@Override
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}

}
