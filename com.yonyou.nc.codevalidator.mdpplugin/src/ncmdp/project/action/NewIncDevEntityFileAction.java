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

public class NewIncDevEntityFileAction extends Action {

	public NewIncDevEntityFileAction() {
		super(Messages.NewIncDevEntityFileAction_0);// 增量开发实体组件
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		if (view == null)
			return;
		if(MDPUtil.getDevVersionType()==null){//如果行业信息为空，则返回
			return;
		}
		Tree tree = view.getExplorerTree();// treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		Shell shell = new Shell(Display.getCurrent());

		try {
			if (selTIs == null || selTIs.length == 0)
				throw new Exception(Messages.NewIncDevEntityFileAction_1);// 请选中一个节点。
			TreeItem selTI = selTIs[0];
			if (!(selTI instanceof IMDPTreeNode))
				return;
			File parentFile = ((IMDPTreeNode) selTI).getFile();
			if (parentFile.isFile())
				throw new Exception(Messages.NewIncDevEntityFileAction_2);// 不能在文件下新建文件。

			// parentFile
//			IProject project = MDPExplorerTreeView.getMDPExploerTreeView(
//					view.getSite().getPage()).getFileOwnProject(parentFile);
			IProject project = ProjectUtil.getFileOwnProject(parentFile);

			IndustryIncDevFileDilog input = new IndustryIncDevFileDilog(shell,
					project, new String[] { Constant.MDFILE_SUFFIX_EXTENTION });

			if (input.open() == InputDialog.OK) {
				// 开发维度
				String versionTypeText = input.getVersiontype();
				String versiontype = StringUtil
						.isEmptyWithTrim(versionTypeText) ? MDPConstants.INDUSTRY_VERSIONTYPE
						: versionTypeText;//versiontype 读取的 MDPUtil.getDevInfo()
				// 所属行业
				String industryCode = input.getIndustry();
				String industryName = MDPUtil.getCurIndustryName();
				// 开发维度编码
				String programTag = input.getProgramTag();
				String programCode = StringUtil.isEmptyWithTrim(programTag) ? "0" //$NON-NLS-1$
						: programTag;

				// 新建一个备份文件
				File sourceFile = input.getSelectFile();
				String[] names = sourceFile.getName().split("\\."); //$NON-NLS-1$
				String fileName = "" + names[0] + MDPUtil.getExtSuffix() + "." //$NON-NLS-1$ //$NON-NLS-2$
						+ names[1];
				File newFile = new File(parentFile, fileName);
				if (!newFile.exists()) {
					newFile.createNewFile();
					BuildFileTreeUtil.copyFile(sourceFile, newFile);
					// 新建一个组件
					JGraph graph = JGraphSerializeTool.loadFromFile(newFile,
							false);
					// 校验是否可开发
					String errorMsg = NewFileUtil.checkNewIndFile(graph,
							versiontype, industryCode, programCode);
					if (!StringUtil.isEmptyWithTrim(errorMsg)) {
						MessageDialog.openError(null, "note", errorMsg);
						throw new RuntimeException(errorMsg);
					}
					// 文件处理

					graph.dealIncDevForIndustry(new Industry(industryCode,
							industryName), versiontype, programCode);
					MDPFileTreeItem fileTreeItem = view.addFileTreeNode(selTI,
							fileName, graph);
					fileTreeItem.setGraph(graph);
					view.openEditorTemp(fileTreeItem);
				} else {
					throw new Exception(Messages.NewIncDevEntityFileAction_8
							+ newFile.getPath());// 文件已经存在：
				}
			}
		} catch (Exception e) {
			String title = Messages.NewIncDevEntityFileAction_9;// 增量开发实体组件
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
}
