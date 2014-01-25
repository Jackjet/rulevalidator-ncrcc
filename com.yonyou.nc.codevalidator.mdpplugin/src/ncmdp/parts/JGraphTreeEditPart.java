package ncmdp.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import ncmdp.model.JGraph;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

public class JGraphTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener{

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(JGraph.PROP_CHILD_ADD.equals(name)||JGraph.PROP_CHILD_REMOVE.equals(name)){
			refreshChildren();
		}
	}

	@Override
	public void activate() {
		super.activate();
		((JGraph)getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((JGraph)getModel()).removePropertyChangeListener(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelChildren() {
		return ((JGraph)getModel()).getCells();
	}

}
