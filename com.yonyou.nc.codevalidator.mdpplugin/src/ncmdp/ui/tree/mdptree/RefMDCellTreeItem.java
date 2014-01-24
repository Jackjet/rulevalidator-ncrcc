package ncmdp.ui.tree.mdptree;

import ncmdp.factory.ImageFactory;
import ncmdp.model.BusinInterface;
import ncmdp.model.Cell;
import ncmdp.model.Entity;
import ncmdp.model.Enumerate;
import ncmdp.model.Service;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 引用类型，在资源管理树中显示以及refmodule中
 * @author wangxmn
 *
 */
public class RefMDCellTreeItem extends TreeItem {
	private String moduleName = null;
	private String mdFilePath = null;
	public String getMdFilePath() {
		return mdFilePath;
	}
	public void setMdFilePath(String mdFilePath) {
		this.mdFilePath = mdFilePath;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * 显示的图标
	 * @param parentItem
	 * @param cell
	 */
	public RefMDCellTreeItem(TreeItem parentItem,Cell cell) {
		super(parentItem, SWT.NONE);
		setText(cell.getDisplayName());
		setData(cell);
		if(cell instanceof Entity){
			setImage(ImageFactory.getEntityImg());
		}else if(cell instanceof ValueObject){
			setImage(ImageFactory.getValueObjectImg());
		}else if(cell instanceof OpInterface){
			setImage(ImageFactory.getOpItfImg());
		}else if(cell instanceof Enumerate){
			setImage(ImageFactory.getEnumImg());
		}else if(cell instanceof BusinInterface){
			setImage(ImageFactory.getBusiItfImg());
		}else if(cell instanceof Service){
			setImage(ImageFactory.getAbstractClassImg());
		}else if(cell instanceof BusiOperation){
			setImage(ImageFactory.getBusiOperationImg());
		}else if(cell instanceof BusiActivity){
			setImage(ImageFactory.getBusiActivityImg());
		}
	}
	protected void checkSubclass () {
	}
	public Cell getCell(){
		return (Cell)getData();
	}
	public void update(Cell cell){
		setData(cell);
		setText(cell.getDisplayName());
	}
}
