package ncmdp.views;

import java.util.ArrayList;
import java.util.HashMap;

import ncmdp.actions.AddCellPropAction;
import ncmdp.actions.AttrCopyAction;
import ncmdp.actions.AttrMoveButtomAction;
import ncmdp.actions.AttrMoveDownAction;
import ncmdp.actions.AttrMoveTopAction;
import ncmdp.actions.AttrMoveUpAction;
import ncmdp.actions.AttrPasteAction;
import ncmdp.actions.DelCellPropAction;
import ncmdp.model.Attribute;
import ncmdp.model.CanZhao;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.Type;
import ncmdp.model.ValueObject;
import ncmdp.parts.CellPart;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.ColSelectDlg.ColSelObj;
import ncmdp.ui.CustomCheckboxCellEditor;
import ncmdp.ui.KeyValueComboBoxCellEditor;
import ncmdp.ui.LowerCaseTextCellEditor;
import ncmdp.ui.MyComboboxCellEditor;
import ncmdp.ui.NumberTextCellEditor;
import ncmdp.ui.TypeSelectCellEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 实体属性编辑视图
 * @author wangxmn
 *
 */
public class CellPropertiesView extends Composite {
	private CellPart cellPart = null;

	private TreeViewer tv = null;

	private ViewForm vf = null;

	private MyComboboxCellEditor czCellEditor = null;

	//存放各列的编辑器
	private HashMap<String, CellEditor> hmEditor = new HashMap<String, CellEditor>();

	protected String[] propColNames = null;

	/**
	 * 显示列名
	 * @return
	 */
	protected String[] getDisplayColNames() {
		if (propColNames == null) {
			ArrayList<ColSelObj> al = NCMDPTool.getPropTableSelectedColNames();
			ArrayList<String> alColNames = new ArrayList<String>();
			for (int i = 0; i < al.size(); i++) {
				if (al.get(i).isSel()) {
					alColNames.add(al.get(i).getColName());
				}
			}
			propColNames = alColNames.toArray(new String[0]);
		}
		return propColNames;
	}

	public CellPropertiesView(Composite parent, int style) {
		super(parent, style);
		createPartControl(this);
	}

	/**
	 * 如果这条属性的type类型，是引用的一个实体，则去查找这个实体的参照
	 * 所以参照类型字段一定是与类型字段相关联
	 * @param attr
	 */
	public void updateCanzhaoCellEditor(Attribute attr) {
		Type type = attr.getType();
		Cell cell = null;
		if (type != null)
			cell = type.getCell();
		if (cell != null && cell instanceof ValueObject) {
			ValueObject vo = (ValueObject) cell;
			String defaultCanzhao = "";
			ArrayList<CanZhao> alCanzhao = vo.getAlCanzhao();
			String[] canzhaos = new String[alCanzhao.size()];
			for (int i = 0; i < canzhaos.length; i++) {
				canzhaos[i] = alCanzhao.get(i).getName();
				if (alCanzhao.get(i).isDefault())
					defaultCanzhao = canzhaos[i];
			}
			czCellEditor.setItems(canzhaos);
			if (attr.getRefModuleName() == null
					|| attr.getRefModuleName().trim().length() == 0)
				attr.setRefModuleName(defaultCanzhao);
		} else {
			attr.setRefModuleName("");
			czCellEditor.setItems(new String[] {});
		}
	}

