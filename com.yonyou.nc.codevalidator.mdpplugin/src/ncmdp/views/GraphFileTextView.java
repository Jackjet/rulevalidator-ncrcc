package ncmdp.views;

import java.util.ResourceBundle;

import ncmdp.model.JGraph;
import ncmdp.serialize.JGraphSerializeTool;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.FindNextAction;
import org.eclipse.ui.texteditor.FindReplaceAction;

/**
 * 序列化文件的视图
 * @author wangxmn
 *
 */
public class GraphFileTextView extends Composite {
	private JGraph graph = null;

	private Text text = null;

	private IWorkbenchPart part = null;

	public GraphFileTextView(Composite parent, int style, IWorkbenchPart part) {
		super(parent, style | SWT.BORDER);
		this.part = part;
		initControl();
	}

	private void initControl() {
		setLayout(new FillLayout());
		text = new Text(this, SWT.H_SCROLL | SWT.V_SCROLL);

		text.setEditable(false);
		text.setBackground(ColorConstants.white);
		//鼠标右击的弹出pop按钮
		MenuManager mm = new MenuManager();
		
		ResourceBundle bundle = ResourceBundle.getBundle("ncmdp.views.ConstructedEditorMessages");
		final IAction findAction = new FindReplaceAction(bundle, "Editor.FindReplace.", part); //$NON-NLS-1$
		final IAction findnextAction = new FindNextAction(bundle,"Editor.FindNext.",part, true);
		final IAction findpreviousAction = new FindNextAction(bundle,"Editor.FindPrevious.",part, false);
		mm.add(findAction);
		mm.add(findnextAction);
		mm.add(findpreviousAction);
		Menu menu = mm.createContextMenu(text);
		text.setMenu(menu);
		
		text.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.keyCode=='f'&& e.stateMask==SWT.CONTROL){
					findAction.run();
				}else if(e.keyCode == SWT.F3 && e.stateMask==SWT.SHIFT){
					findpreviousAction.run();
				}else if(e.keyCode==SWT.F3){
					findnextAction.run();
				}
			}
		});
	}

	public Text getText() {
		return text;
	}

	protected void refreshText() {
		if (graph != null) {
			String str = JGraphSerializeTool.serializeToString(graph);
			text.setText(str);
		} else {
			text.setText("");
		}
	}

	public void setTextContent(JGraph graph) {
		this.graph = graph;
		Composite parent = getParent();
		if (parent instanceof TabFolder) {
			TabFolder folder = (TabFolder) parent;
			int i = folder.getSelectionIndex();
			if (i != -1 && this.equals(folder.getItem(i).getControl())) {
				refreshText();
			}
		}

	}
}
