package ncmdp.ruler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;

public class EditorRulerProvider extends RulerProvider {
	
	private class GuideListener implements PropertyChangeListener{
		public void propertyChange(PropertyChangeEvent evt) {
			String propName = evt.getPropertyName();
			if(Guide.PROP_CHILDREN.equals(propName)){
				notifyPartAttachmentChanged(evt.getNewValue(), evt.getSource());
			}else if(Guide.PROP_POSITION.equals(propName)){
				notifyGuideMoved(evt.getSource());
			}
		}
		
	}
	private class RulerListener implements PropertyChangeListener{
		public void propertyChange(PropertyChangeEvent evt) {
			String propName = evt.getPropertyName();
			if(Ruler.PROP_GUIDS_CHANGE.equals(propName)){
				Guide guide =(Guide) evt.getNewValue();
				if(ruler.getGuides().contains(guide)){
					guide.addPropChangeListener(guideListener);
				}else{
					guide.removePropChangeListener(guideListener);
				}
				notifyGuideReparented(guide);
			}else if(Ruler.PROP_UNIT_CHAGNGE.equals(propName)){
				Integer integer = (Integer)evt.getNewValue();
				notifyUnitsChanged(integer.intValue());
			}
		}
		
	}
	private void notifyGuideReparented(Object guide){
		for (int i = 0; i < listeners.size(); i++) {
			((RulerChangeListener)listeners.get(i)).notifyGuideReparented(guide);
		}
	}
	private void notifyUnitsChanged(int unit){
		for (int i = 0; i < listeners.size(); i++) {
			((RulerChangeListener)listeners.get(i)).notifyUnitsChanged(unit);
		}
	}
	private void notifyGuideMoved(Object guide){
		for (int i = 0; i < listeners.size(); i++) {
			((RulerChangeListener)listeners.get(i)).notifyGuideMoved(guide);
		}
	}
	private void notifyPartAttachmentChanged(Object cell, Object guid){
		for (int i = 0; i < listeners.size(); i++) {
			((RulerChangeListener)listeners.get(i)).notifyPartAttachmentChanged(cell, guid);
		}
	}
	
	
	
	private GuideListener guideListener = new GuideListener();
	private RulerListener rulerListener = new RulerListener();
	private Ruler ruler = null;
	
	public EditorRulerProvider(Ruler ruler) {
		super();
		this.ruler = ruler;
		this.ruler.addPropertyChangeListener(rulerListener);
		List<Guide> guides = this.ruler.getGuides();
		for (int i = 0; i < guides.size(); i++) {
			guides.get(i).addPropChangeListener(guideListener);
		}
	}
	@Override
	public List getAttachedModelObjects(Object guide) {
		return new ArrayList(((Guide)guide).getCells());
	}

	@Override
	public int getGuidePosition(Object guide) {
		return ((Guide)guide).getPosition();
	}
	@Override
	public int[] getGuidePositions() {
		List<Guide> guides = ruler.getGuides();
		int[] posis = new int[guides.size()];
		for (int i = 0; i < posis.length; i++) {
			posis[i] = guides.get(i).getPosition();
		}
		return posis;
	}
	@Override
	public List getGuides() {
		return ruler.getGuides();
	}
	

	@Override
	public Object getRuler() {
		return ruler;
	}
	@Override
	public int getUnit() {
		return ruler.getUnit();
	}
	@Override
	public void setUnit(int unit) {
		ruler.setUnit(unit);
	}
	@Override
	public Command getCreateGuideCommand(int position) {
		return new CreateGuideCommand(ruler, position);
	}
	@Override
	public Command getDeleteGuideCommand(Object guide) {
		return new DeleteGuideCommand((Guide)guide, ruler);
	}
	@Override
	public Command getMoveGuideCommand(Object guide, int delta) {
	
		return new MoveGuideCommand((Guide)guide, delta);
	}

}
