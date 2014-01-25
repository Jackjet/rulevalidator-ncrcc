package ncmdp.actions;

import java.io.File;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.JGraph;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.PublishMetaData;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class PublishMetaDataAction extends Action {
	private boolean allowLowVersionPublish = false;
	public PublishMetaDataAction(boolean allowLowVersionPublish) {
		super(Messages.PublishMetaDataAction_0);
		if(allowLowVersionPublish){
			setText(Messages.PublishMetaDataAction_1);
		}else{
			setText(Messages.PublishMetaDataAction_2);
		}
		this.allowLowVersionPublish = allowLowVersionPublish;

	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			if (editor.isDirty()) {
				MessageDialog.openConfirm(editor.getSite().getShell(), Messages.PublishMetaDataAction_3, Messages.PublishMetaDataAction_4);
				return;
			}
			IProject project = NCMDPTool.getProjectOfEditor(editor);
			if(project==null){
				return;
			}
			JGraph graph = editor.getModel();
			String validateStr = graph.validate();
			if(validateStr != null && validateStr.trim().length() > 0){
				String message = Messages.PublishMetaDataAction_5+validateStr;
				MessageDialog.openInformation(editor.getSite().getShell(), Messages.PublishMetaDataAction_6, message);
				return;
			}
			if(!MessageDialog.openConfirm(editor.getSite().getShell(), Messages.PublishMetaDataAction_7, Messages.PublishMetaDataAction_8))
				return;
			File file = editor.getFile();
			try {
				new PublishMetaData().publishMetaData(file,project,allowLowVersionPublish);
			} catch (SecurityException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), 
						"错误", "出现安全问题");
			} catch (NoSuchMethodException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), 
						"错误", "没有对应的方法，请查看中间件是否启动");
			} catch (ClassNotFoundException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), 
						"错误", "没有对应的类，请查看中间件是否启动");
			}
		}
	}
	
	
}
