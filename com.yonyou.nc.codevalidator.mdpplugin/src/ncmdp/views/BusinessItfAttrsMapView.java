package ncmdp.views;

import java.util.ArrayList;

import java.util.List;

import ncmdp.model.Attribute;



import ncmdp.model.ValueObject;
import ncmdp.parts.CellPart;
import ncmdp.ui.ObjectComboBoxCellEditor;

import org.eclipse.jface.viewers.CellEditor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
/**
 * 业务接口属性映射视图，只对于接口的实现有联系
 * @author wangxmn
 *
 */
public class BusinessItfAttrsMapView extends Composite {
	private CellPart cellPart = null;
	private TreeViewer tv = null;
	private ObjectComboBoxCellEditor ObjectEditor = null;
    private ObjectComboBoxCellEditor ObjectEditorExtend = null;

	public BusinessItfAttrsMapView(Composite parent, int style) {
		super(parent, style);
		createPartControl(this);
	}
	public TreeViewer getTreeViewer(){
		return tv;
	}
	public ValueObject getVauleObejct(){
		ValueObject vo = null;
		CellPart part=getCellPart();
		if(part != null && part.getModel() instanceof ValueObject){
			vo = (ValueObject)part.getModel();
		}
		return vo;
		
	}
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm vf = new ViewForm(parent, SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < BusinessItfAttrsMapModifier.colNames.length; i++) {
			createColumn(tree, BusinessItfAttrsMapModifier.colNames[i], 120, SWT.LEFT, i);
		}

		BusinessItfAttrsMapViewProvider provider = new BusinessItfAttrsMapViewProvider(this);
		tv.setContentProvider(provider);
		tv.setLabelProvider(provider);
		tv.setColumnProperties(BusinessItfAttrsMapModifier.colNames);
		CellEditor[] editors = new CellEditor[BusinessItfAttrsMapModifier.colNames.length];
		editors[0] = null;
		editors[1] = null;
		editors[2] = new ObjectComboBoxCellEditor(tree, null);
		editors[3] = new ObjectComboBoxCellEditor(tree, null);
		ObjectEditor =(ObjectComboBoxCellEditor) editors[2];
		ObjectEditorExtend = (ObjectComboBoxCellEditor) editors[3];
		tv.setCellEditors(editors);
		tv.setCellModifier(new BusinessItfAttrsMapModifier(this));
		vf.setContent(tv.getControl());
	}

	private TreeColumn createColumn(Tree tree, String colName , int width, int align, int index){
		TreeColumn col = new TreeColumn(tree,SWT.NONE, index );
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}
	@Override
	public boolean setFocus() {
		return tv.getTree().setFocus();
	}

	public CellPart getCellPart() {
		return cellPart;
	}

	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
		if(cellPart != null && cellPart.getModel() instanceof ValueObject){
			ValueObject vo = (ValueObject)cellPart.getModel();
			List<Attribute> list = vo.getProps();//获得实体的所有属性
			ArrayList<Attribute> al = new ArrayList<Attribute>();
			al.add(null);
			al.addAll(list);
			ObjectEditor.setObjectItems(al.toArray(new Attribute[0]));
		}else{
			ObjectEditor.setObjectItems(new Object[]{});
		}
	}
	
	/**yuyonga*/
	public void setObjectEditorValue(List<Attribute> list) {
		ArrayList<Attribute> al = new ArrayList<Attribute>();
		al.add(null);
		if(list != null){
		  al.addAll(list);
		}
		ObjectEditorExtend.setObjectItems(al.toArray(new Attribute[0]));
	}
}
