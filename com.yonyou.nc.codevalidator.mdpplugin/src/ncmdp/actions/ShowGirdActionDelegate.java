package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class ShowGirdActionDelegate implements IEditorActionDelegate {
	private NCMDPEditor editor = null;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof NCMDPEditor) {
			editor = (NCMDPEditor) targetEditor;
			GraphicalViewer diagramViewer = editor.getGV();
			Object obj = diagramViewer.getProperty(SnapToGrid.PROPERTY_GRID_VISIBLE);
			if (obj != null && obj instanceof Boolean) {
				action.setChecked(((Boolean) obj).booleanValue());
			} else {
				action.setChecked(false);
			}
		} else {
			editor = null;
		}
	}

	public void run(IAction action) {
		boolean val = action.isChecked();
		if (editor != null) {
			GraphicalViewer diagramViewer = editor.getGV();
			diagramViewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(val));
			diagramViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(val));
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
