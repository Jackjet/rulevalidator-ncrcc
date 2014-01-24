package ncmdp.figures;

import ncmdp.model.Entity;

import org.eclipse.swt.graphics.Color;

/**
 * ����ʵ�壬��Ҫ�����ñ�����ɫ
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
