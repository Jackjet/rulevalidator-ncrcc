package ncmdp.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import ncmdp.model.Cell;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

public class CellTreeEditPart extends AbstractTreeEditPart implements
		PropertyChangeListener {

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(Cell.PROP_ELEMENT_DISPLAY_NAME.equals(name)){
			refresh();
		}

	}

	@Override
	protected Image getImage() {
		return super.getImage();
	}

	@Override
	protected String getText() {
		Cell cell = (Cell)getModel();
		return cell.getDisplayName();
	}

	@Override
	public void activate() {
		super.activate();
		((Cell)getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((Cell)getModel()).removePropertyChangeListener(this);
	}

}
