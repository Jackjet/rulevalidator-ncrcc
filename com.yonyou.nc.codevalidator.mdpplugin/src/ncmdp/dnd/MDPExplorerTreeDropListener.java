package ncmdp.dnd;

import ncmdp.command.CellCreateCommand;
import ncmdp.command.CellDeleteCommand;
import ncmdp.editor.NCMDPBpfEditor;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.project.MDFileEditInput;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.tree.mdptree.IMDPTreeNode;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.MDPUtil;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * 在资源管理树中拖拽触发的类
 * @author wangxmn
 *
 */
public class MDPExplorerTreeDropListener implements DropTargetListener {
	private int oldDetail = DND.DROP_DEFAULT;

	public void dragEnter(DropTargetEvent event) {
		oldDetail = event.detail;

	}

	public void dragLeave(DropTargetEvent event) {

	}

	private Tree getTree(DropTargetEvent e) {
		return (Tree) ((DropTarget) e.widget).getControl();
	}

	public void dragOperationChanged(DropTargetEvent event) {
		oldDetail = event.detail;
	}

	public void dragOver(DropTargetEvent event) {
		TreeItem item = getTargetTreeItem(event);
		if (item != null) {
			if (!(item.getParentItem() instanceof IMDPTreeNode)) {
				event.detail = DND.DROP_NONE;

			}
		}
		if (!(event.item instanceof MDPFileTreeItem)) {
			event.detail = DND.DROP_NONE;
		} else {
			event.detail = oldDetail;
		}
	}

	private TreeItem getTargetTreeItem(DropTargetEvent event) {
		Tree tree = getTree(event);
		TreeItem sel = null;
		TreeItem tis[] = tree.getSelection();
		if (tis != null && tis.length > 0) {
			sel = tis[0];
		}
		return sel;

	}

	public void drop(DropTargetEvent event) {
		Object obj = event.data;
		if (obj instanceof RefMDCellTreeItem) {
			RefMDCellTreeItem item = (RefMDCellTreeItem) obj;
			Cell cell = item.getCell();
			TreeItem sourFileTI = item.getParentItem();
			if (!(sourFileTI instanceof MDPFileTreeItem) && event.detail == DND.DROP_MOVE) {
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "提示", "不能移动引用模型的图元");
				return;
			}
			if (!(event.item instanceof MDPFileTreeItem)) { return; }
			if (sourFileTI.equals(event.item)) {
				MessageDialog
						.openInformation(Display.getCurrent().getActiveShell(), "提示", "源和目标模型文件不能是同一个");
				return;
			}
			MDPFileTreeItem target = (MDPFileTreeItem) event.item;
			if (event.detail == DND.DROP_MOVE) {
				//				 移动
				MDPFileTreeItem source = (MDPFileTreeItem) sourFileTI;

				NCMDPEditor sourEditor = getNCMDPEditor(source);
				if (sourEditor != null) {
					JGraph sourGraph = sourEditor.getModel();
					cell = sourGraph.getCellByID(cell.getId());
					CellDeleteCommand cmd = new CellDeleteCommand(sourGraph, cell);
					cmd.setCanUndo(false);
					sourEditor.executComand(cmd);
					// sourGraph.removeCell(cell);
				}
				NCMDPEditor part = getNCMDPEditor(target);
				if (part != null) {
					JGraph graph = part.getModel();
					Rectangle rect = new Rectangle(cell.getLocation(), cell.getSize());
					CellCreateCommand cmd = new CellCreateCommand(cell, graph, rect);
					cmd.setCanUndo(false);
					part.executComand(cmd);
					// graph.addCell(cell);
				}

				/**yuyonga 创建移动的日志*/
				writeLogIntoFile2(source, target, cell);
				if (part.save()) {
					sourEditor.save();
				}
				//				part.save();
				//writeLogIntoFile(source,target,cell);

			} else if (event.detail == DND.DROP_COPY) {
				//复制
				NCMDPEditor part = getNCMDPEditor(target);
				if (part != null) {
					JGraph graph = part.getModel();
					Rectangle rect = new Rectangle(cell.getLocation(), cell.getSize());
					Cell copyCell = cell.copy();
					CellCreateCommand cmd = new CellCreateCommand(copyCell, graph, rect);

					part.executComand(cmd);
				}

			}

		}
		// System.out.println(event);
	}

	public void dropAccept(DropTargetEvent event) {
		// event.

	}

	/**该方法的目的就是源图元的移动轨迹给记录下来,以便与寻找图元*/
	private void writeLogIntoFile2(MDPFileTreeItem source, MDPFileTreeItem target, Cell cell) {
		/**目标路径*/
		String targetPath = target.getMDFileRelativePath();
		String cellid = cell.getId();
		NCMDPEditor sourEditor = getNCMDPEditor(source);
		JGraph sourGraph = null;
		if (sourEditor != null) {
			sourGraph = sourEditor.getModel();
		}
		sourGraph.setCellRemoveLog(cellid, targetPath);
	}

	private NCMDPEditor getNCMDPEditor(MDPFileTreeItem fileTreeItem) {
		if (fileTreeItem.getFile() != null && fileTreeItem.getFile().isFile()) {
			IEditorInput input = new MDFileEditInput(fileTreeItem);
			IWorkbenchPage page = JavaPlugin.getActivePage();
			IEditorPart part = null;
			if (page != null) {
				try {
					part = page.findEditor(input);
					if (part == null) {
						if (MDPUtil.isBpfInput(fileTreeItem.getFile().getName())) {
							part = page.openEditor(input, NCMDPBpfEditor.EDITOR_ID, true);
						} else {
							part = page.openEditor(input, NCMDPEditor.EDITOR_ID, true);
						}
					}
				} catch (PartInitException e) {
					MDPLogger.error(e.getMessage(), e);
					NCMDPTool.showErrDlg(NCMDPTool.getExceptionRecursionError(e));
				}
				if (part instanceof NCMDPEditor) { return (NCMDPEditor) part; }
			}
		}
		return null;
	}
}
