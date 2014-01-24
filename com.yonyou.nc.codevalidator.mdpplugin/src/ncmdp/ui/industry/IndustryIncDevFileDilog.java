package ncmdp.ui.industry;

import java.io.File;

import ncmdp.common.MDPConstants;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 行业增量开发 文件 对话框
 * 
 * @author dingxm
 */
public class IndustryIncDevFileDilog extends TitleAreaDialog {
	private Text SourceBMFText;
	private Text IndustryText;
	private Text partnerCodeText;
	private Text versionCodeText;

	private String versiontype = ""; 
	private String programTag = ""; 
	private String industry = null;
	private File selectFile = null;
	IProject project = null;
	String[] fileTags = null;

	public String getVersiontype() {
		return versiontype;
	}

	public String getProgramTag() {
		return programTag;
	}

	public String getIndustry() {
		return industry;
	}

	public File getSelectFile() {
		return selectFile;
	}


	public IndustryIncDevFileDilog(Shell parentShell, IProject project,
			String[] fileTag) {
		super(parentShell);
		// , "新建增量开发实体组件", "请输入行业代码",
		this.project = project;
		this.fileTags = fileTag;
	}

	@Override
	protected void okPressed() {
		// / 如果不是基础领域 && 不是行业，需要指定开发代码
		if (!MDPConstants.BASE_VERSIONTYPE.equalsIgnoreCase(getVersiontype())
				&& !MDPConstants.INDUSTRY_VERSIONTYPE
						.equalsIgnoreCase(getVersiontype())) {
			if (StringUtil.isEmptyWithTrim(getProgramTag())) {
				MessageDialog.openError(getShell(), Messages.IndustryIncDevFileDilog_2,
						Messages.IndustryIncDevFileDilog_3 + getProgramTag());
				return;
			}
		}
		super.okPressed();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.IndustryIncDevFileDilog_4);
		setMessage("select info"); 
		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayout(new GridLayout(1, false));

		Composite topComp = new Composite(parent, SWT.NULL);
		topComp.setLayout(new GridLayout(3, false));
		// 文件框
		Label sourceMDLable = new Label(topComp, SWT.NONE);
		sourceMDLable.setText("MDFile："); 
		SourceBMFText = new Text(topComp, SWT.BORDER);
		GridData textData = new GridData(300, -1);
		SourceBMFText.setLayoutData(textData);
		// 来源元数据选择按钮
		Button fileButton = new Button(topComp, SWT.NULL);// PUSH|SWT.DOWN|SWT.BORDER);
		fileButton.setText("Source MDFile"); 

		fileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				SourceFileSelectDiolag dialog = new SourceFileSelectDiolag(
						getShell(), fileTags);
				int result = dialog.open();
				if (result == IDialogConstants.OK_ID) {
					selectFile = dialog.getSelectFile();
					SourceBMFText.setText(selectFile.getAbsolutePath());
				}
			}
		});
		GridData buttonData2 = new GridData(100, -1);
		buttonData2.horizontalSpan = 1;
		fileButton.setLayoutData(buttonData2);

		// 所属行业
		Label industryLable = new Label(topComp, SWT.NONE);
		industryLable.setText("Industry："); 
		IndustryText = new Text(topComp, SWT.BORDER);
		IndustryText.setLayoutData(textData);

		String curIndustry = MDPUtil.getDevCode();
		industry = curIndustry;
		IndustryText.setText(MDPUtil.getCurIndustryName());
		IndustryText.setEditable(false);
		IndustryText.setEnabled(false);
		GridData textData2 = new GridData(200, -1);
		textData2.horizontalSpan = 2;
		IndustryText.setLayoutData(textData2);

		// 开发维度
		Label partnerLable = new Label(topComp, SWT.NONE);
		partnerLable.setText("Assetlayer："); 

		// 开发维度text
		versionCodeText = new Text(topComp, SWT.BORDER);
		initVersionCodeText();
		GridData textData4 = new GridData(200, -1);
		textData4.horizontalSpan = 2;
		versionCodeText.setLayoutData(textData4);
		// 开发维度编码
		Label partnerCodeLable = new Label(topComp, SWT.NONE);
		partnerCodeLable.setText("developCode："); 
		// Button partnerBtn = new Button(topComp, SWT.CHECK);
		partnerCodeText = new Text(topComp, SWT.BORDER);
		initPartnerCodeText();

		return comp;
	}

	private void initPartnerCodeText() {
		GridData textData2 = new GridData(50, -1);
		partnerCodeText.setLayoutData(textData2);
		programTag = MDPUtil.getDevCode();
		partnerCodeText.setText(programTag);
		partnerCodeText.setEditable(false);
		partnerCodeText.setEnabled(false);
	}

	private void initVersionCodeText() {
		GridData textData2 = new GridData(50, -1);
		versionCodeText.setLayoutData(textData2);
		versiontype = MDPUtil.getDevVersionType();
		versionCodeText.setText(versiontype);
		versionCodeText.setEditable(false);
		versionCodeText.setEnabled(false);
	}
}
