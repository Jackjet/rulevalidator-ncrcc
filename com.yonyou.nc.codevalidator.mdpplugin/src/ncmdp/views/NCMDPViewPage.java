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

	//ģ����ͼ��ʵ�壩tab
	private TabFolder voTabFolder = null;

	//ģ����ͼ���tab
	private TabFolder OpItfTabFolder = null;

	//ģ����ͼ��ҵ��ӿ���ͼ
	private OperationView operationView = null;

	private OpItfImplView opItfImplView = null;

	private BusiOperationView busiOperationView = null;

	private BusiActivityView busiActivityView = null;

	private TabItem cellPropTabItem = null;

	private BusinessItfAttrsMapView businessItfAttrsMapView = null;

	//ʵ��tab������tab
	private CellPropertiesView cellPropertiesView = null;

	//	private OperationView operationView = null;

//	private OperationPropertyView operationPropertyView = null;

	private CanzhaoView canzhaoView = null;

	private BusinessInterfaceAttrsView businessInterfaceAttrsView = null;

	private EnumItemView enumItemView = null;

	private TabFolder graphTabFolder = null;

	//���ͼԪ����ͼ
	private GraphCellsView graphCellsView = null;

	//���л��ļ�����ͼ
	private GraphFileTextView graphFileTextView = null;

	public NCMDPViewPage() {
		super();
	}

	@Override
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		
		/********************ģ����ͼ��ʵ�壩tab��ʼ��*****************************/
		voTabFolder = new TabFolder(comp, SWT.NONE);

		TabItem prop = new TabItem(voTabFolder, SWT.NONE);
		prop.setText(Messages.NCMDPViewPage_0);//����
		cellPropTabItem = prop;
		cellPropertiesView = new CellPropertiesView(voTabFolder, SWT.NONE);
		prop.setControl(cellPropertiesView);

		TabItem mapping = new TabItem(voTabFolder, SWT.NONE);
		mapping.setText(Messages.NCMDPViewPage_1);//ҵ��ӿ�����ӳ��
		businessItfAttrsMapView = new BusinessItfAttrsMapView(voTabFolder, SWT.NONE);
		mapping.setControl(businessItfAttrsMapView);

		TabItem refer = new TabItem(voTabFolder, SWT.NONE);
		refer.setText(Messages.NCMDPViewPage_2);//����
		canzhaoView = new CanzhaoView(voTabFolder, SWT.NONE);
		refer.setControl(canzhaoView);
		
		/********************ģ����ͼ��ҵ��ӿڣ�tab��ʼ��*****************************/
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
		
		/********************ģ����ͼ�������tab��ʼ��*****************************/
		graphTabFolder = new TabFolder(comp, SWT.NONE);
		
		TabItem component = new TabItem(graphTabFolder, SWT.NONE);
		component.setText(Messages.NCMDPViewPage_5);//���ͼԪ
		graphCellsView = new GraphCellsView(graphTabFolder, SWT.NONE);
		component.setControl(graphCellsView);
		
		TabItem serifile = new TabItem(graphTabFolder, SWT.NONE);
		serifile.setText(Messages.NCMDPViewPage_6);//���л��ļ�
		graphFileTextView = new GraphFileTextView(graphTabFolder, SWT.NONE, getSite().getPage()
				.getActivePart());
		serifile.setControl(graphFileTextView);
		
		//��ɵ�������л��ļ�ʱ�ͻ���ʾ���е�����
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
	 * ���༭����ѡ�з����仯ʱ�ᴥ�����¼��������հ������ֵ��ʵ���
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof PropertySheet){//������Խű�����
			return;
		}
		//---------------������б�---------------//
		clearAllView();
		//---------------������б�---------------//
		String partName = Messages.NCMDPViewPage_7;//�༭��
		if (part == null || selection == null) {
			return;
		} else if (part instanceof NCMDPEditor) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if(sel==null){//��Ϊ�գ��򷵻�
				return;
			}
			if (sel instanceof CellPart) {//��Ϊ��ͨʵ��
				CellPart cellPart = (CellPart) sel;
				Object model = cellPart.getModel();
				if (model instanceof ValueObject) {
					//ʵ������
					ValueObject vo = (ValueObject) model;
					List<Attribute> props = vo.getProps();
					List<List<Attribute>> al = new ArrayList<List<Attribute>>();
					al.add(props);
					//����
					getCellPropertiesView().getTv().setInput(al);
					getCellPropertiesView().setCellPart(cellPart);
					getCellPropertiesView().getTv().expandAll();
					//ҵ��ӿ�ӳ��
					getBusinessItfAttrsMapView().setCellPart(cellPart);
					getBusinessItfAttrsMapView().getTreeViewer().setInput(vo.getBusiItfs());
					getBusinessItfAttrsMapView().getTreeViewer().expandAll();
					//��������
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
					//ģ����ͼ��ö�٣�
					Enumerate enumerate = (Enumerate) model;
					List<EnumItem> items = enumerate.getEnumItems();
					getEnumItemView().getEnumItemTableViewer().setInput(items);
					getEnumItemView().setCellPart(cellPart);
					sl.topControl = getEnumItemView();
					partName += Messages.NCMDPViewPage_10;
					comp.layout();
				} else if (model instanceof BusinInterface) {
					//ģ����ͼ��ҵ��ӿڣ�
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
				} else if (model instanceof OpInterface) {//ҵ��ӿ�
					OpInterface bo = (OpInterface) model;
					if (model instanceof Service) {
						/** ��ʱ�ò�������ʡ�Ե�**/
//						ArrayList<List<XMLElement>> al = new ArrayList<List<XMLElement>>();
//						al.add(bo.getAlPropertys());
//						getOperationPropertyView().getTreeViewer().setInput(al);
//						getOperationPropertyView().getTreeViewer().expandAll();
//						sl.topControl = getOperationPropertyView();//defaultComp;
//						partName += Messages.NCMDPViewPage_12;
					} else {
						//ģ����ͼ�������ӿڣ�
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
					//ģ����ͼ��ҵ�������
					BusiOperation busiOperation = (BusiOperation) model;
					getBusiOperationView().setCellPart(cellPart);
					getBusiOperationView().getTableView().setInput(busiOperation.getOperations());
					sl.topControl = getBusiOperationView();
					partName += Messages.NCMDPViewPage_14;
					comp.layout();
				} else if (model instanceof BusiActivity) {
					//ģ����ͼ��ҵ����
					BusiActivity busiActivity = (BusiActivity) model;
					getBusiActivityView().setCellPart(cellPart);
					getBusiActivityView().getTableView().setInput(busiActivity.getRefBusiOperations());
					sl.topControl = getBusiActivityView();
					partName += Messages.NCMDPViewPage_15;
					comp.layout();
				}
			} else if (sel instanceof JGraphPart) {
				//����հ���Ļ
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
				getGraphCellsView().getTreeViewer().expandAll();//չ�����нڵ�
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
	 * ���������ͼ�嵥
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
