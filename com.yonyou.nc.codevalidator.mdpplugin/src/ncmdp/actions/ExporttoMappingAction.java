package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exportmapping.wizard.ExportMappingWizard;
import ncmdp.model.ValueObject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
/**
 * 导出映射的action
 * @author wangxmn
 *
 */
public class ExporttoMappingAction extends Action{
	private ValueObject model;//被选中的实体
	
	public ExporttoMappingAction(ValueObject model){
		super(Messages.exportToMappingAction);//导出特性
		this.model = model;
	}
	@Override
	public void run(){
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		//获得模块名称
		String moduleName = model.getGraph().getOwnModule();
		//创建向导对话框wizad
		if(editor!=null){
			if(moduleName!=null&&this.model!=null){
				ExportMappingWizard wizard = new ExportMappingWizard(model);
				WizardDialog wd = new WizardDialog(editor.getSite().getShell(), wizard);
				wd.open();
			}
		}
	}
}

