package ncmdp.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncmdp.model.StereoType;
import ncmdp.serialize.StereoTypeConfig;
import ncmdp.tool.basic.StringUtil;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * 版型选择对话框，以支持多选
 * @author dingxm 2009-09-09
 */
public class StereoTypeSelectDialog extends TitleAreaDialog {
	Table table = null;

	Map<String, StereoType> allStereotypeMap = new HashMap<String, StereoType>();

	List<StereoType> selectedStereotypeList = new ArrayList<StereoType>();

	Shell parentShell = null;

	public StereoTypeSelectDialog(Shell parentShell, List<StereoType> stereoTypeList) {
		super(parentShell);
		this.parentShell = parentShell;
		allStereotypeMap.clear();
		for (StereoType type : StereoTypeConfig.getStereoTypes()) {
			allStereotypeMap.put(type.getName(), type);
		}
		selectedStereotypeList = stereoTypeList;
	}

	@Override
	protected void okPressed() {
		if (selectedStereotypeList == null) {
			selectedStereotypeList = new ArrayList<StereoType>();
		} else {
			selectedStereotypeList.clear();
		}
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getChecked()) {
				selectedStereotypeList.add(allStereotypeMap.get(items[i].getText(0)));
			}
		}
		super.okPressed();
	}

	public List<StereoType> getSelectedTypeList() {
		return selectedStereotypeList;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(476, 500);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("sterotype dialog");
		setMessage("please select the sterotype");
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayout(new FillLayout());
		group.setText("sterotype to selected");
		List<String[]> list = new ArrayList<String[]>();
		for (StereoType type : allStereotypeMap.values()) {
			if (type != null && !StringUtil.isEmptyWithTrim(type.getName())) {
				String[] tempString = new String[] { type.getName(), type.getDisplayName(),
						type.getListAccessors().toString() };
				list.add(tempString);
			}
		}
		createTable(group, SWT.BORDER, list.toArray(new String[][] {}));
		//设定选定的行
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			if (selectedStereotypeList != null && selectedStereotypeList.size() > 0) {
				for (StereoType type : selectedStereotypeList) {
					if (type.getName().equals(items[i].getText(0))) {
						items[i].setChecked(true);
					}
				}
			}
		}
		return container;
	}

	//Create the Table and TableColumns 
	protected Table createTable(Composite parent, int mode, Object[] contents) {
		if (table == null) {
			table = new Table(parent, mode | SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL
					| SWT.CHECK);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			createTableColumn(table, SWT.LEFT, "name", 100);
			createTableColumn(table, SWT.LEFT, "sterotype", 150);
			createTableColumn(table, SWT.LEFT, "access Accessor", 200);//访问策略
			addTableContents(contents);
		}
		return table;
	}

	protected TableColumn createTableColumn(Table table, int style, String title, int width) {
		TableColumn tc = new TableColumn(table, style);
		tc.setText(title);
		tc.setResizable(true);
		tc.setWidth(width);
		return tc;
	}

	protected void addTableContents(Object[] items) {
		for (int i = 0; i < items.length; i++) {
			String[] item = (String[]) items[i];
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(item);
		}
	}

}
