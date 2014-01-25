package com.yonyou.nc.codevalidator.sqltrans;

import java.util.ArrayList;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class TranslateToOracle extends TranslatorObject {
	public TranslateToOracle() {
		super(ORACLE);
		m_apsFunList = m_apsOracleFunctions;
		m_apiErrorList = m_apiOracleError;
	}

	public String getSql() throws Exception {
		Logger.setThreadState("oracle translator : getSql");
		translateSql();
		String sResult = "";
		if (isTrigger) {
			sResult = m_sResorceSQL;
			sResult = sResult.replaceAll("\r\n", "\n");
		} else {
			if (m_sbDestinationSql == null)
				return null;
			sResult = m_sbDestinationSql.toString();
			if (sResult.endsWith(";")) {
				sResult = sResult.substring(0, sResult.length() - 1);
			}
			if (sResult.indexOf("ltrim_case") >= 0) {
				sResult = sResult.replaceAll("ltrim_case", "ltrim");
			}
			if (sResult.indexOf("rtrim_case") >= 0) {
				sResult = sResult.replaceAll("rtrim_case", "rtrim");
			}
		}
		Logger.setThreadState("oracle translator : getSql Over");
		return sResult;
	}

	/**
	 * 转换Create语句
	 */
	private void translateCreate() throws Exception {
		Logger.setThreadState("oracle translator : translateCreate");
		m_sbDestinationSql = new StringBuffer("");
		for (int i = 0; i < m_asSqlWords.length; i++) {
			if (m_asSqlWords[i].equalsIgnoreCase("varchar")) {
				m_asSqlWords[i] = "varchar2";
			} else if (m_asSqlWords[i].equalsIgnoreCase("nvarchar")) {
				m_asSqlWords[i] = "varchar2";
			} else if (m_asSqlWords[i].equalsIgnoreCase("nchar")) {
				m_asSqlWords[i] = "char";
			} else if (m_asSqlWords[i].equalsIgnoreCase("float")) {
				m_asSqlWords[i] = "number";
			} else if (m_asSqlWords[i].equalsIgnoreCase("datetime")
					|| m_asSqlWords[i].equalsIgnoreCase("smalldatetime")) {
				m_asSqlWords[i] = "date";
			}
			m_sbDestinationSql.append(" " + m_asSqlWords[i]);
		}
		Logger.setThreadState("oracle translator :translateCreate Over");
	}

	/**
	 * 转换Delete语句
	 */
	private StringBuffer translateDelete(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateDelete");
		Logger.setThreadState("oracle translator :translateDelete Over");
		return translateSelect(asSqlWords);
	}

	/**
	 * 转换Drop语句
	 */
	private void translateDrop() throws Exception {
		Logger.setThreadState("oracle translator :translateDrop");
		m_sbDestinationSql = new StringBuffer(m_sResorceSQL);
		Logger.setThreadState("oracle translator :translateDrop Over");
	}

	/**
	 * 根据函数对照表进行函数转换
	 */
	private void translateFunction() throws Exception {
		Logger.setThreadState("oracle translator :translateFunction");
		String sWord = null;

		int iOffSet = -1;

		while (iOffSet < m_asSqlWords.length) {
			iOffSet++;
			sWord = m_asSqlWords[iOffSet];
			if ((iOffSet + 1) >= m_asSqlWords.length)
				break;
			if (m_asSqlWords[iOffSet + 1].equals("(")) {
				m_asSqlWords[iOffSet] = getFunction(sWord);
				iOffSet++;
			}
		}
		Logger.setThreadState("oracle translator :translateFunction Over");
	}

	/**
	 * 转换Insert语句
	 */
	private StringBuffer translateInsert(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateInsert");
		Logger.setThreadState("oracle translator :translateInsert Over");
		return translateSelect(asSqlWords);
	}

	/**
	 * 处理连接更新 创建日期：(00-6-9 8:38:35)
	 * 
	 * @return java.lang.String[]
	 * @param asSqlWords
	 *            java.lang.String[]
	 * 
	 *            格式： 第一种情况： update table1 set col1=b.col2 from table1 a, table2
	 *            b where a.col3=b.col3 -> update table1 a set col1=(select
	 *            b2.col2 from table2 b where a.col3=b.col3)
	 * 
	 *            第二种情况 update table1 set col1=col1+常量a, a.col2=b.col2+a.col2
	 *            from table1 a,table2 b
	 * 
	 *            where a.col3=b.col3 and a.col2=常量 -> update table1 a set
	 *            col1=col1+常量a,col2=(select b2.col2+a.col2 from table2 b
	 * 
	 *            where a.col3=b.col3 and a.col2=常量b)
	 * 
	 *            where a.col2=常量b
	 */
	public String[] translateJoinUpdate(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateJoinUpdate");

		int iOffSet = 0; // 偏移量
		int iOffSet1 = 1; // 取表名的偏移量
		String[] asWords = asSqlWords;
		// String sSql = ""; //返回值
		boolean bFind = false;
		// String sLeftField = "";
		// String sRightField = "";
		// java.util.Vector vSetList = new java.util.Vector(); //存放set语句
		String sTableName = ""; // 表名
		String sTableAlias = ""; // 表的别名
		// String s = ""; //中间变量
		// Vector vecTable = new Vector();

		// 是否存在连接更新，并取得更新表的表名和别名
		if (iOffSet1 < asSqlWords.length && iOffSet1 + 5 < asSqlWords.length && asSqlWords[iOffSet1].equals("/")
				&& asSqlWords[iOffSet1 + 1].equals("*") && asSqlWords[iOffSet1 + 2].equals("+")) {
			iOffSet1 += 3;
			while (!asSqlWords[iOffSet1].equals("*") && !asSqlWords[iOffSet1 + 1].equals("/")) {
				iOffSet1 += 1;
			}
			iOffSet1 += 2;
		}
		sTableName = asWords[iOffSet1];

		int iSelectNum = 0;
		int iFromNum = 0;

		// 计数小于输入字符串集的长度
		while (iOffSet < asWords.length) {
			// 若当前字符串是“from”
			if (asWords[iOffSet].equalsIgnoreCase("from")) {
				iFromNum++;

				if (iFromNum > iSelectNum) {
					// 记录当前位置

					iOffSet++;
					// 计数小于输入字符串集的长度
					while (iOffSet < asWords.length) {
						// 若当前字符串等于表名
						if (asWords[iOffSet].equalsIgnoreCase(sTableName)) {
							// 当前位置加1
							// iOffSet++;
							// 若当前字符串为逗号，则跳出循环
							// if (iOffSet < asWords.length
							// && (asWords[iOffSet].equalsIgnoreCase(",")
							// || asWords[iOffSet].equalsIgnoreCase("where")))
							{
								if (iOffSet >= 1
										&& (asWords[iOffSet - 1].equalsIgnoreCase(",") || asWords[iOffSet - 1]
												.equalsIgnoreCase("from"))) {
									// sTableAlias = asWords[iOffSet - 1];

									if (iOffSet + 1 < asWords.length) {
										if (asWords[iOffSet + 1].equalsIgnoreCase("as")) {
											if (iOffSet + 2 < asWords.length) {
												sTableAlias = asWords[iOffSet + 2];
											}
										} else {
											if (!asWords[iOffSet + 1].equals(",")
													&& !asWords[iOffSet + 1].equalsIgnoreCase("where")
													&& !asWords[iOffSet + 1].equalsIgnoreCase("left")
													&& !asWords[iOffSet + 1].equalsIgnoreCase("right")
													&& !asWords[iOffSet + 1].equals("(")
													&& !asWords[iOffSet + 1].equals(")")) {
												sTableAlias = asWords[iOffSet + 1];
											}
										}
									}
								} /*
								 * else if (iOffSet >= 2 && (asWords[iOffSet -
								 * 2].equalsIgnoreCase(","))) { sTableAlias =
								 * ""; }
								 */
								else if (iOffSet >= 2
										&& (asWords[iOffSet - 2].equalsIgnoreCase(",") || asWords[iOffSet - 2]
												.equalsIgnoreCase("from"))) {
									sTableName = asWords[iOffSet - 1];
									sTableAlias = asWords[iOffSet];
								} else if (iOffSet >= 3
										&& (asWords[iOffSet - 3].equalsIgnoreCase(",") || asWords[iOffSet - 3]
												.equalsIgnoreCase("from"))) {
									sTableName = asWords[iOffSet - 2];
									sTableAlias = asWords[iOffSet];
								} /*
								 * else { //sTableName=asWords[iOffSet-2];
								 * sTableAlias = sTableName; }
								 */
								break;
							}

						} else {
							iOffSet++;
						}
					}
					break;
				}
			}

			if (asWords[iOffSet].equalsIgnoreCase("select")) {
				iSelectNum++;
			}

			iOffSet++;

		} // while结束处

		if (iFromNum > iSelectNum) {
			bFind = true;
		}
		if (!bFind) // 没有发现连接更新或子查询
		{
			return asWords;
		} else {
			Logger.setThreadState("oracle translator :translateJoinUpdate Over");
			return translateJoinUpdate(asSqlWords, sTableName, sTableAlias);
		}
	}

	/**
	 * 翻译Select语句,进行: 函数转换 连接转换(左连接) 转换CASE WHEN 语句 模式匹配
	 */
	private StringBuffer translateSelect(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateSelect");
		int iOffSet = 0;
		String sSql = new String();
		String sWord = "";
		String sPreWord;

		boolean dontHaveWhere = true;

		// added by hegy
		int orderbyFrom = -1;
		int orderbyTo = -1;

		boolean ifTop = false;

		TransUnit aTransUnit = null;

		String rowNum = "";

		// 临时表判定
		boolean hasTempTable = false;

		while (iOffSet < asSqlWords.length) {
			sPreWord = sWord;
			// 取得当前单词
			sWord = asSqlWords[iOffSet];
			String lowWord = sWord.toLowerCase();
			if (lowWord.startsWith("temp") || lowWord.startsWith("temq") || lowWord.startsWith("tmp")
					|| lowWord.startsWith("ut")) {
				hasTempTable = true;
			}

			// 在此对函数进行处理
			// 如果当前单词为函数名称
			if ((iOffSet + 1 < asSqlWords.length) && isFunctionName(sWord, asSqlWords[iOffSet + 1])) {
				aTransUnit = dealFunction(asSqlWords, sWord, iOffSet);

				iOffSet = aTransUnit.getIOffSet();

				if (iOffSet > asSqlWords.length - 1) {
					// 函数嵌套
					return null;
				}
			}
			// 处理Oracle优化关键字
			else if (iOffSet < asSqlWords.length && iOffSet + 5 < asSqlWords.length && asSqlWords[iOffSet].equals("/")
					&& asSqlWords[iOffSet + 1].equals("*") && asSqlWords[iOffSet + 2].equals("+")) {
				iOffSet += 3;
				m_sbDestinationSql.append(" /*+ ");
				while (!asSqlWords[iOffSet].equals("*") && !asSqlWords[iOffSet + 1].equals("/")) {
					m_sbDestinationSql.append(asSqlWords[iOffSet] + " ");
					iOffSet += 1;
				}
				iOffSet += 2;
				m_sbDestinationSql.append(" */ ");
			}
			// (字段1,字段2) in (…)的支持
			else if (sWord.equalsIgnoreCase("&&")) {
				m_sbDestinationSql.append(",");
				iOffSet += 1;
			}
			// 处理PI()
			else if (iOffSet < asSqlWords.length && sWord.equalsIgnoreCase("PI") && asSqlWords[iOffSet + 1].equals("(")
					&& asSqlWords[iOffSet + 2].equals(")")) {
				m_sbDestinationSql.append(" 3.1415926535897931");
				iOffSet += 3;
			}
			// 处理取模%
			else if (iOffSet + 2 < asSqlWords.length && asSqlWords[iOffSet + 1].equals("%")) {
				m_sbDestinationSql.append(" mod(" + sWord + "," + asSqlWords[iOffSet + 2] + ")");
				iOffSet += 3;
			}
			// 处理getdate()
			else if (iOffSet + 2 < asSqlWords.length && sWord.equalsIgnoreCase("getdate")
					&& asSqlWords[iOffSet + 1].equals("(") && asSqlWords[iOffSet + 2].equals(")")) {
				m_sbDestinationSql.append(" sysdate");
				iOffSet += 3;
			}
			// 处理模式匹配符
			else if (iOffSet < asSqlWords.length && sWord.equalsIgnoreCase("like")) {
				aTransUnit = dealLike(asSqlWords, sWord, iOffSet);
				iOffSet = aTransUnit.getIOffSet();
			}
			// 处理top
			else if (sWord.equalsIgnoreCase("top")) {
				ifTop = true;
				rowNum = asSqlWords[iOffSet + 1];
				iOffSet += 2;

				// 保存前一个单词
				sPreWord = sWord;
				// 取得当前单词
				sWord = asSqlWords[iOffSet];
			}
			// 处理CASE WHEN
			else if (getDbVersion() <= 8 && sWord.equalsIgnoreCase("case")
					&& !asSqlWords[iOffSet + 1].equalsIgnoreCase("when")) {
				// 当为oracle8.x翻译
				aTransUnit = dealCaseWhen(asSqlWords, sWord, iOffSet);
				iOffSet = aTransUnit.getIOffSet();
				sSql = aTransUnit.getSql();
				translateSelect(parseSql(sSql));
			}
			// 处理表的别名,
			else if (iOffSet < asSqlWords.length && sWord.equalsIgnoreCase("as")
					&& !asSqlWords[0].equalsIgnoreCase("create")) {
				iOffSet++;
			}
			// 处理子查询
			else if (iOffSet < asSqlWords.length && sWord.equalsIgnoreCase("select") && iOffSet > 0
					&& asSqlWords[iOffSet - 1].equalsIgnoreCase("(")) {
				aTransUnit = dealSelect(asSqlWords, sWord, iOffSet);

				iOffSet = aTransUnit.getIOffSet();
			} else if (sWord.equals(";")) {
				iOffSet++;
			} else if (sWord.equalsIgnoreCase("from") && iOffSet < asSqlWords.length - 1
					&& asSqlWords[iOffSet + 1].equals("(")
					// && !asSqlWords[iOffSet + 2].equalsIgnoreCase("select")
					&& !getFirstTrueWord(asSqlWords, iOffSet).equalsIgnoreCase("select")) {
				/*
				 * aTransUnit = dealFrom(asSqlWords, sWord, iOffSet);
				 * 
				 * iOffSet = aTransUnit.getIOffSet();
				 * 
				 * asSqlWords = aTransUnit.getSqlArray();
				 */
				asSqlWords = trimKuohao(asSqlWords, iOffSet + 1);
			} else {
				if (iOffSet < asSqlWords.length) {

					// added by hgy
					if (sWord.equals("order") && (iOffSet + 1 < asSqlWords.length)) {
						if ("by".equals(asSqlWords[iOffSet + 1])) {
							orderbyFrom = m_sbDestinationSql.length();
						}
					}
					aTransUnit = dealOther(asSqlWords, sWord, iOffSet, ifTop, dontHaveWhere, rowNum, sPreWord);

					iOffSet = aTransUnit.getIOffSet();

					dontHaveWhere = aTransUnit.isDontHaveWhere();
					// added by hegy
					if (sWord.equals("by") && "order".equals(asSqlWords[iOffSet - 1])) {
						orderbyTo = m_sbDestinationSql.length();
					}
				} else
					break;
			}
		}

		/*
		 * 原为NCv3.1修改，但在top 语句为子查询时有问题->嵌套连接有错 if (dontHaveWhere && ifTop) {
		 * String m_DestinationSql= m_sbDestinationSql.toString();
		 * m_sbDestinationSql = new StringBuffer("select * from (");
		 * m_sbDestinationSql.append(m_DestinationSql);
		 * m_sbDestinationSql.append(" ) where "); m_sbDestinationSql.append( "
		 * rownum < " + (Integer.valueOf(rowNum).intValue() + 1)); }
		 */

		if (dontHaveWhere && ifTop) {

			// modifid by hegy
			if (orderbyFrom > -1 && orderbyTo > -1) {
				// int index = m_sbDestinationSql.indexOf("order by");
				int index = orderbyFrom;
				// if (index > -1) {
				StringBuffer first = new StringBuffer(m_sbDestinationSql.substring(0, index));
				StringBuffer second = new StringBuffer(m_sbDestinationSql.substring(index, m_sbDestinationSql.length()));
				first.append(" where ");
				first.append(" rownum<" + (Integer.valueOf(rowNum).intValue() + 1));
				first.append(" ").append(second.toString());
				m_sbDestinationSql = first;
				// } else {

			}

			else {
				m_sbDestinationSql.append(" where ");
				m_sbDestinationSql.append(" rownum<" + (Integer.valueOf(rowNum).intValue() + 1));
			}
		}
		/* 如果查询中有临时表，则在select 后加上 /*+dynamic_sampling(4) */
		/* liujb+ 通过数据库设置dynmaic_sampling=2对于临时表已经足够了，没有必要在这里设置 */
		// if (hasTempTable) {
		// if (m_sbDestinationSql.toString().indexOf("dynamic_sampling") < 0) {
		// String firstTrueWord = getFirstTrueWord(asSqlWords, -1);
		// if (firstTrueWord != null
		// && firstTrueWord.trim().equalsIgnoreCase("select")) {
		// int posSelect = m_sbDestinationSql.indexOf(firstTrueWord);
		// m_sbDestinationSql.insert(posSelect + 7,
		// "/*+dynamic_sampling(4)*/ ");
		// }
		// }
		// }

		Logger.setThreadState("oracle translator :translateSelect Over");
		return m_sbDestinationSql;
	}

	/**
	 * 根据语句类型进行转换
	 */

	private void translateSql() throws Exception {
		Logger.setThreadState("oracle translator :translateSql");
		if (m_asSqlWords == null) {
			m_sbDestinationSql = null;
			Logger.setThreadState("oracle translator :translateSql Over");
			return;
		}

		m_sbDestinationSql = new StringBuffer();
		m_sLeftWhere = new String();
		m_sLeftTable = new String();

		isTrigger = (m_asSqlWords.length >= 2
				&& (m_asSqlWords[0].equalsIgnoreCase("create") || m_asSqlWords[0].equalsIgnoreCase("replace")) && m_asSqlWords[1]
					.equalsIgnoreCase("trigger"))
				|| (m_asSqlWords.length >= 4 && m_asSqlWords[3].equalsIgnoreCase("trigger"));

		if (!isTrigger) {
			translateFunction();
			switch (getStatementType()) {
			case SQL_SELECT:
				translateSelect(m_asSqlWords);
				break;
			case SQL_INSERT:
				translateInsert(m_asSqlWords);
				break;
			case SQL_CREATE:
				translateCreate();
				break;
			case SQL_DROP:
				translateDrop();
				break;
			case SQL_DELETE:
				translateDelete(m_asSqlWords);
				break;
			case SQL_UPDATE:
				translateUpdate(m_asSqlWords);
				break;
			}
		}
	}

	/**
	 * 转换Update语句
	 */
	private StringBuffer translateUpdate(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateUpdate");

		// 处理连接更新
		String[] asWords = translateJoinUpdate(asSqlWords);

		Logger.setThreadState("oracle translator :translateUpdate Over");
		return translateSelect(asWords);
	}

	private int[][] m_apiOracleError = { { 942, 208 }, // 表或视图不存在
			{ 907, 2715 }, // 函数不存在
			{ 904, 207 }, // 无效的列名
			{ 398, 205 }, // 使用union的语句和目标列表应具有相同数目的表达式
			{ 516, 213 }, // 插入数据和表数据类型不一致
			{ 2627, 1 }, // 不能插入相同主键的记录
			{ 515, 1400 }, // 列值不能为空
			{ 8152, 1401 } // 插入的值对于列过大
	};

	// 函数对照表,列出sqlServer函数与Oracle函数的对应关系,
	private String[][] m_apsOracleFunctions = { { "isnull", "nvl" }, { "substring", "substr" }, { "ceiling", "ceil" } };

	Express_str express = new Express_str();

	public int INNERJOIN = 3;

	boolean isTrigger = false;

	public int LEFTJOIN = 1;

	public int RIGHTJOIN = 2;

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-28 18:38:31)
	 * 
	 * @return nc.bs.mw.sqltrans.TransUnit
	 * @param asSqlWords
	 *            java.lang.String[]
	 * @param sWord
	 *            java.lang.String
	 * @param iOffSet
	 *            int
	 */
	public TransUnit dealCaseWhen(String[] asSqlWords, String sWord, int iOffSet) throws Exception {
		Logger.setThreadState("oracle translator :dealCaseWhen");

		String sSql = "";

		if (iOffSet < asSqlWords.length) {
			TransUnit aTransUnit = getSubSql(asSqlWords, "case", "end", iOffSet);
			String[] newCaseSql = aTransUnit.getSqlArray();
			iOffSet = aTransUnit.getIOffSet();

			sSql = preTranCaseWhen(newCaseSql);

			sSql = translateCaseWhen(parseSql(sSql));

			StringOperator stOp = new StringOperator(sSql);
			stOp.replaceAllString("ltrim", "ltrim_case");
			stOp.replaceAllString("rtrim", "rtrim_case");

			sSql = stOp.toString();

			iOffSet++;

		}

		Logger.setThreadState("oracle translator :dealCaseWhen Over");
		return new TransUnit(null, sSql, iOffSet);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-28 18:38:31)
	 * 
	 * @return nc.bs.mw.sqltrans.TransUnit
	 * @param asSqlWords
	 *            java.lang.String[]
	 * @param sWord
	 *            java.lang.String
	 * @param iOffSet
	 *            int
	 */
	public TransUnit dealFrom(String[] asSqlWords, String sWord, int iOffSet) throws Exception {
		Logger.setThreadState("oracle translator :dealFrom");
		int includeIndex = iOffSet + 1;
		int includeNum = 0;

		while (asSqlWords[includeIndex].equals("(") && !asSqlWords[includeIndex + 1].equalsIgnoreCase("select")) {
			includeNum++;
			includeIndex++;
		}

		int leftIndex = 0;
		int rightIndex = 0;

		includeIndex = iOffSet + 1;

		while (includeIndex < asSqlWords.length && includeNum > 0) {
			if (asSqlWords[includeIndex].equals("(")) {
				leftIndex++;
				if (leftIndex == 1) {
					asSqlWords[includeIndex] = "";
				}
			} else if (asSqlWords[includeIndex].equals(")")) {
				rightIndex++;
				if (rightIndex == leftIndex) {
					asSqlWords[includeIndex] = "";
					includeNum--;
					includeIndex = iOffSet + 1;
					leftIndex = 0;
					rightIndex = 0;
					break;
				}
			}
			includeIndex++;
		}

		m_sbDestinationSql.append(" " + asSqlWords[iOffSet]);

		iOffSet++;

		Logger.setThreadState("oracle translator :dealFrom Over");
		return new TransUnit(asSqlWords, null, iOffSet);
	}

	private TransUnit dealFunction(String[] asSqlWords, String sWord, int iOffSet) throws Exception {
		Logger.setThreadState("oracle translator :dealFunction");

		ArrayList<String> vec = new ArrayList<String>();
		vec.add(asSqlWords[iOffSet]);
		iOffSet += 1;
		TransUnit aTransUnit = getSubSql(asSqlWords, "(", ")", iOffSet);
		String[] newFuncSql = aTransUnit.getSqlArray();
		iOffSet = aTransUnit.getIOffSet() + 1;

		for (int i = 0; i < newFuncSql.length; i++) {
			vec.add(newFuncSql[i]);
		}
		newFuncSql = new String[vec.size()];
		vec.toArray(newFuncSql);

		if (sWord.equalsIgnoreCase("left")) {
			translateFunLeft(newFuncSql);
		} else if (sWord.equalsIgnoreCase("right")) {
			translateFunRight(newFuncSql);
		} else if (sWord.equalsIgnoreCase("square")) {
			translateFunSquare(newFuncSql);
		} else if (sWord.equalsIgnoreCase("cast")) {
			translateFunCast(newFuncSql);
		} else if (sWord.equalsIgnoreCase("coalesce")) {
			translateFunCoalesce(newFuncSql);
		} else if (sWord.equalsIgnoreCase("ltrim")) {
			translateFunLtrim(newFuncSql);
		} else if (sWord.equalsIgnoreCase("rtrim")) {
			translateFunRtrim(newFuncSql);
		} else if (sWord.equalsIgnoreCase("patindex")) {
			translateFunPatindex(newFuncSql);
		} else if (sWord.equalsIgnoreCase("len")) {
			translateFunLen(newFuncSql);
		} else if (sWord.equalsIgnoreCase("round")) {
			translateFunRound(newFuncSql);
		} else if (sWord.equalsIgnoreCase("convert")) {
			translateFunConvert(newFuncSql);
		} else if (sWord.equalsIgnoreCase("dateadd")) {
			translateFunDateAdd(newFuncSql);
		} else if (sWord.equalsIgnoreCase("datediff")) {
			translateFunDateDiff(newFuncSql);
		}

		Logger.setThreadState("oracle translator :dealFunction Over");
		return new TransUnit(null, null, iOffSet);
	}

	private TransUnit dealLike(String[] asSqlWords, String sWord, int iOffSet) throws Exception {
		Logger.setThreadState("oracle translator :dealLike");
		String s = "";

		if (iOffSet + 1 < asSqlWords.length) {
			s = asSqlWords[iOffSet + 1];
			m_sbDestinationSql.append(" like " + s);
			iOffSet += 2;
		} else {
			m_sbDestinationSql.append(" like ");
			iOffSet++;
		}

		Logger.setThreadState("oracle translator :dealLike Over");
		return new TransUnit(null, null, iOffSet);
	}

	private TransUnit dealOther(String[] asSqlWords, String sWord, int iOffSet, boolean ifTop, boolean dontHaveWhere,
			String rowNum, String sPreWord) throws Exception {
		Logger.setThreadState("oracle translator :dealOther");
		if (iOffSet < asSqlWords.length) {
			if (!sWord.equals(",") && !(sWord.equals(")") && sPreWord.equals("("))
					&& !(sWord.equals("]") && sPreWord.equals("["))) {
				m_sbDestinationSql.append(" ");
			}
			// m_sbDestinationSql.append(" ");
			m_sbDestinationSql.append(asSqlWords[iOffSet]);

			if (asSqlWords[iOffSet].equalsIgnoreCase("where")) {
				dontHaveWhere = false;
				if (ifTop) {
					m_sbDestinationSql.append(" rownum<" + (Integer.valueOf(rowNum).intValue() + 1));
					m_sbDestinationSql.append(" and ");
				}
			}

			iOffSet++;
		}
		TransUnit aTransUnit = new TransUnit(null, null, iOffSet);
		aTransUnit.setDontHaveWhere(dontHaveWhere);
		Logger.setThreadState("oracle translator :dealOther Over");
		return aTransUnit;
	}

	private TransUnit dealSelect(String[] asSqlWords, String sWord, int iOffSet) throws Exception {
		Logger.setThreadState("oracle translator :dealSelect");

		if (iOffSet < asSqlWords.length) {
			TransUnit aTransUnit = getSubSql(asSqlWords, "(", ")", iOffSet);
			String[] newCaseSql = aTransUnit.getSqlArray();
			iOffSet = aTransUnit.getIOffSet();

			String newSql[] = new String[newCaseSql.length - 1];

			for (int i = 0; i < newSql.length; i++) {
				newSql[i] = newCaseSql[i];
			}

			// 处理子查询
			TranslateToOracle newTranslateToOracle = new TranslateToOracle();

			newTranslateToOracle.setSqlArray(newSql);

			m_sbDestinationSql.append(newTranslateToOracle.getSql());
		}

		Logger.setThreadState("oracle translator :dealSelect Over");
		return new TransUnit(null, null, iOffSet);
	}

	private String dealWhenAnd(String[] whenSql) throws Exception {
		Logger.setThreadState("oracle translator :dealWhenAnd");
		try {
			int offset = 0;
			ArrayList<String> vec = new ArrayList<String>();

			String str = "";

			// else
			if (offset < whenSql.length && !whenSql[offset].equalsIgnoreCase("when")) {
				while (offset < whenSql.length) {
					str += whenSql[offset] + " ";
					offset++;
				}
				return str;
			}

			// 遇到 then 之前
			while (offset < whenSql.length && !whenSql[offset].equalsIgnoreCase("then")) {
				if (!whenSql[offset].equalsIgnoreCase("when") && !whenSql[offset].equalsIgnoreCase("and")) {
					str += whenSql[offset] + " ";
				} else {
					if (offset > 0) {
						vec.add(str);
						str = "";
					}
				}
				offset++;
			}

			// 遇到 then 之后
			vec.add(str); // or then 之间
			str = "";
			offset++;

			while (offset < whenSql.length) {
				str += whenSql[offset] + " ";
				offset++;
			}

			String re = "";
			for (int i = 0; i < vec.size(); i++) {
				String strin = vec.get(i).toString();

				if (i < vec.size() - 1) {
					re += "when" + " " + strin + " " + "then case ";
				} else {
					re += "when" + " " + strin + " " + "then " + str;
				}
			}

			for (int i = 0; i < vec.size() - 1; i++) {

				re += " end";

			}

			Logger.setThreadState("oracle translator :dealWhenAnd Over");
			return re;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public String dealWhenOr(String[] whenSql) throws Exception {
		Logger.setThreadState("oracle translator :dealWhenOr");
		try {
			int offset = 0;
			ArrayList<String> vec = new ArrayList<String>();

			String str = "";

			// else
			if (offset < whenSql.length && !whenSql[offset].equalsIgnoreCase("when")) {
				while (offset < whenSql.length) {
					str += whenSql[offset] + " ";
					offset++;
				}
				return str;
			}
			// 遇到 then 之前
			while (offset < whenSql.length && !whenSql[offset].equalsIgnoreCase("then")) {
				if (!whenSql[offset].equalsIgnoreCase("when") && !whenSql[offset].equalsIgnoreCase("or")) {
					str += whenSql[offset] + " ";
				} else {
					if (offset > 0) // 不是第一个 when
					{
						vec.add(str);
						str = "";
					}
				}
				offset++;
			}

			// 遇到 then 之后
			vec.add(str); // or then 之间
			str = "";
			offset++;

			while (offset < whenSql.length) {
				str += whenSql[offset] + " ";
				offset++;
			}

			String re = "";
			for (int i = 0; i < vec.size(); i++) {
				String strin = vec.get(i).toString();

				re += "when" + " " + strin + " then " + str + " ";
			}
			Logger.setThreadState("oracle translator :dealWhenOr Over");
			return re;
		} catch (Exception ex) {
			throw ex;
		}
	}

	private String getFirstTrueWord(String[] asSqlWords, int iOffSet) {
		Logger.setThreadState("oracle translator :getFirstTrueWord");
		String trueWord = "";

		for (int i = iOffSet + 1; i < asSqlWords.length; i++) {
			if (!asSqlWords[i].equals("(")) {
				trueWord = asSqlWords[i];
				break;
			}
		}
		Logger.setThreadState("oracle translator :getFirstTrueWord Over");
		return trueWord;
	}

	private String preTranCaseWhen(String[] sql) throws Exception {
		Logger.setThreadState("oracle translator :preTranCaseWhen");
		try {
			String re = "";
			int offset = 0;

			while (offset < sql.length) // 格式化 when then之间的语句
			{
				if (sql[offset].equalsIgnoreCase("when")) {
					re += sql[offset] + " "; // +when

					offset++;

					String whenStr = "";

					while (offset < sql.length && !sql[offset].equalsIgnoreCase("then")) {
						whenStr += sql[offset] + " ";

						offset++;
					}

					// translateSelect(parseSql(sSql)); //张森，2001。10。18
					TranslateToOracle newTranslateToOracle = new TranslateToOracle();

					newTranslateToOracle.setSql(whenStr);

					whenStr = newTranslateToOracle.getSql();

					// 预处理，格式化case and or，使其成为指定的格式
					whenStr = express.getValue(whenStr);

					re += whenStr + " ";

					re += sql[offset] + " "; // +then

					offset++;

				} else {
					if (offset < sql.length) {
						re += sql[offset] + " ";
					}

					offset++;
				}
			}

			try {
				String re_or = preTranCaseWhen_or(parseSql(re)); // 先处理 or
				// return preTranCaseWhen_and(parseSql(re_or)); //处理 and
				Logger.setThreadState("oracle translator :preTranCaseWhen Over");
				return re_or;
			} catch (Exception ex) {
				throw ex;
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-3-25 10:06:40)
	 * 
	 * @return java.lang.String//原语句，以case开头 以end结束
	 * @param sql
	 *            java.lang.String[]
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public String preTranCaseWhen_and(String[] sql) throws Exception {
		Logger.setThreadState("oracle translator :preTranCaseWhen_and");
		try {
			int offset = 0;

			String result = "";

			while (offset < sql.length) // 有问题，else后的语句没解决
			{
				if (sql[offset].equalsIgnoreCase("when") || sql[offset].equalsIgnoreCase("else")) {
					String when_str = sql[offset]; // 加 when
					offset++;

					// 未遇到下一个 when else 前
					while ((offset < sql.length) && (!sql[offset].equalsIgnoreCase("when"))
							&& (!sql[offset].equalsIgnoreCase("else"))) {
						// 如果遇到嵌套CASE WHEN
						if (sql[offset].equalsIgnoreCase("case")) {
							// 张森，2001.3.23 加，改，处理嵌套 case when
							int caseCount = 1; // case计数
							int endCount = 0; // end技数

							if (offset < sql.length) {
								String sSql = "";

								sSql += " " + sql[offset];

								// 如果 case计数！= end技数，说明嵌套没结束
								while ((caseCount != endCount) && (offset < sql.length - 1)) {
									offset++;

									if (sql[offset].equalsIgnoreCase("case")) {
										caseCount++;
									} else if (sql[offset].equalsIgnoreCase("end")) {
										endCount++;
									}

									sSql += " " + sql[offset];

								}
								// sSql += " " + sql[offset];
								sSql = preTranCaseWhen_and(parseSql(sSql));
								when_str += (" " + sSql);
								offset++;

							} else
								break;
						} else // 没有遇到嵌套CASE WHEN
						{
							when_str += (" " + sql[offset]);
							offset++;
						}
					} // while ends
					result += (" " + dealWhenAnd(parseSql(when_str))); // deal
					// with
					// when
				} else {
					result += (" " + sql[offset]);
					offset++;
				}

			}
			Logger.setThreadState("oracle translator :preTranCaseWhen_and Over");
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-3-25 10:06:40)
	 * 
	 * @return java.lang.String//原语句，以case开头 以end结束
	 * @param sql
	 *            java.lang.String[]
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public String preTranCaseWhen_or(String[] sql) throws Exception {
		Logger.setThreadState("oracle translator :preTranCaseWhen_or");
		try {
			int offset = 0;

			String result = "";

			while (offset < sql.length) // 有问题，else后的语句没解决
			{
				if (sql[offset].equalsIgnoreCase("when") || sql[offset].equalsIgnoreCase("else")) {
					String when_str = sql[offset]; // 加 when
					offset++;

					// 未遇到下一个 when else end前
					while ((offset < sql.length) && (!sql[offset].equalsIgnoreCase("when"))
							&& (!sql[offset].equalsIgnoreCase("else")) && (!sql[offset].equalsIgnoreCase("end"))) {
						// 如果遇到嵌套CASE WHEN
						if (sql[offset].equalsIgnoreCase("case")) {
							// 张森，2001.3.23 加，改，处理嵌套 case when
							int caseCount = 1; // case计数
							int endCount = 0; // end技数

							if (offset < sql.length) {
								String sSql = "";

								sSql += " " + sql[offset];

								// 如果 case计数！= end技数，说明嵌套没结束
								while ((caseCount != endCount) && (offset < sql.length - 1)) {
									offset++;

									if (sql[offset].equalsIgnoreCase("case")) {
										caseCount++;
									} else if (sql[offset].equalsIgnoreCase("end")) {
										endCount++;
									}

									sSql += " " + sql[offset];

								}
								// sSql += " " + sql[offset];
								sSql = preTranCaseWhen_or(parseSql(sSql));
								when_str += (" " + sSql);
								offset++;

							} else
								break;
						} else // 没有遇到嵌套CASE WHEN
						{
							when_str += (" " + sql[offset]);
							offset++;
						}
					} // while ends
					result += (" " + dealWhenOr(parseSql(when_str))); // deal
					// with
					// when
				} else {
					result += (" " + sql[offset]);
					offset++;
				}

			}
			Logger.setThreadState("oracle translator :preTranCaseWhen_or Over");
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 转换 CASE WHEN 语句 参数: asSqlWords CaseWhen语句 返回: destSql 目标语句 规则: case s0
	 * when exp1 then exp2 when exp3 then exp4 ->
	 * decode(s0,exp1,exp2,exp3,exp4,exp5) else exp5 end
	 * 
	 * case when s0 is null then exp1 when s0=exp2 then exp3 when s0<=exp4 then
	 * exp5 ->
	 * decode(s0,null,exp1,exp2,exp3,least(s0,exp4),exp5,GREATEST(s0,exp6
	 * ),exp7,exp8) when s0>=exp6 then exp7 else exp8 end
	 */
	private String translateCaseWhen(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateCaseWhen");
		// 若第一个字符串不等于“when”，第一种情况
		if (!asSqlWords[1].equalsIgnoreCase("when")) {
			return translateSimpleCaseWhen(asSqlWords);
		} else { // 若第一个字符串是“when”，第二种情况
			Logger.setThreadState("oracle translator :translateCaseWhen Over");
			return translateComplexCaseWhen(asSqlWords);
		}
	}

	/**
	 * 转换 CASE WHEN 语句 参数: asSqlWords CaseWhen语句 返回: destSql 目标语句 规则: case s0
	 * when exp1 then exp2 when exp3 then exp4 ->
	 * decode(s0,exp1,exp2,exp3,exp4,exp5) else exp5 end
	 * 
	 * case when s0 is null then exp1 when s0=exp2 then exp3 when s0<=exp4 then
	 * exp5 ->
	 * decode(s0,null,exp1,exp2,exp3,least(s0,exp4),exp5,GREATEST(s0,exp6
	 * ),exp7,exp8) when s0>=exp6 then exp7 else exp8 end
	 */
	private String translateComplexCaseWhen(String[] asSqlWords) throws Exception {
		int iOff = 0;
		// 参数
		String s0 = "";

		String s1 = "";
		iOff++;

		int andNum = 0;

		// 第一个字符串是“when”，第二种情况

		// s1 = "nvl(decode(";
		s1 = "decode(decode(";

		int whenCount = 0;

		int elseCount = 0;

		// 若当前字符串不是“end”
		while ((iOff < asSqlWords.length) && !asSqlWords[iOff].trim().equalsIgnoreCase("end")) {
			// 若当前字符串是“is”
			if (asSqlWords[iOff].trim().equalsIgnoreCase("is")) {
				// 不支持is not 语句
				if (asSqlWords[iOff + 1].trim().equalsIgnoreCase("not")) {
					throw new Exception("'is not' clause is not superted in CASE...WHEN...END statement!");
				}
				s1 += " ,";
				iOff++;
				// 当下一个字符串不是“then”
				while (!asSqlWords[iOff].trim().equalsIgnoreCase("then")
						&& !asSqlWords[iOff].trim().equalsIgnoreCase("and")) {
					// 记录字符串
					s1 += " " + asSqlWords[iOff];
					iOff++;
				}
			} else // 若当前字符串不是“is”
			// 若当前字符串是等于号
			if (asSqlWords[iOff].trim().equals("=")) {
				s1 += " ,";
				iOff++;
				// 当前字符串不是“then”
				while (!asSqlWords[iOff].trim().equalsIgnoreCase("then")
						&& !asSqlWords[iOff].trim().equalsIgnoreCase("and")) {
					s1 += " " + asSqlWords[iOff];
					iOff++;
				}
			} else
			// 若当前字符串是小于等于号
			if (asSqlWords[iOff].trim().equals("<=")) {
				s1 += " ,least(" + s0 + ",";
				iOff++;
				// 当前字符串不是“then”
				while (!asSqlWords[iOff].trim().equalsIgnoreCase("then")
						&& !asSqlWords[iOff].trim().equalsIgnoreCase("and")) {
					s1 += " " + asSqlWords[iOff];
					iOff++;
				}
				s1 += " ) ";
			} else
			// 若当前字符串是大于等于号
			if (asSqlWords[iOff].trim().equals(">=")) {
				s1 += " ,greatest(" + s0 + ",";
				iOff++;
				// 当前字符串不是“then”
				while (!asSqlWords[iOff].trim().equalsIgnoreCase("then")
						&& !asSqlWords[iOff].trim().equalsIgnoreCase("and")) {
					s1 += " " + asSqlWords[iOff];
					iOff++;
				}
				s1 += " )";
			} else
			// 当前字符串是“then”
			if (asSqlWords[iOff].trim().equalsIgnoreCase("then")) {
				// s1="decode(decode("+s1;
				// s1 += " ,";
				s1 += ",1)";
				for (int andIndex = 0; andIndex < andNum; andIndex++) {
					s1 += ")";
				}
				// new add
				s1 += ",1,";
				// new add end
				iOff++;
				// 当前字符串不是“else”、“when”、“end”
				while (!asSqlWords[iOff].trim().equalsIgnoreCase("else")
						&& !asSqlWords[iOff].trim().equalsIgnoreCase("when")
						&& !asSqlWords[iOff].trim().equalsIgnoreCase("case")
						&& !asSqlWords[iOff].trim().equalsIgnoreCase("end")) {
					s1 += " " + asSqlWords[iOff];
					iOff++;
				}
				andNum = 0;
			} else
			// 当前字符串是“when”
			if (asSqlWords[iOff].trim().equalsIgnoreCase("when")) {
				iOff++;
				whenCount++;
				s0 = "";
				// 当前位置小于
				while (iOff < asSqlWords.length - 1) {
					if (asSqlWords[iOff].trim().equalsIgnoreCase("is") || asSqlWords[iOff].trim().equals(">=")
							|| asSqlWords[iOff].trim().equals("<=") || asSqlWords[iOff].trim().equals("=")) {

						break;
					} else {

						if (whenCount > 1) {
							// 嵌套下一个 when
							// s1 += " ), nvl(decode(";
							// s1 += " ), decode(decode(";
							s1 += " , decode(decode(";
						}

						String whenSt = "";
						while (iOff < asSqlWords.length
								&& !(asSqlWords[iOff].trim().equalsIgnoreCase("is")
										|| asSqlWords[iOff].trim().equals(">=") || asSqlWords[iOff].trim().equals("<=") || asSqlWords[iOff]
										.trim().equals("="))) {
							whenSt += asSqlWords[iOff];
							iOff++;
						}

						// 运算符左边

						s1 += " " + whenSt;

						s0 += " " + whenSt;

						// iOff++;
					}
				}
			} else
			// 当前字符串是“else”
			if (asSqlWords[iOff].trim().equalsIgnoreCase("else")) {

				// 嵌套 else
				elseCount++;
				// s1 += " ),";
				s1 += " ,";
				iOff++;
			} else
			// 当前字符串是关系符
			if (asSqlWords[iOff].trim().equals("<>") || asSqlWords[iOff].trim().equals("<")
					|| asSqlWords[iOff].trim().equals(">") || asSqlWords[iOff].trim().equals("!=")) {
				// 不支持<>,!=,<,> 操作符
				throw new Exception("'<>,!=,<,>' operator is not superted in CASE...WHEN...END statement!");
			} else if (asSqlWords[iOff].trim().equalsIgnoreCase("case")) {
				// 张森，2001.3.23 加，改，处理嵌套 case when
				int caseCount = 1; // case计数
				int endCount = 0; // end技数

				if (iOff < asSqlWords.length) {
					ArrayList<String> vec = new ArrayList<String>();

					vec.add(asSqlWords[iOff]);
					// String sSql = "";

					// sSql += " " + asSqlWords[iOff];

					// 如果 case计数！= end技数，说明嵌套没结束
					while ((caseCount != endCount) && (iOff < asSqlWords.length)) {
						iOff++;

						if (asSqlWords[iOff].trim().equalsIgnoreCase("case")) {
							caseCount++;
						} else if (asSqlWords[iOff].trim().equalsIgnoreCase("end")) {
							endCount++;
						}

						// sSql += " " + asSqlWords[iOff];
						vec.add(asSqlWords[iOff]);

					}

					String newStArray[] = new String[vec.size()];
					vec.toArray(newStArray);

					// 解决嵌套 case when

					s1 += " " + translateCaseWhen(newStArray) + " ";

					iOff++;

				} else
					break;
			} else
			// 当前字符串是“else”
			if (asSqlWords[iOff].trim().equalsIgnoreCase("and")) {
				andNum++;
				s1 += " ,decode(";
				s0 = "";
				iOff++;
			} else {
				s1 += " " + asSqlWords[iOff];
				s0 += " " + asSqlWords[iOff];
				iOff++;
			}
			// 若标志小于输入字符串集合的长度
			if (iOff < asSqlWords.length) {
				// 若当前字符串是“end”，则结束
				if (asSqlWords[iOff].trim().equalsIgnoreCase("end")) {
					/*
					 * if (elseCount <= 0) { s1 += " ),null"; } else { s1 += "
					 * )"; }
					 */
					if (elseCount > 0) {
						s1 += " ) ";
					}
					// 张森，2001。3。23
					break;
				}
			}
		}

		int rightKuoHao = whenCount;

		if (elseCount > 0)
			rightKuoHao = whenCount - 1;

		// 补齐右括号
		for (int i = 0; i < rightKuoHao; i++) {
			s1 += " ) ";
		}

		// return m_sbDestinationSql;
		Logger.setThreadState("oracle translator :translateComplexCaseWhen Over");
		return s1;
	}

	/**
	 * 转换Cast函数 参数: asWords cast函数子句
	 * 
	 * 规则: cast(f as char(n))->to_char(f) cast(f as date)->to_date(f)
	 */
	private void translateFunCast(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunCast");

		String s = new String();
		s = "";
		int iOff = 2;
		int iLBracket = 0;
		int iRBracket = 0;
		boolean charLenCtrl = false;
		boolean isDate = false;
		boolean isChar = false;
		String charLenth = null;
		// 未至倒数第一，则循环
		while (iOff < (asWords.length - 1)) {
			// 若当前字符串为左括号
			if (asWords[iOff].equals("("))
				iLBracket++;
			// 若当前字符串为右括号
			if (asWords[iOff].equals(")"))
				iRBracket++;
			// 若左标记等于右标记，且下一个字符串为“as”
			if (iLBracket == iRBracket && asWords[iOff + 1].equalsIgnoreCase("as")) {
				if (isDateType(asWords[iOff + 2])) {
					isDate = true;
					m_sbDestinationSql.append(" to_date(");
				} else if (isCharType(asWords[iOff + 2])) {
					isChar = true;
					if (iOff + 4 < asWords.length && asWords[iOff + 3].equals("(")) {
						charLenCtrl = true;
						charLenth = asWords[iOff + 4];
						m_sbDestinationSql.append(" substr(to_char(");
					} else {
						m_sbDestinationSql.append(" to_char(");
					}
				} else {
					m_sbDestinationSql.append(" cast(");
				}
				s += " " + asWords[iOff];
				iOff++;
				break;

			}
			s += " " + asWords[iOff];
			iOff++;
		}
		try {
			translateSelect(parseSql(s));
			if (isChar) {
				if (charLenCtrl)
					m_sbDestinationSql.append(") ,1," + charLenth + " )");
				else
					m_sbDestinationSql.append(" )");
			} else if (isDate) {
				m_sbDestinationSql.append(", 'yyyy-mm-dd hh24:mi:ss')");
			} else
				while (iOff < (asWords.length)) {
					m_sbDestinationSql.append(" " + asWords[iOff]);
					iOff++;
				}
		} catch (Exception e) {
			throw e;
		}
		Logger.setThreadState("oracle translator :translateFunCast Over");
	}

	/**
	 * 转换 coalesce 函数 参数: asSqlWords coalesce函数子句 规则:
	 * coalesce(exp1,exp2,exp3,exp4)->nvl(nvl(nvl(exp1,exp2),exp3,exp4))
	 */

	private void translateFunCoalesce(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunCoalesce");

		int iOffSet = 0;
		int iLBracket = 0;
		int iRBracket = 0;
		// 标识每个参数的起始位置
		int iStart = 0;
		ArrayList<String> v = new ArrayList<String>();
		String sWord = asSqlWords[iOffSet];

		TranslateToOracle newTranslateToOracle = new TranslateToOracle();

		// newTranslateToOracle.setSql(sSql);

		// m_sbDestinationSql.append(newTranslateToOracle.getSql());

		// 若当前字符串为“coalesce”，且下一个字符串为左括号
		if (sWord.equalsIgnoreCase("coalesce") && asSqlWords[iOffSet + 1].equals("(")) {
			m_bFinded = true;
			m_sbDestinationSql.append(" ");
			// 标识加1
			iOffSet++;
			// 起始位置加2
			iStart += 2;
			// 若标志小于字符串集长度，则循环
			while (iOffSet < asSqlWords.length) {
				iOffSet++;
				// 若当前字符串为左括号，则左标志加1
				if (asSqlWords[iOffSet].equals("("))
					iLBracket++;
				// 若当前字符串为右括号，则右标志加1
				if (asSqlWords[iOffSet].equals(")"))
					iRBracket++;
				// 若左右标识相等，且当前字符串为逗号
				if (iLBracket == iRBracket && asSqlWords[iOffSet].equals(",")) {
					String str = new String();
					// 保存从开始位置到标识位置的字符串
					for (int i = iStart; i < iOffSet; i++) {
						str += " " + asSqlWords[i];
					}

					if (str.indexOf("(") >= 0) {
						newTranslateToOracle.setSql(str);
						str = newTranslateToOracle.getSql();
					}
					v.add(str);
					// 开始位置为标识位置加1
					iStart = iOffSet + 1;
				}
				// 若左右标识相等，且下一个字符串为右括号
				if ((iLBracket == iRBracket) && asSqlWords[iOffSet + 1].equals(")")) {
					String str = new String();
					// 保存从开始位置到标识位置加1的字符串
					for (int i = iStart; i < iOffSet + 1; i++) {
						str += " " + asSqlWords[i];
					}

					if (str.indexOf("(") >= 0) {
						newTranslateToOracle.setSql(str);
						str = newTranslateToOracle.getSql();
					}

					v.add(str);
					String s = new String();

					// 增加nvl
					for (int i = 1; i < v.size(); i++)
						m_sbDestinationSql.append("nvl(");
					m_sbDestinationSql.append(v.get(0));

					// 格式整理
					for (int j = 1; j < v.size(); j++) {
						s += " " + ",";
						s += " " + v.get(j);
						s += " " + ")";
					}
					translateSelect(parseSql(s));
					break;
				}
			}
		}
		Logger.setThreadState("oracle translator :translateFunCoalesce Over");
	}

	/*
	 * 转换convert函数 参数: asWords convert函数子句
	 * 
	 * 规则: convert(char(n),f)->to_char(f) convert(date,f)->to_date(f)
	 */
	private void translateFunConvert(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunConvert");

		int iOff = 2;

		boolean charLenCtrl = false;
		boolean isDate = false;
		boolean isChar = false;
		String charLenth = null;
		String dataType = "";
		String col = "";
		// 取出函数的参数
		String params[] = getFunParam(asWords, iOff, asWords.length - 1);
		dataType = params[0];
		col = params[1];
		dataType = dataType.trim();
		String oldDataType = dataType;
		// 取出数据类型
		if (dataType.indexOf("(") > 0) {
			dataType = dataType.substring(0, dataType.indexOf("("));
		}
		// 日期数据类型
		if (isDateType(dataType)) {
			isDate = true;
			m_sbDestinationSql.append(" to_date(");
		} else if (isCharType(dataType)) {
			isChar = true;
			if (oldDataType.indexOf("(") > 0) {
				charLenCtrl = true;
				charLenth = oldDataType.substring(oldDataType.indexOf("(") + 1, oldDataType.length() - 1);
			}
			if (charLenCtrl)
				m_sbDestinationSql.append(" substr(to_char(");
			else
				m_sbDestinationSql.append(" to_char(");
		} else
			m_sbDestinationSql.append(" cast(");

		try {
			translateSelect(parseSql(col));

			if (isChar) {
				if (charLenCtrl)
					m_sbDestinationSql.append(") ,1," + charLenth + " )");
				else
					m_sbDestinationSql.append(" )");
			} else if (isDate) {
				m_sbDestinationSql.append(", 'yyyy-mm-dd')");
			} else
				m_sbDestinationSql.append(" as " + oldDataType + ")");

		} catch (Exception e) {
			throw e;
		}
		Logger.setThreadState("oracle translator :translateFunConvert Over");
	}

	/*
	 * 转换DateAdd函数 参数: asWords DateAdd函数子句 规则: DATEADD ( datepart , number, date
	 * ) datepart：yy、mm、dd
	 */
	private void translateFunDateAdd(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunDateAdd");

		int iOff = 2;

		String params[] = getFunParam(asWords, iOff, asWords.length - 1);

		String dateType = params[0].trim();

		String theNumber = params[1].trim();

		String theDate = params[2].trim();

		TranslateToOracle newTranslateToOracle = new TranslateToOracle();

		newTranslateToOracle.setSql(theDate);

		theDate = newTranslateToOracle.getSql();
		theDate = theDate.trim();

		if (!(theDate.toLowerCase().startsWith("to_date(") && theDate.toLowerCase().indexOf("'yyyy-mm-dd'") > 0)) {
			if (theDate.toLowerCase().startsWith("sysdate"))
				theDate = "to_date(sysdate)";
			else
				theDate = "to_date(substr(" + theDate + ",1,10),'yyyy-mm-dd')";
		}

		if (dateType.equalsIgnoreCase("yy") || dateType.equalsIgnoreCase("yyyy") || dateType.equalsIgnoreCase("year")) {
			theNumber = "(" + theNumber + "*12)";
			dateType = "mm";
		}

		if (dateType.equalsIgnoreCase("mm") || dateType.equalsIgnoreCase("m") || dateType.equalsIgnoreCase("month")) {
			m_sbDestinationSql.append(" add_months( " + theDate + ", " + theNumber + ") ");
		} else {
			m_sbDestinationSql.append(" " + theDate + "+" + theNumber + " ");
		}

		Logger.setThreadState("oracle translator :translateFunDateAdd Over");
	}

	/*
	 * 转换DateDiff函数 参数: asWords DateDiff函数子句
	 * 
	 * 规则: DATEDIFF ( datepart , startdate , enddate )
	 * datepart支持：yy、mm、dd、hh、mm、ss等
	 */
	private void translateFunDateDiff(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunDateDiff");

		int iOff = 2;

		String params[] = getFunParam(asWords, iOff, asWords.length - 1);

		String dateType = params[0].trim();

		String startDate = params[1].trim();

		String endDate = params[2].trim();

		TranslateToOracle newTranslateToOracle = new TranslateToOracle();

		newTranslateToOracle.setSql(startDate);

		startDate = newTranslateToOracle.getSql().trim();

		newTranslateToOracle.setSql(endDate);

		endDate = newTranslateToOracle.getSql().trim();

		if (!(startDate.toLowerCase().startsWith("to_date(") && startDate.toLowerCase().indexOf("'yyyy-mm-dd'") > 0)) {
			if (startDate.toLowerCase().startsWith("sysdate"))
				startDate = "to_date(sysdate)";
			else
				startDate = "to_date(substr(" + startDate + ",1,10),'yyyy-mm-dd')";
		}

		if (!(endDate.toLowerCase().startsWith("to_date(") && endDate.toLowerCase().indexOf("'yyyy-mm-dd'") > 0)) {
			if (endDate.toLowerCase().startsWith("sysdate"))
				endDate = "to_date(sysdate)";
			else
				endDate = "to_date(substr(" + endDate + ",1,10),'yyyy-mm-dd')";
		}

		if (dateType.equalsIgnoreCase("yy") || dateType.equalsIgnoreCase("yyyy") || dateType.equalsIgnoreCase("year")) {
			m_sbDestinationSql.append(" extract(year from  " + endDate + ") - extract(year from  " + startDate + ")");
		} else if (dateType.equalsIgnoreCase("mm") || dateType.equalsIgnoreCase("m")
				|| dateType.equalsIgnoreCase("month")) {
			m_sbDestinationSql.append(" months_between( " + endDate + ", " + startDate + ") ");
		} else {
			m_sbDestinationSql.append(" (" + endDate + "-" + startDate + ") ");
		}

		Logger.setThreadState("oracle translator :translateFunDateDiff Over");
	}

	/**
	 * 转换left函数 参数: asWords: left函数语句 规则: left(str,n)->substr(str,1,4)
	 */
	private void translateFunLeft(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunLeft");

		String s = new String();
		int iOff = 2;
		int iLBracket = 0;
		int iRBracket = 0;

		s = "substr(";

		while (iOff < (asWords.length)) {
			if (asWords[iOff].equals("("))
				iLBracket++;
			if (asWords[iOff].equals(")"))
				iRBracket++;
			if ((iLBracket == iRBracket) && asWords[iOff + 1].equals(",")) {
				s += " " + asWords[iOff];
				iOff++;
				s += ",1";
			}
			s += " " + asWords[iOff];
			iOff++;
		}
		try {
			translateSelect(parseSql(s));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		Logger.setThreadState("oracle translator :translateFunLeft Over");
	}

	private void translateFunLen(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunLen");
		String sSql = "";
		int iOffSet = 2;
		int iLBracket = 0;
		int iRBracket = 0;

		sSql = sSql + "length(rtrim(";
		iLBracket = 1;
		while (iOffSet < asSqlWords.length) {
			if (asSqlWords[iOffSet].equals("("))
				iLBracket++;
			if (asSqlWords[iOffSet].equals(")"))
				iRBracket++;
			if ((iLBracket == iRBracket + 1) && asSqlWords[iOffSet + 1].equals(")")) {
				sSql += " " + asSqlWords[iOffSet];
				break;
			}
			sSql += " " + asSqlWords[iOffSet];
			iOffSet++;
		}
		sSql += "))";
		try {
			translateSelect(parseSql(sSql));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		Logger.setThreadState("oracle translator :translateFunLen Over");
	}

	/**
	 * 转换Rtrim函数 参数: asSqlWords: Ltrim函数子句 规则: Ltrim(str)->Ltrim(str,' ')
	 */
	private void translateFunLtrim(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunLtrim");
		int iOff = 2;
		int iLBracket = 0;
		int iRBracket = 0;
		String s = new String();

		m_sbDestinationSql.append(" ltrim(");
		while (iOff < asSqlWords.length) {
			if (asSqlWords[iOff].equals("(")) {
				iLBracket++;
			}
			if (asSqlWords[iOff].equals(")")) {
				iRBracket++;
			}
			if (iLBracket == iRBracket && asSqlWords[iOff + 1].equals(")")) {
				if (iLBracket > 0) {
					for (int i = 2; i < iOff + 1; i++) {
						s += " " + asSqlWords[i];
					}
					try {
						translateSelect(parseSql(s));
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
					}
				} else {
					for (int i = 2; i < iOff + 1; i++) {
						m_sbDestinationSql.append(" " + asSqlWords[i]);
					}
				}
				m_sbDestinationSql.append(",' ')");
				break;
			} else if (asSqlWords[asSqlWords.length - 1].equals(")")) {
				// 函数嵌套
				String[] newFunSql = new String[asSqlWords.length - (iOff + 1)];
				for (int index = 0; index < newFunSql.length; index++) {
					newFunSql[index] = asSqlWords[iOff];
					iOff++;
				}
				translateSelect(newFunSql);
				m_sbDestinationSql.append(",' ')");
				break;
			} else {
				m_sbDestinationSql.append(asSqlWords[iOff]);
				iOff++;
				throw new Exception(asSqlWords[0] + " After this the parameter does not match!!");
			}
		}
		Logger.setThreadState("oracle translator :translateFunLtrim Over");
	}

	/**
	 * 转换patindex函数 参数: asWords: patindex函数语句 规则:
	 * patindex('%exp1%',exp2)->instr(exp2,'exp1',1,1)
	 */
	private void translateFunPatindex(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunPatindex");

		String sSql = new String();
		int iOff = 2;
		String sWord = new String();
		sSql = "instr(";

		sWord = asWords[iOff];
		if (sWord.length() > 4 && asWords[iOff + 1].equals(",")) {
			String s = "";
			s += "'" + sWord.substring(2, sWord.length() - 2) + "'";

			iOff += 2;
			while (iOff < asWords.length) {
				if (!asWords[iOff].equals(")")) {
					sSql += " " + asWords[iOff];
					iOff++;
				} else {
					sSql += "," + s + ",1,1)";
					break;
				}
			}
			try {
				translateSelect(parseSql(sSql));
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		} else {
			for (int i = 0; i < asWords.length; i++) {
				m_sbDestinationSql.append(asWords[i]);
			}
		}
		Logger.setThreadState("oracle translator :translateFunPatindex Over");
	}

	/**
	 * 转换right函数 参数: asWords: right函数子句 规则:
	 * right(f,n)->substr(f,length(f)-n+1,n)
	 */
	private void translateFunRight(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunRight");
		String s = new String();
		int iOff = 2;
		int iLBracket = 0;
		int iRBracket = 0;
		s = "substr(";
		while (iOff < (asWords.length)) {
			if (asWords[iOff].equals("("))
				iLBracket++;
			if (asWords[iOff].equals(")"))
				iRBracket++;
			if ((iLBracket == iRBracket) && asWords[iOff + 1].equals(",")) {
				s += " " + asWords[iOff];
				iOff++;
				s += ",length(";
				for (int i = 2; i < iOff; i++) {
					s += " " + asWords[i];
				}
				s += ")-";
				for (int j = iOff + 1; j < asWords.length - 1; j++) {
					s += " " + asWords[j];
				}
				s += "+1";
			}
			s += " " + asWords[iOff];
			iOff++;
		}
		try {
			translateSelect(parseSql(s));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		Logger.setThreadState("oracle translator :translateFunRight Over");
	}

	/**
	 * 转换Rtrim函数 参数: asSqlWords: Ltrim函数子句 规则: Ltrim(str)->Ltrim(str,' ')
	 */
	private void translateFunRound(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunRound");
		int iOff = 2;
		int iLBracket = 0;
		int iRBracket = 0;
		int commaCount = 0;
		int doubleQuotationCount = 0;
		int singleQuotationCount = 0;

		int firstCommaIndex = 0;
		int secondCommaIndex = 0;

		String theNumber = "";
		String theLength = "";
		String theTyle = "";

		TranslateToOracle newTranslateToOracle = null;

		while (iOff < asSqlWords.length - 1) {
			if (asSqlWords[iOff].equals("(")) {
				iLBracket++;
			}
			if (asSqlWords[iOff].equals(")")) {
				iRBracket++;
			}
			if (asSqlWords[iOff].equals("\'")) {
				singleQuotationCount++;
			}
			if (asSqlWords[iOff].equals("\"")) {
				doubleQuotationCount++;
			}
			if (asSqlWords[iOff].equals(",") && iRBracket == iLBracket && doubleQuotationCount % 2 == 0
					&& singleQuotationCount % 2 == 0) {
				commaCount++;
				if (commaCount == 1) {
					firstCommaIndex = iOff;

					for (int i = 2; i < iOff; i++) {
						theNumber += " " + asSqlWords[i];
					}

					if (iOff - 2 > 1) {
						if (newTranslateToOracle == null) {
							newTranslateToOracle = new TranslateToOracle();
						}

						newTranslateToOracle.setSql(theNumber);

						theNumber = newTranslateToOracle.getSql();
					}
				} else {
					secondCommaIndex = iOff;

					for (int i = firstCommaIndex + 1; i < iOff; i++) {
						theLength += " " + asSqlWords[i];
					}

					if (iOff - (firstCommaIndex + 1) > 1) {
						if (newTranslateToOracle == null) {
							newTranslateToOracle = new TranslateToOracle();
						}

						newTranslateToOracle.setSql(theLength);

						theLength = newTranslateToOracle.getSql();
					}
				}
			}
			iOff++;
		}

		String s = " ";

		if (commaCount == 0) {
			for (int i = 0; i < asSqlWords.length; i++) {
				s += asSqlWords[i];
			}
		}

		else if (commaCount == 1) {
			for (int i = firstCommaIndex + 1; i < asSqlWords.length - 1; i++) {
				theLength += " " + asSqlWords[i];
			}

			if ((asSqlWords.length - 1) - (firstCommaIndex + 1) > 1) {
				if (newTranslateToOracle == null) {
					newTranslateToOracle = new TranslateToOracle();
				}

				newTranslateToOracle.setSql(theLength);

				theLength = newTranslateToOracle.getSql();
			}

			s = " round(" + theNumber + ", " + theLength + ") ";
		} else {
			for (int i = secondCommaIndex + 1; i < asSqlWords.length - 1; i++) {
				theTyle += " " + asSqlWords[i];
			}

			if ((asSqlWords.length - 1) - (secondCommaIndex + 1) > 1) {
				if (newTranslateToOracle == null) {
					newTranslateToOracle = new TranslateToOracle();
				}

				newTranslateToOracle.setSql(theTyle);

				theTyle = newTranslateToOracle.getSql();
			}

			int tyle = Integer.valueOf(theTyle.trim()).intValue();

			if (tyle == 0) {
				s = " round(" + theNumber + ", " + theLength + ") ";
			} else {
				s = " floor(" + theNumber + "*(power(10," + theLength + ")))/(power(10," + theLength + ")) ";
			}
		}
		m_sbDestinationSql.append(s);
		Logger.setThreadState("oracle translator :translateFunRound Over");
	}

	/**
	 * 转换Rtrim函数 参数: asSqlWords: Rtrim函数子句 规则: Rtrim(str)->Rtrim(str,' ')
	 */
	private void translateFunRtrim(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunRtrim");
		int iOff = 2;
		int iLBracket = 0;
		int iRBracket = 0;
		String s = new String();

		m_sbDestinationSql.append(" rtrim(");
		while (iOff < asSqlWords.length) {
			if (asSqlWords[iOff].equals("(")) {
				iLBracket++;
			}
			if (asSqlWords[iOff].equals(")")) {
				iRBracket++;
			}
			if (iLBracket == iRBracket && asSqlWords[iOff + 1].equals(")")) {
				if (iLBracket > 0) {
					for (int i = 2; i < iOff + 1; i++) {
						s += " " + asSqlWords[i];
					}
					try {
						translateSelect(parseSql(s));
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
					}
				} else {
					for (int i = 2; i < iOff + 1; i++) {
						m_sbDestinationSql.append(" " + asSqlWords[i]);
					}
				}
				m_sbDestinationSql.append(",' ')");
				break;
			} else if (asSqlWords[asSqlWords.length - 1].equals(")")) {
				// 函数嵌套
				String[] newFunSql = new String[asSqlWords.length - (iOff + 1)];
				for (int index = 0; index < newFunSql.length; index++) {
					newFunSql[index] = asSqlWords[iOff];
					iOff++;
				}
				translateSelect(newFunSql);
				// zhangsd modify at 2002-09-01
				if (!newFunSql[newFunSql.length - 1].equalsIgnoreCase("' '")
						&& !newFunSql[newFunSql.length - 1].equalsIgnoreCase(",")) {
					m_sbDestinationSql.append(",' '");
				}
				m_sbDestinationSql.append(" )");
				break;
			} else {
				m_sbDestinationSql.append(asSqlWords[iOff]);
				iOff++;
				throw new Exception(asSqlWords[0] + " After this the parameter does not match!!");
			}
		}
		Logger.setThreadState("oracle translator :translateFunRtrim Over");
	}

	/**
	 * 转换Square函数 参数: asWords square函数子句 规则: square(f)->power(f,2)
	 */
	private void translateFunSquare(String[] asWords) throws Exception {
		Logger.setThreadState("oracle translator :translateFunSquare");
		String s = new String();
		int iOff = 2;
		int iLBracket = 0;
		int iRBracket = 0;
		s += "power(";
		while (iOff < (asWords.length)) {
			if (asWords[iOff].equals("("))
				iLBracket++;
			if (asWords[iOff].equals(")"))
				iRBracket++;
			if ((iLBracket == iRBracket) && asWords[iOff + 1].equals(")")) {
				s += " " + asWords[iOff];
				iOff++;
				s += ",2";
			}
			s += " " + asWords[iOff];
			iOff++;
		}
		try {
			translateSelect(parseSql(s));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		Logger.setThreadState("oracle translator :translateFunSquare Over");
	}

	private String[] translateJoinUpdate(String[] asSqlWords, String sTableName, String sTableAlias) throws Exception {
		Logger.setThreadState("oracle translator :translateJoinUpdate");

		int iOffSet = 0; // 偏移量
		String[] asWords = asSqlWords;
		String sSql = ""; // 返回值
		String m_Sql = ""; // 临时sql
		String sLeftField = "";
		String sRightField = "";
		ArrayList<String> vSetList = new ArrayList<String>(); // 存放set语句
		String s = ""; // 中间变量
		ArrayList<String> vecTable = new ArrayList<String>();
		String whereSql = "";
		String m_whereSql = "";
		String fromSt = "";
		// Vector vSql = new Vector(); //存放sql语句

		int iJoinCount = 0; // join的个数
		int iSingleCount = 0; // 单计数个数
		String subfromSt = "";
		ArrayList<String> vWhList = new ArrayList<String>(); // 存放where语句
		String inSql1 = "";
		String inSql2 = "";
		String andSql = "";
		boolean bExist = true;
		String[] asTables = null;
		iOffSet = -1; // 偏移量退回
		// 取出from后的表名
		asTables = parseTable(asSqlWords, sTableName, sTableAlias);
		// 计数小于输入字符串集的长度减1
		while (iOffSet < asWords.length - 1) {
			// 计数加1
			iOffSet++;

			// 若当前字符串是“set”
			if (asWords[iOffSet].equalsIgnoreCase("set")) {
				String str = "";
				String setsql = "";
				int setcount = 0;
				// 若表别名不为空，则记录之

				if (!sTableAlias.equalsIgnoreCase("") && !sTableAlias.equalsIgnoreCase(sTableName)) {
					sSql += " " + sTableAlias;
					// vSql.addElement(sTableAlias);
				}

				sSql += " set";
				// vSql.addElement("set");
				iOffSet++;

				int leftCount = 0; // 左括号数
				int rightCount = 0; // 右括号数

				// 若当前字符串不是“from”，则循环
				while (iOffSet < asWords.length && !asWords[iOffSet].equalsIgnoreCase("from")) {
					if (asWords[iOffSet].equalsIgnoreCase("(")) {
						leftCount++;
					} else if (iOffSet < asWords.length && asWords[iOffSet].equalsIgnoreCase(")")) {
						rightCount++;
						if (leftCount == rightCount) {
							leftCount = 0;
							rightCount = 0;
						}
					}

					// 若当前字符串是逗号
					if (iOffSet < asWords.length && asWords[iOffSet].equalsIgnoreCase(",") && leftCount == rightCount) {
						// 记录累计数据，将累计器清空
						vSetList.add(str);
						str = "";
						iOffSet++;
					} else {
						// 累计当前字符串
						str += " " + asWords[iOffSet];
						iOffSet++;
					}
				}
				// 记录累计数据
				vSetList.add(str);

				// 只对一列进行更新
				// 若只有一行数据
				{ // 多列更新
					int i0 = 0;
					// 计数小于记录向量长度，则循环
					while (i0 < vSetList.size()) {
						// 取得当前字符串
						s = (String) vSetList.get(i0);
						// 取得等号的位置
						int start = s.indexOf("=");
						if (haveTab(s.substring(start + 1), asTables)) { // 若发现表名和别名
							iJoinCount++;
							if (iJoinCount > 1) {
								sLeftField += "," + s.substring(0, start); // 暂时保存起来，最后拼接到sql语句
								sRightField += "," + s.substring(start + 1);
							} else {
								sLeftField += " " + s.substring(0, start);
								sRightField += " " + s.substring(start + 1);
							}
						} else {
							setcount++;
							if (setcount > 1) {
								setsql += "," + s;
							} else {
								setsql += " " + s;
							}
						}
						i0++;
					}
				} // //多列更新 else 结束处

				// 若单计数和联合计数均大于0
				if (iSingleCount > 0 && iJoinCount > 0) {
					sSql += ",";
					// vSql.addElement(",");
				}

				// 若单计数大于0或联合计数大于0
				// if (iSingleCount > 0 || iJoinCount > 0)
				if (setcount == 0) {
					if (iJoinCount > 1)
						sSql += "(" + sLeftField + ")=(select " + sRightField; // 将暂时保存起来的语句，拼接到sql语句
					else if (iJoinCount == 1)
						sSql += "" + sLeftField + "=(select " + sRightField;
				} else {
					if (iJoinCount > 1)
						sSql += setsql + ",(" + sLeftField + ")=(select " + sRightField;
					else if (iJoinCount == 1)
						sSql += setsql + "," + sLeftField + "=(select " + sRightField;
					else
						sSql += setsql;
				}
			} // if (asWords[iOffSet].equalsIgnoreCase("set"))结束处

			// 将更新表表名从from子句中剔除（oracle不支持）
			// 若当前字符串是“from”
			if (iOffSet < asWords.length && asWords[iOffSet].equalsIgnoreCase("from")) {
				int f_leftCount = 1;
				int f_rightCount = 0;
				iOffSet++;
				ArrayList<String> aNewVec = new ArrayList<String>();
				while (iOffSet < asWords.length && !asWords[iOffSet].equalsIgnoreCase("where")) {
					// /////////////

					if (asWords[iOffSet].equalsIgnoreCase("(")) {
						subfromSt = "(";
						while ((f_leftCount != f_rightCount) && (iOffSet < asWords.length)) {
							iOffSet++;

							if (asWords[iOffSet].equalsIgnoreCase("(")) {
								f_leftCount++;
							} else if (asWords[iOffSet].equalsIgnoreCase(")")) {
								f_rightCount++;
							}

							subfromSt += " " + asWords[iOffSet];

						}

						aNewVec.add(subfromSt);
						iOffSet++;
					}
					// ////////////
					aNewVec.add(asWords[iOffSet]);
					iOffSet++;
				}

				for (int newIndex = 0; newIndex < aNewVec.size(); newIndex++) {
					String othtable = aNewVec.get(newIndex).toString();
					String trueName = othtable;
					boolean isOth = false;

					if (!othtable.equalsIgnoreCase(sTableName)) {
						isOth = true;
						if (vecTable.size() > 0) {
							fromSt += ",";
						}
						fromSt += " " + othtable;
						vecTable.add(othtable);
					}
					newIndex++;
					if (newIndex < aNewVec.size()) {
						othtable = aNewVec.get(newIndex).toString();
						if (othtable.equalsIgnoreCase("as")) {
							newIndex++;
							othtable = aNewVec.get(newIndex).toString();
						}

						if (!othtable.equalsIgnoreCase(",")) {
							if (isOth) {
								fromSt += " " + othtable;
								vecTable.add(othtable);
							} else {
								if (sTableAlias != null && sTableAlias.trim().length() > 0) {
									if (!othtable.equalsIgnoreCase(sTableAlias)) {
										if (vecTable.size() > 0) {
											fromSt += ",";
										}
										fromSt += trueName + " " + othtable;

										vecTable.add(trueName);
										vecTable.add(othtable);
									}
								}
							}
							newIndex++;
						}
					}
				}

				if (fromSt.trim().length() > 0) {
					fromSt = " from " + fromSt;
				}

				if (fromSt.endsWith(",")) {
					fromSt = fromSt.substring(0, fromSt.length() - 1);
				}
				if (iJoinCount > 0) {
					sSql += fromSt;
				}

			}
			if (iOffSet < asWords.length && asWords[iOffSet].equalsIgnoreCase("where")) {
				int w_leftCount = 0;
				int w_rightCount = 0;
				int inCount = 0;
				int andCount = 0;
				int whereCount = 0;
				int i1 = 0;
				boolean isExist = false;
				String sw = "";
				String s1 = "";
				String w_leftField = "";
				String w_rightField = "";
				iOffSet++;
				m_Sql = sSql;
				m_Sql += " where";
				m_whereSql += " where";
				while (iOffSet < asWords.length) {
					m_Sql += " " + asWords[iOffSet];
					m_whereSql += " " + asWords[iOffSet];
					if (asWords[iOffSet].equalsIgnoreCase("(")) {
						w_leftCount++;
					} else if (iOffSet < asWords.length && asWords[iOffSet].equalsIgnoreCase(")")) {
						w_rightCount++;
						if (w_leftCount == w_rightCount) {
							w_leftCount = 0;
							w_rightCount = 0;
						}
					}

					// 分拆where后的条件
					if (iOffSet < asWords.length
							&& (asWords[iOffSet].equalsIgnoreCase("and") || asWords[iOffSet].equalsIgnoreCase("or"))
							&& w_leftCount == w_rightCount) {
						// 记录累计数据，将累计器清空
						vWhList.add(sw);
						vWhList.add(asWords[iOffSet]);
						sw = "";
						iOffSet++;
					} else {
						// 累计当前字符串
						sw += " " + asWords[iOffSet];
						iOffSet++;
					}
				}
				// 记录累计数据
				vWhList.add(sw);

				// 计数小于记录向量长度，则循环
				while (i1 < vWhList.size()) {
					// 取得当前字符串
					s1 = (String) vWhList.get(i1);
					// 判断特殊算术运算符的存在
					if (s1.indexOf("!=") > 0 || s1.indexOf("! =") > 0 || s1.indexOf("<") > 0 || s1.indexOf(">") > 0) {
						isExist = true;
					}

					if (!s1.trim().startsWith("(") && !isExist) {
						// 取得等号的位置
						int start = s1.indexOf("=");

						if (start > 0) {
							// 若发现表名和别名
							w_leftField = s1.substring(0, start);
							w_rightField = s1.substring(start + 1);
							if ((isMasterTab(w_leftField, sTableName) || isMasterTab(w_leftField, sTableAlias))
									&& isMasterTab(asTables, w_rightField)) {
								inCount++;
								if (inCount > 1) {
									inSql1 += "," + w_leftField; // 暂时保存起来，最后拼接到sql语句
									inSql2 += "," + w_rightField;
								} else {
									inSql1 += " " + w_leftField;
									inSql2 += " " + w_rightField;
								}
							} else if ((isMasterTab(w_rightField, sTableName) || isMasterTab(w_rightField, sTableAlias))
									&& isMasterTab(asTables, w_leftField)) {
								inCount++;
								if (inCount > 1) {
									inSql1 += "," + w_rightField; // 暂时保存起来，最后拼接到sql语句
									inSql2 += "," + w_leftField;
								} else {
									inSql1 += " " + w_rightField;
									inSql2 += " " + w_leftField;
								}
							} else if (isMasterTab(asTables, w_leftField)) {
								whereCount++;
								if (whereCount > 1) {
									whereSql += " " + (String) vWhList.get(i1 - 1) + " " + s1;
								} else {
									whereSql += " " + s1;
								}
							} else if (isMasterTab(w_leftField, sTableName) || isMasterTab(w_leftField, sTableAlias)) {
								// else{
								andCount++;
								if (andCount > 1) {
									andSql += " " + (String) vWhList.get(i1 - 1) + " " + s1;
								} else {
									andSql += " " + s1;
								}
							} else {
								bExist = false;
								break;
							}
						} else { // 不是"="，可能是"is ( not ) null"等
							String firstWord = parseWord(s1);
							if (haveTab(firstWord, asTables)) {
								whereCount++;
								if (whereCount > 1) {
									whereSql += " " + (String) vWhList.get(i1 - 1) + " " + s1;
								} else {
									whereSql += " " + s1;
								}
							} else if (haveTab(firstWord, sTableName) || haveTab(firstWord, sTableAlias)) {
								// else{
								andCount++;
								if (andCount > 1) {
									andSql += " " + (String) vWhList.get(i1 - 1) + " " + s1;
								} else {
									andSql += " " + s1;
								}
							} else {
								bExist = false;
								break;
							}
						}
					} else { // 以"()"括起来的条件、特殊算术运算符存在的条件
						if ((haveTab(s1, sTableName) || haveTab(s1, sTableAlias)) && haveTab(s1, asTables)) {
							bExist = false;
							break;
						} else if (haveTab(s1, asTables)) {
							whereCount++;
							if (whereCount > 1) {
								whereSql += " " + (String) vWhList.get(i1 - 1) + " " + s1;
							} else {
								whereSql += " " + s1;
							}

						} else if (haveTab(s1, sTableName) || haveTab(s1, sTableAlias)) {
							andCount++;
							if (andCount > 1) {
								andSql += " " + (String) vWhList.get(i1 - 1) + " " + s1;
							} else {
								andSql += " " + s1;
							}
						} else {
							bExist = false;
							break;
						}
					}
					i1 += 2;
				}
			}

			if (iOffSet < asWords.length && asWords[iOffSet].equalsIgnoreCase("case")) {
				TransUnit aTransUnit = dealCaseWhen(asWords, asWords[iOffSet], iOffSet);

				iOffSet = aTransUnit.getIOffSet();

				String sSql_new = aTransUnit.getSql();

				sSql += translateJoinUpdate(parseSql(sSql_new));
			} else if (iOffSet < asWords.length) {
				if (asWords[iOffSet].equalsIgnoreCase(sTableName) || asWords[iOffSet].equalsIgnoreCase(sTableAlias)) {
					sSql += " " + sTableName;
				} else {
					sSql += " " + asWords[iOffSet];
				}

			}
		}

		m_Sql += ")";
		if (iJoinCount > 0) {
			sSql = m_Sql;
		}
		if (bExist) {
			if (andSql.trim() != null && andSql.trim().length() > 0 && inSql1.trim() != null
					&& inSql1.trim().length() > 0 && whereSql.trim() != null && whereSql.trim().length() > 0) {
				sSql += " where " + andSql + " and (" + inSql1 + ") in ( select " + inSql2 + " " + fromSt + " where "
						+ whereSql + " )";
			} else if (andSql.trim() != null && andSql.trim().length() > 0 && inSql1.trim() != null
					&& inSql1.trim().length() > 0) {
				sSql += " where " + andSql + " and (" + inSql1 + ") in ( select " + inSql2 + " " + fromSt + " )";
			} else if (inSql1.trim() != null && inSql1.trim().length() > 0 && whereSql.trim() != null
					&& whereSql.trim().length() > 0) {
				sSql += " where  (" + inSql1 + ") in ( select " + inSql2 + " " + fromSt + " where " + whereSql + " )";
			} else if (inSql1.trim() != null && inSql1.trim().length() > 0) {
				sSql += " where  (" + inSql1 + ") in ( select " + inSql2 + " " + fromSt + " )";
			} else if (andSql.trim() != null && andSql.trim().length() > 0) {
				sSql += " where " + andSql;
			}
		} else {
			if (m_whereSql != null && m_whereSql.trim().length() > 0) {
				sSql += " where exists( select 1 " + fromSt + " " + m_whereSql + " )";
			}
		}
		asWords = parseSql(sSql);

		Logger.setThreadState("oracle translator :translateJoinUpdate Over");
		return asWords;
	}

	/**
	 * 转换 CASE WHEN 语句 参数: asSqlWords CaseWhen语句 返回: destSql 目标语句 规则: case s0
	 * when exp1 then exp2 when exp3 then exp4 ->
	 * decode(s0,exp1,exp2,exp3,exp4,exp5) else exp5 end
	 * 
	 * case when s0 is null then exp1 when s0=exp2 then exp3 when s0<=exp4 then
	 * exp5 ->
	 * decode(s0,null,exp1,exp2,exp3,least(s0,exp4),exp5,GREATEST(s0,exp6
	 * ),exp7,exp8) when s0>=exp6 then exp7 else exp8 end
	 */
	private String translateSimpleCaseWhen(String[] asSqlWords) throws Exception {
		Logger.setThreadState("oracle translator :translateSimpleCaseWhen");
		int iOff = 0;

		String s1 = "decode(";
		iOff++;

		// 第一个字符串不等于“when”，第一种情况

		// 若当前位置的字符串不是“end”
		while ((iOff < asSqlWords.length) && !asSqlWords[iOff].equalsIgnoreCase("end")) {
			// 若当前字符串是“when”、“then”或“else”
			if ((asSqlWords[iOff].equalsIgnoreCase("when")) || (asSqlWords[iOff].equalsIgnoreCase("then"))
					|| (asSqlWords[iOff].equalsIgnoreCase("else"))) {
				// 加上逗号
				s1 = s1 + ",";

			} else if ((asSqlWords[iOff].equalsIgnoreCase("case"))) {
				// 处理嵌套 case when
				TransUnit aTransUnit = dealCaseWhen(asSqlWords, asSqlWords[iOff], iOff);

				iOff = aTransUnit.getIOffSet() - 1;
				String sSql = aTransUnit.getSql();

				// s1 += translateCaseWhen(parseSql(sSql));
				s1 += sSql;
			} else { // 否则，加上当前字符串
				s1 = s1 + " " + asSqlWords[iOff];
			}
			iOff++;
		}
		// 加上括号
		s1 = s1 + ")";

		// return m_sbDestinationSql;
		Logger.setThreadState("oracle translator : translateSimpleCaseWhen Over");
		return s1;
	}

	private String[] trimKuohao(String[] asSqlWords, int iOffSet) {
		Logger.setThreadState("oracle translator : trimKuohao");
		int start = iOffSet;
		int left = 0;
		int right = 0;
		while (asSqlWords[iOffSet].equals("(")) {
			for (int i = iOffSet; i < asSqlWords.length; i++) {
				if (asSqlWords[i].equals("(")) {
					left++;
				}
				if (asSqlWords[i].equals(")")) {
					right++;
				}
				if (left == right) {
					asSqlWords[iOffSet] = "";
					asSqlWords[i] = "";
					iOffSet++;
					break;
				}
			}
		}
		ArrayList<String> vec = new ArrayList<String>();

		for (int i = 0; i < start; i++) {
			vec.add(asSqlWords[i]);
		}

		for (int i = start; i < asSqlWords.length; i++) {
			if (asSqlWords[i] != null && asSqlWords[i].trim().length() > 0) {
				vec.add(asSqlWords[i]);
			}
		}
		String[] stArray = new String[vec.size()];
		vec.toArray(stArray);
		Logger.setThreadState("oracle translator : trimKuohao Over");
		return stArray;
	}

	String m_TabName = null;

}