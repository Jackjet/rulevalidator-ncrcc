package ncmdp.wizard.multiwizard;

import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.tool.basic.StringUtil;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class MultiCellModifier implements ICellModifier {
	String[] columnProiprtties = null;

	//	new String[] { "resid", "simpSource", "tradSource","engSource" };

	TableViewer tableViewer = null;

	List<MultiContentVO> voList = new ArrayList<MultiContentVO>();

	public MultiCellModifier(TableViewer tableViewer, String[] columnProperties) {
		this.tableViewer = tableViewer;
		this.columnProiprtties = columnProperties;
	}

	@Override
	public boolean canModify(Object element, String property) {
		if (property.equals(columnProiprtties[0])) { return false; }
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		MultiContentVO content = (MultiContentVO) element;
		if (property.equals(columnProiprtties[0])) {
			return content.getResid();
		} else {
			return StringUtil.isEmptyWithTrim(content.getValue()) ? "" : content.getValue();
		}
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TableItem[] items = tableViewer.getTable().getItems();
		voList.clear();
		for (TableItem item : items) {
			voList.add((MultiContentVO) item.getData());
		}
		if (value == null || StringUtil.isEmptyWithTrim(value.toString())) { return; }
		TableItem item = (TableItem) element;
		MultiContentVO content = (MultiContentVO) item.getData();
		String oldValue = "";
		if (property.equals(columnProiprtties[0])) {
			oldValue = content.getResid();
			content.setResid((String) value);
			return;
		} else {
			oldValue = content.getValue();
			content.getValue();
		}
		tableViewer.update(content, null);
		if (!value.equals(oldValue)) {
			NCMDPEditor.getActiveMDPEditor().setDirty(true);
		}
	}
}
