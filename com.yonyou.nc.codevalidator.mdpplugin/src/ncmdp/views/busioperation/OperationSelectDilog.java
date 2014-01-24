package ncmdp.views.busioperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.factory.ImageFactory;
import ncmdp.model.JGraph;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.model.activity.RefOperation;
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

public class OperationSelectDilog extends TitleAreaDialog {

	List<String> initOpIDList = new ArrayList<String>();

	List<RefOperation> resultOpList = new ArrayList<RefOperation>();

	private Tree tree = null;

	private OperationTreeItem[] items = null;

	private HashMap<String, TreeItem> hmParentItem = new HashMap<String, TreeItem>();

	private HashMap<String, OpInterface> opItfMap = new HashMap<String, OpInterface>();

	private List<OperationTreeItem> curOperationTreeItem = new ArrayList<OperationTreeItem>();

	private boolean hasOK = false;

	public OperationSelectDilog(Shell shell, List<RefOperation> initOpList) {
		super(shell);
		for (RefOperation refOp : initOpList) {
			initOpIDList.add(refOp.getId());
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Operation"); //$NON-NLS-1$
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
		group.setText("operation"); //$NON-NLS-1$

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

	private OperationTreeItem[] getAllItems() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		//		JGraph realGraph = JGraphSerializeTool.loadFromFile(editor.getFile());
		//		List<OpInterface> opItfList = realGraph.getAllBusinessOperation();
		JGraph currentGraph = editor.getModel();
		List<OpInterface> opItfList = currentGraph.getAllOpInterface();
		List<OperationTreeItem> itemList = new ArrayList<OperationTreeItem>();
		if (opItfList != null && opItfList.size() > 0) {
			for (int i = 0; i < opItfList.size(); i++) {

				OpInterface opItf = opItfList.get(i);
				opItfMap.put(opItf.getId(), opItf);
				TreeItem opItfItem = hmParentItem.get(opItf.getId());
				if (opItfItem == null) {
					opItfItem = new TreeItem(tree, SWT.NONE);
					opItfItem.setData(opItf);
				}
				hmParentItem.put(opItf.getId(), opItfItem);
				List<Operation> opList = opItf.getOperations();
				for (Operation op : opList) {
					OperationTreeItem opItem = new OperationTreeItem(opItfItem, op);
					itemList.add(opItem);
				}
			}
		}
		return itemList.toArray(new OperationTreeItem[0]);
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
			MessageDialog.openInformation(tree.getShell(), Messages.OperationSelectDilog_3, Messages.OperationSelectDilog_4);
		}
	}

	private TreeItem getParentTI(String opItfID) {
		TreeItem ti = hmParentItem.get(opItfID);
		if (ti == null) {
			OpInterface opItf = opItfMap.get(opItfID);
			if (opItf == null) { return null; }
			ti = new TreeItem(tree, SWT.NONE);
			ti.setData(opItf);
			ti.setText(opItf.getDisplayName());
			ti.setImage(ImageFactory.getOpItfImg());
			hmParentItem.put(opItfID, ti);
		}
		return ti;
	}

	private void setTreeData(String filter) {
		hmParentItem.clear();
		curOperationTreeItem.clear();
		ArrayList<String> alID = new ArrayList<String>();
		int count = items == null ? 0 : items.length;
		for (int i = 0; i < count; i++) {
			OperationTreeItem treeItem = items[i];
			if (treeItem.getOperation() instanceof Operation) {
				Operation curOp = (Operation) treeItem.getOperation();
				if (alID.contains(curOp.getId())) {
					continue;
				} else {
					alID.add(curOp.getId());
				}
				if (filter != null && filter.trim().length() > 0) {
					if (curOp.getDisplayName().toLowerCase().indexOf(filter.toLowerCase()) == -1)
						continue;
				}
				String opItfId = curOp.getOpItfID();//curOp.getModuleName() + treeItem.getMdFilePath().replace('\\', '/');
				TreeItem parent = getParentTI(opItfId);
				OperationTreeItem OPItem = new OperationTreeItem(parent, treeItem.getOperation());
				if (initOpIDList.contains(treeItem.getOperation().getId())) {
					OPItem.setChecked(true);
				}
				curOperationTreeItem.add(OPItem);
			}
		}
	}

	private class OperationTreeItem extends TreeItem {
		private Operation operation;

		public OperationTreeItem(TreeItem parent, Object operation) {
			super(parent, SWT.NONE);
			Operation op = (Operation) operation;
			setOperation(op);
			setText(op.getDisplayName());
			setImage(ImageFactory.getOperationImg());
		}

		@Override
		protected void checkSubclass() {
		}

		public Operation getOperation() {
			return operation;
		}

		public void setOperation(Operation operation) {
			this.operation = operation;
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 600);
	}

	@Override
	protected void okPressed() {
		List<TreeItem> checkedItem = new ArrayList<TreeItem>();
		for (OperationTreeItem opItem : curOperationTreeItem) {
			if (opItem.getChecked()) {
				checkedItem.add(opItem);
				RefOperation refOperation = new RefOperation();
				refOperation.setId(opItem.getOperation().getId());
				refOperation.setOpItfID(opItem.getOperation().getOpItfID());
				resultOpList.add(refOperation);
			}
		}
		super.okPressed();
		hasOK = true;
	}

	public boolean isOK() {
		return hasOK;
	}

	public List<RefOperation> getResultOpList() {
		return resultOpList;
	}
}
