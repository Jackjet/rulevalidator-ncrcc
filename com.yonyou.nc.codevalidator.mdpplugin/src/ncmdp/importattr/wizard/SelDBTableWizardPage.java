package ncmdp.importattr.wizard;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import ncmdp.pdmxml.Field;
import ncmdp.pdmxml.Table;
import ncmdp.serialize.DBConnProp;
import ncmdp.tool.ClassTool;
import ncmdp.tool.DBTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 从数据表中导入数据源界面
 * @author wangxmn
 *
 */
public class SelDBTableWizardPage extends WizardPage implements IImportTables {
	private CCombo driverCombo = null;

	private Text userText = null;

	private Text pwdText = null;

	private Text urlText = null;

	private DBConnProp[] dbProps = null;

	private HashMap<String, DBConnProp> driverMap = new HashMap<String, DBConnProp>();

	public SelDBTableWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		dbProps = NCMDPTool.getDBConnProps();
		for (int i = 0; i < dbProps.length; i++) {
			driverMap.put(dbProps[i].getDbType(), dbProps[i]);

		}
	}

	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		new Label(container, SWT.NONE).setText(Messages.SelDBTableWizardPage_0);
		driverCombo = new CCombo(container, SWT.BORDER);
		driverCombo.setEditable(false);
		driverCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		driverCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doDriverComboSelction();
			}

		});
		String[] items = driverMap.keySet().toArray(new String[0]);
		driverCombo.setItems(items);
		driverCombo.setText(items[0]);
		new Label(container, SWT.NONE).setText("URL"); //$NON-NLS-1$
		urlText = new Text(container, SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// urlText.setText("jdbc:sqlserver://127.0.0.1:1433;database=dbName;sendStringParametersAsUnicode=false");
		new Label(container, SWT.NONE).setText(Messages.SelDBTableWizardPage_2);
		userText = new Text(container, SWT.BORDER);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// userText.setText("sa");
		new Label(container, SWT.NONE).setText(Messages.SelDBTableWizardPage_3);
		pwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		pwdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		doDriverComboSelction();
		setControl(container);
	}

	private void doDriverComboSelction() {
		String text = driverCombo.getText();
		DBConnProp prop = driverMap.get(text);
		urlText.setText(getDefaultStr(text + "defaultURL", prop.getUrl())); //$NON-NLS-1$
		userText.setText(getDefaultStr(text + "defaultuserstr", prop.getUser())); //$NON-NLS-1$
		pwdText.setText(getDefaultStr(text + "defaultuserpwdstr", prop.getPassword())); //$NON-NLS-1$
		// if ("SQLServer".equalsIgnoreCase(text)) {
		// String url =
		// getDefaultStr("defaultURL","jdbc:sqlserver://127.0.0.1:1433;database=dbName;sendStringParametersAsUnicode=false");
		// urlText.setText(url);
		// userText.setText(getDefaultStr("defaultuserstr","sa"));
		// pwdText.setText(getDefaultStr("defaultuserpwdstr","sa"));
		// }
	}

	private String getDefaultStr(String strType, String defaultStr) {
		IStringVariableManager vvManager = VariablesPlugin.getDefault().getStringVariableManager();
		IValueVariable var = vvManager.getValueVariable(strType);
		String value = var == null ? "" : var.getValue(); //$NON-NLS-1$
		if (value == null || value.trim().length() == 0)
			value = defaultStr;
		return value;
	}

	private void putStr(String strType, String str) {
		IStringVariableManager vvManager = VariablesPlugin.getDefault().getStringVariableManager();
		IValueVariable var = vvManager.getValueVariable(strType);
		if (var == null) {
			var = vvManager.newValueVariable(strType, ""); //$NON-NLS-1$
			try {
				vvManager.addVariables(new IValueVariable[] { var });
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				MDPLogger.error(e.getMessage(), e);
			}
		}
		var.setValue(str);
	}

	@Override
	public IWizardPage getNextPage() {
		String text = driverCombo.getText();
		putStr(text + "defaultURL", urlText.getText()); //$NON-NLS-1$
		putStr(text + "defaultuserstr", userText.getText()); //$NON-NLS-1$
		putStr(text + "defaultuserpwdstr", pwdText.getText()); //$NON-NLS-1$
		return getWizard().getPage(SelTableColumnWizardPage.class.getCanonicalName());
	}

	public Connection getConnection() throws Exception {
		DBConnProp prop = driverMap.get(driverCombo.getText());
		String driverCls = prop.getDbDriver();
		String url = urlText.getText();
		String user = userText.getText();
		String pwd = pwdText.getText();
		Connection con = getDBConnection(driverCls, url, user, pwd);
		return con;
	}


	@SuppressWarnings("rawtypes")
	private Connection getDBConnection(String driverCls, String url, String user, String password) 
			throws Exception {
		Class cls = null;
		try {
			cls = Class.forName(driverCls);
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		}
		if (cls == null) {
			IProject[] projects = NCMDPTool.getOpenedJavaProjects();
			int count = projects == null ? 0 : projects.length;
			for (int i = 0; i < count; i++) {
				try {
					cls = ClassTool.loadClass(driverCls, projects[i]);
					if (cls != null)
						break;
				} catch (ClassNotFoundException e) {
					MDPLogger.error(e.getMessage(), e);
				}
			}
		}
		if (cls == null) {
			throw new ClassNotFoundException(driverCls);
		}
		Driver driver = (Driver) cls.newInstance();
		Connection con = null;
		Properties props = new Properties();
		props.put("user", user); 
		props.put(Messages.SelDBTableWizardPage_13, password);
		con = driver.connect(url, props);
		return con;
	}

	public Table[] getAllTables() {
		Table[] tables = new Table[0];
		Connection con = null;
		try {
			con = getConnection();
			tables = DBTool.getAllTables(con);
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "err", NCMDPTool.getExceptionRecursionError(e)); //$NON-NLS-1$
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					MDPLogger.error(e.getMessage(), e);
				}
			}
		}
		return tables;

	}

	public ArrayList<Field> getFields(Table table) {
		ArrayList<Field> al = table.getFields();
		if(al == null){
			Connection con = null;
			try {
				con = getConnection();
				Field[] fields = DBTool.getTableColumns(con, table.getName());
				table.addField(fields);
				al = table.getFields();
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "err", NCMDPTool.getExceptionRecursionError(e)); //$NON-NLS-1$
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						MDPLogger.error(e.getMessage(), e);
					}
				}
			}

		}
		return al;
	}
}
