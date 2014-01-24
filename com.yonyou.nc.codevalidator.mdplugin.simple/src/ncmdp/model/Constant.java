package ncmdp.model;

import java.util.ArrayList;
import java.util.Arrays;

public interface Constant {
	public static final String MDFILE_SUFFIX = ".bmf";
	public static final String MDFILE_BPF_SUFFIX = ".bpf";

	public static final String MDFILE_SUFFIX_EXTENTION = "bmf";
	public static final String MDFILE_BPF_SUFFIX_EXTENTION = "bpf";

	public static final String TYPE_INT = "int";
	public static final String TYPE_STRING = "String";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_FLOAT = "float";
	public static final String TYPE_CHAR = "char";
	public static final String TYPE_BYTE = "byte";
	public static String[] TYPES = { TYPE_INT, TYPE_STRING, TYPE_DOUBLE,
			TYPE_FLOAT, TYPE_CHAR, TYPE_BYTE };

	public static final String VISIBILITY_PUBLIC = "public";
	public static final String VISIBILITY_PROTECTED = "protected";
	public static final String VISIBILITY_DEFAULT = "default";
	public static final String VISIBILITY_PRIVATE = "private";
	public static String[] VISIBILITYS = { VISIBILITY_PUBLIC,
			VISIBILITY_PROTECTED, VISIBILITY_DEFAULT, VISIBILITY_PRIVATE };

	public static final String MULTI_1_N = "1..n";
	public static final String MULTI_1 = "1";

	public static final String[] RELATION_MULTI_STRS = { "0", MULTI_1, "n",
			MULTI_1_N, "0..n", "0..1" };

	public static final String BEANTYPE_POJO = "POJO";
	public static final String BEANTYPE_NCVO = "NCVO";
	public static final String BEANTYPE_AGGVO_HEAD = "AGGVO_HEAD";
	// public static final String[]
	// BEANTYPES={BEANTYPE_POJO,BEANTYPE_NCVO,BEANTYPE_AGGVO_HEAD};

	public static final String TRANS_REQUIRED = "REQUIRED";
	public static final String TRANS_SUPPORTS = "SUPPORTS";
	public static final String TRANS_MANDATORY = "MANDATORY";
	public static final String TRANS_REQUIRES_NEW = "REQUIRES_NEW";
	public static final String TRANS_NOT_SUPPORTED = "NOT_SUPPORTED";
	public static final String TRANS_NEVER = "NEVER";
	public static final String[] TRANSKINDS = { TRANS_NEVER, TRANS_REQUIRED,
			TRANS_SUPPORTS, TRANS_MANDATORY, TRANS_REQUIRES_NEW,
			TRANS_NOT_SUPPORTED };

	public static final String[] TYPE_STYLES = { "SINGLE", "ARRAY", "LIST",
			"REF" };
	public static final String[] SET_TYPE_STYLES = { "ARRAY", "LIST" };
	public static ArrayList<String> ALSET_TYPE_STYLES = new ArrayList<String>(
			Arrays.asList(SET_TYPE_STYLES));

	public static final String TRANS_PROP_NONE = "NONE";
	public static final String TRANS_PROP_CMT = "CMT";
	public static final String TRANS_PROP_BMT = "BMT";
	public static final String[] TRANS_PROPS = { TRANS_PROP_NONE,
			TRANS_PROP_CMT, TRANS_PROP_BMT };

	public static final String LOG_TYPE_NONE = "IGNORE";
	public static final String LOG_TYPE_ADD = "ADD";
	public static final String LOG_TYPE_MODIFY = "MODIFY";
	public static final String LOG_TYPE_DELETE = "DELETE";
	public static final String LOG_TYPE_OTHER = "OTHER";
	public static final String[] LOG_TYPES = { LOG_TYPE_NONE, LOG_TYPE_ADD,
			LOG_TYPE_MODIFY, LOG_TYPE_DELETE, LOG_TYPE_OTHER };

	public static final String XML_TYPE_REF = "ref";
	public static final String XML_TYPE_VALUE = "value";
	public static final String XML_TYPE_PROPS = "props";
	public static final String XML_TYPE_LIST = "list";
	public static final String XML_TYPE_MAP = "map";
	public static final String XML_TYPE_SET = "set";
	public static final String XML_TYPE_PROP = "prop";
	public static final String XML_TYPE_ENTRY = "entry";
	public static final String XML_TYPE_PROPERTY = "property";

	public static final String GENCODE_STYLE_STD = "标准样式";
	public static final String GENCODE_STYLE_NC = "NC传统样式";
	public static final String GENCODE_STYLE_DEF = "自定义样式";
	public static final String[] GENCODE_STYLES = { GENCODE_STYLE_NC,
			GENCODE_STYLE_STD, GENCODE_STYLE_DEF };

	public static final int CELL_CORNER_SIZE = 10;

	public static final String CONN_ROUTER_MANUAL = "手动";
	public static final String CONN_ROUTER_MANHATTAN = "曼哈顿直角";
	public static final String CONN_ROUTER_SHORTEST_PATH = "最短路径";
	public static String[] CONN_ROUTER_TYPES = { CONN_ROUTER_MANUAL,
			CONN_ROUTER_MANHATTAN, CONN_ROUTER_SHORTEST_PATH };

	public static final String CELL_EMTITY = "cell_entity";
	public static final String CELL_OPINTERFACE = "cell_opinterface";
}
