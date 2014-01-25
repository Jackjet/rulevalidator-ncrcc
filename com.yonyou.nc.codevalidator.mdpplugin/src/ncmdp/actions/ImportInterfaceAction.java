package ncmdp.actions;

import java.io.File;
import java.lang.reflect.Method;

import ncmdp.editor.NCMDPEditor;
import ncmdp.importitf.wizard.ImpoerItfDiolg;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.model.activity.Parameter;
import ncmdp.tool.ClassTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;

public class ImportInterfaceAction extends Action {
	public ImportInterfaceAction() {
		setText(Messages.ImportInterfaceAction_0);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			JGraph graph = editor.getModel();

			ImpoerItfDiolg input = new ImpoerItfDiolg(editor.getSite()
					.getShell());

			if (input.open() == InputDialog.OK) {
				String importItfClassName = input.getImportItf();
//				MDPExplorerTreeView view = MDPExplorerTreeView
//						.getMDPExploerTreeView(null);
				File file = editor.getFile();
//				IProject project = view.getFileOwnProject(file);
				IProject project = ProjectUtil.getFileOwnProject(file);
				Class<?> itfClass = null;
				try {
					itfClass = ClassTool.loadClass(importItfClassName, project);
				} catch (ClassNotFoundException e) {
					MDPLogger.error(e.getMessage(), e);
					throw new RuntimeException(Messages.ImportInterfaceAction_1 + importItfClassName
							+ Messages.ImportInterfaceAction_2);
				}
				if (itfClass == null) {
					throw new RuntimeException(Messages.ImportInterfaceAction_3 + importItfClassName
							+ Messages.ImportInterfaceAction_4);
				} else {
					OpInterface itf = new OpInterface();
					graph.addCell(itf);
					itf.setFullClassName(importItfClassName);
					if (!importItfClassName.contains(".")) { //$NON-NLS-1$
						throw new RuntimeException(Messages.ImportInterfaceAction_6
								+ importItfClassName + Messages.ImportInterfaceAction_7);
					}
					String name = importItfClassName
							.substring(importItfClassName.lastIndexOf(".") + 1); //$NON-NLS-1$
					itf.setDisplayName(name);
					itf.setName(name);
					Method[] ms = itfClass.getDeclaredMethods();
					if (ms.length > 0) {
						for (Method method : ms) {
							Operation operation = new Operation();
							itf.addOperation(operation);
							operation.setName(method.getName());
							operation.setDisplayName(method.getName());
							operation.setOpItfID(itf.getId());

							Class<?>[] params = method.getParameterTypes();
							if (params.length > 0) {
								for (int i = 0; i < params.length; i++) {
//									Class<?> para = params[i];
									Parameter param = new Parameter();
									param.setName("param" + i); 
									param.setDisplayName(Messages.ImportInterfaceAction_10 + i);
									param.setTypeStyle(Constant.TYPE_STYLES[0]);
									param.setParaType(NCMDPTool.getBaseTypes()[0]);
									operation.getParas().add(param);
								}
							}
						}
					}
				}
			}
		}
	}
}
