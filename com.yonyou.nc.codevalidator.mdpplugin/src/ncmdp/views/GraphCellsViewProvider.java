package ncmdp.views;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.Cell;
import ncmdp.model.Reference;
import ncmdp.tool.NCMDPTool;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class GraphCellsViewProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider {

	public GraphCellsViewProvider() {
		super();
	}

	/**
	 * 显示图标
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * 处理setinput的内容
	 */
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof ArrayList && columnIndex == 0){
			return Messages.GraphCellsViewProvider_0;
		}else if(element instanceof Cell){
			Cell cell = (Cell)element;
			if(cell instanceof Reference)
				cell = ((Reference)cell).getReferencedCell();
			switch(columnIndex){
			case 1 : return cell.getId();
			case 2 : return cell.getName();
			case 3 : return cell.getDisplayName();
			case 4 : return cell.getCreator();
			case 5 : return NCMDPTool.formatDateString(cell.getCreateTime());
			case 6 : return cell.getModifier();
			case 7 : return NCMDPTool.formatDateString(cell.getModifyTime());
			}
		}
		return ""; 
	}

	@SuppressWarnings("rawtypes")
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof List){
			return ((List)parentElement).toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList)
			return true;
		else{
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		return ((List)inputElement).toArray();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
