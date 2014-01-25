package ncmdp.views.busiactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncmdp.common.MDPConstants;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.RefBusiOperation;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.actions.ActionGroup;

public class BusiActivityMenuActionGroup extends ActionGroup {
	private BusiActivityView view = null;

	public BusiActivityMenuActionGroup(BusiActivityView view) {
		super();
		this.view = view;
	}

	private class AddAction extends Action {

		public AddAction() {
			setText("Add"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof BusiActivity) {
				List<RefBusiOperation> opList = ((BusiActivity) cell)
						.getRefBusiOperations();
				BusiOperationSelectDilog opDilog = new BusiOperationSelectDilog(
						view.getShell(), opList);
				opDilog.open();
				if (opDilog.isOK()) {
					if (NCMDPEditor.getActiveMDPEditor() != null) {
						((NCMDPEditor) NCMDPEditor.getActiveMDPEditor())
								.setDirty(true);
					}

					// ��ҵ�����£���ҪУ��
					boolean isIndustrvDev = NCMDPEditor.getActiveMDPEditor()
							.getModel().isIndustryIncrease();
					Map<String, RefBusiOperation> toMap = new HashMap<String, RefBusiOperation>();// ҵ�����id
																									// ->
																									// ref
					List<BusiOperation> resultList = opDilog.getResultOpList();
					if (isIndustrvDev) {
						List<RefBusiOperation> sourceList = ((BusiActivity) cell)
								.getRefBusiOperations();
						for (int i = 0; i < sourceList.size(); i++) {
							RefBusiOperation refOP = sourceList.get(i);
							if (refOP.isSource()) {
								boolean contaisKey = false;// ԭʼ��ҵ�������Ƿ�������µ�����
								for (int j = 0; j < resultList.size(); j++) {
									BusiOperation busiOP = resultList.get(j);
									if (busiOP.getId().equalsIgnoreCase(
											refOP.getRealBusiOpid())) {
										contaisKey = true;
										// �滻
										toMap.put(busiOP.getId(), refOP);
										break;
									}
								}
								if (!contaisKey) {
									MessageDialog.openError(view.getShell(),
											Messages.BusiActivityMenuActionGroup_1, Messages.BusiActivityMenuActionGroup_2);
									return;
								}
							}
						}
					}

					// 1��ɾ��
					((BusiActivity) cell).removeAllOperation();
					List<RefBusiOperation> list = (List<RefBusiOperation>) view
							.getTableView().getInput();
					list.clear();
					// 2������
					for (BusiOperation op : resultList) {
						RefBusiOperation refop = null;
						if (toMap.containsKey(op.getId())) {
							refop = toMap.get(op.getId());
						} else {
							refop = new RefBusiOperation(op,
									((BusiActivity) cell).getId());
						}
						if(!refop.isSource()){
							refop.setIndustry(NCMDPEditor.getActiveMDPEditor()
									.getModel().getIndustry().getCode());
							refop.setVersionType(NCMDPEditor.getActiveMDPEditor()
									.getModel().getVersionType());
						}
						

						((BusiActivity) cell).addOperation(refop);
					}
					view.getTableView().refresh();
				}
			}
		}
	}

	private class DelAction extends Action {
		public DelAction() {
			setText("delete"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof BusiActivity) {
				// ������
				List<RefBusiOperation> toDel = new ArrayList<RefBusiOperation>();
				List<?> inputList = (List<?>) view
						.getTableView().getInput();
				TableItem[] items = view.getTableView().getTable().getItems();

				boolean isIndustrvDev = NCMDPEditor.getActiveMDPEditor()
						.getModel().isIndustryIncrease();

				for (int i = 0; i < items.length; i++) {
					if (items[i] != null && items[i].getChecked()) {
						RefBusiOperation curBusiOP = (RefBusiOperation) view
								.getTableView().getElementAt(i);
						if (isIndustrvDev && cell.isSource()
								&& curBusiOP.isSource()) {// ��ҵ�£�����Ƿ�����ɾ��
							MessageDialog.openError(view.getShell(), Messages.BusiActivityMenuActionGroup_0,
									Messages.BusiActivityMenuActionGroup_5);
							return;
						}
						toDel.add(curBusiOP);
					}
				}
				if (toDel.size() > 0) {
					if (NCMDPEditor.getActiveMDPEditor() != null) {
						((NCMDPEditor) NCMDPEditor.getActiveMDPEditor())
								.setDirty(true);
					}
					view.getTableView().remove(
							toDel.toArray(new RefBusiOperation[0]));
					inputList.remove(toDel);
					// ����ͼ����ʾ
					for (RefBusiOperation operation : toDel) {
						((BusiActivity) cell).removeOperation(operation);
					}
				} else {
					MessageDialog
							.openInformation(view.getTableView().getControl()
									.getShell(), "title", "Please select the BusiOperation"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	private class SelectAllAvtion extends Action {
		public SelectAllAvtion() {
			setText("All selected"); //$NON-NLS-1$
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
			setText("un selected"); //$NON-NLS-1$
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
