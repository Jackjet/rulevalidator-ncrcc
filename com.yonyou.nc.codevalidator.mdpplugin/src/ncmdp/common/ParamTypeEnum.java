package ncmdp.common;

//主实体VO=0
// 子实体VO=1
// 主实体主键=2
// 子实体主键=3
// 主实体差异VO=4
// 子实体差异VO=5
// 其他含主实体主键的结构=6
/**
 * 参数类型枚举
 */
public enum ParamTypeEnum {

	MAINENTITY(0), SUBENTITY(1), MAINENTITYPK(2), SUBENTITYPK(3), MAINENTITYEXVO(
			4), SUBENTITYEXVO(5), OTHER(6), NoRelation(7);
	private int type;

	private ParamTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getStrinfType() {
		return Integer.toBinaryString(type);
	}

	public static ParamTypeEnum getEnum(int type) {
		switch (type) {
		case 0:
			return MAINENTITY;
		case 1:
			return SUBENTITY;
		case 2:
			return MAINENTITYPK;
		case 3:
			return SUBENTITYPK;
		case 4:
			return MAINENTITYEXVO;
		case 5:
			return SUBENTITYEXVO;
		case 6:
			return OTHER;
		case 7:
			return NoRelation;
		default:
			return NoRelation;
		}
	}

	@Override
	public String toString() {
		switch (type) {
		case 0:
			return Messages.ParamTypeEnum_0;
		case 1:
			return Messages.ParamTypeEnum_1;
		case 2:
			return Messages.ParamTypeEnum_2;
		case 3:
			return Messages.ParamTypeEnum_3;
		case 4:
			return Messages.ParamTypeEnum_4;
		case 5:
			return Messages.ParamTypeEnum_5;
		case 6:
			return Messages.ParamTypeEnum_6;
		case 7:
			return Messages.ParamTypeEnum_7;
		default:
			return ""; //$NON-NLS-1$
		}
	}

	public static String[] getAlltype() {
		ParamTypeEnum[] allType = values();
		String[] result = new String[allType.length];
		for (int i = 0; i < allType.length; i++) {
			ParamTypeEnum enumType = allType[i];
			result[i] = enumType.toString();
		}
		return result;
	}
}