	private void initCellEditor(final TreeViewer tv) {
		String[] colNames = CellModifier.colNames;
		Tree tree = tv.getTree();
		hmEditor.put("", null);
		//名称编辑
		hmEditor.put(colNames[1], new LowerCaseTextCellEditor(tree));
		//显示名称编辑
		hmEditor.put(colNames[2], new TextCellEditor(tree));//普通text编辑器
		//类型样式编辑
		hmEditor.put(colNames[3], new MyComboboxCellEditor(tree,
				Attribute.TYPE_STYLES, SWT.READ_ONLY));
		//类型编辑
		hmEditor.put(colNames[4],
				new TypeSelectCellEditor(tree, NCMDPTool.getBaseTypes()));
		//字段名称编辑
		hmEditor.put(colNames[5], new LowerCaseTextCellEditor(tree));
		//字段类型编辑
		hmEditor.put(colNames[6],
				new MyComboboxCellEditor(tree, NCMDPTool.getDBTypes()));// new
		//参照名称编辑更新
		hmEditor.put(colNames[7], new MyComboboxCellEditor(tree,
				new String[] {}, SWT.READ_ONLY) {
			@Override
			protected void doSetValue(Object value) {
				Tree tree = tv.getTree();
				TreeItem[] tis = tree.getSelection();
				if (tis != null && tis.length > 0) {
					TreeItem ti = tis[0];
					Attribute attr = (Attribute) ti.getData();
					updateCanzhaoCellEditor(attr);//更新参照
				}
				super.doSetValue(value);
			}
		});
		//参照名称编辑
		czCellEditor = (MyComboboxCellEditor) hmEditor.get(colNames[7]);
		//描述编辑
		hmEditor.put(colNames[8], new TextCellEditor(tree));//
		//隐藏编辑
		hmEditor.put(colNames[9], new CustomCheckboxCellEditor(tv));// new
		// 空编辑,判断该属性是否为空
		hmEditor.put(colNames[10], new CustomCheckboxCellEditor(tv));//
		//只读编辑，该属性是否只读
		hmEditor.put(colNames[11], new CustomCheckboxCellEditor(tv));// new
		// 访问策略
		hmEditor.put(colNames[12],
				new MyComboboxCellEditor(tree, NCMDPTool.getAttrAccessors()));// new
		//可视类型
		hmEditor.put(colNames[13], new MyComboboxCellEditor(tree,
				Constant.VISIBILITYS, SWT.READ_ONLY));
		//缺省值编辑
		hmEditor.put(colNames[14], new KeyValueComboBoxCellEditor(tv));// TextCellEditor(tree);
		//最大值
		hmEditor.put(colNames[15], new TextCellEditor(tree));
		//最小值
		hmEditor.put(colNames[16], new TextCellEditor(tree));
		//长度编辑
		hmEditor.put(colNames[17], new NumberTextCellEditor(tree));
		//精度编辑
		hmEditor.put(colNames[18], new NumberTextCellEditor(tree));
		//定长编辑
		hmEditor.put(colNames[19], new CustomCheckboxCellEditor(tv));
		//计算属性编辑
		hmEditor.put(colNames[20], new CustomCheckboxCellEditor(tv));
		//启用编辑
		hmEditor.put(colNames[21], new CustomCheckboxCellEditor(tv));
		//授权编辑，被隐藏了没有使用
		hmEditor.put(colNames[22], new CustomCheckboxCellEditor(tv));
		//多语资源id
		hmEditor.put(colNames[23], new TextCellEditor(tree));
		//扩展标签
		hmEditor.put(colNames[24], new TextCellEditor(tree));
		//不可序列化
		hmEditor.put(colNames[25], new CustomCheckboxCellEditor(tv));
		//动态属性
		hmEditor.put(colNames[26], new CustomCheckboxCellEditor(tv));
		//扩展表
		hmEditor.put(colNames[27], new TextCellEditor(tree));
		//使用权
		hmEditor.put(colNames[28], new CustomCheckboxCellEditor(tv));
		//使用权组
		hmEditor.put(colNames[29], new TextCellEditor(tree));
		//支持本地化
		hmEditor.put(colNames[30], new CustomCheckboxCellEditor(tv));
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		vf = new ViewForm(parent, SWT.NONE);
		vf.setLayout(new FillLayout());
		createTable();
	}

