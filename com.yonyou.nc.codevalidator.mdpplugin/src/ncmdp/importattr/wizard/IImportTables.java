package ncmdp.importattr.wizard;

import java.util.ArrayList;

import ncmdp.pdmxml.Field;
import ncmdp.pdmxml.Table;

public interface IImportTables {
	public Table[] getAllTables();
	public ArrayList<Field> getFields(Table table);
}
