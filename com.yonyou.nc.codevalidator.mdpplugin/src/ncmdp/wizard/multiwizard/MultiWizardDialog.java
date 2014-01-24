package ncmdp.wizard.multiwizard;

import ncmdp.wizard.multiwizard.util.IWizardPageHandler;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class MultiWizardDialog extends WizardDialog {
	public MultiWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	protected void nextPressed() {
		if (getCurrentPage() != null && getCurrentPage() instanceof IWizardPageHandler) {
			((IWizardPageHandler) getCurrentPage()).onPressedNext();
		}
		super.nextPressed();
	}

}
