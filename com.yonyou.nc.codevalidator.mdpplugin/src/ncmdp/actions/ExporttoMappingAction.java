package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exportmapping.wizard.ExportMappingWizard;
import ncmdp.model.ValueObject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
/**
 * ����ӳ���action
 * @author wangxmn
 *
 */
public class ExporttoMappingAction extends Action{
	private ValueObject model;//��ѡ�е�ʵ��
	
	public ExporttoMappingAction(ValueObject model){
		super(Messages.exportToMappingAction);//��������
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
				ExportMappingWizard wizard = new ExportMappingWizard(model);
				WizardDialog wd = new WizardDialog(editor.getSite().getShell(), wizard);
				wd.open();
			}
		}
	}
}

