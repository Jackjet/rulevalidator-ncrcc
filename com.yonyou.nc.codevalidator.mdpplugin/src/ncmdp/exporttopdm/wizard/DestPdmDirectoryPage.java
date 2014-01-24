package ncmdp.exporttopdm.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

/**
 * 选择生成pdm文件导入地址的界面
 * @author wangxmn
 *
 */
public class DestPdmDirectoryPage extends WizardPage {
	private Text destDirText = null;
	private String defaultPath = null;
	private Button openBtn = null;

	public DestPdmDirectoryPage(String pageName) {
		super(pageName);
	}

	public DestPdmDirectoryPage(String pageName, String defaultPath,String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.defaultPath = defaultPath;
	}

	/**
	 * 绘制界面
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		destDirText = new Text(container, SWT.BORDER);
		destDirText.setEnabled(false);
		destDirText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		destDirText.setText(defaultPath);
		openBtn = new Button(container, SWT.BORDER);
		openBtn.setText("...");
		openBtn.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBtnClicked(e);
			}
		});
		setControl(container);
	}
	/**
	 * 处理鼠标点击事件
	 * @param e
	 */
	protected void handleBtnClicked(SelectionEvent e) {
		FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
		fd.setFilterNames(new String[]{"PDM"});
		fd.setFilterExtensions(new String[]{"*.pdm"});
		fd.setText(destDirText.getText());
		String text = fd.open();
		if(text != null){
			destDirText.setText(text);
		}
	}
	public String getDestDirPath(){
		return destDirText.getText();
	}
}
