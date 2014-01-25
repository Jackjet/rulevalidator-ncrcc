package ncmdp.factory;

import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.parts.CellTreeEditPart;
import ncmdp.parts.JGraphTreeEditPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class ElementTreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if(model instanceof JGraph){
			editPart= new JGraphTreeEditPart();
		}else if(model instanceof Cell){
			editPart= new CellTreeEditPart();
		}else{
			throw new RuntimeException("illegal param");
		}
		editPart.setModel(model);
		return editPart;
	}

}
