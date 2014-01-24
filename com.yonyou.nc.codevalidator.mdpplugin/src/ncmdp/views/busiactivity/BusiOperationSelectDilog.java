package ncmdp.views.busiactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.factory.ImageFactory;
import ncmdp.model.JGraph;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.RefBusiOperation;
import ncmdp.util.TreeUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class BusiOperationSelectDilog extends TitleAreaDialog {
	List<String> initOpIDList = new ArrayList<String>();

	List<BusiOperation> resultOpList = new ArrayList<BusiOperation>();

	private boolean hasOK = false;

	private Tree tree = null;

	private BusiOperationTreeItem[] items = null;

	private List<BusiOperationTreeItem> curOperationTreeItem = new ArrayList<BusiOperationTreeItem>();

	private HashMap<String, TreeItem> hmParentItem = new HashMap<String, TreeItem>();

	private HashMap<String, BusiOperation> opItfMap = new HashMap<String, BusiOperation>();

	public BusiOperationSelectDilog(Shell shell, List<RefBusiOperation> initOpList) {
		super(shell);
		for (RefBusiOperation refOp : initOpList) {
			initOpIDList.add(refOp.getRealBusiOpid());
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("BusiOperation"); //$NON-NLS-1$
		setMessage("select"); //$NON-NLS-1$
		Composite toComposite = new Composite(parent, SWT.NONE);
		toComposite.setLayout(new GridLayout());
		toComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Text opText = new Text(toComposite, SWT.BORDER);
		opText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		opText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text t = (Text) e.widget;
				String str = t.getText();
				buildTree(str);
			}
		});

		Group group = new Group(toComposite, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText("busioperation"); //$NON-NLS-1$

		tree = new Tree(group, SWT.BORDER | SWT.CHECK);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//::TODO
				super.mouseDown(e);
			}
		});
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		items = getAllItems();
		buildTree(null);
		return toComposite;
	}

	private BusiOperationTreeItem[] getAllItems() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		JGraph currentGraph = editor.getModel();
		List<BusiOperation> opItfList = currentGraph.getAllToRefBusiOperation();
		List<BusiOperationTreeItem> itemList = new ArrayList<BusiOperationTreeItem>();
		if (opItfList != null && opItfList.size() > 0) {
			for (int i = 0; i < opItfList.size(); i++) {
				BusiOperationTreeItem opItem = new BusiOperationTreeItem(tree, opItfList.get(i));
				itemList.add(opItem);
			}
		}
		return itemList.toArray(new BusiOperationTreeItem[0]);
	}

	private void buildTree(String filter) {
		int count = tree.getItemCount();
		for (int i = 0; i < count; i++) {
			tree.getItem(0).dispose();
		}
		setTreeData(filter);
		TreeUtil.expandTree(tree);
		if (tree.getItemCount() > 0) {
			tree.setSelection(tree.getItem(0));
		} else {
			MessageDialog.openInformation(tree.getShell(), Messages.BusiOperationSelectDilog_3, Messages.BusiOperationSelectDilog_4);
		}
	}

	private void setTreeData(String filter) {
		hmParentItem.clear();
		curOperationTreeItem.clear();
		ArrayList<String> alID = new ArrayList<String>();
		int count = items == null ? 0 : items.length;
		for (int i = 0; i < count; i++) {
			BusiOperationTreeItem treeItem = items[i];
			if (treeItem.getBusiOperation() instanceof BusiOperation) {
				BusiOperation curOp = (BusiOperation) treeItem.getBusiOperation();
				if (alID.contains(curOp.getId())) {
					continue;
				} else {
					alID.add(curOp.getId());
				}
				if (filter != null && filter.trim().length() > 0) {
					if (curOp.getDisplayName().toLowerCase().indexOf(filter.toLowerCase()) == -1)
						continue;
				}
				//				String opItfId = curOp.getOpItfID();//curOp.getModuleName() + treeItem.getMdFilePath().replace('\\', '/');
				//				TreeItem parent = getParentTI(opItfId);
				BusiOperationTreeItem OPItem = new BusiOperationTreeItem(tree, treeItem.getBusiOperation());
				if (initOpIDList.contains(treeItem.getBusiOperation().getId())) {
					OPItem.setChecked(true);
				}
				curOperationTreeItem.add(OPItem);
			}
		}
	}

	private class BusiOperationTreeItem extends TreeItem {
		private BusiOperation operation;

		public BusiOperationTreeItem(Tree parent, Object operation) {
			super(parent, SWT.NONE);
			BusiOperation op = (BusiOperation) operation;
			setBusiOperation(op);
			setText(op.getDisplayName());
			setImage(ImageFactory.getOperationImg());
		}

		@Override
		protected void checkSubclass() {
		}

		public BusiOperation getBusiOperation() {
			return operation;
		}

		public void setBusiOperation(BusiOperation operation) {
			this.operation = operation;
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 600);
	}

	@Override
	protected void okPressed() {
		//		List<TreeItem> checkedItem = new ArrayList<TreeItem>();
		for (BusiOperationTreeItem opItem : curOperationTreeItem) {
			if (opItem.getChecked()) {
				//				checkedItem.add(opItem);
				//				RefBusiOperation refOperation = new RefBusiOperation();
				//				refOperation.setId(opItem.getBusiOperation().getId());
				//				refOperation.setRealBusiOpid(opItem.getBusiOperation().getId());
				resultOpList.add(opItem.getBusiOperation());
			}
		}
		super.okPressed();
		hasOK = true;
	}

	public boolean isOK() {
		return hasOK;
	}

	public List<BusiOperation> getResultOpList() {
		return resultOpList;
	}
}
