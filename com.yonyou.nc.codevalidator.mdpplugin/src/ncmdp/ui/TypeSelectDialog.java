package ncmdp.ui;

import java.util.ArrayList;
import java.util.HashMap;

import ncmdp.model.Cell;
import ncmdp.model.Entity;
import ncmdp.model.Enumerate;
import ncmdp.model.Type;
import ncmdp.model.ValueObject;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 选择类型的对话框
 * @author wangxmn
 *
 */
public class TypeSelectDialog extends TitleAreaDialog {
	private Type selType = null;
	private Tree tree = null;
	private TreeItem baseTypeRoot = null;
	private RefMDCellTreeItem[] items = null;
	private HashMap<String, TreeItem> hmParentItem = new HashMap<String, TreeItem>();
	
	public TypeSelectDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void okPressed() {
		TreeItem[] items = tree.getSelection();
		if(items != null && items.length > 0){
			TreeItem item = items[0];
			if(item instanceof TypeTreeItem){
				selType = ((TypeTreeItem)item).getType();
				super.okPressed();
			}else{
				MessageDialog.openConfirm(this.getShell(), Messages.TypeSelectDialog_3, Messages.TypeSelectDialog_4);
				tree.setFocus();
			}
		}
	}
	@Override
	protected Point getInitialSize() {
		return new Point(350,500); 
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX;
	}
	public Type getSelectedType(){
		return selType;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Text text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				Text t =(Text) e.widget;
				String str = t.getText();
				buildTree(str);
			}
		});
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayout(new FillLayout());
		group.setText(Messages.TypeSelectDialog_5);
		
		tree = new Tree(group,SWT.BORDER);
		tree.addMouseListener(new MouseAdapter(){
			public void mouseDoubleClick(MouseEvent e) {
				TreeItem[] items = tree.getSelection();
				if(items != null && items.length > 0){
					TreeItem item = items[0];
					if(item instanceof TypeTreeItem){
						okPressed();
					}
				}
			}
		});
		setTitle(Messages.TypeSelectDialog_6); 
		setMessage(Messages.TypeSelectDialog_7); 
//		items = MDPExplorerTreeView.getMDPExploerTreeView(null).getAllCellTreeItem();
		items = ProjectUtil.getAllCellTreeItem();
		buildTree(null);
		return container;
	}
	/**
	 * 构建类型树
	 * @param filter
	 */
	private void buildTree(String filter){
		int count = tree.getItemCount();
		for (int i = 0; i < count; i++) {
			tree.getItem(0).dispose();
		}
		setTreeData(filter);
		initBaseTypeTree(filter);
		expandTree(tree);
		tree.setSelection(tree.getItem(0));

	}
	private void initBaseTypeTree(String filter){
		baseTypeRoot = new TreeItem(tree, SWT.NONE);
		baseTypeRoot.setData(""); 
		baseTypeRoot.setText(Messages.TypeSelectDialog_9);
		Type[] baseTypes = NCMDPTool.getBaseTypes();
		int count = baseTypes == null ? 0 : baseTypes.length;
		for (int i = 0; i < count; i++) {
			if(filter!=null && filter.trim().length() > 0){
				if(baseTypes[i].getDisplayName().toLowerCase().indexOf(filter.toLowerCase()) == -1)
					continue;
			}
			new TypeTreeItem(baseTypeRoot, baseTypes[i]);//.setImage(ImageDescriptor.getMissingImageDescriptor().createImage());
		}
	}
	
	private TreeItem getParentTI(String path){
		TreeItem ti = hmParentItem.get(path);
		if(ti == null){
			int index = path.lastIndexOf("/"); 
			
			String tiName = ""; 
			if(index == -1){
					ti = new TreeItem(tree, SWT.NONE);
					tiName = path;
			}else{
				String parentStr = path.substring(0, index);
				TreeItem parent = getParentTI(parentStr);
				ti = new TreeItem(parent, SWT.NONE);
				tiName = path.substring(index+1);
			}
			ti.setText(tiName);
			hmParentItem.put(path, ti);
		}
		return ti;
	}
	/**
	 * 过滤树数据
	 * @param filter
	 */
	private void setTreeData(String filter){
		hmParentItem.clear();
		ArrayList<String> alID = new ArrayList<String>();
		int count = items == null ? 0 : items.length;
		for (int i = 0; i < count; i++) {
			RefMDCellTreeItem treeItem = items[i];
			Cell cell = treeItem.getCell();
			if( !(cell instanceof Entity ||cell instanceof ValueObject || cell instanceof Enumerate)){
				continue;
			}
			if(alID.contains(cell.getId())){
				continue;
			}else{
				alID.add(cell.getId());
			}
			
			if(filter!=null && filter.trim().length() > 0){
				if(cell.getDisplayName().toLowerCase().indexOf(filter.toLowerCase()) == -1)
					continue;
			}

			String path = treeItem.getModuleName()+treeItem.getMdFilePath().replace('\\', '/');
			TreeItem parent = getParentTI(path);
			new TypeTreeItem(parent, treeItem);
		
		}
		
	}
	private void expandTree(Tree tree){
		int count = tree.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem child = tree.getItem(i);
			expandTreeItem(child);
		}	
	}
	private void expandTreeItem(TreeItem ti){
		ti.setExpanded(true);
		int count = ti.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem child = ti.getItem(i);
			expandTreeItem(child);
		}
	}
	
	/**
	 * 应用在类型选择对话框中的树类型
	 * @author wangxmn
	 *
	 */
	private class TypeTreeItem extends TreeItem{
		public TypeTreeItem(TreeItem parent, RefMDCellTreeItem treeItem) {
			super(parent, SWT.NONE);
			Cell cell = treeItem.getCell();
			Type type = cell.converToType();
			setData(type);
			String text = type.getDisplayName();
			if (cell instanceof Entity) {
				text += Messages.TypeSelectDialog_0;
			} else if (cell instanceof ValueObject) {
				text += Messages.TypeSelectDialog_1;
			} else if (cell instanceof Enumerate) {
				text += Messages.TypeSelectDialog_2;
			}
			setText(text);
		}
		public TypeTreeItem(TreeItem parent, Type type) {
			super(parent, SWT.NONE);
			setData(type);
			setText(type.getDisplayName());
		}
		@Override
		protected void checkSubclass() {
		}
		public Type getType(){
			return (Type)getData();
		}
		
	}
	
}
