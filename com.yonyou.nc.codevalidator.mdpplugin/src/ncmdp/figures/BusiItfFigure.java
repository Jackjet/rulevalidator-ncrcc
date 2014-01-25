package ncmdp.figures;

import java.util.HashMap;
import java.util.List;

import ncmdp.figures.ui.BusiItfAttrLabel;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import org.eclipse.swt.graphics.Color;
/**
 * 业务接口图形
 * @author wangxmn
 *
 */
public class BusiItfFigure extends CellFigure {
	private static Color bgColor = new Color(null, 225,172,175);
	private PartmentFigure attrPartmentFigure = null;
	private HashMap<BusiItfAttr, BusiItfAttrLabel> hmAttrToLabel = new HashMap<BusiItfAttr, BusiItfAttrLabel>();
	public BusiItfFigure(BusinInterface busiItf) {
		super(busiItf);
		setTypeLabText(Messages.BusiItfFigure_0);
		setBackgroundColor(bgColor);
		List<BusiItfAttr> list = busiItf.getBusiItAttrs();
		for (int i = 0; i < list.size(); i++) {
			addBusiItfAttr(list.get(i));
		}
		add(getAttrPartmentFigure());
	}
	private PartmentFigure getAttrPartmentFigure() {
		if(attrPartmentFigure == null){
			attrPartmentFigure = new PartmentFigure();
		}
		return attrPartmentFigure;
	}
	public void addBusiItfAttr(BusiItfAttr attr){
		BusiItfAttrLabel lab = new BusiItfAttrLabel(attr);
		hmAttrToLabel.put(attr, lab);
		getAttrPartmentFigure().add(lab);
	}
	public void removeBusiItfAttr(BusiItfAttr attr){
		BusiItfAttrLabel figure = hmAttrToLabel.remove(attr);
		getAttrPartmentFigure().remove(figure);
	}
	public void updateBusiItfAttr(BusiItfAttr attr){
		BusiItfAttrLabel figure = hmAttrToLabel.get(attr);
		figure.updateFigure(attr);
	}
}
