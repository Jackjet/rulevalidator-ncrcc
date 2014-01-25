package ncmdp.ui.industry;

import ncmdp.common.MDPConstants;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * 新建实体弹出框
 * @author wangxmn
 *
 */
public class IndustryNewFileDilog extends TitleAreaDialog {
	private Text fileNameText;
	private Text IndustryText;
	private Text partnerCodeText;
	private Text versionCodeText;
	private Industry industry = null;
	private String versiontype = ""; 
	private String fileName = ""; 
	private String programCode = ""; 

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public String getVersiontype() {
		return versiontype;
	}

	public String getProgramCode() {
		return programCode;
	}

	public Industry getIndustry() {
		return industry;
	}

	public IndustryNewFileDilog(Shell parentShell, IProject project) {
		super(parentShell);
	}
	
	public IndustryNewFileDilog(Shell parentShell){
		super(parentShell);
	}

	/**
	 * 点击确定，新建实体
	 */
	@Override
	protected void okPressed() {
		if(MDPUtil.getExtSuffix()==null){
			return;
		}
		// 文件名非空
		if (StringUtil.isEmptyWithTrim(getFileName())) {
			MessageDialog.openError(getShell(), Messages.IndustryNewFileDilog_3, Messages.IndustryNewFileDilog_4);
			return;
		}
		// 如果不是基础领域开发，则需要加上后缀
		if (!MDPConstants.BASE_VERSIONTYPE.equalsIgnoreCase(getVersiontype())) {
			String suffix = MDPUtil.getExtSuffix();
			if (!getFileName().endsWith(suffix)) {
				setFileName(getFileName() + suffix);
			}
		}
		// 如果不是基础领域 && 不是行业，需要指定开发代码
		if (!MDPConstants.BASE_VERSIONTYPE.equalsIgnoreCase(getVersiontype())
				&& !MDPConstants.INDUSTRY_VERSIONTYPE
						.equalsIgnoreCase(getVersiontype())) {
			if (StringUtil.isEmptyWithTrim(getProgramCode())
					|| getProgramCode().length() > 2) {
				MessageDialog.openError(getShell(), Messages.IndustryNewFileDilog_6, Messages.IndustryNewFileDilog_7);
				return;
			}
		}
		super.okPressed();
	}

	/**
	 * 创建界面
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.IndustryNewFileDilog_8);
		setMessage(Messages.IndustryNewFileDilog_9);
		Composite topComp = new Composite(parent, SWT.NULL);
		topComp.setLayout(new GridLayout(3, false));
		// 文件名
		GridData textData = new GridData(200, -1);
		textData.horizontalSpan = 2;
		Label fileNameLable = new Label(topComp, SWT.NONE);
		fileNameLable.setText(Messages.IndustryNewFileDilog_11); 
		fileNameText = new Text(topComp, SWT.BORDER);
		fileNameText.setLayoutData(textData);
		fileNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fileName = (String) fileNameText.getText();
			}
		});
		
		// 所属行业，标签
		Label industryLable = new Label(topComp, SWT.NONE);
		industryLable.setText(Messages.IndustryNewFileDilog_10); 
		IndustryText = new Text(topComp, SWT.BORDER);
		GridData textData3 = new GridData(200, -1);
		textData3.horizontalSpan = 2;
		IndustryText.setLayoutData(textData3);
		// 所属行业，内容
		String curIndustry = MDPUtil.getDevCode();//回去调用黄斌的方法，导致弹出开发者信息提示
		industry = new Industry(curIndustry, MDPUtil.getCurIndustryName());
		IndustryText.setText(MDPUtil.getCurIndustryName());
		IndustryText.setEditable(false);
		IndustryText.setEnabled(false);
		
		// 开发维度 ，标签
		Label partnerLable = new Label(topComp, SWT.NONE);
		partnerLable.setText(Messages.IndustryNewFileDilog_12);
		// 开发维度text
		versionCodeText = new Text(topComp, SWT.BORDER);
		initVersionCodeText();
		GridData textData4 = new GridData(200, -1);
		textData4.horizontalSpan = 2;
		versionCodeText.setLayoutData(textData4);

		// 开发组织编码
		Label partnerCodeLable = new Label(topComp, SWT.NONE);
		partnerCodeLable.setText(Messages.IndustryNewFileDilog_13);
		// 开发组织编码文本框
		partnerCodeText = new Text(topComp, SWT.BORDER);
		initPartnerCodeText();

		return topComp;
	}

	/**
	 * 获得开发维度信息
	 */
	private void initVersionCodeText() {
		GridData textData2 = new GridData(50, -1);
		versionCodeText.setLayoutData(textData2);
		versiontype = MDPUtil.getDevVersionType();//开发者纬度
		versionCodeText.setText(versiontype);
		versionCodeText.setEditable(false);
		versionCodeText.setEnabled(false);
	}

	/**
	 * 获得开发组织编码
	 */
	private void initPartnerCodeText() {
		GridData textData2 = new GridData(50, -1);
		partnerCodeText.setLayoutData(textData2);
		programCode = MDPUtil.getDevCode();
		partnerCodeText.setText(programCode);
		partnerCodeText.setEditable(false);
		partnerCodeText.setEnabled(false);
	}
}
