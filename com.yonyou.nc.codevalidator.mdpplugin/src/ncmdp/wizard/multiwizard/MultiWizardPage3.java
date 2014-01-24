package ncmdp.wizard.multiwizard;

import java.util.ArrayList;
import java.util.List;

import ncmdp.wizard.multiwizard.util.CommonUtil;
import ncmdp.wizard.multiwizard.util.IMultiElement;
import ncmdp.wizard.multiwizard.util.MultiUtils;
import ncmdp.wizard.multiwizard.util.UTFProperties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MultiWizardPage3 extends WizardPage {

	private TableViewer tableViewer = null;

	private String preResID;

	private PropsToContentConvert convert;

	// 多语元素
	private List<IMultiElement> multiAttrList;

	// 表格VO
	private List<MultiContentVO> sourceContentList = new ArrayList<MultiContentVO>();

	private LangPathVO defLangVO = null;

	String toFilePath = null;

	// 界面元素
	private Composite parent = null;

	private Composite topComp = null;

	/** 本次操作依赖的common模块 */
	List<String> depCommonModule = null;

	protected MultiWizardPage3(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		this.parent = parent;
		setTitle(Messages.MultiWizardPage3_0);
		topComp = new Composite(parent, SWT.BORDER);
		topComp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		topComp.setLayout(new FillLayout());
		setControl(topComp);
	}

	/**
	 * 通知页面3更新
	 * 
	 * @param fileDir
	 */
	public void updateTableData() {

		convert = new PropsToContentConvert(preResID, defLangVO,
				depCommonModule);
		//
		topComp.dispose();
		topComp = new Composite(parent, SWT.BORDER);
		topComp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		topComp.setLayout(new FillLayout());
		setControl(topComp);
		tableViewer = createTable(topComp, SWT.BORDER);
		topComp.layout();
		parent.layout();
		// 读取properies文件信息，匹配获取id
		sourceContentList = convert.getContents(multiAttrList);
		tableViewer.setInput(sourceContentList);
	}

	public void updateElements() {
		convert.updateElements(multiAttrList);
	}

	public String getFilePath() {
		return toFilePath;
	}

	private TableViewer createTable(Composite parent, int mode) {
		tableViewer = new TableViewer(parent, mode | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = tableViewer.getTable();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		layout.addColumnData(new ColumnWeightData(200));
		new TableColumn(table, SWT.NONE).setText(Messages.MultiWizardPage3_1);
		// 中文
		layout.addColumnData(new ColumnWeightData(200));
		new TableColumn(table, SWT.NONE).setText(defLangVO.getLangType() + ":" //$NON-NLS-1$
				+ defLangVO.getCharsetName());

		MultiTableHelper helper = new MultiTableHelper();
		tableViewer.setContentProvider(helper);
		tableViewer.setLabelProvider(helper);

		String[] columnProperties = new String[] { "resid", MultiUtils.SIMP_TAG }; //$NON-NLS-1$
		tableViewer.setColumnProperties(columnProperties);
		CellEditor[] cellEditors = new CellEditor[2];
		cellEditors[0] = new TextCellEditor(tableViewer.getTable());
		cellEditors[1] = new TextCellEditor(tableViewer.getTable());
		tableViewer.setCellEditors(cellEditors);
		tableViewer.setCellModifier(new MultiCellModifier(tableViewer,
				columnProperties));
		return tableViewer;
	}

	public List<IMultiElement> getMultiAttrList() {
		return multiAttrList;
	}

	public void setMultiAttrList(List<IMultiElement> multiAttrList) {
		this.multiAttrList = multiAttrList;
	}

	public void setPreResID(String preResID) {
		this.preResID = preResID;
	}

	public void setToFilePath(String toFilePath) {
		this.toFilePath = toFilePath;
		// String charSet = CommonUtil.getCurCharSet();
		defLangVO = new LangPathVO(toFilePath, MultiUtils.SIMP_TAG,
				CommonUtil.getCurCharSet()/* "GBK" */, true); //$NON-NLS-1$
	}

	public UTFProperties getUTFProperty() {
		return convert.getDefProp();
	}

	public void setDepCommonModule(List<String> depCommonModule) {
		this.depCommonModule = depCommonModule;
	}
}
