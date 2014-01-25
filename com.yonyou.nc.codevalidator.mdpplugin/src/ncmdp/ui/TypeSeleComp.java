package ncmdp.ui;

import ncmdp.model.Type;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
/**
 * 类型选择组件
 * @author wangxmn
 *
 */
public class TypeSeleComp extends Composite{
	private Button btn = null;
	private Type selType = null;
	private int btnWid = 20;
	private CCombo combo = null;
	private MyOpenTypeAction action = new MyOpenTypeAction();//选择连接的action
	
	private class MyListener implements Listener{
		private Composite com = null;
		public MyListener(Composite com) {
			super();
			this.com=com;
		}

		public void handleEvent(Event event) {
			if(com.equals(event.widget)){
				innerLayout(com);
			}else if(btn.equals(event.widget)){
				Event e = new Event ();
				notifyListeners (SWT.FocusIn, e);
			}
		}
	}
	private class MyOpenTypeAction extends Action{
		public MyOpenTypeAction() {
			super();
			setImageDescriptor(JavaPluginImages.DESC_TOOL_OPENTYPE);
		}
		public void run() {
			Shell parent= TypeSeleComp.this.getShell();
			TypeSelectDialog dialog= new TypeSelectDialog(parent);
			
			int result= dialog.open();
			if (result == IDialogConstants.OK_ID){
				Type type= dialog.getSelectedType();
				if (type != null ) {
					setSelType(type);
				}
			}
			combo.setFocus();
		}
	}
	
	public TypeSeleComp(Composite parent,int style) {
		super(parent, style);
		btn = new Button(this, SWT.NONE);
		btn.setImage(JavaPluginImages.DESC_TOOL_OPENTYPE.createImage());
		btn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				action.run();//点击打开选择界面
			}
		});
		combo = new CCombo(this, SWT.READ_ONLY);//只读combo
		
		combo.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				int index = combo.getSelectionIndex();
				Type type = (Type)combo.getData(index+"");
				if(type !=null)
					selType = type;
			}
		});
		
		/**
		 * 按住CTRL的同时按下R键
		 */
		combo.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				if(e.keyCode=='r'&& e.stateMask==SWT.CONTROL)
					action.run();
			}
		});
		
		Listener listener = new MyListener(this);
		addListener(SWT.Resize, listener);
	}
	public void setItems(Type[] types){
		if(combo == null)
			return;
		int count = types == null ? 0: types.length;
		String[] items = new String[count];
		for (int i = 0; i < count; i++) {
			items[i]=types[i].getDisplayName();
			combo.setData(i+"", types[i]); 
		}
		combo.setItems(items);
	}
	private void innerLayout(Composite comp){
		if(combo==null||btn==null)
			return;
		Rectangle rect = getClientArea();
		int w = rect.width;
		int h = rect.height;
		combo.setBounds(0, 0, w-btnWid, h);
		btn.setBounds(w-btnWid, 0, btnWid,h);
	}
	void setButnWidth(int width){
		btnWid = width;
	}

	public void doFocus(){
		if(combo != null){
			combo.setFocus();
		}
	}
	public boolean setFocus(){
		if(combo != null){
			return combo.setFocus();
		}else{
			return false;
		}
	}
	public Type getSelType() {
		return selType;
	}
	public void setSelType(Type selType) {
		this.selType = selType;
		if(combo != null){
			if(selType == null)
				combo.setText("");
			else
				combo.setText(selType.getDisplayName()==null?"":selType.getDisplayName());
		}
	}
	public CCombo getCombo() {
		return combo;
	}
	public boolean isFocusControl () {
		checkWidget();
		if (combo.isFocusControl()||btn.isFocusControl()) {
			return true;
		} 
		return super.isFocusControl ();
	}
	public Button getBtn() {
		return btn;
	}
}