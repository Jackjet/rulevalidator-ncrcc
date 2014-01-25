package ncmdp.common;

//忽略=0
// 新增=1
// 修改=2
// 删除=3
// 其他=4
/**
 * 日志类型枚举
 */
public enum LogTypeEnum {
	IGNORE(0), ADD(1), MODIFY(2), DELETE(3), OTHER(4);
	private int type;

	private LogTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		switch (type) {
		case 0:
			return Messages.LogTypeEnum_0;
		case 1:
			return Messages.LogTypeEnum_1;
		case 2:
			return Messages.LogTypeEnum_2;
		case 3:
			return Messages.LogTypeEnum_3;
		case 4:
			return Messages.LogTypeEnum_4;
		default:
			return ""; //$NON-NLS-1$
		}
	}

	public String getStrinfType() {
		return Integer.toBinaryString(type);
	}

	public static LogTypeEnum getEnum(int type) {
		switch (type) {
		case 0:
			return IGNORE;
		case 1:
			return ADD;
		case 2:
			return MODIFY;
		case 3:
			return DELETE;
		case 4:
			return OTHER;
		default:
			return OTHER;
		}
	}

	public static String[] getAlltype() {
		LogTypeEnum[] allType = values();
		String[] result = new String[allType.length];
		for (int i = 0; i < allType.length; i++) {
			LogTypeEnum enumType = allType[i];
			result[i] = enumType.toString();
		}
		return result;
	}
}
