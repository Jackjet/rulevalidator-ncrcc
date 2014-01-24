package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttofea.wizard.ExportFeatureWizard;
import ncmdp.model.ValueObject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
/**
 * 导出为特性action
 * @author wangxmn
 *
 */
public class ExportToFeatureAction extends Action{
	private ValueObject model;//被选中的实体
	
	public ExportToFeatureAction(ValueObject model){
		super(Messages.exportToFeatureAction);//导出特性
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
				ExportFeatureWizard wizard = new ExportFeatureWizard(model);
				WizardDialog wd = new WizardDialog(editor.getSite().getShell(), wizard);
				wd.open();
			}
		}
	}
}
