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
 * ɾ��������Ԫ����
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
					.getCode() : ""; //�ж��Ƿ�Ϊ���������������������������ҵ����
			File file = editor.getFile();
			int versiontype = Integer.parseInt(graph.getVersionType());//��ÿ���ά��
			boolean isIncreased = graph.isIndustryIncrease();//�ж��Ƿ���������
			new DeletePublishedMetaData().deletePublishedMetaData(compId,
					versiontype, industry, isIncreased, file, project);
		}
	}
}
