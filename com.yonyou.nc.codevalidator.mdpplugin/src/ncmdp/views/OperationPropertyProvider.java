package ncmdp.views;

import java.util.ArrayList;
import java.util.List;

import ncmdp.factory.ImageFactory;
import ncmdp.model.Constant;
import ncmdp.model.property.XMLAttribute;
import ncmdp.model.property.XMLElement;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class OperationPropertyProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider ,ITableColorProvider, ITableFontProvider{
	public static Font font = null;
	public OperationPropertyProvider() {
		super();
		Font f = Display.getCurrent().getSystemFont();
		FontData fd = f.getFontData()[0];
		font = new Font(f.getDevice(), fd.getName(), fd.getHeight(),SWT.BOLD);
	}

	public Image getColumnImage(Object element, int columnIndex) {
		if(columnIndex == 0){
			if(element instanceof XMLAttribute){
				return ImageFactory.getXMLAttrImg();
			}else if(element instanceof XMLElement){
				return ImageFactory.getXMLElement();
			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof XMLElement){
			XMLElement ele =(XMLElement)element; 
			switch(columnIndex){
			case 0:
				return ele.getElementType();
			case 1:
				return ele.getText();
			}
			
		}else if(element instanceof XMLAttribute){
			XMLAttribute attr = (XMLAttribute)element;
			switch(columnIndex){
			case 0:
				return attr.getKey();
			case 1:
				return attr.getValue();
			}
		}else if(element instanceof ArrayList){
			switch(columnIndex){
			case 0:
				return Messages.OperationPropertyProvider_0;
			case 1:
				return ""; //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof XMLElement){
			XMLElement ele =(XMLElement)parentElement; 
			ArrayList al = new ArrayList();
			al.addAll(ele.getAttributes());
			al.addAll(ele.getChilds());
			return al.toArray(new Object[0]);
			
		}else if(parentElement instanceof ArrayList){
			return ((ArrayList)parentElement).toArray(new Object[0]);
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		if( element instanceof ArrayList){
			return true;
		}else if(element instanceof XMLElement ){
			String type = ((XMLElement)element).getElementType();
			return !type.equals(Constant.XML_TYPE_REF) && !type.equals(Constant.XML_TYPE_VALUE);
		}else {
			return false;
		}
	}

	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
			return ((List)inputElement).toArray(new Object[0]);
		}else{
			return getChildren(inputElement);
		}
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	public Color getBackground(Object element, int columnIndex) {
		if(columnIndex == 1){
			if(OperationPropertyModifer.shouldModify(element)){
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}else{
				return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
			}
		}
		return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	}

	public Color getForeground(Object element, int columnIndex) {
		return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	}

	public Font getFont(Object element, int columnIndex) {
		if(columnIndex == 0 && element instanceof XMLElement){
			return font;
		}else{
			return Display.getCurrent().getSystemFont();
		}
	}

}
