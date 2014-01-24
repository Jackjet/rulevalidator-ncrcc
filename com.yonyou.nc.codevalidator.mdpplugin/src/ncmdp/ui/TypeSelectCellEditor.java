package ncmdp.ui;

import ncmdp.model.Type;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 类型选择编辑器，用于类型字段的选择
 * @author wangxmn
 *
 */
public class TypeSelectCellEditor extends CellEditor {
	
    public TypeSelectCellEditor(Composite parent, Type[] items) {
        super(parent,SWT.NONE);
        ((TypeSeleComp)getControl()) .setItems(items);
        
    }
    public TypeSelectCellEditor(Composite parent, Type[] items, boolean showBtn) {
        super(parent,SWT.NONE);
        ((TypeSeleComp)getControl()) .setItems(items);
        if(!showBtn){
        	((TypeSeleComp)getControl()).setButnWidth(0);	
        }
    }

	@Override
	protected Control createControl(Composite parent) {
		final TypeSeleComp typeSelComp = new TypeSeleComp(parent,getStyle());
		typeSelComp.setFont(parent.getFont());
		typeSelComp.getCombo().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });
		typeSelComp.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE
                        || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });

		typeSelComp.getCombo().addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	if(!typeSelComp.isFocusControl())
            		TypeSelectCellEditor.this.focusLost();
            }
        });
		typeSelComp.getBtn().addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	if(!typeSelComp.isFocusControl())
            		TypeSelectCellEditor.this.focusLost();
            }
        });
        return typeSelComp;
	}

	@Override
	protected void doSetFocus() {
		TypeSeleComp typeSel = (TypeSeleComp)getControl();
		typeSel.doFocus();

	}
    protected void focusLost() {
        if (isActivated()) {
            applyEditorValueAndDeactivate();
        }
    }
	@Override
	protected Object doGetValue() {
		TypeSeleComp typeSel = (TypeSeleComp)getControl();
		return typeSel.getSelType();
	}
	@Override
	protected void doSetValue(Object value) {
		TypeSeleComp typeSel = (TypeSeleComp)getControl();
		typeSel.setSelType((Type)value);

	}
    void applyEditorValueAndDeactivate() {
        Object newValue = doGetValue();
        markDirty();
        boolean isValid = isCorrect(newValue);
        setValueValid(isValid);
        fireApplyEditorValue();
        deactivate();
    }
}
