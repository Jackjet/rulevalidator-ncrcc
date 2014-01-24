package ncmdp.actions;

import java.io.File;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.JGraph;
import ncmdp.tool.DeletePublishedMetaData;
import ncmdp.tool.NCMDPTool;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
/**
 * 删除发布的元数据
 * @author wangxmn
 *
 */
public class DeletePublishedMetaDataAction extends Action {

	public DeletePublishedMetaDataAction() {
		super(Messages.DeletePublishedMetaDataAction_0);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			if (!MessageDialog.openConfirm(editor.getSite().getShell(),
					Messages.DeletePublishedMetaDataAction_1,
					Messages.DeletePublishedMetaDataAction_2))
				return;
			IProject project = NCMDPTool.getProjectOfEditor(editor);
			if(project==null){
				return;
			}
			JGraph graph = editor.getModel();
			String compId = graph.getId();
			String industry = graph.isIndustryIncrease() ? graph.getIndustry()
					.getCode() : ""; //判断是否为增量开发，并获得增量开发的行业编码
			File file = editor.getFile();
			int versiontype = Integer.parseInt(graph.getVersionType());//获得开发维度
			boolean isIncreased = graph.isIndustryIncrease();//判断是否增量开发
			new DeletePublishedMetaData().deletePublishedMetaData(compId,
					versiontype, industry, isIncreased, file, project);
		}
	}
}
