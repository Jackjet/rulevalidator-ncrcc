package ncmdp.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TableUtil {
	public Table creatTable(Composite parent, int[] modeArray, Object[] contents) {
		int mode = SWT.BORDER;
		for (int i = 0; i < modeArray.length; i++) {
			mode = mode | modeArray[i];
		}
		Table table = new Table(parent, mode);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createTableColumn(table, SWT.LEFT, "name", 100);
		createTableColumn(table, SWT.LEFT, "_sterotype", 150);
		createTableColumn(table, SWT.LEFT, "_accessor", 200);
		addTableContents(contents, table);
		return table;
	}

	protected TableColumn createTableColumn(Table table, int style, String title, int width) {
		TableColumn tc = new TableColumn(table, style);
		tc.setText(title);
		tc.setResizable(true);
		tc.setWidth(width);
		return tc;
	}

	protected void addTableContents(Object[] items, Table table) {
		for (int i = 0; i < items.length; i++) {
			String[] item = (String[]) items[i];
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(item);
		}
	}
}
