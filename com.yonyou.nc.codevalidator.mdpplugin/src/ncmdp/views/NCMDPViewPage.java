package ncmdp.views;

import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.model.CanZhao;
import ncmdp.model.Cell;
import ncmdp.model.Entity;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;
import ncmdp.model.JGraph;
import ncmdp.model.Note;
import ncmdp.model.Reference;
import ncmdp.model.Service;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.parts.CellPart;
import ncmdp.parts.JGraphPart;
import ncmdp.views.busiactivity.BusiActivityView;
import ncmdp.views.busioperation.BusiOperationView;
import ncmdp.views.opImpl.OpItfImplView;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.properties.PropertySheet;

public class NCMDPViewPage extends Page implements INCMDPViewPage {

	private Composite comp = null;

	private StackLayout sl = new StackLayout();

//	private Composite defaultComp = null;

	//模型视图（实体）tab
	private TabFolder voTabFolder = null;

	//模型视图组件tab
	private TabFolder OpItfTabFolder = null;

	//模型视图，业务接口视图
	private OperationView operationView = null;

	private OpItfImplView opItfImplView = null;

	private BusiOperationView busiOperationView = null;

	private BusiActivityView busiActivityView = null;

	private TabItem cellPropTabItem = null;

	private BusinessItfAttrsMapView businessItfAttrsMapView = null;

	//实体tab中属性tab
	private CellPropertiesView cellPropertiesView = null;

	//	private OperationView operationView = null;

//	private OperationPropertyView operationPropertyView = null;

	private CanzhaoView canzhaoView = null;

	private BusinessInterfaceAttrsView businessInterfaceAttrsView = null;

	private EnumItemView enumItemView = null;

	private TabFolder graphTabFolder = null;

	//组件图元表单视图
	private GraphCellsView graphCellsView = null;

	//序列化文件表单视图
	private GraphFileTextView graphFileTextView = null;

	public NCMDPViewPage() {
		super();
	}

	@Override
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		
		/********************模型视图（实体）tab初始化*****************************/
		voTabFolder = new TabFolder(comp, SWT.NONE);

		TabItem prop = new TabItem(voTabFolder, SWT.NONE);
		prop.setText(Messages.NCMDPViewPage_0);//属性
		cellPropTabItem = prop;
		cellPropertiesView = new CellPropertiesView(voTabFolder, SWT.NONE);
		prop.setControl(cellPropertiesView);

		TabItem mapping = new TabItem(voTabFolder, SWT.NONE);
		mapping.setText(Messages.NCMDPViewPage_1);//业务接口属性映射
		businessItfAttrsMapView = new BusinessItfAttrsMapView(voTabFolder, SWT.NONE);
		mapping.setControl(businessItfAttrsMapView);

		TabItem refer = new TabItem(voTabFolder, SWT.NONE);
		refer.setText(Messages.NCMDPViewPage_2);//参照
		canzhaoView = new CanzhaoView(voTabFolder, SWT.NONE);
		refer.setControl(canzhaoView);
		
		/********************模型视图（业务接口）tab初始化*****************************/
		OpItfTabFolder = new TabFolder(comp, SWT.NONE);
		
		TabItem oper = new TabItem(OpItfTabFolder, SWT.NONE);
		oper.setText(Messages.NCMDPViewPage_3);
		operationView = new OperationView(OpItfTabFolder, SWT.NONE);
		oper.setControl(operationView);
		
		TabItem opTi2 = new TabItem(OpItfTabFolder, SWT.NONE);
		opTi2.setText(Messages.NCMDPViewPage_4);
		opItfImplView = new OpItfImplView(OpItfTabFolder, SWT.NONE);
		opTi2.setControl(opItfImplView);
		
		busiOperationView = new BusiOperationView(comp, SWT.NONE);
		busiActivityView = new BusiActivityView(comp, SWT.NONE);
		businessInterfaceAttrsView = new BusinessInterfaceAttrsView(comp, SWT.NONE);
		enumItemView = new EnumItemView(comp, SWT.NONE);
		
		/********************模型视图（组件）tab初始化*****************************/
		graphTabFolder = new TabFolder(comp, SWT.NONE);
		
		TabItem component = new TabItem(graphTabFolder, SWT.NONE);
		component.setText(Messages.NCMDPViewPage_5);//组件图元
		graphCellsView = new GraphCellsView(graphTabFolder, SWT.NONE);
		component.setControl(graphCellsView);
		
		TabItem serifile = new TabItem(graphTabFolder, SWT.NONE);
		serifile.setText(Messages.NCMDPViewPage_6);//序列化文件
		graphFileTextView = new GraphFileTextView(graphTabFolder, SWT.NONE, getSite().getPage()
				.getActivePart());
		serifile.setControl(graphFileTextView);
		
		//完成当点击序列化文件时就会显示其中的内容
		graphTabFolder.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				TabFolder folder = (TabFolder) e.getSource();
				int i = folder.getSelectionIndex();
				if (i != -1 && graphFileTextView.equals(folder.getItem(i).getControl())) {
					graphFileTextView.refreshText();
				}
			}
		});
