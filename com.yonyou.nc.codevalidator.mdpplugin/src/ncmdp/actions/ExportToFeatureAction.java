package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttofea.wizard.ExportFeatureWizard;
import ncmdp.model.ValueObject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
/**
 * ����Ϊ����action
 * @author wangxmn
 *
 */
public class ExportToFeatureAction extends Action{
	private ValueObject model;//��ѡ�е�ʵ��
	
	public ExportToFeatureAction(ValueObject model){
		super(Messages.exportToFeatureAction);//��������
		this.model = model;
	}
	@Override
	public void run(){
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		//���ģ������
		String moduleName = model.getGraph().getOwnModule();
		//�����򵼶Ի���wizad
		if(editor!=null){
			if(moduleName!=null&&this.model!=null){
				ExportFeatureWizard wizard = new ExportFeatureWizard(model);
				WizardDialog wd = new WizardDialog(editor.getSite().getShell(), wizard);
				wd.open();
			}
		}
	}
}
