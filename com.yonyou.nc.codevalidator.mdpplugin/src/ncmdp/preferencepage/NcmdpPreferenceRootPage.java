package ncmdp.preferencepage;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * ��ѡ�������࣬����������ѡ��ҳ���е����ݣ�
 * ����ΪNCMDP����
 * @author wangxmn
 */
public class NcmdpPreferenceRootPage extends PreferencePage 
implements IWorkbenchPreferencePage {

	public NcmdpPreferenceRootPage() {}

	public NcmdpPreferenceRootPage(String title) {
		super(title);
	}

	public NcmdpPreferenceRootPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new RowLayout());
		new Label(topComp,SWT.NONE).setText(Messages.NcmdpPreferenceRootPage_0);
		return topComp;
	}

	public void init(IWorkbench workbench) {
	}

}
