package ncmdp.common;

//��ʵ��VO=0
// ��ʵ��VO=1
// ��ʵ������=2
// ��ʵ������=3
// ��ʵ�����VO=4
// ��ʵ�����VO=5
// ��������ʵ�������Ľṹ=6
/**
 * ��������ö��
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
