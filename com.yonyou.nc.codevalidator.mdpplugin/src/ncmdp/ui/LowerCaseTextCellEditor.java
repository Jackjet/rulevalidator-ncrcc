package ncmdp.ui;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * 小写编辑器，用于属性中名称的编辑
 * @author wangxmn
 *
 */
public class LowerCaseTextCellEditor extends TextCellEditor {

	public LowerCaseTextCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Control createControl(Composite parent) {
		Text text = (Text)super.createControl(parent);
    	 text.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {
				e.text = e.text.toLowerCase();
			}
    	 });
		return text;
	}
}
