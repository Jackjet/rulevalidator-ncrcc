package ncmdp.pdmxml;

public class DataTypeConst {

	public final static int[] VALUE = new int[] { -7, -6, 5, 4, -5, 6, 7, 8, 2, 3, 1, 12, -1, 91, 92,
			93, -2, -3, -4, 1111, 2000, 2001, 2002, 2003, 2004, 2005, 2006 };

	public final static String[] NAME = new String[] { "bit", "tinyint", "smallint", "integer",
			"bigint", "float", "real", "double", "numeric", "decimal", "char", "varchar", "longvarchar",
			"date", "time", "timestamp", "binary", "varbinary", "longvarbinary", "other", "java_object",
			"distinct", "struct", "array", "blob", "clob", "ref" };

	public DataTypeConst() {
		super();
	}

	/**
	 * 通过值获得数据类型名称
	 * 创建日期：(02-2-6 15:03:14)
	 * @return int
	 * @param name java.lang.String
	 */
	public static String getNameByValue(int value) {
		for (int i = 0; i < VALUE.length; i++)
			if (value == VALUE[i])
				return NAME[i];
		return "";
	}

	/**
	 * 通过数据类型名称获得值
	 * 创建日期：(02-2-6 15:03:14)
	 * @return int
	 * @param name java.lang.String
	 */
	public static int getValueByName(String name) {
		for (int i = 0; i < NAME.length; i++)
			if (name.equals(NAME[i]))
				return VALUE[i];
		return 0;
	}

	/**
	 * @param index
	 * @return
	 */
	public static int getValueByIndex(int index) {
		return VALUE[index];
	}

}
