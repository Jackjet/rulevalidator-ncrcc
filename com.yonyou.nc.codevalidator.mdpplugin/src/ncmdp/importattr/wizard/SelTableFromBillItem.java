package ncmdp.importattr.wizard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import ncmdp.pdmxml.Field;
import ncmdp.pdmxml.Table;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
/**
 * 从billItem中导入数据源
 * @author wangxmn
 *
 */
public class SelTableFromBillItem extends SelDBTableWizardPage {
	public SelTableFromBillItem(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		
	}

	@Override
	public Table[] getAllTables() {
		Table[] tables = new Table[0];
		Connection con = null;
		try {
			con = getConnection();
			tables =getTablesFromBillItem(con);
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "err", NCMDPTool.getExceptionRecursionError(e));
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

	private Table[] getTablesFromBillItem(Connection con) throws Exception {
		String sql = "select b.votable,a.resourceid, a.attrname, a.itemtype,b.pkfield from dap_defitem a, pub_votable b where a.pk_billtype=b.pk_billtype and a.headflag=b.headbodyflag";
		HashMap<String, Table> hm = new HashMap<String, Table>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String tableName = rs.getString(1);
				Table table = hm.get(tableName);
				if(table == null){
					table = new Table();
					table.setDisplayName(tableName);
					table.setName(tableName);
					hm.put(tableName, table);
				}
				Field f = new Field();
				String colDisplayName = rs.getString(2);
				String colName = rs.getString(3);
				int type = rs.getInt(4);
				String typeStr = "varchar";
				switch(type){
				case 0:
					typeStr = "varchar";
					f.setLength("50");
					break;
				case 1:
					typeStr = "decimal";
					break;
				case 2:
					typeStr = "char";
					f.setLength("10");
					break;
				case 3:
					typeStr = "integer";
					break;
				case 4:
					typeStr = "decimal";
					break;
				case 5:
					typeStr = "char";
					break;
				}
				String pkfield = rs.getString(5);
				f.setName(colName);
				f.setDisplayName(colDisplayName);
				f.setType(typeStr);
				f.setKey(colName.equals(pkfield));
				table.addField(f);
			}
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
			throw e;
		} finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		return  hm.values().toArray(new Table[0]);
	}
	public ArrayList<Field> getFields(Table table) {
		return table.getFields();
	}
}
