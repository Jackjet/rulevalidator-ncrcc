package ncmdp.ruler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.rulers.RulerProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Ruler {
	public static final String PROP_GUIDS_CHANGE="GUIDS_CHANGED";
	public static final String PROP_UNIT_CHAGNGE="UNIT_CHANGE";
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private int unit = -1;
	private List<Guide> guides = new ArrayList<Guide>();
	private boolean isHorizontal = true;
	public Ruler(boolean isHorizontal) {
		this(isHorizontal,RulerProvider.UNIT_INCHES );
	}
	public Ruler(boolean isHorizontal, int unit){
		super();
		this.isHorizontal = isHorizontal;
//		this.unit = unit;
		setUnit(unit);
	}
	public boolean isHorizontal(){
		return isHorizontal;
	}
	public boolean isVertical(){
		return !isHorizontal;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		if(this.unit != unit){
			int old = this.unit;
			this.unit = unit;
			listeners.firePropertyChange(PROP_UNIT_CHAGNGE, old, this.unit);
		}
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
		listeners.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		listeners.removePropertyChangeListener(listener);
	}
	public List<Guide> getGuides(){
		return guides;
	}
	public void addGuide(Guide guide){
		if(!guides.contains(guide)){
			guide.setOrientation(!isHorizontal());
			guides.add(guide);
			listeners.firePropertyChange(PROP_GUIDS_CHANGE, null, guide);
		}
	}
	public void removeGuide(Guide guide){
		if(guides.remove(guide)){
			listeners.firePropertyChange(PROP_GUIDS_CHANGE, null, guide);
		}
	}
	public Element createElement(Document doc){
		Element ele = doc.createElement("ruler");
		ele.setAttribute("unit", getUnit()+"");
		ele.setAttribute("isHorizontal", isHorizontal?"true":"false");
		Element childEles = doc.createElement("guideList");
		ele.appendChild(childEles);
		int count = guides.size();
		for (int i = 0; i < count; i++) {
			Guide guide = guides.get(i);
			childEles.appendChild(guide.createElement(doc));
		}
		return ele;
	}
	public static Ruler parseNode(Node node){
		Ruler ruler = null;
		if("ruler".equalsIgnoreCase(node.getNodeName())){
			NamedNodeMap nodemap = node.getAttributes();
			if(nodemap.getNamedItem("isHorizontal")!=null){
				boolean horizontal = "true".equalsIgnoreCase(nodemap.getNamedItem("isHorizontal").getNodeValue());
				ruler = new Ruler(horizontal);
				if(nodemap.getNamedItem("unit")!=null){
					ruler.setUnit(Integer.parseInt(nodemap.getNamedItem("unit").getNodeValue()));
				}
				NodeList childs = node.getChildNodes();
				int count = childs.getLength();
				for (int i = 0; i < count; i++) {
					Node child = childs.item(i);
					parseGuides(child, ruler);
					
				}
			}
		}
		return ruler;
	}
	private static void parseGuides(Node node, Ruler ruler) {
		if("guideList".equalsIgnoreCase(node.getNodeName())){
			NodeList nl = node.getChildNodes();
			int count = nl.getLength();
			for (int i = 0; i < count; i++) {
				Node child = nl.item(i);
				Guide guide = Guide.parseNode(child);
				if(guide != null){
					ruler.addGuide(guide);
				}
				
			}
		}
		
	}
}
