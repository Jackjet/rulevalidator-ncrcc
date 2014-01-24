package ncmdp.ruler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ncmdp.model.Cell;
import ncmdp.serialize.XMLSerialize;

public class Guide {
	public static final String PROP_CHILDREN = "CHILD MODEL CHANGED";
	public static final String PROP_POSITION = "POSITION CHANGED";
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private boolean isHorizontal = true;
	private Map<Cell, Integer> map = new HashMap<Cell, Integer>();
	
	private int position=-1;
	public Guide() {
		super();
	}
	
	public Guide(boolean isHorizontal) {
		super();
		this.isHorizontal = isHorizontal;
	}

	public void addPropChangeListener(PropertyChangeListener listener){
		listeners.addPropertyChangeListener(listener);
	}
	public void removePropChangeListener(PropertyChangeListener listener){
		listeners.removePropertyChangeListener(listener);
	}
	public boolean isHorizontal(){
		return isHorizontal;
	}
	public boolean isVertical(){
		return !isHorizontal;
	}
	public void setOrientation(boolean isHorizontal){
		this.isHorizontal = isHorizontal;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		if(this.position != position){
			int old = this.position;
			this.position = position;
			listeners.firePropertyChange(PROP_POSITION, Integer.valueOf(old), Integer.valueOf(this.position));
		}
	}
	public Integer getAlignment(Cell cell){
		return map.get(cell);
	}
	public void detachCell(Cell cell){
		if(map.containsKey(cell)){
			map.remove(cell);
			if(isHorizontal()){
				cell.setHorizontalGuide(null);
			}else{
				cell.setVerticalGuide(null);
			}
			listeners.firePropertyChange(PROP_CHILDREN, null, cell);
		}
	}
	public void attachCell(Cell cell, Integer alignment){
		if(map.containsKey(cell) && getAlignment(cell) == alignment){
			return;
		}
		map.put(cell, alignment);
		if(isHorizontal()){
			if(cell.getHorizontalGuide() != null && cell.getHorizontalGuide() != this){
				cell.getHorizontalGuide().detachCell(cell);
			}
			cell.setHorizontalGuide(this);
		}else{
			if(cell.getVerticalGuide() != null && cell.getVerticalGuide() != this){
				cell.getVerticalGuide().detachCell(cell);
			}
			cell.setVerticalGuide(this);
		}
		listeners.firePropertyChange(PROP_CHILDREN, null, cell);
	}
	public Set<Cell> getCells(){
		return map.keySet();
	}
	public Map<Cell, Integer> getMap(){
		return map;
	}
	public Element createElement(Document doc){
		Element guideEle = doc.createElement("guide");
		guideEle.setAttribute("isHorizontal", isHorizontal?"true":"false");
		guideEle.setAttribute("position", position+"");
		Iterator<Cell> cells = getCells().iterator();
		Element guideChilds=doc.createElement("guideChilds");
		guideEle.appendChild(guideChilds);
		while(cells.hasNext()){
			Cell cell = cells.next();
			Element child = doc.createElement("child");
			child.setAttribute("refId",cell.getId());
			child.setAttribute("alignment", map.get(cell).intValue()+"");
			guideChilds.appendChild(child);
		}
		return guideEle;
	}
	public static Guide parseNode(Node node){
		Guide guide = null;
		String tagName = node.getNodeName();
		if("guide".equalsIgnoreCase(tagName)){
			guide = new Guide();
			NamedNodeMap nodemap = node.getAttributes();
			if(nodemap.getNamedItem("isHorizontal") != null){
				guide.setOrientation("true".equalsIgnoreCase(nodemap.getNamedItem("isHorizontal").getNodeValue()));
			}
			if(nodemap.getNamedItem("position")!=null){
				guide.setPosition(Integer.parseInt(nodemap.getNamedItem("position").getNodeValue()));
			}
			NodeList nl = node.getChildNodes();
			int count = nl.getLength();
			for (int i = 0; i < count; i++) {
				Node childNode = nl.item(i);
				parseGuideChilds(childNode, guide);
			}
		}
		return guide;
	}
	private static void parseGuideChilds(Node node, Guide guide){
		String tagName = node.getNodeName();
		if("guideChilds".equalsIgnoreCase(tagName)){
			NodeList nl = node.getChildNodes();
			int count = nl.getLength();
			for (int i = 0; i < count; i++) {
				Node childNode = nl.item(i);
				if("child".equalsIgnoreCase(childNode.getNodeName())){
					NamedNodeMap nodemap = childNode.getAttributes();
					if(nodemap.getNamedItem("refId") != null && nodemap.getNamedItem("alignment") != null){
						String refId = nodemap.getNamedItem("refId").getNodeValue();
						Integer alignment = Integer.valueOf(nodemap.getNamedItem("alignment").getNodeValue());
						Cell cell = XMLSerialize.getInstance().getCell(refId);
						if(cell != null){
							guide.attachCell(cell, alignment);
						}
					}
				}
				
			}
			
		}
	}
}
