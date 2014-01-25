package ncmdp.figures;

import ncmdp.model.Service;

import org.eclipse.swt.graphics.Color;

public class ServiceFigure extends OpItfFigure {
	private static Color bgColor = new Color(null, 222,65,233);
	public ServiceFigure(Service service) {
		super(service);
		bgColor = new Color(null, 72,142,195);
		setTypeLabText(Messages.ServiceFigure_0);
		setBackgroundColor(bgColor);

	}

}