//		operationPropertyView = new OperationPropertyView(comp, SWT.NONE);
//		defaultComp = new Composite(comp, SWT.NONE);
//		defaultComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}

	@Override
	public Control getControl() {
		return this.comp;
	}

	@Override
	public void setFocus() {
		getControl().setFocus();
	}

	/**
	 * 当编辑区的选中发生变化时会触发该事件，如点击空白区，又点击实体等
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet){//点击属性脚本区域
			return;
		}
		//---------------清空所有表单---------------//
		clearAllView();
		//---------------清空所有表单---------------//
		String partName = Messages.NCMDPViewPage_7;//编辑区
		if (part == null || selection == null) {
			return;
		} else if (part instanceof NCMDPEditor) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if(sel==null){//若为空，则返回
				return;
			}
			if (sel instanceof CellPart) {//若为普通实体
				CellPart cellPart = (CellPart) sel;
				Object model = cellPart.getModel();
				if (model instanceof ValueObject) {
					//实体属性
					ValueObject vo = (ValueObject) model;
					List<Attribute> props = vo.getProps();
					List<List<Attribute>> al = new ArrayList<List<Attribute>>();
					al.add(props);
					//属性
					getCellPropertiesView().getTv().setInput(al);
					getCellPropertiesView().setCellPart(cellPart);
					getCellPropertiesView().getTv().expandAll();
					//业务接口映射
					getBusinessItfAttrsMapView().setCellPart(cellPart);
					getBusinessItfAttrsMapView().getTreeViewer().setInput(vo.getBusiItfs());
					getBusinessItfAttrsMapView().getTreeViewer().expandAll();
					//参照属性
					ArrayList<List<CanZhao>> canzhaoList = new ArrayList<List<CanZhao>>();
					canzhaoList.add(vo.getAlCanzhao());
					getCanzhaoView().setCellPart(cellPart);
					getCanzhaoView().getTreeViewer().setInput(canzhaoList);
					getCanzhaoView().getTreeViewer().expandAll();
					//
					if (sl.topControl == null || !sl.topControl.equals(voTabFolder)) {
						sl.topControl = voTabFolder;
						voTabFolder.setSelection(cellPropTabItem);
						if (model instanceof Entity) {
							partName += Messages.NCMDPViewPage_8;
						} else {
							partName += Messages.NCMDPViewPage_9;
						}
						comp.layout();
					}
				} else if (model instanceof Enumerate) {
					//模型视图（枚举）
					Enumerate enumerate = (Enumerate) model;
					List<EnumItem> items = enumerate.getEnumItems();
					getEnumItemView().getEnumItemTableViewer().setInput(items);
					getEnumItemView().setCellPart(cellPart);
					sl.topControl = getEnumItemView();
					partName += Messages.NCMDPViewPage_10;
					comp.layout();
				} else if (model instanceof BusinInterface) {
					//模型视图（业务接口）
					BusinInterface itf = (BusinInterface) model;
					List<BusiItfAttr> attrs = itf.getBusiItAttrs();
					List<List<BusiItfAttr>> al = new ArrayList<List<BusiItfAttr>>();
					al.add(attrs);
					getBusinessInterfaceAttrsView().getTreeViewer().setInput(al);
					getBusinessInterfaceAttrsView().setCellPart(cellPart);
					getBusinessInterfaceAttrsView().getTreeViewer().expandAll();
					sl.topControl = getBusinessInterfaceAttrsView();
					partName += Messages.NCMDPViewPage_11;
					comp.layout();
				} else if (model instanceof OpInterface) {//业务接口
					OpInterface bo = (OpInterface) model;
					if (model instanceof Service) {
						/** 暂时用不到，先省略掉**/
