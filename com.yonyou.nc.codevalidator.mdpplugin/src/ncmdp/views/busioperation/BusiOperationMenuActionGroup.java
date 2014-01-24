package ncmdp.views.busioperation;

import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.RefOperation;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.actions.ActionGroup;

public class BusiOperationMenuActionGroup extends ActionGroup {
	private BusiOperationView view = null;

	public BusiOperationMenuActionGroup(BusiOperationView view) {
		super();
		this.view = view;
	}

	private class AddAction extends Action {

		public AddAction() {
			setText(Messages.BusiOperationMenuActionGroup_0);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof BusiOperation) {
				List<RefOperation> opList = ((BusiOperation) cell)
						.getOperations();
				OperationSelectDilog opDilog = new OperationSelectDilog(
						view.getShell(), opList);
				opDilog.open();
				if (opDilog.isOK()) {
					if (NCMDPEditor.getActiveMDPEditor() != null) {
						((NCMDPEditor) NCMDPEditor.getActiveMDPEditor())
								.setDirty(true);
					}
					List<RefOperation> resultList = opDilog.getResultOpList();

					// 行业开发下，需要校验
					boolean isIndustrvDev = NCMDPEditor.getActiveMDPEditor()
							.getModel().isIndustryIncrease();
					if (isIndustrvDev) {

						List<RefOperation> sourceList = ((BusiOperation) cell)
								.getOperations();

						for (RefOperation refOP : sourceList) {
							boolean containsKey = false;
							if (refOP.isSource()) {
								for (int i = 0; i < resultList.size(); i++) {
									RefOperation curRefOP = resultList.get(i);
									if (curRefOP.getId().equalsIgnoreCase(
											refOP.getId())) {
										containsKey = true;
										// 替换
										resultList.remove(i);
										resultList.add(i, refOP);
										break;
									}
								}
								if (!containsKey) {
									MessageDialog.openError(view.getShell(),
											Messages.BusiOperationMenuActionGroup_1,
											Messages.BusiOperationMenuActionGroup_2);
									return;
								}
							}
						}
						// 设置行业信息
						JGraph graph = NCMDPEditor.getActiveMDPEditor()
								.getModel();
						// boolean isSourceCell = ((BusiOperation) cell)
						// .isSource();

						for (RefOperation curRefOP : resultList) {
							if (!curRefOP.isSource()) {
								curRefOP.setIndustry(graph.getIndustry()
										.getCode());
								curRefOP.setVersionType(graph.getVersionType());
							}
						}
					}

					// 1）删除
					((BusiOperation) cell).removeAllOperation();
					List<RefOperation> list = (List<RefOperation>) view
							.getTableView().getInput();
					list.clear();
					// 2）增加
					BusiOperationUtil.fillOperationInfo(resultList, false);
					// 处理图形显示
					for (RefOperation op : resultList) {
						((BusiOperation) cell).addOperation(op);
					}
					view.getTableView().refresh();
				}
			}
		}
	}

	private class DelAction extends Action {
		public DelAction() {
			setText(Messages.BusiOperationMenuActionGroup_3);
		}

		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof BusiOperation) {
				// 处理表格
				List<RefOperation> toDel = new ArrayList<RefOperation>();
				List<?> inputList = (List<?>) view
						.getTableView().getInput();
				TableItem[] items = view.getTableView().getTable().getItems();
				boolean isIndustrvDev = NCMDPEditor.getActiveMDPEditor()
						.getModel().isIndustryIncrease();
				for (int i = 0; i < items.length; i++) {
					if (items[i] != null && items[i].getChecked()) {
						RefOperation curRefOp = (RefOperation) view
								.getTableView().getElementAt(i);
						if (isIndustrvDev && cell.isSource()
								&& curRefOp.isSource()) {
							MessageDialog.openError(view.getShell(), Messages.BusiOperationMenuActionGroup_4,
									Messages.BusiOperationMenuActionGroup_5);
							return;
						}
						toDel.add(curRefOp);
					}
				}
				if (toDel.size() > 0) {
					if (NCMDPEditor.getActiveMDPEditor() != null) {
						((NCMDPEditor) NCMDPEditor.getActiveMDPEditor())
								.setDirty(true);
					}
					view.getTableView().remove(
							toDel.toArray(new RefOperation[0]));
					inputList.remove(toDel);
					// 处理图形显示
					for (RefOperation operation : toDel) {
						((BusiOperation) cell).removeOperation(operation);
					}
				} else {
					MessageDialog.openInformation(view.getTableView()
							.getControl().getShell(), Messages.BusiOperationMenuActionGroup_6, Messages.BusiOperationMenuActionGroup_7);
				}
			}
		}

	}

	private class SelectAllAvtion extends Action {
		public SelectAllAvtion() {
			setText(Messages.BusiOperationMenuActionGroup_8);
		}

		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof BusiOperation) {
				TableItem[] items = view.getTableView().getTable().getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i] != null) {
						items[i].setChecked(true);
					}
				}
			}
		}
	}

	private class RemoveAllAvtion extends Action {
		public RemoveAllAvtion() {
			setText(Messages.BusiOperationMenuActionGroup_9);
		}

		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof BusiOperation) {
				TableItem[] items = view.getTableView().getTable().getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i] != null) {
						items[i].setChecked(false);
					}
				}
			}
		}
	}

	public void fillActionToolBar(ToolBarManager tm) {
		Action addAction = new AddAction();
		Action delAction = new DelAction();
		Action selectAllAvtion = new SelectAllAvtion();
		Action removeAllAvtion = new RemoveAllAvtion();
		tm.add(addAction);
		tm.add(delAction);
		tm.add(selectAllAvtion);
		tm.add(removeAllAvtion);
		tm.update(true);
	}
}
