package ncmdp.figures;

import ncmdp.model.Entity;

import org.eclipse.swt.graphics.Color;

/**
 * 绘制实体，主要是设置背景颜色
 * @author wangxmn
 *
 */
public class EntityFigure extends ValueObjectFigure {

	private static final Color bgColor = new Color(null, 237, 226, 162);
	public EntityFigure(Entity entity) {
		super(entity);
		setTypeLabText(Messages.EntityFigure_0);
		setBackgroundColor(bgColor);
	}
}