//						ArrayList<List<XMLElement>> al = new ArrayList<List<XMLElement>>();
//						al.add(bo.getAlPropertys());
//						getOperationPropertyView().getTreeViewer().setInput(al);
//						getOperationPropertyView().getTreeViewer().expandAll();
//						sl.topControl = getOperationPropertyView();//defaultComp;
//						partName += Messages.NCMDPViewPage_12;
					} else {
						//模型视图（操作接口）
						ArrayList<List<Operation>> alOpers = new ArrayList<List<Operation>>();
						alOpers.add(bo.getOperations());
						getOperationView().setCellPart(cellPart);
						getOperationView().getParamTableView().setInput(null);
						getOperationView().getTreeView().setInput(alOpers);
						getOperationView().getTreeView().expandAll();
						getOpImplView().setCellPart(cellPart);
						getOpImplView().getTableViewer().setInput(bo.getOpItfImpl());
						sl.topControl = getOpItfTabFolder();
						partName += Messages.NCMDPViewPage_13;
					}
					comp.layout();
				} else if (model instanceof BusiOperation) {
					//模型视图（业务操作）
					BusiOperation busiOperation = (BusiOperation) model;
					getBusiOperationView().setCellPart(cellPart);
					getBusiOperationView().getTableView().setInput(busiOperation.getOperations());
					sl.topControl = getBusiOperationView();
					partName += Messages.NCMDPViewPage_14;
					comp.layout();
				} else if (model instanceof BusiActivity) {
					//模型视图（业务活动）
					BusiActivity busiActivity = (BusiActivity) model;
					getBusiActivityView().setCellPart(cellPart);
					getBusiActivityView().getTableView().setInput(busiActivity.getRefBusiOperations());
					sl.topControl = getBusiActivityView();
					partName += Messages.NCMDPViewPage_15;
					comp.layout();
				}
			} else if (sel instanceof JGraphPart) {
				//点击空白屏幕
				JGraphPart graphPart = (JGraphPart) sel;
				JGraph graph = (JGraph) graphPart.getModel();
				List<Cell> cells = graph.getCells();
				List<Cell> list = new ArrayList<Cell>();
				for (int i = 0; i < cells.size(); i++) {
					Cell cell = cells.get(i);
					if (cell instanceof Note) {
						continue;
					} else if (cell instanceof Reference && ((Reference) cell).getReferencedCell() == null) {
						continue;
					} else {
						list.add(cell);
					}
				}
				ArrayList<List<Cell>> al = new ArrayList<List<Cell>>();
				al.add(list);
				getGraphCellsView().getTreeViewer().setInput(al);
				getGraphCellsView().getTreeViewer().expandAll();//展开所有节点
				getGraphCellsView().setGraphPart(graphPart);
				getGraphFileTextView().setTextContent(graph);
				sl.topControl = graphTabFolder;
				partName += Messages.NCMDPViewPage_16;
				comp.layout();
			}
		}
		if (NCMDPViewSheet.getNCMDPViewSheet() != null)
			NCMDPViewSheet.getNCMDPViewSheet().setPartName(partName);
	}

	private void clearAttributeView() {
		if (getCellPropertiesView() != null) {
			getCellPropertiesView().getTv().setInput(null);
			getCellPropertiesView().setCellPart(null);
		}
	}
	
	/**
	 * 清空所有视图清单
	 */
	private void clearAllView(){
		clearAttributeView();
		clearBusiItfView();
		clearEnumItemsView();
		clearBusiItfMapView();
		clearBusiOperationView();
		clearGraphCellsView();
		clearGraphFileTextView();
		clearCanzhaoView();
	}

	private void clearBusiItfView() {
		if (getBusinessInterfaceAttrsView() != null) {
			getBusinessInterfaceAttrsView().getTreeViewer().setInput(null);
			getBusinessInterfaceAttrsView().setCellPart(null);
		}
	}

	private void clearEnumItemsView() {
		if (getEnumItemView() != null) {
			getEnumItemView().getEnumItemTableViewer().setInput(null);
			getEnumItemView().setCellPart(null);

		}
	}

	private void clearBusiItfMapView() {
		if (getBusinessItfAttrsMapView() != null) {
			getBusinessItfAttrsMapView().getTreeViewer().setInput(null);
			getBusinessItfAttrsMapView().setCellPart(null);
		}
	}

	private void clearBusiOperationView() {
		// ArrayList al = new ArrayList();
		if (getOperationView() != null) {
			getOperationView().getParamTableView().setInput(null);
			getOperationView().getTreeView().setInput(null);
			getOperationView().setCellPart(null);

		}
	}

	private void clearGraphCellsView() {
		// ArrayList al = new ArrayList();
		if (getGraphCellsView() != null) {
			getGraphCellsView().getTreeViewer().setInput(null);
			getGraphCellsView().setGraphPart(null);

		}
	}

	private void clearGraphFileTextView() {
		if (getGraphFileTextView() != null) {
			getGraphFileTextView().setTextContent(null);
		}
	}

	private void clearCanzhaoView() {
		if (getCanzhaoView() != null) {
			getCanzhaoView().getTreeViewer().setInput(null);
			getCanzhaoView().setCellPart(null);
		}
	}

	public BusinessInterfaceAttrsView getBusinessInterfaceAttrsView() {
		return businessInterfaceAttrsView;
	}

	public BusinessItfAttrsMapView getBusinessItfAttrsMapView() {
		return businessItfAttrsMapView;
	}

	public CellPropertiesView getCellPropertiesView() {
		return cellPropertiesView;
	}

	public EnumItemView getEnumItemView() {
		return enumItemView;
	}

	public OperationView getOperationView() {
		return operationView;
	}

	public GraphCellsView getGraphCellsView() {
		return graphCellsView;
	}

	public GraphFileTextView getGraphFileTextView() {
		return graphFileTextView;
	}

	public CanzhaoView getCanzhaoView() {
		return canzhaoView;
	}

	public BusiOperationView getBusiOperationView() {
		return busiOperationView;
	}

	public OpItfImplView getOpImplView() {
		return opItfImplView;
	}

	public TabFolder getOpItfTabFolder() {
		return OpItfTabFolder;
	}

	public BusiActivityView getBusiActivityView() {
		return busiActivityView;
	}
}
