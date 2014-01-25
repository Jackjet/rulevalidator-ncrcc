package com.yonyou.nc.codevalidator.sqltrans;

/**
 *模块:	TranslatorObject.java
 *描述:	Sql翻译器的公共父类
 *作者:	cf
 */

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.type.DBConsts;

public class TranslatorObject implements ITranslator, DBConsts {

	/**
	 * TranslatorObject 构造子注释。
	 */
	public TranslatorObject() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject");
		// 缺省目标数据库类型为sql server
		setDestDbType(SQLSERVER);
		// m_iDestinationDatabaseType = SQLSERVER;
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject Over");
	}

	/**
	 * TranslatorObject 构造子注释。
	 */
	public TranslatorObject(int dbType) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject");
		// 初始设置目标数据库类型
		// m_iDestinationDatabaseType = dbType;
		setDestDbType(dbType);
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject Over");
	}

	public int getDestDbType() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDestDbType");
		// 取得目标数据库类型
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDestDbType Over");
		return m_iDestinationDatabaseType;
	}

	/**
	 * 取得错误码
	 */
	public int getErrorCode(int iErrorCode) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getErrorCode");
		// 若错误码对应关系表为空，则返回源SQL语句
		if (m_apiErrorList == null)
			return iErrorCode;
		// 循环比较错误码
		for (int i = 0; i < m_apiErrorList.length; i++) {
			// 返回目标错误码
			if (m_apiErrorList[i][0] == iErrorCode)
				return m_apiErrorList[i][1];
		}
		// 若未查到错误码，则返回源SQL语句
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getErrorCode Over");
		return iErrorCode;
	}

	/**
	 * 取得函数转换结果
	 */
	public String getFunction(String sSourceFunction) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunction");
		// 若函数列表为空，则返回源函数
		if (m_apsFunList == null)
			return sSourceFunction;
		// 依次比较源函数
		for (int i = 0; i < m_apsFunList.length; i++) {
			String st = m_apsFunList[i][0];
			if (st.equalsIgnoreCase(sSourceFunction)) {
				return m_apsFunList[i][1];
			}
		}
		// 若无匹配函数，则返回源函数
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunction Over");
		return sSourceFunction;
	}

	/**
	 * 取得源SQL语句
	 */
	public String getSourceSql() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSourceSql");
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSourceSql Over");
		return m_sResorceSQL;
	}

	/**
	 * 取得源SQL语句，支持异常返回
	 */
	public String getSql() throws Exception {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSql");
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSql Over");
		return m_sResorceSQL;
	}

	/**
	 * 取得源SQL语句异常
	 */
	public java.sql.SQLException getSqlException() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlException");
		// 若源SQL语句异常为空，则返回空
		if (m_eSqlExp == null)
			return null;
		// 若异常对照表为空，则返回源SQL语句异常
		if (m_apiErrorList == null)
			return m_eSqlExp;
		// 定义SQL异常
		java.sql.SQLException eSQL = new java.sql.SQLException(
				m_eSqlExp.getMessage(), m_eSqlExp.getSQLState(),
				getErrorCode(m_eSqlExp.getErrorCode()));
		// 取得下一个异常
		eSQL.setNextException(m_eSqlExp.getNextException());
		Logger.error("sql original exception", m_eSqlExp);
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlException Over");
		return eSQL;
	}

	/**
	 * 取得语句种类
	 */
	protected int getStatementType() throws Exception {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStatementType");
		int iType = 0;
		// 若语句长度小于2，则提示错误
		if (m_asSqlWords.length < 1) {
			return iType;
		}
		// 依次比较语句类型
		if (m_asSqlWords[0].equalsIgnoreCase("SELECT")) {
			iType = SQL_SELECT;
		} else if (m_asSqlWords[0].equalsIgnoreCase("INSERT")) {
			iType = SQL_INSERT;
		} else if (m_asSqlWords[0].equalsIgnoreCase("CREATE")) {
			if (m_asSqlWords.length > 1
					&& m_asSqlWords[1].equalsIgnoreCase("view")) {
				// create view
				iType = SQL_SELECT;
			} else {
				// create table
				iType = SQL_CREATE;
			}
		} else if (m_asSqlWords[0].equalsIgnoreCase("DROP")) {
			iType = SQL_DROP;
		} else if (m_asSqlWords[0].equalsIgnoreCase("DELETE")) {
			iType = SQL_DELETE;
		} else if (m_asSqlWords[0].equalsIgnoreCase("UPDATE")) {
			iType = SQL_UPDATE;
		} else if (m_asSqlWords[0].equalsIgnoreCase("EXPLAIN")) {
			iType = SQL_EXPLAIN;

		} else if (m_asSqlWords[0].equalsIgnoreCase("if")
				&& m_asSqlWords[1].equalsIgnoreCase("exists")) {
			iType = 8;
		} else {
			iType = SQL_SELECT;
		}
		// 返回语句类型
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStatementType Over");
		return iType;
	}

	/**
	 * 判断给定字符串是否是比较运算符
	 */

	public boolean isCompareOperator(String s) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCompareOperator");
		for (int i = 0; i < m_asOperationStr.length; i++) {
			if (s.equals(m_asOperationStr[i]))
				return true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCompareOperator Over");
		return false;
	}

	/**
	 * 对SQL语句进行拆解分析,得到每一个有意义的字符
	 */
	public String[] parseSql(String sql) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseSql");
		// 若输入的SQL为空，则返回空
		if (sql == null || sql.trim().length() == 0)
			return null;
		// 初始单词序列
		String asKeyWords[] = null;
		// 初始哈西表
		java.util.Hashtable<Integer, String> table = new java.util.Hashtable<Integer, String>();
		// 初始记数器
		int iCount = 0;
		int iOffSet = 0;

		// 找到第一个单词
		String sWord = parseWord(sql.substring(iOffSet));

		// 若单词长度大于0，则开始寻找其余单词
		while (sWord.length() > 0) {
			// 计数加上单词的长度
			iOffSet += sWord.length();
			// 去掉单词内的空格
			sWord = sWord.trim();
			// 若单词长度大于0
			if (sWord.length() > 0) {
				// 存入新单词
				String s = sWord;

				if (s.equalsIgnoreCase("join")) {
					Object obj = table.get(Integer.valueOf(iCount - 1));

					if (obj == null) {
						table.put(Integer.valueOf(iCount), "inner");
						iCount++;
					} else {
						String stSql = obj.toString();
						if (!stSql.equalsIgnoreCase("inner")
								&& !stSql.equalsIgnoreCase("outer")) {
							String joinType = "inner";
							if (stSql.equalsIgnoreCase("right")
									|| stSql.equalsIgnoreCase("left")) {
								joinType = "outer";
							}
							table.put(Integer.valueOf(iCount), joinType);
							iCount++;
						}
					}
				}

				if (iCount > 0) {
					String st = table.get(Integer.valueOf(iCount - 1))
							.toString().trim();

					if (st.endsWith(".") || s.trim().startsWith(".")) {
						table.put(Integer.valueOf(iCount - 1), st + s.trim());
					}
					// else if (st.endsWith("'") && s.trim().startsWith("'"))
					// {
					// table.put(new Integer(iCount - 1), st + s.trim());
					// }
					else {
						// 存入哈西表
						table.put(Integer.valueOf(iCount), s.trim());
						// 计数加1
						iCount++;
					}
				} else {
					// 存入哈西表
					table.put(Integer.valueOf(iCount), s.trim());
					// 计数加1
					iCount++;
				}

			}
			// 查找下一个单词
			sWord = parseWord(sql.substring(iOffSet));

			// 若单词中仅含有空格则结束
			String s = sWord.trim();
			if (s.length() == 0) {
				sWord = s;
			}
		}
		// 初始字符串数组
		asKeyWords = new String[iCount];
		// 从哈西表中提取记录
		for (int i = 0; i < iCount; i++) {
			asKeyWords[i] = (String) table.get(Integer.valueOf(i));
		}
		// 返回字符串组
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseSql Over");
		return asKeyWords;
	}

	/**
	 * 从sql语句 s 中提取第一个有意义的单词 word = (|)|*|=|,|?| |<|>|!=|<>|<=|>=|其他
	 */
	public String parseWord(String s) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseWord");
		// 注意此处不可用 s=s.trim();语句，否则会出错

		// 若输入单词长度为0，则返回""
		if (s.length() == 0) {
			return "";
		}
		// 标志:是否在''内,是否在""内,是否找到单词
		boolean bInSingle = false;
		boolean bInDouble = false;
		boolean bFound = false;
		// 初始计数器
		int iOffSet = 0;
		// 初始字符缓存
		char c;

		// 过滤掉空格,'\t',与回车换行符。计数器保存着除去特定字符的开始位置,即第一个有效字符的位置
		while (// 若计数小于输入字串的长度，且输入字串在计数位的字符串为空格
		(iOffSet < s.length() && s.charAt(iOffSet) == ' ') // 若计数小于输入字串的长度，且输入字串在计数位的字符串为“Tab”
				|| (iOffSet < s.length() && s.charAt(iOffSet) == '\t') // 若计数小于输入字串的长度，且输入字串在计数位的字符串包含于换行符之内
				|| (iOffSet < s.length() && m_sLineSep.indexOf(s
						.charAt(iOffSet)) >= 0)) {
			// 计数器加1
			iOffSet++;
			// 若计数大于输入字符串长度，则返回""
			if (iOffSet > s.length()) {
				return "";
			}
		}
		// 若计数大于输入字符串长度，则返回""
		if (iOffSet >= s.length()) {
			return "";
		}
		// 取得输入字符串在计数位置的字符
		c = s.charAt(iOffSet); // 第一个有效字符

		/*
		 * //过滤掉() if (c == '(' && s.length() >= 2 && s.charAt(1) == ')') { s =
		 * s.substring(2, s.length()); if (s.length() == 0) return ""; }
		 */

		// 返回特殊字符串
		// 计数器加1
		iOffSet++;
		// 若计数小于输入字符串长度
		if (iOffSet < s.length()) {
			// 取得输入字符串在计数位置2位的字符串
			String ss = "" + c + s.charAt(iOffSet);
			// 依次比较是否为特殊字符串
			for (int i = 0; i < m_asSpecialStr.length; i++) {
				if (ss.equals(m_asSpecialStr[i])) {
					// 返回特殊字符串
					return s.substring(0, iOffSet + 1);
				}
			}
		}
		// 计数器减1
		iOffSet--;

		// 查找并返回特殊字符
		for (int i = 0; i < m_sSpecialChar.length(); i++) {
			if (c == m_sSpecialChar.charAt(i)) {
				// if( (c=='-')
				// &&
				// iOffSet>1
				// && (s.charAt(iOffSet-1)=='E' || s.charAt(iOffSet-1)=='e')
				// &&
				// (isNumber(s.substring(0,iOffSet-1)))
				// )
				if (!((c == '-')
						&& iOffSet > 1
						&& (s.charAt(iOffSet - 1) == 'E' || s
								.charAt(iOffSet - 1) == 'e') && (isNumber(s
						.substring(0, iOffSet - 1)))))
				// {
				// iOffSet++;
				// }
				// else
				{
					return s.substring(0, iOffSet + 1);
				}
			}
		}

		// 若计数小于输入字符串的长度
		while (iOffSet < s.length()) {
			// 取得输入字符串在计数位置的字符
			c = s.charAt(iOffSet);
			// 若为单引号
			if (c == '\'') {
				// 若不在双引号内
				if (!bInDouble) {
					// 若在单引号内
					if (bInSingle) {
						// 解析''
						// 若计数加1小于输入字符串的长度，且输入字符串在计数加1位置的字符为单引号
						if ((iOffSet + 1) < s.length()
								&& s.charAt(iOffSet + 1) == '\'') {
							// 计数加1
							iOffSet++;
						} else {
							// 否则，计数加1，跳出循环
							iOffSet++;
							break;
						}
					}
					// 是否在单引号中设为真
					bInSingle = true;
				}
			}

			// 若为双引号
			if (c == '"') {
				// 若不在单引号中
				if (!bInSingle) {
					// 若在双引号中
					if (bInDouble) {
						// 计数加1，跳出循环
						iOffSet++;
						break;
					}
					// 是否在双引号中设为真
					bInDouble = true;
				}
			}

			// 不在单引号内且不在双引号内
			if ((!bInDouble) && (!bInSingle)) {

				// 计数加1
				iOffSet++;
				// 若计数小于输入字符串的长度
				if (iOffSet < s.length()) {
					// 取得输入字符串在计数位置2位的字符串
					String ss = "" + c + s.charAt(iOffSet);
					// 循环比较是否为特殊字符串
					for (int i = 0; i < m_asSpecialStr.length; i++) {
						// 若找到，则跳出循环
						if (ss.equals(m_asSpecialStr[i])) {
							bFound = true;
							break;
						}
					}
				}
				// 计数减1
				iOffSet--;

				// 循环查找是否为特殊字符
				for (int i = 0; i < m_sSpecialChar.length(); i++) {
					if (c == m_sSpecialChar.charAt(i)) {
						if (!((c == '-')
								&& iOffSet > 1
								&& (s.charAt(iOffSet - 1) == 'E' || s
										.charAt(iOffSet - 1) == 'e') && (isNumber(s
								.substring(0, iOffSet - 1))))) {
							// 若找到，则跳出循环
							bFound = true;
							break;
						}
					}
				}
				// 若找到，则跳出循环
				if (bFound) {
					break;
				}
			}
			// 计数加1
			iOffSet++;
		}

		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseWord Over");
		return s.substring(0, iOffSet);
	}

	/**
	 * 设置SQL语句
	 */
	public void setSql(String sql) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSql");
		m_sResorceSQL = sql;
		// 分析sql语句,得到sql单词序列
		setSqlArray(parseSql(m_sResorceSQL));
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSql Over");
	}

	/**
	 * 设置SQL异常
	 */
	public void setSqlException(java.sql.SQLException e) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlException");
		m_eSqlExp = e;
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlException Over");
	}

	// ErrorCode对照表,列出sqlServer ErrorCode与其他数据库 ErrorCode的对应关系,
	int[][] m_apiErrorList = null;

	// 函数对照表,列出sqlServer ErrorCode与其他数据库 函数的对应关系,
	String[][] m_apsFunList = null;

	// 操作符
	String m_asOperationStr[] = { "=", "!=", "<>", "<", "<=", ">", ">=", "--",
			"\t" };

	// 定义特殊字符串
	String m_asSpecialStr[] = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<",
			">", "||", "&&", " ", "--", m_sLineSep, "\t" };

	// sql单词序列
	String[] m_asSqlWords = null;

	boolean m_bFinded = false; // 词法分析标志

	java.sql.SQLException m_eSqlExp = null; // 源SQLException

	int m_iBracket = 0; // 函数是否结束

	int m_iDestinationDatabaseType = SQLSERVER; // 目标数据库类型:缺省为sql server

	StringBuffer m_sbDestinationSql = null; // 目标数据库类型sql语句

	String m_sLeftTable = ""; // 左连接中用到的表(包含别名)

	String m_sLeftWhere = " where "; // 左连接中的where子句

	static String m_sLineSep = "\r\n";

	// System.getProperty("line.separator"); //换行符
	String m_sResorceSQL = null; // 源sql语句 SQL Resorce

	// 定义特殊字符
	String m_sSpecialChar = "-+()*=,? <>; " + "\t" + m_sLineSep;

	public String dateTypes[] = new String[] { "datetime", "smalldatetime",
			"timestamp", "UFDatabasedate", };

	String numberTypes[] = new String[] { "decimal", "float", "real", "int",
			"money", "numeric", "smallint", "smallmoney", "tinyint",
			"UFNumber5", "UFFactor", "UFPercent", "UFRate", "UFRebate",
			"UFTaxRate", "UFInterestRate", "UFInteger", "UFSeq", "UFIndex",
			"UFIndexInteger", "UFVersionNO", "UFFlag", "UFSeqshort",
			"UFDirection", "UFWaItem", "UFPeriod", "UFLevel", "UFWidth",
			"UFApproveStatus", "UFDefaultset", "UFInt", "UFStatus" };

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-19 14:06:10)
	 * 
	 * @return java.lang.String[]
	 * @param asWords
	 *            java.lang.String
	 * @param startIndex
	 *            int
	 * @param endIndex
	 *            int
	 */
	public String[] getFunParam(String[] asWords, int startIndex, int endIndex) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunParam");
		int iLBracket = 0;
		int iRBracket = 0;

		// int douHaoNum = 0;
		Vector<String> vec = new Vector<String>();
		String st = "";

		for (int iOff = startIndex; iOff < endIndex; iOff++) {
			// 若当前字符串为左括号
			if (asWords[iOff].equals("("))
				iLBracket++;
			// 若当前字符串为右括号
			if (asWords[iOff].equals(")"))
				iRBracket++;
			if (asWords[iOff].equals(",") && iLBracket == iRBracket) {
				vec.addElement(st);
				st = "";
			} else {
				st += " " + asWords[iOff];
			}
		}
		vec.addElement(st);
		String[] re = new String[vec.size()];
		vec.copyInto(re);
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunParam Over");
		return re;
	}

	/**
	 * 查询在source字符串中是否存在不包含在引号内的单个单词dest 创建日期：(2001-11-29 20:59:23)
	 * 
	 * @return int
	 * @param source
	 *            java.lang.String
	 * @param dest
	 *            java.lang.String
	 * @param number
	 *            int
	 */
	public int getIndexOf(String source, String dest) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getIndexOf");
		boolean find = false;
		int index = 0;
		while (!find) {
			index = source.indexOf(dest, index);

			if (index < 0
					|| (!inQuotation(source, index) && isSingleWord(source,
							dest, index))) {
				find = true;
			} else {
				index += dest.length();
			}
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getIndexOf Over");
		return index;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-17 17:16:50)
	 * 
	 * @return nc.bs.mw.sqltrans.TransUnit
	 * @param asSqlWords
	 *            java.lang.String[]
	 * @param iOffSet
	 *            int
	 */
	public String[] getLeftSql(String[] asSqlWords, int iOffSet) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getLeftSql");
		boolean inLeft = true;
		Vector<String> vec = new Vector<String>();
		/*
		 * while(iOffSet<asSqlWords.length && joinNotEnd(asSqlWords[iOffSet]) &&
		 * !asSqlWords[iOffSet].equalsIgnoreCase("and") &&
		 * !asSqlWords[iOffSet].equalsIgnoreCase("or") && inLeft)
		 */
		while (iOffSet < asSqlWords.length
				&& !asSqlWords[iOffSet].equalsIgnoreCase("and")
				&& !asSqlWords[iOffSet].equalsIgnoreCase("or") && inLeft) {
			if (isBiJiaoFu(asSqlWords[iOffSet])) {
				inLeft = false;
			}
			if (inLeft && haveAloneSt(asSqlWords[iOffSet], ".")) {
				vec.addElement(asSqlWords[iOffSet]);
			}
			iOffSet++;
		}
		String leftSql[] = new String[vec.size()];
		vec.copyInto(leftSql);
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getLeftSql Over");
		return leftSql;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-17 17:16:50)
	 * 
	 * @return nc.bs.mw.sqltrans.TransUnit
	 * @param asSqlWords
	 *            java.lang.String[]
	 * @param iOffSet
	 *            int
	 */
	public TransUnit getRightSql(String[] asSqlWords, int iOffSet) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getRightSql");
		boolean inRight = false;
		String rightSql = "";
		int leftKuoHao = 0;
		int rightKouHao = 0;
		/*
		 * && ( (!asSqlWords[iOffSet].equals(",") &&
		 * joinNotEnd(asSqlWords[iOffSet])) || (asSqlWords[iOffSet].equals(",")
		 * && leftKuo!=rightKuo) )
		 */
		while (iOffSet < asSqlWords.length
				&& ((!asSqlWords[iOffSet].equals(",") && joinNotEnd(asSqlWords[iOffSet])) || (asSqlWords[iOffSet]
						.equals(",") && leftKuoHao != rightKouHao))
				&& !asSqlWords[iOffSet].equalsIgnoreCase("and")
				&& !asSqlWords[iOffSet].equalsIgnoreCase("or") // && leftKuoHao
																// <=
																// rightKouHao
		) {
			if (asSqlWords[iOffSet].equals("(")) {
				leftKuoHao++;
			}
			if (asSqlWords[iOffSet].equals(")")) {
				rightKouHao++;
			}

			if (inRight && leftKuoHao <= rightKouHao) {
				rightSql += " " + asSqlWords[iOffSet];
			}
			if (isBiJiaoFu(asSqlWords[iOffSet])) {
				inRight = true;
				if (asSqlWords[iOffSet].equalsIgnoreCase("is")
						&& iOffSet < asSqlWords.length - 1
						&& asSqlWords[iOffSet + 1].equalsIgnoreCase("not")) {
					iOffSet++;
				}
			}
			iOffSet++;
		}
		if (rightKouHao > leftKuoHao) {
			iOffSet--;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getRightSql Over");
		return new TransUnit(null, rightSql, iOffSet);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-17 17:16:50)
	 * 
	 * @return nc.bs.mw.sqltrans.TransUnit
	 * @param asSqlWords
	 *            java.lang.String[]
	 * @param iOffSet
	 *            int
	 */
	public int getStartIndex(String[] asSqlWords, int iOffSet) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStartIndex");
		// boolean inRight = false;
		// String rightSql = "";
		// int leftKuoHao = 0;
		// int rightKouHao = 0;

		while (iOffSet > 0 && !asSqlWords[iOffSet - 1].equalsIgnoreCase("on")
				&& !asSqlWords[iOffSet - 1].equalsIgnoreCase("and")
				&& !asSqlWords[iOffSet - 1].equalsIgnoreCase("or")) {
			iOffSet--;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStartIndex Over");
		return iOffSet;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-30 15:58:03)
	 * 
	 * @return nc.bs.mw.sqltrans.TransUnit
	 * @param asSqlWords
	 *            java.lang.String[]
	 * @param leftWord
	 *            java.lang.String
	 * @param rightWord
	 *            java.lang.String
	 * @param iOffSet
	 *            int
	 */
	public TransUnit getSubSql(String[] asSqlWords, String leftWord,
			String rightWord, int iOffSet) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSubSql");
		int left = 1;
		int right = 0;

		Vector<String> vec = new Vector<String>();

		vec.addElement(asSqlWords[iOffSet]);

		while (iOffSet < asSqlWords.length && left != right) {
			iOffSet++;

			if (asSqlWords[iOffSet].equalsIgnoreCase(leftWord)) {
				left++;
			}
			if (asSqlWords[iOffSet].equalsIgnoreCase(rightWord)) {
				right++;
			}

			vec.addElement(asSqlWords[iOffSet]);

		}

		String newCaseSql[] = new String[vec.size()];
		vec.copyInto(newCaseSql);

		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSubSql Over");
		return new TransUnit(newCaseSql, null, iOffSet);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-17 13:59:39)
	 * 
	 * @return boolean
	 * @param source
	 *            java.lang.String
	 * @param dest
	 *            java.lang.String
	 */
	public boolean haveAloneSt(String source, String dest) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveAloneSt");
		boolean have = false;

		while (source.indexOf(dest) >= 0) {
			int singleNum = 0;
			for (int i = 0; i < source.indexOf(dest); i++) {
				char ch = source.charAt(i);
				if (ch == '\'') {
					singleNum++;
				}
			}
			if (singleNum % 2 == 0) {
				have = true;
				break;
			} else {
				source = source.substring(source.indexOf(dest) + 1);
			}
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveAloneSt Over");
		return have;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-19 10:00:22)
	 * 
	 * @return boolean
	 * @param sql
	 *            java.lang.String
	 * @param sTableName
	 *            java.lang.String
	 * @param sTableAlias
	 *            java.lang.String
	 */
	public boolean haveOtherTable(String sql_old, Vector<Object> vecTable) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveOtherTable");
		boolean haveOtherTable = false;

		if (vecTable != null && vecTable.size() > 0) {
			int size = vecTable.size();

			for (int i = 0; i < size; i++) {
				Object obj = vecTable.elementAt(i);
				String sql = sql_old;

				if (obj != null) {
					String table = obj.toString();

					while (sql.indexOf(".") >= 0) {
						if (table.trim().length() > 0
								&& ((sql.indexOf(table + ".") == 0) || (sql
										.indexOf(table + ".") > 0 && (sql
										.charAt(sql.indexOf(table + ".") - 1) == ' '
										|| sql.charAt(sql.indexOf(table + ".") - 1) == '+'
										|| sql.charAt(sql.indexOf(table + ".") - 1) == '-'
										|| sql.charAt(sql.indexOf(table + ".") - 1) == '*'
										|| sql.charAt(sql.indexOf(table + ".") - 1) == '/' || sql
										.charAt(sql.indexOf(table + ".") - 1) == '|')))) {
							haveOtherTable = true;
							// return haveOtherTable;
							break;
						} else {
							sql = sql.substring(sql.indexOf(".") + 1);
						}
					}
				}
			}
		}

		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveOtherTable Over");
		return haveOtherTable;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-1 9:04:04)
	 * 
	 * @return boolean
	 * @param formula
	 *            java.lang.String
	 * @param endIndex
	 *            int
	 */
	public boolean inQuotation(String formula, int endIndex) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.inQuotation");
		int quotationNum = 0;

		int left = 0;
		int right = 0;

		for (int i = 0; i < endIndex; i++) {
			if (formula.charAt(i) == '\'') {
				quotationNum++;
			}
			if (formula.charAt(i) == '(' && quotationNum % 2 == 0) {
				left++;
			}
			if (formula.charAt(i) == ')' && quotationNum % 2 == 0) {
				right++;
			}
		}
		if (quotationNum % 2 == 0 && left == right) {
			return false;
		} else {
			Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.inQuotation Over");
			return true;
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-29 11:18:23)
	 * 
	 * @return boolean
	 * @param st
	 *            java.lang.String
	 */
	public boolean isBiJiaoFu(String st) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isBiJiaoFu");
		boolean isOprater = false;

		// 没有遇到as， 也没有遇到on,别名
		if (st.equals("=") //
				|| st.equals("<=") || st.equals(">=")
				|| st.equals("<")
				|| st.equals(">") || st.equals("<>")
				|| st.equals("!=")
				|| st.equalsIgnoreCase("is")) {
			isOprater = true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isBiJiaoFu Over");
		return isOprater;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2005-01-05 17:04:03)
	 * 
	 * @return boolean
	 * @param dataType
	 *            java.lang.String
	 */
	public boolean isCharType(String dataType) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCharType");
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCharType Over");
		return isType(charTypes, dataType);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-18 17:04:03)
	 * 
	 * @return boolean
	 * @param dataType
	 *            java.lang.String
	 */
	public boolean isDateType(String dataType) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isDateType");
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isDateType Over");
		return isType(dateTypes, dataType);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-28 19:43:29)
	 * 
	 * @return boolean
	 * @param st
	 *            java.lang.String
	 */
	public boolean isFunctionName(String sWord, String nextWord) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isFunctionName");
		boolean isFunc = false;

		if (((sWord.equalsIgnoreCase("left") && !nextWord
				.equalsIgnoreCase("outer"))
				/** 1 **/
				|| (sWord.equalsIgnoreCase("right") && !nextWord
						.equalsIgnoreCase("outer"))
				/** 2 **/
				|| sWord.equalsIgnoreCase("square") // 或当前单词为“square”
				|| sWord.equalsIgnoreCase("cast") // 或当前单词为“cast”
				|| sWord.equalsIgnoreCase("coalesce")
				|| sWord.equalsIgnoreCase("ltrim")
				|| sWord.equalsIgnoreCase("rtrim")
				|| sWord.equalsIgnoreCase("patindex")
				|| sWord.equalsIgnoreCase("len")
				|| sWord.equalsIgnoreCase("round")
				|| sWord.equalsIgnoreCase("convert")
				|| sWord.equalsIgnoreCase("dateadd") || sWord
				.equalsIgnoreCase("datediff")) // 且下一个单词是“(”
				&& nextWord.equals("(")) {
			isFunc = true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isFunctionName Over");
		return isFunc;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-29 10:12:17)
	 * 
	 * @return boolean
	 * @param first
	 *            java.lang.String
	 * @param second
	 *            java.lang.String
	 * @param third
	 *            java.lang.String
	 */
	public boolean isInnerJoin(String first, String second) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isInnerJoin");
		boolean isInnerJoin = false;
		if (first.equalsIgnoreCase("inner") && second.equalsIgnoreCase("join")) {
			isInnerJoin = true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isInnerJoin Over");
		return isInnerJoin;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-27 16:53:29)
	 * 
	 * @return boolean
	 * @param st
	 *            java.lang.String
	 */
	public boolean isNumber(String st) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumber");
		boolean isNumber = false;

		if (st != null && st.trim().length() > 0) {
			try {
//				nc.vo.pub.lang.UFDouble ufd = new nc.vo.pub.lang.UFDouble(
//						st.trim());
//				if (ufd != null) {
//					isNumber = true;
//				}
				Integer.parseInt(st);
				return true;
			} catch (Exception e) {
			}
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumber Over");
		return isNumber;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-18 17:04:03)
	 * 
	 * @return boolean
	 * @param dataType
	 *            java.lang.String
	 */
	public boolean isNumberType(String dataType) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumberType");
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumberType Over");
		return isType(numberTypes, dataType);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-29 10:12:17)
	 * 
	 * @return boolean
	 * @param first
	 *            java.lang.String
	 * @param second
	 *            java.lang.String
	 * @param third
	 *            java.lang.String
	 */
	public boolean isOuterJoin(String first, String second, String third) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isOuterJoin");
		boolean isOuterJoin = false;
		if ((first.equalsIgnoreCase("left") || first.equalsIgnoreCase("right"))
				&& second.equalsIgnoreCase("outer")
				&& third.equalsIgnoreCase("join")) {
			isOuterJoin = true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isOuterJoin Over");
		return isOuterJoin;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-26 10:45:40)
	 * 
	 * @return boolean
	 * @param source
	 *            java.lang.String
	 * @param dest
	 *            java.lang.String
	 * @param index
	 *            int
	 */
	public boolean isSingleWord(String source, String dest, int index) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isSingleWord");
		boolean isSingleWord = false;

		if (index <= 0 || source.charAt(index - 1) == ' ') {
			if (index + dest.length() >= source.length() - 1
					|| source.charAt(index + dest.length()) == ' ') {
				isSingleWord = true;
			}
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isSingleWord Over");
		return isSingleWord;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-29 11:18:23)
	 * 
	 * @return boolean
	 * @param st
	 *            java.lang.String
	 */
	public boolean isTableOtherName(String st) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isTableOtherName");
		boolean isTableOtherName = false;

		// 没有遇到as， 也没有遇到on,别名
		if (!st.equalsIgnoreCase("on") && !st.equalsIgnoreCase("where")
				&& !st.equalsIgnoreCase("inner")
				&& !st.equalsIgnoreCase("left")
				&& !st.equalsIgnoreCase("right") && !st.equalsIgnoreCase(",")) {
			isTableOtherName = true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isTableOtherName Over");
		return isTableOtherName;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-18 17:40:51)
	 * 
	 * @return boolean
	 * @param dataTypes
	 *            java.lang.String[]
	 * @param type
	 *            java.lang.String
	 */
	public boolean isType(String[] dataTypes, String type) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isType");
		type = type.trim();
		boolean isType = false;

		for (int i = 0; i < dataTypes.length; i++) {
			if (type.equalsIgnoreCase(dataTypes[i]) // ||
													// dataTypes[i].toLowerCase().startsWith(type.toLowerCase())
			) {
				isType = true;
				break;
			}
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isType Over");
		return isType;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-29 11:18:23)
	 * 
	 * @return boolean
	 * @param st
	 *            java.lang.String
	 */
	public boolean joinNotEnd(String st) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.joinNotEnd");
		boolean joinNotEnd = false;

		// 没有遇到as， 也没有遇到on,别名
		if (!st.equalsIgnoreCase("left") && !st.equalsIgnoreCase("right")
				&& !st.equalsIgnoreCase("where")
				&& !st.equalsIgnoreCase("order")
				&& !st.equalsIgnoreCase("group")
				&& !st.equalsIgnoreCase("inner")
				&& !st.equalsIgnoreCase("union") && !st.equalsIgnoreCase("on")
				&& !st.equalsIgnoreCase(",")) {
			joinNotEnd = true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.joinNotEnd Over");
		return joinNotEnd;
	}

	/**
	 * 此处插入方法说明。 功能：设置目标数据库类型 创建日期：(2001-3-18 14:44:11)
	 */
	public void setDestDbType(int dbType) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setDestDbType");
		if (dbType >= 0 && dbType <= 3)
			m_iDestinationDatabaseType = dbType;
		else
			m_iDestinationDatabaseType = SQLSERVER; // 缺省为sql server
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setDestDbType Over");
	}

	/**
	 * 设置SQL语句
	 */
	public void setSqlArray(String[] sqlArray) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlArray");
		// 分析sql语句,得到sql单词序列
		m_asSqlWords = sqlArray;
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlArray Over");
	}

	/**
	 * 设置SQL语句
	 */
	public String[] getSqlArray() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlArray");
		// 分析sql语句,得到sql单词序列
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlArray Over");
		return m_asSqlWords;
	}

	/**
	 * 设置SQL语句
	 */
	public String[] getTableNames() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getTableNames");
		String[] tablename = null;
		Vector<String> v = new Vector<String>();
		Hashtable<String,String> ht = new Hashtable<String,String>();
		if (m_asSqlWords == null)
			return null;
		if (!m_asSqlWords[0].equalsIgnoreCase("where"))
			return null;
		for (int i = 0; i < m_asSqlWords.length; i++) {
			if (Character.isLetter(m_asSqlWords[i].charAt(0))) {
				if (m_asSqlWords[i].indexOf(".") > 0
						&& m_asSqlWords[i].indexOf("'") < 0
						&& m_asSqlWords[i].indexOf("\"") < 0) {
					// v.addElement();
					ht.put(m_asSqlWords[i].substring(0,
							m_asSqlWords[i].indexOf(".")),
							m_asSqlWords[i].substring(0,
									m_asSqlWords[i].indexOf(".")));
				}
			}
		}
		if (!ht.isEmpty()) {
			Enumeration em = ht.elements();
			while (em.hasMoreElements()) {
				String table = (String) em.nextElement();
				v.addElement(table);
			}
			if (v.size() > 0) {
				tablename = new String[v.size()];
				v.copyInto(tablename);
			}
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getTableNames Over");
		return tablename;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-20 9:27:31)
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(String[] args) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.main");
		// 2002-06-20 测试使用
		TranslatorObject transOb = new TranslatorObject();
		String sql = "where h.cbillid = b.cbillid and h.pk_corp = '1003' and upper(h.cbilltypecode) in ('I0', 'I1') and b.fpricemodeflag=5 and h.bdisableflag = 'N'";
		transOb.setSql(sql);
		transOb.getTableNames();
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.main Over");
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-3 15:10:44)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] parseTable(String[] asWords, String sTableName,
			String sTableAlias) {
		int iOffSet = 0;
		String othtable = "";
		String trueName = "";
		String[] sTable = null;
		boolean isOth = false;
		Vector<String> vecTable = new Vector<String>();
		while (iOffSet < asWords.length) {
			if (iOffSet < asWords.length
					&& asWords[iOffSet].equalsIgnoreCase("from")) {
				int leftCount = 1;
				int rightCount = 0;
				String subfromSt = "";
				Vector<String> tabVec = new Vector<String>();
				iOffSet++;
				while (iOffSet < asWords.length
						&& !asWords[iOffSet].equalsIgnoreCase("where")) {

					if (asWords[iOffSet].equalsIgnoreCase("(")) {
						subfromSt = "(";
						while ((leftCount != rightCount)
								&& (iOffSet < asWords.length)) {
							iOffSet++;

							if (asWords[iOffSet].equalsIgnoreCase("(")) {
								leftCount++;
							} else if (asWords[iOffSet].equalsIgnoreCase(")")) {
								rightCount++;
							}

							subfromSt += " " + asWords[iOffSet];

						}

						tabVec.addElement(subfromSt);
						iOffSet++;
					}
					tabVec.addElement(asWords[iOffSet]);
					iOffSet++;
				}

				for (int newIndex = 0; newIndex < tabVec.size(); newIndex++) {
					othtable = tabVec.elementAt(newIndex).toString();
					trueName = othtable;
					isOth = false;

					if (!othtable.equalsIgnoreCase(sTableName)) {
						isOth = true;
						vecTable.addElement(othtable);
					}
					newIndex++;
					if (newIndex < tabVec.size()) {
						othtable = tabVec.elementAt(newIndex).toString();
						if (othtable.equalsIgnoreCase("as")) {
							newIndex++;
							othtable = tabVec.elementAt(newIndex).toString();
						}

						if (!othtable.equalsIgnoreCase(",")) {
							if (isOth) {
								vecTable.addElement(othtable);
							} else {
								if (sTableAlias != null
										&& sTableAlias.trim().length() > 0) {
									if (!othtable.equalsIgnoreCase(sTableAlias)) {
										vecTable.addElement(trueName);
										vecTable.addElement(othtable);
									}
								}
							}
							newIndex++;
						}
					}

				}
			}
			iOffSet++;
		}
		sTable = new String[vecTable.size()];
		// 从哈西表中提取记录
		for (int i = 0; i < vecTable.size(); i++) {
			sTable[i] = (String) vecTable.elementAt(i);
		}
		return sTable;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-2 16:02:34)
	 * 
	 * @return boolean
	 */
	public boolean haveTab(String Sql, String[] v_table) {
		boolean MasterTab = false;
		int dotIndex = 0;
		int i = 0;
		String s = "";
		while (i < v_table.length) {
			s = v_table[i];
			dotIndex = Sql.indexOf(s.trim() + ".");
			if (dotIndex > 0) {
				// if
				// (Sql.substring(dotIndex+1,dotIndex+2).equalsIgnoreCase("."))
				// {
				MasterTab = true;
				return MasterTab;
				// }
			}
			i++;
		}
		return MasterTab;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-2 16:02:34)
	 * 
	 * @return boolean
	 */
	public boolean haveTab(String Sql, String Table) {
		boolean MasterTab = false;
		if (Table.trim() == null || Table.trim().length() == 0) {
			return MasterTab;
		}

		int dotIndex = Sql.indexOf(Table + ".");
		if (dotIndex > 0) {
			// if (Sql.substring(dotIndex+1,dotIndex+2).equalsIgnoreCase(".")) {
			MasterTab = true;
			// }
		}

		return MasterTab;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-5-25 19:42:34)
	 * 
	 * @return boolean
	 */
	public boolean isMasterTab(String[] v_table, String sql) {
		boolean MasterTab = false;
		int i = 0;
		int dotIndex = 0;
		String s = "";
		dotIndex = sql.indexOf(".");
		if (dotIndex >= 0) {
			String tabName = sql.substring(0, dotIndex).trim().toLowerCase();
			while (i < v_table.length) {
				s = v_table[i];
				if (s.trim().equalsIgnoreCase(tabName)) {
					MasterTab = true;
					return MasterTab;
				}
				i++;
			}
		}
		return MasterTab;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-5-25 19:42:34)
	 * 
	 * @return boolean
	 */
	public boolean isMasterTab(String Sql, String Table) {
		boolean MasterTab = false;

		int dotIndex = Sql.indexOf(".");
		if (dotIndex >= 0) {
			String tabName = Sql.substring(0, dotIndex).trim().toLowerCase();
			if (tabName.equalsIgnoreCase(Table)) {
				MasterTab = true;

			}
		}

		return MasterTab;
	}

	public String charTypes[] = new String[] { "char", "varchar" };

	// 当前的连接
	private java.sql.Connection m_con = null;

	// 数据库版本
	private int m_DbVersion = 0;

	/**
	 * 此处插入方法说明。 功能：取得目标数据库版本 创建日期：(2005-01-05 11:44:11)
	 */
	public int getDbVersion() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDbVersion");
		// 取得目标数据库版本
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDbVersion Over");
		return m_DbVersion;
	}

	/**
	 * 此处插入方法说明。 功能：设置目标数据库的连接 创建日期：(2005-01-25 09:44:00)
	 */
	public void setDbVersion(int dbVersion) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setConnection");
		m_DbVersion = dbVersion;
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setConnection Over");
	}
}