package ncmdp.exportmapping.wizard;

import java.util.ArrayList;
import java.util.List;

import ncmdp.exporttofea.wizard.FeaConfigTVLabelProvider;
import ncmdp.model.BusinInterface;
import ncmdp.model.ValueObject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MappingConfigPage extends WizardPage{

	private ValueObject model = null;
	private Table table = null;
	private CheckboxTableViewer cbtable = null;
	protected MappingConfigPage(String pageName,ValueObject model) {
		super(pageName);
		this.model = model;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		table = new Table(container, SWT.CHECK|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		TableLayout tl = new TableLayout();
		table.setLayout(tl);
		tl.addColumnData(new ColumnWeightData(20));	
		TableColumn choice = new TableColumn(table, SWT.BORDER);
		choice.setText("选择");
		tl.addColumnData(new ColumnWeightData(200));	
		TableColumn fea = new TableColumn(table, SWT.BORDER);
		fea.setText("业务接口属性映射");
		table.setHeaderVisible(true);
		cbtable = new CheckboxTableViewer(table);
		List<BusinInterface> busiIts = model.getBusiItfs();
		cbtable.setContentProvider(new ArrayContentProvider());
		cbtable.setLabelProvider(new FeaConfigTVLabelProvider());
		cbtable.setInput(busiIts);
		setControl(container);
	}

	public List<BusinInterface> getSelection(){
		Object[] os = cbtable.getCheckedElements();
		List<BusinInterface> exportAttrs = new ArrayList<BusinInterface>();
		if(os==null||os.length==0){
			return null;
		}
		for(Object o:os){
			if(o instanceof BusinInterface){
				exportAttrs.add((BusinInterface) o);
			}
		}
		return exportAttrs;
	}
}

