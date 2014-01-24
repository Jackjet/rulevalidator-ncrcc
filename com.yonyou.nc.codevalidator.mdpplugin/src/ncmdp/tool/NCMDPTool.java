package ncmdp.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import ncmdp.NCMDPActivator;
import ncmdp.factory.ImageFactory;
import ncmdp.model.Accessor;
import ncmdp.model.Attribute;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;
import ncmdp.model.JGraph;
import ncmdp.model.StereoType;
import ncmdp.model.Type;
import ncmdp.preferencepage.GenSqlsSetPage;
import ncmdp.project.MDFileEditInput;
import ncmdp.project.MDPExplorerTreeBuilder;
import ncmdp.serialize.AccessorConfig;
import ncmdp.serialize.BaseTypeConfig;
import ncmdp.serialize.DBConnProp;
import ncmdp.serialize.DBConnPropCofig;
import ncmdp.serialize.DBTypesConfig;
import ncmdp.serialize.StereoTypeConfig;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.ColSelectDlg;
import ncmdp.ui.ColSelectDlg.ColSelObj;
import ncmdp.ui.KeyValueComboBox.KeyValue;
import ncmdp.ui.KeyValueComboBoxCellEditor;
import ncmdp.ui.tree.mdptree.RefMDCellTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.ProjectUtil;
import ncmdp.views.CellModifier;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class NCMDPTool {
	
	private static boolean isSupportGDI = getSupportGDI();
	private static final String FIELD_NC_HOME = "FIELD_NC_HOME";
	private static final String FIELD_EX_MODULES = "FIELD_EX_MODULES";
	private static final String IS_SET_LOCATOR = "IS_SET_LOCATOR";
	private static final String Cell_PROPTABLE_COL_SET = "Cell_PROPTABLE_COL_SET";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Type[] baseTypes = BaseTypeConfig.loadBaseTypes();
	private static String[] dbTypes = DBTypesConfig.loadDBTypesConfigs();
	private static DBConnProp[] dbConnProps = null;
	private static String[] attrAccessors = null;//访问策略
	private static Type[] enumTypes = null;//枚举类型
	
	public static Integer getVisibilityIndex(String visibility) {
		for (int i = 0; i < Constant.VISIBILITYS.length; i++) {
			if (Constant.VISIBILITYS[i].equals(visibility)) {
				return Integer.valueOf(i);
			}
		}
		return Integer.valueOf(0);
	}

	public static boolean isSupportGDI() {
		return isSupportGDI;
	}
	
	private static boolean getSupportGDI(){
//		try {
//			System.loadLibrary("gdiplus");
//			return true;
//		} catch (Throwable e) {
//			MDPLogger.error(e.getMessage(),e);
//			return false;
//		}
		return false;
	}

	/**
	 * 产生唯一标识符
	 * @return
	 */
	public static String generateID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 转换时间格式
	 * @param date
	 * @return
	 */
	public static String formatDateString(Date date) {
		if (date == null) {
			return "";
		}
		String dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * 解析字符串为时间
	 * @param dateStr
	 * @return
	 */
	public static Date parseStringToDate(String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0)
			return null;
		Date d = null;
		try {
			d = sdf.parse(dateStr);
		} catch (ParseException e) {
			MDPLogger.error(e.getMessage(),e);
		}
		return d;
	}

	/**
	 * 获得工作空间目录路径
	 * @return
	 */
	public static String getWrokspaceDirPath() {
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		String strPath = path.toOSString();
		return strPath;
	}

	/**
	 * 获得当年所有的java程序
	 * @return
	 */
	public static IProject[] getJavaProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		return projects;
	}

	/**
	 * 获得当前打开的项目，项目必须是打开的，not close
	 * @return
	 */
	public static IProject[] getOpenedJavaProjects() {
		IProject[] allProjects = getJavaProjects();
		ArrayList<IProject> al = new ArrayList<IProject>();
		int count = allProjects == null ? 0 : allProjects.length;
		for (int i = 0; i < count; i++) {
			if (allProjects[i].isOpen()) {
//				MDPLogger.info("项目："+allProjects[i]+"已打开并加载");
				al.add(allProjects[i]);
			}
		}
		return al.toArray(new IProject[0]);
	}

	/**
	 * 删除文件
	 * @param f
	 */
	public static void deleteFile(File f) {
		File[] childs = f.listFiles();
		int count = childs == null ? 0 : childs.length;
		for (int i = 0; i < count; i++) {
			deleteFile(childs[i]);
		}
		f.delete();
	}

	/**
	 * 打开错误对话框
	 * @param errMsg
	 */
	public static void showErrDlg(String errMsg) {
		if (errMsg != null) {
			Shell parent = new Shell(Display.getCurrent());
			MessageDialog.openError(parent, "错误提示", errMsg);
		}
	}

	//定位？
	public static boolean isSetLocator() {
		IStringVariableManager vvManager = VariablesPlugin.getDefault()
				.getStringVariableManager();
		IValueVariable isSetLocator = vvManager
				.getValueVariable(IS_SET_LOCATOR);
		if (isSetLocator == null) {
			isSetLocator = vvManager.newValueVariable(IS_SET_LOCATOR, "false");
			try {
				vvManager.addVariables(new IValueVariable[] { isSetLocator });
			} catch (CoreException e) {
				e.printStackTrace();
			}

		}
		String value = isSetLocator.getValue();
		return value != null && value.equals("true");
	}

	//定位？
	public static void setLocator(boolean locator) {
		IStringVariableManager vvManager = VariablesPlugin.getDefault()
				.getStringVariableManager();
		IValueVariable isSetLocator = vvManager
				.getValueVariable(IS_SET_LOCATOR);
		if (isSetLocator == null) {
			isSetLocator = vvManager.newValueVariable(IS_SET_LOCATOR, "false");
			try {
				vvManager.addVariables(new IValueVariable[] { isSetLocator });
			} catch (CoreException e) {
			MDPLogger.error(e.getMessage(), e);
			}
		}
		isSetLocator.setValue(locator ? "true" : "false");
	}

	/**
	 * 获得NCHome路径
	 * @return
	 */
	public static String getNCHome() {
		IStringVariableManager vvManager = VariablesPlugin.getDefault()
				.getStringVariableManager();
		IValueVariable ncHome = vvManager.getValueVariable(FIELD_NC_HOME);
		String ncHomeStr = ncHome == null ? "" : ncHome.getValue();
		return ncHomeStr;
	}

	/**
	 * 获得模块名称
	 * @return
	 */
	public static String[] getExModuleNames() {
		IStringVariableManager vvManager = VariablesPlugin.getDefault()
				.getStringVariableManager();
		IValueVariable var = vvManager.getValueVariable(FIELD_EX_MODULES);
		String moduleStr = var == null ? "" : var.getValue();
		String[] exModuleNames = moduleStr.split(",");
		return exModuleNames;
	}

	/**
	 * 获得本项目的模块名，通过读取module_prj配置文件
	 * @param project
	 * @return
	 */
	public static String getProjectModuleName(IProject project) {
		String module = null;
		File f = project.getLocation().toFile();
		File moduleFile = new File(f, ".module_prj");
		InputStream is = null;
		if (moduleFile.exists()) {
			try {
				is = new FileInputStream(moduleFile);
				Properties prop = new Properties();
				prop.load(is);
				module = prop.getProperty("module.name");
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			}finally{
				if(is!=null){
					try {
						is.close();
					} catch (IOException e) {}
				}
			}
		}
		return module;
	}

	/**
	 * 获得基础类型
	 * @return
	 */
	public static Type[] getBaseTypes() {
		return baseTypes;
	}

	/**
	 * 获得存入数据库后的属性的基本类型，如UFID、Integer类型等
	 * @return
	 */
	public static String[] getDBTypes() {
		return dbTypes;
	}

	/**
	 * 纯粹是为了应付静态代码检查，其实这里根本用不到同步
	 * @return
	 */
	public static synchronized DBConnProp[] getDBConnProps() {
		if (dbConnProps == null) {
			dbConnProps = DBConnPropCofig.loadDBConnProps();
			if (dbConnProps == null || dbConnProps.length == 0) {
				dbConnProps = new DBConnProp[1];
				dbConnProps[0] = new DBConnProp();
				dbConnProps[0].setDbType("SQLSERVER");
				dbConnProps[0]
						.setDbDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				dbConnProps[0]
						.setUrl("jdbc:sqlserver://127.0.0.1:1433;database=dbName;sendStringParametersAsUnicode=false");
				dbConnProps[0].setUser("sa");
			}
		}
		return dbConnProps;
	}

	/**
	 * 获得基本的枚举类型，目前只有String和Integer类型
	 * @return
	 */
	public static synchronized Type[] getBaseEnumTypes() {
		if (enumTypes == null) {
			Type[] types = getBaseTypes();
			ArrayList<Type> al = new ArrayList<Type>();
			int count = types == null ? 0 : types.length;
			for (int i = 0; i < count; i++) {
				if ("Integer".equalsIgnoreCase(types[i].getDisplayName())
						|| "String".equalsIgnoreCase(types[i].getDisplayName())) {
					al.add(types[i]);
				}
			}
			enumTypes = al.toArray(new Type[0]);
		}
		return enumTypes;
	}

	/**
	 * 根据类型ID，获得当前的类型
	 * @param typeId
	 * @return
	 */
	public static Type getBaseTypeById(String typeId) {
		Type[] types = getBaseTypes();
		Type type = null;
		int count = types == null ? 0 : types.length;
		for (int i = 0; i < count; i++) {
			if (types[i].getTypeId().equals(typeId)) {
				type = types[i];
				break;
			}
		}
		return type;

	}

	public static String getCellFullClassName(JGraph graph, Cell cell) {
		String nameSpace = graph.getNameSpace();
		String componentName = "";
		if (graph != null) {
			nameSpace = graph.getNameSpace();
			componentName = graph.getName();
		}
		String cellName = cell.getName();
		StringBuilder sb = new StringBuilder("nc.vo");
		if (nameSpace != null && nameSpace.trim().length() > 0) {
			sb.append(".").append(nameSpace.toLowerCase());
		}
		sb.append(".").append(componentName.toLowerCase()).append(".")
				.append(cellName);
		return sb.toString();
	}

	public static String getDefaultFieldType(String typeID) {
		String fType = "";
		Type[] types = getBaseTypes();
		int count = types == null ? 0 : types.length;
		for (int i = 0; i < count; i++) {
			if (types[i].getTypeId().equals(typeID)) {
				fType = types[i].getDbType();
				break;
			}
		}
		return fType;
	}

	/**
	 * 打开某个视图
	 * @param viewId
	 * @return
	 */
	public static IViewPart showView(String viewId) {
		IViewPart part = null;
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			if (page != null){
				part = page.findView(viewId);
			}
			if(part == null){
				part = page.showView(viewId, null, IWorkbenchPage.VIEW_VISIBLE);
			}
		} catch (PartInitException e) {
			MDPLogger.error(e.getMessage(), e);
		}
		return part;
	}

	public static void setDefaultValueScope(Type type) {
		RefMDCellTreeItem[] items = ProjectUtil.getAllCellTreeItem();
		int count = items == null ? 0 : items.length;
		boolean find = false;
		CellEditor ce = NCMDPViewSheet.getNCMDPViewPage()
				.getCellPropertiesView().getCellEditorByColName("缺省值");
		if (ce instanceof KeyValueComboBoxCellEditor) {
			KeyValueComboBoxCellEditor editor = (KeyValueComboBoxCellEditor) ce;
			for (int i = 0; i < count; i++) {
				Cell cell = items[i].getCell();
				if (cell instanceof Enumerate
						&& cell.getId().equals(type.getTypeId())) {
					find = true;
					Enumerate enu = (Enumerate) cell;
					KeyValue[] values = new KeyValue[enu.getEnumItems().size()];
					for (int j = 0; j < values.length; j++) {
						EnumItem ei = enu.getEnumItems().get(j);
						values[j] = new KeyValue(ei.getEnumDisplay(),
								ei.getEnumValue());
					}
					editor.setItems(values);
				}
			}
			if (!find) {
				KeyValue[] values = new KeyValue[1];
				values[0] = new KeyValue("", "");
				editor.setItems(values);
			}
		}
	}

	public static StereoType getStereoTypeByName(String name) {
		StereoType st = null;
		StereoType[] sts = StereoTypeConfig.getStereoTypes();
		int count = sts == null ? 0 : sts.length;
		for (int i = 0; i < count; i++) {
			if (sts[i].getName().equals(name)) {
				st = sts[i];
				break;
			}
		}
		return st;
	}

	/**
	 * 从字符串 "stereo1,stereo2,stereo3"中解析出List<StereoType>
	 * 
	 * @param stereoStr
	 * @return
	 */
	public static List<StereoType> getStereoTypeListFromString(String stereoStr) {
		List<StereoType> stereoList = new ArrayList<StereoType>();
		if (!StringUtil.isEmptyWithTrim(stereoStr)) {
			String[] stereoArr = stereoStr.split(",");
			for (int i = 0; i < stereoArr.length; i++) {
				if (!StringUtil.isEmptyWithTrim(stereoArr[i])) {
					stereoList.add(getStereoTypeByName(stereoArr[i]));
				}
			}
		}
		return stereoList;
	}

	/**
	 * 从List<StereoType> 中根据StereoType.getName()拼成字符串，用",隔开"
	 * 
	 * @param stereoList
	 * @return
	 */
	public static String getStrByStereoTypeList(List<StereoType> stereoList) {
		StringBuffer resultStr = new StringBuffer();
		if (stereoList != null && stereoList.size() > 0) {
			for (StereoType type : stereoList) {
				if (type != null && !StringUtil.isEmptyWithTrim(type.getName())) {
					resultStr.append(type.getName() + ",");
				}
			}
			return resultStr.substring(0, resultStr.length() - 1);
		}
		return null;
	}

	/**
	 * 取所有stereoList中Accessor[]的交集
	 * 
	 * @param stereoList
	 * @return
	 */
	public static List<Accessor> getUseableAccessors(List<StereoType> stereoList) {
		// 取交集？
		if (stereoList == null||stereoList.isEmpty()) {
			return new ArrayList<Accessor>();
		}
		List<Accessor> accessorList = stereoList.get(0).getListAccessors();
		if (accessorList == null || accessorList.size() == 0) {
			return new ArrayList<Accessor>();
		}
		if (stereoList.size() > 1) {
			List<Accessor> delList = new ArrayList<Accessor>();
			for (int i = 1; i < stereoList.size(); i++) {
				if (stereoList.get(i) == null) {
					continue;
				}
				List<Accessor> accessorTempList = stereoList.get(i)
						.getListAccessors();
				if (accessorList.size() == 0) {
					return new ArrayList<Accessor>();
				}
				delList.clear();
				for (int j = 0; j < accessorList.size(); j++) {
					if (!accessorTempList.contains(accessorList.get(j))) {
						delList.add(accessorList.get(j));
					}
				}
				accessorList.removeAll(delList);
			}
		}
		return accessorList;
	}

	public static Accessor[] getAccessors() {
		Accessor[] accessorTempleate = AccessorConfig.loadAccessor();
		int count = accessorTempleate == null ? 0 : accessorTempleate.length;
		Accessor[] accs = new Accessor[count];
		for (int i = 0; i < count; i++) {
			accs[i] = accessorTempleate[i].clone();
		}
		return accs;
	}

	/**
	 * 又是纯粹应付静态代码检查，给它同步下，压根用不着同步嘛
	 * @return
	 */
	public static synchronized String[] getAttrAccessors() {
		if (attrAccessors == null) {
			String attrAccessorFilePath = getNCHome()
					+ "/ierp/metadata/AttrAccessors.txt";
			ArrayList<String> al = new ArrayList<String>();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(attrAccessorFilePath));
				String line = null;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (line.trim().length() > 0) {
						al.add(line);
					}
				}
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
			} finally {
				if (br != null)
					try {
						br.close();
					} catch (Exception e) {
					}
			}
			attrAccessors = al.toArray(new String[0]);
		}
		return attrAccessors;
	}

	public static boolean isBaseType(String typeId) {
		return getBaseTypeById(typeId) != null;
	}

	public static TableColumn createTableColumn(Table table, String colName,
			int width, int align, int index) {
		TableColumn col = new TableColumn(table, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}

	public static TreeColumn createTreeColumn(Tree tree, String colName,
			int width, int align, int index) {
		TreeColumn col = new TreeColumn(tree, SWT.NONE, index);
		col.setText(colName);
		col.setWidth(width);

		col.setAlignment(align);
		return col;
	}

	/**
	 * 获得属性的图标
	 * @param attr
	 * @return
	 */
	public static Image getAttrImg(Attribute attr) {
		Image img = ImageFactory.getAttrImage();
		Type type = attr.getType();
		if (type != null) {
			if (!isBaseType(type.getTypeId())) {
				img = ImageFactory.getRefAttrImage();//引用的图片标志
				String typeStyle = attr.getTypeStyle();
				if (Constant.ALSET_TYPE_STYLES.contains(typeStyle)) {
					img = ImageFactory.getCollectAttrImage();//聚合的图标
				}
			}
		}
		if (attr.isKey())//主键的图片
			img = ImageFactory.getkeyAttrImage();
		return img;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Object[]> getCanzhaoFromDB(String mdTypeName,
			IProject project) {
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String clsName = "nc.ui.bd.ref.RefInfoHelper";//利用该类查找参照
			String methodName = "getRefInfoList";
			Class c = ClassTool.loadClass(clsName, project);
			Method m = c.getMethod(methodName, new Class[] { String.class });
			list = (List<Object[]>) m.invoke(c, new Object[] { mdTypeName });
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(),e);
			String errMsg = getExceptionRecursionError(e);
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"异常", errMsg);
		}
		return list;
	}

	public static String getExceptionRecursionError(Throwable t) {
		StringBuilder sb = new StringBuilder("Error:\r\n");
		Throwable tt = t;
		while (tt != null) {
			sb.append("cause by ").append(tt.getClass().getName()).append(":")
					.append(tt.getMessage()).append(";\r\n");
			tt = tt.getCause();
		}
		return sb.toString();
	}

	/**
	 * 获得该editor所属的工程
	 * @param editor
	 * @return
	 */
	public static IProject getProjectOfEditor(IEditorPart editor){
		MDFileEditInput input = null;
		IEditorInput temp = editor.getEditorInput();
		if(temp instanceof MDFileEditInput){
			input = (MDFileEditInput) temp;
		}else{
			return null;
		}
		IFile ifile = input.getIFile();
		return ifile.getProject();
	}
	/**
	 * 保存参照信息
	 * @param mdTypeName
	 * @param project
	 * @param list
	 */
	public static void updateCanzhaoInfo(String mdTypeName, IProject project,
			List<Object[]> list) {
		try {
			String clsName = "nc.ui.bd.ref.RefInfoHelper";
			String methodName = "saveRefoList";
			Class<?> c = ClassTool.loadClass(clsName, project);
			Method m = c.getMethod(methodName, new Class[] { List.class });
			m.invoke(c, new Object[] { list });
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
			String errMsg = getExceptionRecursionError(e);
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"异常", errMsg);
		}
	}

	public static String getGenSqlsRootDir() {
		IPreferenceStore ps = NCMDPActivator.getDefault().getPreferenceStore();
		String dir = ps.getString(GenSqlsSetPage.SQL_ROOT_DIR);
		if (dir == null || dir.trim().length() == 0) {
			dir = "c:/sqls";
		}
		return dir;
	}

	public static boolean isShowWizardWhenGenSqls() {
		IPreferenceStore ps = NCMDPActivator.getDefault().getPreferenceStore();
		return !ps.getBoolean(GenSqlsSetPage.NOT_SHOW_WIZARD);
	}

	public static void silentSetWriterable(String filename)
			throws CoreException {
		IFileInfo fileinfo = new FileInfo(filename);
		fileinfo.setAttribute(EFS.ATTRIBUTE_READ_ONLY, false);
		IFileSystem fs = EFS.getLocalFileSystem();
		IFileStore store = fs.fromLocalFile(new File(filename));
		store.putInfo(fileinfo, EFS.SET_ATTRIBUTES, null);
	}

	public static void showHintMsg(final Shell shell, final String title,
			final String msg) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openInformation(shell, "提示", msg);
			}
		});
	}

	public static StringBuilder getColNames() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < CellModifier.colNames.length; i++) {
			sb.append("#").append(CellModifier.colNames[i]).append(":true")
					.append(",");
		}
		return sb;
	}

	/**
	 * 获得属性表的列的显示名称
	 * @return
	 */
	public static ArrayList<ColSelObj> getPropTableSelectedColNames() {
		IStringVariableManager vvManager = VariablesPlugin.getDefault()
				.getStringVariableManager();
		IValueVariable cellpropColset = vvManager
				.getValueVariable(Cell_PROPTABLE_COL_SET);
		/** yuyonga 修改增加属性的逻辑 */
		if (cellpropColset == null
				|| !cellpropColset.getValue().equals(getColNames().toString())) {
			StringBuilder sb = new StringBuilder();
			//构建列的名称
			for (int i = 0; i < CellModifier.colNames.length; i++) {
				sb.append("#").append(CellModifier.colNames[i]).append(":true")
						.append(",");
			}
			//注册到StringVariableManager
			cellpropColset = vvManager.newValueVariable(Cell_PROPTABLE_COL_SET,
					"");
			cellpropColset.setValue(sb.toString());
			try {
				if (cellpropColset != null) {
					vvManager
							.removeVariables(new IValueVariable[] { cellpropColset });
				}
				vvManager.addVariables(new IValueVariable[] { cellpropColset });
			} catch (CoreException e) {
				MDPLogger.error(e.getMessage(), e);
			}
		}
		String value = cellpropColset.getValue();
		String[] cols = value.split(",");
		ArrayList<ColSelObj> al = new ArrayList<ColSelObj>();
		int count = cols == null ? 0 : cols.length;
		for (int i = 0; i < count; i++) {
			String str = cols[i];
			if (str.startsWith("#")) {
				String[] s = str.substring(1).split(":");
				if (s != null && s.length == 2) {
					al.add(new ColSelectDlg.ColSelObj(s[0], "true"
							.equalsIgnoreCase(s[1])));
				} else {
					al.add(new ColSelectDlg.ColSelObj(s[0], true));
				}
			}
		}
		return al;
	}

	public static void setPropTableSelectedColNames(ArrayList<ColSelObj> cols) {
		IStringVariableManager vvManager = VariablesPlugin.getDefault()
				.getStringVariableManager();
		IValueVariable cellpropcolset = vvManager
				.getValueVariable(Cell_PROPTABLE_COL_SET);
		if (cellpropcolset == null) {
			cellpropcolset = vvManager.newValueVariable(Cell_PROPTABLE_COL_SET,
					"");
			try {
				vvManager.addVariables(new IValueVariable[] { cellpropcolset });
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cols.size(); i++) {
			sb.append("#").append(cols.get(i).getColName()).append(":")
					.append(cols.get(i).isSel() ? "true" : "false").append(",");
		}
		cellpropcolset.setValue(sb.toString());
	}

	/**
	 * 根据模块名和相对路径获得文件， 主要是用在Reference中查找引用的具体文件
	 * @param moduleName
	 * @param relativePath
	 * @return
	 */
	public static File getMDPFileByModuleAndPath(String moduleName,
			String relativePath) {
		File f = null;
		IProject[] projects = getOpenedJavaProjects();
		int count = projects == null ? 0 : projects.length;
		for (int i = 0; i < count; i++) {
			IProject project = projects[i];
			String projectModuleName = getProjectModuleName(project);
			File tempFile = new File(project.getLocation().toFile(),
					MDPExplorerTreeBuilder.METADATA_ROOT_DIR + File.separator
							+ relativePath);
			if (moduleName.equals(projectModuleName) && tempFile.exists()) {
				f = tempFile;
				break;
			}
		}
		if (f == null) {
			String ncHome = getNCHome();
			String filePath = ncHome + File.separator + "modules"
					+ File.separator + moduleName + File.separator
					+ MDPExplorerTreeBuilder.METADATA_ROOT_DIR + File.separator
					+ relativePath;
			File tempfile = new File(filePath);
			if (tempfile.exists()) {
				f = tempfile;
			}

		}
		return f;
	}

	/**
	 * 根据文件过滤器，递归搜索指定目录下所有的子文件。
	 * 
	 * @param dir目录名
	 * @param filter文件过滤器
	 * @return 搜索到的文件列表，元素为{@link java.io.File}。
	 */
	public static List<File> listFiles(File dir, FilenameFilter filter) {
		List<File> filesList = new ArrayList<File>();
		if (null == dir || !dir.exists() || dir.isFile()){
			return filesList;
		}
		if(dir.isFile()){
			filesList.add(dir);
			return filesList;
		}
		listFiles(filesList, dir, filter);
		return filesList;
	}

	/**
	 * @param filesList保存搜索到的文件
	 * @param dir
	 *            目录名
	 * @param filter文件过滤器
	 */
	private static List<File> listFiles(List<File> filesList, File dir,
			FilenameFilter filter) {
		File[] files = dir.listFiles(filter);
		List<File> curFiles = Arrays.asList(files);
		filesList.addAll(curFiles);

		File[] subDirs = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.exists() && pathname.isDirectory();
			}
		});
		if (null != subDirs)
			for (int i = 0; i < subDirs.length; i++) {
				listFiles(filesList, subDirs[i], filter);
			}
		return filesList;
	}
}
