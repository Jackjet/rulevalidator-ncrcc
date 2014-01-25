package com.yonyou.nc.codevalidator.sqltrans;

import java.util.ArrayList;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class TranslateToGbase extends TranslatorObject {

	// 函数对照表,列出sqlServer函数与gbase函数的对应关系,
	private static final String[][] m_apsGbaseFunctions = {
			{ "len", "length" }, { "isnull", "nvl" } };

	boolean isTrigger = false;

	public TranslateToGbase() {
		super(GBASE);
		m_apsFunList = m_apsGbaseFunctions;
		m_apiErrorList = m_apiOracleError;
	}

	public String getSql() throws Exception {
		Logger.setThreadState("gbase translator : getSql");
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
		Logger.setThreadState("gbase translator : getSql Over");
		return sResult;
	}

	/**
	 * 转换Create语句
	 */
	private void translateCreate() throws Exception {
		Logger.setThreadState("gbase translator : translateCreate");
		m_sbDestinationSql = new StringBuffer("");
		for (int i = 0; i < m_asSqlWords.length; i++) {
			if (m_asSqlWords[i].equalsIgnoreCase("nvarchar")) {
				m_asSqlWords[i] = "varchar";
			} else if (m_asSqlWords[i].equalsIgnoreCase("nchar")) {
				m_asSqlWords[i] = "char";
			} else if (m_asSqlWords[i].equalsIgnoreCase("datetime")
					|| m_asSqlWords[i].equalsIgnoreCase("smalldatetime")) {
				m_asSqlWords[i] = "date";
			}
			m_sbDestinationSql.append(" " + m_asSqlWords[i]);
		}
		Logger.setThreadState("gbase translator :translateCreate Over");
	}

	/**
	 * 转换Delete语句
	 */
	private StringBuffer translateDelete(String[] asSqlWords) throws Exception {
		Logger.setThreadState("gbase translator :translateDelete");
		Logger.setThreadState("gbase translator :translateDelete Over");
		return translateSelect(asSqlWords);
	}

	/**
	 * 转换Drop语句
	 */
	private void translateDrop() throws Exception {
		Logger.setThreadState("gbase translator :translateDrop");
		m_sbDestinationSql = new StringBuffer(m_sResorceSQL);
		Logger.setThreadState("gbase translator :translateDrop Over");
	}

	/**
	 * 根据函数对照表进行函数转换
	 */
	private void translateFunction() throws Exception {
		Logger.setThreadState("gbase translator :translateFunction");
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
		Logger.setThreadState("gbase translator :translateFunction Over");
	}

	/**
	 * 转换Insert语句
	 */
	private StringBuffer translateInsert(String[] asSqlWords) throws Exception {
		Logger.setThreadState("gbase translator :translateInsert");
		Logger.setThreadState("gbase translator :translateInsert Over");
		return translateSelect(asSqlWords);
	}

	public boolean isFunctionName(String sWord, String nextWord) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isFunctionName");
		boolean isFunc = false;

		if ((sWord.equalsIgnoreCase("square") // 或当前单词为“square”
				|| sWord.equalsIgnoreCase("patindex")
				|| sWord.equalsIgnoreCase("convert")
				|| sWord.equalsIgnoreCase("dateadd")
				|| sWord.equalsIgnoreCase("datediff") || sWord
				.equals("substring")) // 且下一个单词是“(”
				&& nextWord.equals("(")) {
			isFunc = true;
		}
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isFunctionName Over");
		return isFunc;
	}

	/**
	 * 翻译Select语句
	 */
	private StringBuffer translateSelect(String[] asSqlWords) throws Exception {
		Logger.setThreadState("gbase translator :translateSelect");
		int iOffSet = 0;
		String sWord = "";
		String sPreWord;

		boolean dontHaveWhere = true;

		boolean ifTop = false;

		TransUnit aTransUnit = null;

		String rowNum = "";

		while (iOffSet < asSqlWords.length) {
			sPreWord = sWord;
			// 取得当前单词
			sWord = asSqlWords[iOffSet];

			// 在此对函数进行处理
			// 如果当前单词为函数名称
			if ((iOffSet + 1 < asSqlWords.length)
					&& isFunctionName(sWord, asSqlWords[iOffSet + 1])) {
				aTransUnit = dealFunction(asSqlWords, sWord, iOffSet);

				iOffSet = aTransUnit.getIOffSet();

				if (iOffSet > asSqlWords.length - 1) {
					// 函数嵌套
					return null;
				}
			}
			// 处理Oracle优化关键字
			else if (iOffSet < asSqlWords.length
					&& iOffSet + 5 < asSqlWords.length
					&& asSqlWords[iOffSet].equals("/")
					&& asSqlWords[iOffSet + 1].equals("*")
					&& asSqlWords[iOffSet + 2].equals("+")) {
				iOffSet += 3;
				m_sbDestinationSql.append(" /*+ ");
				while (!asSqlWords[iOffSet].equals("*")
						&& !asSqlWords[iOffSet + 1].equals("/")) {
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
			else if (iOffSet < asSqlWords.length
					&& sWord.equalsIgnoreCase("PI")
					&& asSqlWords[iOffSet + 1].equals("(")
					&& asSqlWords[iOffSet + 2].equals(")")) {
				m_sbDestinationSql.append(" 3.1415926535897931");
				iOffSet += 3;
			}
			// 处理取模%
			else if (iOffSet + 2 < asSqlWords.length
					&& asSqlWords[iOffSet + 1].equals("%")) {
				m_sbDestinationSql.append(" mod(" + sWord + ","
						+ asSqlWords[iOffSet + 2] + ")");
				iOffSet += 3;
			}
			// 处理getdate()
			else if (iOffSet + 2 < asSqlWords.length
					&& sWord.equalsIgnoreCase("getdate")
					&& asSqlWords[iOffSet + 1].equals("(")
					&& asSqlWords[iOffSet + 2].equals(")")) {
				m_sbDestinationSql
						.append("  to_char(now(),'yyyy-mm-dd hh24:mi:ss')");
				iOffSet += 3;
			}
			// 处理模式匹配符
			else if (iOffSet < asSqlWords.length
					&& sWord.equalsIgnoreCase("like")) {
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
			// 处理子查询
			else if (iOffSet < asSqlWords.length
					&& sWord.equalsIgnoreCase("select") && iOffSet > 0
					&& asSqlWords[iOffSet - 1].equalsIgnoreCase("(")) {
				aTransUnit = dealSelect(asSqlWords, sWord, iOffSet);

				iOffSet = aTransUnit.getIOffSet();
			} else if (sWord.equals(";")) {
				iOffSet++;
			} else if (sWord.equalsIgnoreCase("from")
					&& iOffSet < asSqlWords.length - 1
					&& asSqlWords[iOffSet + 1].equals("(")
					// && !asSqlWords[iOffSet + 2].equalsIgnoreCase("select")
					&& !getFirstTrueWord(asSqlWords, iOffSet).equalsIgnoreCase(
							"select")) {
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

					aTransUnit = dealOther(asSqlWords, sWord, iOffSet, ifTop,
							dontHaveWhere, rowNum, sPreWord);

					iOffSet = aTransUnit.getIOffSet();

					dontHaveWhere = aTransUnit.isDontHaveWhere();

				} else
					break;
			}
		}

		if (dontHaveWhere && ifTop) {
			m_sbDestinationSql.append(" limit "
					+ (Integer.valueOf(rowNum).intValue()));
		}

		Logger.setThreadState("gbase translator :translateSelect Over");
		return m_sbDestinationSql;
	}

	/**
	 * 根据语句类型进行转换
	 */

	private void translateSql() throws Exception {
		Logger.setThreadState("gbase translator :translateSql");
		if (m_asSqlWords == null) {
			m_sbDestinationSql = null;
			Logger.setThreadState("gbase translator :translateSql Over");
			return;
		}

		m_sbDestinationSql = new StringBuffer();
		m_sLeftWhere = new String();
		m_sLeftTable = new String();

		isTrigger = (m_asSqlWords.length >= 2
				&& (m_asSqlWords[0].equalsIgnoreCase("create") || m_asSqlWords[0]
						.equalsIgnoreCase("replace")) && m_asSqlWords[1]
				.equalsIgnoreCase("trigger"))
				|| (m_asSqlWords.length >= 4 && m_asSqlWords[3]
						.equalsIgnoreCase("trigger"));

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
		Logger.setThreadState("gbase translator :translateUpdate");

		Logger.setThreadState("gbase translator :translateUpdate Over");
		return translateSelect(asSqlWords);
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

	private TransUnit dealFunction(String[] asSqlWords, String sWord,
			int iOffSet) throws Exception {
		Logger.setThreadState("gbase translator :dealFunction");

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

		if (sWord.equalsIgnoreCase("square")) {
			translateFunSquare(newFuncSql);
		} else if (sWord.equalsIgnoreCase("patindex")) {
			translateFunPatindex(newFuncSql);
		} else if (sWord.equalsIgnoreCase("convert")) {
			translateFunConvert(newFuncSql);
		} else if (sWord.equalsIgnoreCase("dateadd")) {
			translateFunDateAdd(newFuncSql);
		} else if (sWord.equalsIgnoreCase("datediff")) {
			translateFunDateDiff(newFuncSql);
		} else if (sWord.equalsIgnoreCase("substring")) {
			translateSubstring(newFuncSql);
		}

		Logger.setThreadState("gbase translator :dealFunction Over");
		return new TransUnit(null, null, iOffSet);
	}

	private TransUnit dealLike(String[] asSqlWords, String sWord, int iOffSet)
			throws Exception {
		Logger.setThreadState("gbase translator :dealLike");
		String s = "";

		if (iOffSet + 1 < asSqlWords.length) {
			s = asSqlWords[iOffSet + 1];
			m_sbDestinationSql.append(" like " + s);
			iOffSet += 2;
		} else {
			m_sbDestinationSql.append(" like ");
			iOffSet++;
		}

		Logger.setThreadState("gbase translator :dealLike Over");
		return new TransUnit(null, null, iOffSet);
	}

	private TransUnit dealOther(String[] asSqlWords, String sWord, int iOffSet,
			boolean ifTop, boolean dontHaveWhere, String rowNum, String sPreWord)
			throws Exception {
		Logger.setThreadState("gbase translator :dealOther");
		if (iOffSet < asSqlWords.length) {
			if (!sWord.equals(",") && !(sWord.equals("("))
					&& !(sWord.equals(")") && sPreWord.equals("("))
					&& !(sWord.equals("]") && sPreWord.equals("["))) {
				m_sbDestinationSql.append(" ");
			}
			// m_sbDestinationSql.append(" ");
			m_sbDestinationSql.append(asSqlWords[iOffSet]);

			iOffSet++;
		}
		TransUnit aTransUnit = new TransUnit(null, null, iOffSet);
		aTransUnit.setDontHaveWhere(dontHaveWhere);
		Logger.setThreadState("gbase translator :dealOther Over");
		return aTransUnit;
	}

	private TransUnit dealSelect(String[] asSqlWords, String sWord, int iOffSet)
			throws Exception {
		Logger.setThreadState("gbase translator :dealSelect");

		if (iOffSet < asSqlWords.length) {
			TransUnit aTransUnit = getSubSql(asSqlWords, "(", ")", iOffSet);
			String[] newCaseSql = aTransUnit.getSqlArray();
			iOffSet = aTransUnit.getIOffSet();

			String newSql[] = new String[newCaseSql.length - 1];

			for (int i = 0; i < newSql.length; i++) {
				newSql[i] = newCaseSql[i];
			}

			// 处理子查询
			TranslateToGbase nt = new TranslateToGbase();

			nt.setSqlArray(newSql);

			m_sbDestinationSql.append(nt.getSql());
		}

		Logger.setThreadState("gbase translator :dealSelect Over");
		return new TransUnit(null, null, iOffSet);
	}

	private String getFirstTrueWord(String[] asSqlWords, int iOffSet) {
		Logger.setThreadState("gbase translator :getFirstTrueWord");
		String trueWord = "";

		for (int i = iOffSet + 1; i < asSqlWords.length; i++) {
			if (!asSqlWords[i].equals("(")) {
				trueWord = asSqlWords[i];
				break;
			}
		}
		Logger.setThreadState("gbase translator :getFirstTrueWord Over");
		return trueWord;
	}

	/*
	 * 转换convert函数 参数: asWords convert函数子句
	 * 
	 * 规则: convert(char(n),f)->substring(to_char(f),1,n)
	 */
	private void translateFunConvert(String[] asWords) throws Exception {
		Logger.setThreadState("gbase translator :translateFunConvert");

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
				charLenth = oldDataType.substring(oldDataType.indexOf("(") + 1,
						oldDataType.length() - 1);
			}
			if (charLenCtrl)
				m_sbDestinationSql.append(" substring(to_char(");
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
		Logger.setThreadState("gbase translator :translateFunConvert Over");
	}

	/*
	 * 转换DateAdd函数 参数: asWords DateAdd函数子句 规则: DATEADD ( datepart , number, date
	 * ) datepart：yy、mm、dd ->date_add(date,interval number datepart) [which
	 * datepart:year,month,day]
	 */
	private void translateFunDateAdd(String[] asWords) throws Exception {
		Logger.setThreadState("gbase translator :translateFunDateAdd");

		int iOff = 2;

		String params[] = getFunParam(asWords, iOff, asWords.length - 1);

		String dateType = params[0].trim();

		String theNumber = params[1].trim();

		String theDate = params[2].trim();

		TranslateToGbase newTranslateToGbase = new TranslateToGbase();

		newTranslateToGbase.setSql(theDate);

		theDate = newTranslateToGbase.getSql();
		theDate = theDate.trim();

		if (!(theDate.toLowerCase().startsWith("to_date(") && theDate
				.toLowerCase().indexOf("'yyyy-mm-dd'") > 0)) {
			if (theDate.toLowerCase().startsWith("sysdate"))
				theDate = "date_add(now())";
			else
				theDate = "date_add(" + theDate + ",";
		}

		if (dateType.equalsIgnoreCase("yy")
				|| dateType.equalsIgnoreCase("yyyy")
				|| dateType.equalsIgnoreCase("year")) {
			m_sbDestinationSql.append(" ( " + theDate + " interval "
					+ theNumber + " year)) ");
		} else if (dateType.equalsIgnoreCase("mm")
				|| dateType.equalsIgnoreCase("m")
				|| dateType.equalsIgnoreCase("month")) {
			m_sbDestinationSql.append(" ( " + theDate + " interval "
					+ theNumber + " month)) ");
		} else {
			m_sbDestinationSql.append(" ( " + theDate + " interval "
					+ theNumber + " day)) ");
		}

		Logger.setThreadState("gbase translator :translateFunDateAdd Over");
	}

	/**
	 * 转换DateDiff函数 参数: asWords DateDiff函数子句
	 * 
	 * 规则: DATEDIFF ( datepart , startdate , enddate ) datepart支持：yy、mm、dd
	 * 转为：timestampdiff(datepart,startdate,enddate) datepart支持：year,month,day
	 */
	private void translateFunDateDiff(String[] asWords) throws Exception {
		Logger.setThreadState("gbase translator :translateFunDateDiff");

		int iOff = 2;

		String params[] = getFunParam(asWords, iOff, asWords.length - 1);

		String dateType = params[0].trim();

		String startDate = params[1].trim();

		String endDate = params[2].trim();

		TranslateToGbase newTranslateToGbase = new TranslateToGbase();

		newTranslateToGbase.setSql(startDate);

		startDate = newTranslateToGbase.getSql().trim();

		newTranslateToGbase.setSql(endDate);

		endDate = newTranslateToGbase.getSql().trim();

		if (!(startDate.toLowerCase().startsWith("to_date(") && startDate
				.toLowerCase().indexOf("'yyyy-mm-dd'") > 0)) {
			if (startDate.toLowerCase().startsWith("sysdate"))
				startDate = "now()";
		}

		if (!(endDate.toLowerCase().startsWith("to_date(") && endDate
				.toLowerCase().indexOf("'yyyy-mm-dd'") > 0)) {
			if (endDate.toLowerCase().startsWith("sysdate"))
				endDate = "now()";

		}

		String interval = "day";
		if (dateType.equalsIgnoreCase("yy")
				|| dateType.equalsIgnoreCase("yyyy")
				|| dateType.equalsIgnoreCase("year")) {
			interval = "year";
		} else if (dateType.equalsIgnoreCase("mm")
				|| dateType.equalsIgnoreCase("m")
				|| dateType.equalsIgnoreCase("month")) {
			interval = "month";
		}
		m_sbDestinationSql.append(" ( timestampdiff ");
		m_sbDestinationSql.append(" ( ");
		m_sbDestinationSql.append(interval);
		m_sbDestinationSql.append(" , ");
		m_sbDestinationSql.append(startDate);
		m_sbDestinationSql.append(" , ");
		m_sbDestinationSql.append(endDate);
		m_sbDestinationSql.append(" ) ) ");
		Logger.setThreadState("gbase translator :translateFunDateDiff Over");
	}

	private void translateSubstring(String[] asWords) throws Exception {
		m_sbDestinationSql.append(" ");
		m_sbDestinationSql.append(asWords[0]);
		m_sbDestinationSql.append(asWords[1]);
		for (int i = 2; i < asWords.length; i++) {
			m_sbDestinationSql.append(asWords[i]);
		}
	}

	/**
	 * 转换patindex函数 参数: asWords: patindex函数语句 规则:
	 * patindex('%exp1%',exp2)->instr(exp2,'exp1')
	 */
	private void translateFunPatindex(String[] asWords) throws Exception {
		Logger.setThreadState("gbase translator :translateFunPatindex");

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
					sSql += "," + s + ")";
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
		Logger.setThreadState("gbase translator :translateFunPatindex Over");
	}

	/**
	 * 转换Square函数 参数: asWords square函数子句 规则: square(f)->power(f,2)
	 */
	private void translateFunSquare(String[] asWords) throws Exception {
		Logger.setThreadState("gbase translator :translateFunSquare");
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
		Logger.setThreadState("gbase translator :translateFunSquare Over");
	}

	private String[] trimKuohao(String[] asSqlWords, int iOffSet) {
		Logger.setThreadState("gbase translator : trimKuohao");
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
		Logger.setThreadState("gbase translator : trimKuohao Over");
		return stArray;
	}

	String m_TabName = null;

}