	public void createTable() {
		Object input = null;
		if (tv != null) {
			input = tv.getInput();
			propColNames = null;
		}
		tv = new TreeViewer(vf, SWT.H_SCROLL | SWT.MULTI | SWT.V_SCROLL
				| SWT.FULL_SELECTION);//支持多选SWT.MULTI
		Tree tree = tv.getTree();
		initCellEditor(tv);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		String[] displayColNames = getDisplayColNames();
		for (int i = 0; i < displayColNames.length; i++) {
			TreeColumn tc = createColumn(tree, displayColNames[i], 80,
					SWT.LEFT, i);
			if (i == 22) {// 隐藏掉“授权”
				tc.setWidth(0);
				tc.setResizable(false);
			}
		}

		CellPropertiesViewProvider provider = new CellPropertiesViewProvider(
				this);
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(displayColNames);
		
		CellEditor[] cellEditors = new CellEditor[displayColNames.length];
		for (int i = 0; i < cellEditors.length; i++) {
			cellEditors[i] = hmEditor.get(displayColNames[i]);
		}
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new CellModifier(this));
		
		tree.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				Tree tree = tv.getTree();
				TreeItem[] tis = tree.getSelection();
				if (tis != null && tis.length > 0) {
					TreeItem ti = tis[0];
					Object o = ti.getData();
					if (o instanceof Attribute) {
						NCMDPTool.showErrDlg(((Attribute) o).validate());
					}
				}
			}
		});
		tv.setInput(input);
		tv.expandAll();
		initAction(tree);
	}

	private TreeColumn createColumn(Tree tree, String colName, int width,
			int align, int index) {
		TreeColumn col = new TreeColumn(tree, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);

		col.setAlignment(align);
		return col;
	}

	/**
	 * 初始化各种action和鼠标键盘事件
	 * @param tree
	 */
	private void initAction(Tree tree){
		final Action addProp = new AddCellPropAction(this);
		TreeKeyImpl.setTreeKey(tv, addProp);//关联新增功能和鼠标键盘事件
		Action delProp = new DelCellPropAction(this);
		Action moveDown = new AttrMoveDownAction(this);
		Action moveUp = new AttrMoveUpAction(this);
		Action moveTop = new AttrMoveTopAction(this);
		Action moveBottom = new AttrMoveButtomAction(this);
		AttrCopyAction attrCopy = new AttrCopyAction(this);
		AttrPasteAction attrPaste = new AttrPasteAction(this);
		
		MenuManager mm = new MenuManager();
		mm.add(addProp);
		mm.add(delProp);
		mm.add(moveDown);
		mm.add(moveUp);
		mm.add(moveTop);
		mm.add(moveBottom);
		mm.add(new Separator());
		mm.add(attrCopy);
		mm.add(attrPaste);
		Menu menu = mm.createContextMenu(tree);
		tree.setMenu(menu);
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addProp);
		tbm.add(delProp);
		tbm.add(moveDown);
		tbm.add(moveUp);
		tbm.add(moveTop);
		tbm.add(moveBottom);
		tbm.update(true);
		vf.setContent(tv.getControl());
		vf.setTopLeft(tb);

		KeyListener keylistener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 'i' && e.stateMask == SWT.CONTROL) {
					addProp.run();
				}
			}
		};
		tree.addKeyListener(keylistener);
		CellEditor[] eidtors = hmEditor.values().toArray(new CellEditor[0]);
		for (int i = 0; i < eidtors.length; i++) {
			if (eidtors[i] == null)
				continue;
			Control contrl = eidtors[i].getControl();
			if (contrl != null) {
				contrl.addKeyListener(keylistener);
			}
		}
	}
	@Override
	public boolean setFocus() {
		return tv.getControl().setFocus();
	}

	public TreeViewer getTv() {
		return tv;
	}

	public CellPart getCellPart() {
		return cellPart;
	}

	public void setCellPart(CellPart cellPart) {
		this.cellPart = cellPart;
	}

	public CellEditor getCellEditorByColName(String colName) {
		String[] colNames = (String[]) tv.getColumnProperties();
		int count = colNames == null ? 0 : colNames.length;
		int index = -1;
		for (int i = 0; i < count; i++) {
			if (colNames[i].equals(colName)) {
				index = i;
				break;
			}
		}
		CellEditor ce = null;
		if (index != -1) {
			ce = tv.getCellEditors()[index];
		}
		return ce;
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
