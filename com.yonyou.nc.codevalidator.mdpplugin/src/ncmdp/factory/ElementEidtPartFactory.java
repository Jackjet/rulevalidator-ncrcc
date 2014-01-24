package ncmdp.factory;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.Connection;
import ncmdp.model.JGraph;
import ncmdp.parts.CellPart;
import ncmdp.parts.ConnectionPart;
import ncmdp.parts.JGraphPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
/**
 * ��������editPart��ncmapEditorʹ�ã���Ҫ��ʵ�塢�����Լ��հײ���
 * @author wangxmn
 *
 */
public class ElementEidtPartFactory implements EditPartFactory {
	private NCMDPEditor editor = null;
	public ElementEidtPartFactory(NCMDPEditor editor) {
		super();
		this.editor = editor;
	}
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if(model instanceof JGraph){
			editPart= new JGraphPart(editor);
		}else if(model instanceof Cell){
			editPart= new CellPart();
		}else if(model instanceof Connection){
			editPart= new ConnectionPart();
		}else{
			throw new RuntimeException("illegal param");
		}
		editPart.setModel(model);
		return editPart;
	}

}
