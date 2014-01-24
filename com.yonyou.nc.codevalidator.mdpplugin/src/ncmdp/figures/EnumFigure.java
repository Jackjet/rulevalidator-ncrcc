package ncmdp.figures;

import java.util.HashMap;
import java.util.List;

import ncmdp.figures.ui.EnumitemLabel;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;

import org.eclipse.swt.graphics.Color;
/**
 * 枚举类型的图形绘制
 * @author wangxmn
 *
 */
public class EnumFigure extends CellFigure {
	private static Color bgColor = new Color(null, 219, 229, 166);
	private HashMap<EnumItem, EnumitemLabel> hmEnumToLabel = new HashMap<EnumItem, EnumitemLabel>();

	private PartmentFigure itemsFigure = null;

	public EnumFigure(Enumerate enumerate) {
		super(enumerate);
		setTypeLabText(Messages.EnumFigure_0);
		setBackgroundColor(bgColor);
		add(getEnumItemsFigure());
		List<EnumItem> items = enumerate.getEnumItems();
		for (int i = 0; i < items.size(); i++) {
			EnumItem item = items.get(i);
			addEnumItem(item);
		}
	}

	private PartmentFigure getEnumItemsFigure() {
		if (itemsFigure == null) {
			itemsFigure = new PartmentFigure();
		}
		return itemsFigure;
	}

	public void addEnumItem(EnumItem item) {
		EnumitemLabel lab = new EnumitemLabel(item);
		hmEnumToLabel.put(item, lab);
		getEnumItemsFigure().add(lab);
	}

	public void removeEnumItem(EnumItem item) {
		EnumitemLabel figure = hmEnumToLabel.remove(item);
		getEnumItemsFigure().remove(figure);
	}

	public void updateEnumItem(EnumItem item) {
		EnumitemLabel figure = hmEnumToLabel.get(item);
		figure.updateFigure(item);
	}

}
