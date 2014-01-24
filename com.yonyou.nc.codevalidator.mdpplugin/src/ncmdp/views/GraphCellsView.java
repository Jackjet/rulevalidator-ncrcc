package ncmdp.views;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.parts.JGraphPart;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 组件图元视图
 * @author wangxmn
 *
 */
public class GraphCellsView extends Composite {
	private JGraphPart graphPart = null;
	private String[] colNames = {"","id",Messages.GraphCellsView_2,//id和名称
			Messages.GraphCellsView_3,Messages.GraphCellsView_4,//显示名称、创建者
			Messages.GraphCellsView_5,Messages.GraphCellsView_6,//创建日期，修改者
			Messages.GraphCellsView_7}; //修改日期
	private TreeViewer tv = null;//jface treeviewer
	
	public GraphCellsView(Composite parent, int style) {
		super(parent, style);
		initControl();
	}

	private void initControl() {
		setLayout(new FillLayout());
		ViewForm vf = new ViewForm(this, SWT.NONE);//布局基座
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < colNames.length; i++) {
			createColumn(tree, colNames[i], 80, SWT.LEFT, i);
		}
		GraphCellsViewProvider provider = new GraphCellsViewProvider();
		tv.setContentProvider(provider);
		tv.setLabelProvider(provider);
		tv.setColumnProperties(colNames);

		tree.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent e) {
				Tree tree = (Tree)e.widget;
				TreeItem[] tis = tree.getSelection();
				if(tis != null&&tis.length > 0){
					TreeItem ti = tis[0];
					if(ti.getData() instanceof Cell){
						Cell cell = (Cell)ti.getData();
						if(NCMDPEditor.getActiveMDPEditor() != null)
							NCMDPEditor.getActiveMDPEditor().setSelect(cell);
					}
				}
			}
			
		});
		vf.setContent(tv.getControl());//主体，表格
	}
	public TreeViewer getTreeViewer(){
		return tv;
	}
	
	private TreeColumn createColumn(Tree tree, String colName , int width, int align, int index){
		TreeColumn col = new TreeColumn(tree,SWT.NONE, index );
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		//test checkbox
//		Image image = null;
//		InputStream in = this.getClass().getResourceAsStream("/icons/unChecked.gif");
//		if(in == null){
//			ILog log = NCMDPActivator.getDefault().getLog();
//			log.log(new Status(Status.WARNING, NCMDPActivator.getPluginID(), 
//					"can't find the image"));
//		}else{
//			image = new Image(null, in);
//		}
//		col.setImage(image);
		//test checkbox
		return col;
	}

	public JGraphPart getGraphPart() {
		return graphPart;
	}

	public void setGraphPart(JGraphPart graphPart) {
		this.graphPart = graphPart;
	}
		
	

}
