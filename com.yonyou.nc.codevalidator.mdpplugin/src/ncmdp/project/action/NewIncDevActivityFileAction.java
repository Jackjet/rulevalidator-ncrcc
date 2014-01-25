package ncmdp.project.action;

import java.io.File;

import ncmdp.common.MDPConstants;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.project.BuildFileTreeUtil;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.industry.Industry;
import ncmdp.ui.industry.IndustryIncDevFileDilog;
import ncmdp.ui.tree.mdptree.IMDPTreeNode;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ��������ҵ�����
 * 
 * @author dingxm
 * 
 */
public class NewIncDevActivityFileAction extends Action {
	public NewIncDevActivityFileAction() {
		super(Messages.NewIncDevActivityFileAction_0);// ��������ҵ�����
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		if (view == null)
			return;
		if(MDPUtil.getDevVersionType()==null){
			return;
		}
		if(MDPUtil.getDevCode()==null){
			return;
		}
		Tree tree = view.getExplorerTree();// treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		Shell shell = new Shell(Display.getCurrent());

		try {
			if (selTIs == null || selTIs.length == 0)
				throw new Exception(Messages.NewIncDevActivityFileAction_1);// ��ѡ��һ���ڵ�
			TreeItem selTI = selTIs[0];
			if (!(selTI instanceof IMDPTreeNode))
				return;
			File parentFile = ((IMDPTreeNode) selTI).getFile();
			if (parentFile.isFile())
				throw new Exception(Messages.NewIncDevActivityFileAction_2);// �������ļ����½��ļ���

			// parentFile
//			IProject project = MDPExplorerTreeView.getMDPExploerTreeView(
//					view.getSite().getPage()).getFileOwnProject(parentFile);
			IProject project = ProjectUtil.getFileOwnProject(parentFile);

			IndustryIncDevFileDilog input = new IndustryIncDevFileDilog(shell,
					project,
					new String[] { Constant.MDFILE_BPF_SUFFIX_EXTENTION });

			if (input.open() == InputDialog.OK) {
				// ����ά��
				String versionTypeText = input.getVersiontype();
				String versiontype = StringUtil
						.isEmptyWithTrim(versionTypeText) ? MDPConstants.INDUSTRY_VERSIONTYPE
						: versionTypeText;
				// ������ҵ
				String industryCode = input.getIndustry();
				String industryName = MDPUtil.getCurIndustryName();

				// ����ά�ȱ���
				String programTag = input.getProgramTag();
				String programCode = StringUtil.isEmptyWithTrim(programTag) ? "0" //$NON-NLS-1$
						: programTag;

				// �½�һ�������ļ�
				File sourceFile = input.getSelectFile();
				String[] names = sourceFile.getName().split("\\."); //$NON-NLS-1$
				String fileName = "" + names[0] + MDPUtil.getExtSuffix() + "." //$NON-NLS-1$ //$NON-NLS-2$
						+ names[1];
				File newFile = new File(parentFile, fileName);
				if (!newFile.exists()) {
					newFile.createNewFile();
					BuildFileTreeUtil.copyFile(sourceFile, newFile);
					// �½�һ�����
					JGraph graph = JGraphSerializeTool.loadFromFile(newFile,
							false);
					// У���Ƿ�ɿ���
					String errorMsg = NewFileUtil.checkNewIndFile(graph,
							versiontype, industryCode, programCode);
					if (!StringUtil.isEmptyWithTrim(errorMsg)) {
						MessageDialog.openError(null, "", errorMsg);
						throw new RuntimeException(errorMsg);
					}
					// �ļ�����
					graph.dealIncDevForIndustry(new Industry(industryCode,
							industryName), versiontype, programCode);

					MDPFileTreeItem fileTreeItem = view.addFileTreeNode(selTI,
							fileName, graph);
					fileTreeItem.setGraph(graph);
					view.openEditorTemp(fileTreeItem);
				} else {
					throw new Exception(Messages.NewIncDevActivityFileAction_8
							+ newFile.getPath());/* �ļ��Ѿ����ڣ� */
				}
			}
		} catch (Exception e) {
			String title = Messages.NewIncDevActivityFileAction_9;/* ��������ҵ����� */
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
}
