package ncmdp.exporttofea.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
/**
 * 输入创建特性的显示名与id
 * @author wangxmn
 *
 */
public class FeatureNamePage extends WizardPage{

	private String moduleName = null;
	private Label moduleNameLabel = null;
	private Text moduleNameText = null;
	private Label propIdLabel = null;
	private Text propIdText = null;
	private Label propNameLabel = null;
	private Text propNameText = null;
	
	public FeatureNamePage(String pageName,String moduleName) {
		super(pageName);
		this.moduleName = moduleName;
	}

	public String getPropId() {
		return propIdText.getText().trim();
	}

	public String getPropName() {
		return propNameText.getText().trim();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		
		moduleNameLabel = new Label(container, SWT.None);
		moduleNameLabel.setText(Messages.moduleName);
		moduleNameText = new Text(container, SWT.READ_ONLY|SWT.BORDER);
		moduleNameText.setText(moduleName);
		moduleNameText.setLayoutData(createGridData(GridData.FILL_HORIZONTAL,
				1));
		
		propIdLabel = new Label(container, SWT.None);
		propIdLabel.setText(Messages.propId);
		propIdText = new Text(container, SWT.BORDER);
		propIdText.setText("");
		propIdText.setLayoutData(createGridData(GridData.FILL_HORIZONTAL,
				1));
		
		propNameLabel = new Label(container, SWT.None);
		propNameLabel.setText(Messages.porpName); 
		propNameText = new Text(container, SWT.BORDER);
		propNameText.setText("");
		propNameText.setLayoutData(createGridData(GridData.FILL_HORIZONTAL,
				1));
		
		setControl(container);
	}

	private GridData createGridData(int style,int horizontalSpan){
		GridData gridData = new GridData(style);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}
