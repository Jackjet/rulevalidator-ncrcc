package ncmdp.directedit;


import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Control;

public class CustomCellEditorLocator implements CellEditorLocator{
	private IFigure figure=null;
	public CustomCellEditorLocator(IFigure figure){
		super();
		this.figure = figure;
	}
	public void relocate(CellEditor cellEidtor) {
		Control control = (Control)cellEidtor.getControl();
		Rectangle rect = figure.getBounds().getCopy();
		figure.translateToAbsolute(rect);
		if(cellEidtor instanceof ComboBoxCellEditor){
			control.setBounds(rect.x,rect.y,rect.width,rect.height);
			
		}else{
			control.setBounds(rect.x,rect.y,rect.width,rect.height);
		}
		
	}
	
}