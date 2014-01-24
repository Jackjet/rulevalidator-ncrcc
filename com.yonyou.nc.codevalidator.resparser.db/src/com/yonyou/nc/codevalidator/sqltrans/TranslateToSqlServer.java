package com.yonyou.nc.codevalidator.sqltrans;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 模块: TranslateToSqlServer.java 描述: 将特殊语句翻译到SqlServer语句 作者: cf
 */

public class TranslateToSqlServer extends TranslatorObject {
	/**
	 * TranslateToSqlServer 构造子注释。
	 */
	public TranslateToSqlServer() {
		super(SQLSERVER);
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer Over");
	}

	public String getSql() throws Exception {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.getSql");
		// if ("unicode".equalsIgnoreCase(System.getProperty("sql.encoding"))) {
		translateNSql();
		// } else {
		// translateSql();
		// }
		if (m_sbDestinationSql == null)
			return null;

		String sResult = m_sbDestinationSql.toString();

		// 若翻译结果以分号结尾，则去掉分号
		if (sResult.endsWith(";")) // //张森 2001.3.17 加
			sResult = sResult.substring(0, sResult.length() - 1);

		/*
		 * //张森 2001.3.17 注释掉 if (!sResult.endsWith(";"))//为什么加分号？ { sResult +=
		 * ";"; }
		 */
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.getSql Over");
		return sResult;
	}

	/**
	 * 转换"||" 参数: iOff 偏移量 返回: 偏移量 规则: || -> +
	 */
	int translateII(int iOff) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateII");

		int iOffSet = iOff;
		String sWord = m_asSqlWords[iOffSet];

		if (sWord.equals("||")) {
			m_bFinded = true;
			m_sbDestinationSql.append(" +");
		}

		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateII Over");
		return iOffSet;
	}

	/**
     *
     */
	void translateSql() {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateSql");

		// 分析sql语句,得到sql单词序列
		// m_asSqlWords = parseSql(m_sResorceSQL);
		if (m_asSqlWords == null) {
			m_sbDestinationSql = null;
			Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateSql Over");
			return;
		}

		int iOffSet = 0;

		m_bFinded = false;
		m_sbDestinationSql = new StringBuffer();
		String sWord = "";

		while (iOffSet < m_asSqlWords.length) {

			sWord = m_asSqlWords[iOffSet];

			// 处理Oracle优化关键字
			if (iOffSet < m_asSqlWords.length
					&& iOffSet + 5 < m_asSqlWords.length
					&& m_asSqlWords[iOffSet].equals("/")
					&& m_asSqlWords[iOffSet + 1].equals("*")
					&& m_asSqlWords[iOffSet + 2].equals("+")) {
				iOffSet += 3;

				while (!m_asSqlWords[iOffSet].equals("*")
						&& !m_asSqlWords[iOffSet + 1].equals("/")) {
					iOffSet += 1;
				}
				iOffSet += 2;
				continue;
			}

			// 转换"||"
			if (!m_bFinded) {
				iOffSet = translateII(iOffSet);
			}

			if (!m_bFinded) {
				if (iOffSet > 0) {
					m_sbDestinationSql.append(" ");
				}
				m_sbDestinationSql.append(sWord);
			}

			m_bFinded = false;
			iOffSet++;
		}
	}

	void translateNSql() throws Exception {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateSql");

		// 分析sql语句,得到sql单词序列
		// m_asSqlWords = parseSql(m_sResorceSQL);
		if (m_asSqlWords == null) {
			m_sbDestinationSql = null;
			Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateSql Over");
			return;
		}

		int iOffSet = 0;

		m_bFinded = false;
		m_sbDestinationSql = new StringBuffer();
		String sWord = "";
		int type = getStatementType();

		while (iOffSet < m_asSqlWords.length) {

			sWord = m_asSqlWords[iOffSet];

			// 处理Oracle优化关键字
			if (iOffSet < m_asSqlWords.length
					&& iOffSet + 5 < m_asSqlWords.length
					&& m_asSqlWords[iOffSet].equals("/")
					&& m_asSqlWords[iOffSet + 1].equals("*")
					&& m_asSqlWords[iOffSet + 2].equals("+")) {
				iOffSet += 3;

				while (!m_asSqlWords[iOffSet].equals("*")
						&& !m_asSqlWords[iOffSet + 1].equals("/")) {
					iOffSet += 1;
				}
				iOffSet += 2;
				continue;
			}

			// 转换"||"
			if (!m_bFinded) {
				iOffSet = transII(iOffSet);
			}
			if (m_bFinded)
				sWord = "+";
			else if ((type == SQL_SELECT || type == SQL_UPDATE || type == SQL_INSERT||type==SQL_DELETE)
					&& sWord.startsWith("'") && sWord.endsWith("'")) {
				sWord = "N" + sWord;
			} else if (type == SQL_CREATE) {
				if (m_asSqlWords[iOffSet].equalsIgnoreCase("varchar")) {
					sWord = "nvarchar";
				}
				if (m_asSqlWords[iOffSet].equalsIgnoreCase("char")) {
					sWord = "nchar";
				}
			}
			if (iOffSet > 0) {
				m_sbDestinationSql.append(" ");
			}
			m_sbDestinationSql.append(sWord);

			m_bFinded = false;
			iOffSet++;
		}
	}

	/**
	 * 转换"||" 参数: iOff 偏移量 返回: 偏移量 规则: || -> +
	 */
	int transII(int iOff) {
		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateII");

		int iOffSet = iOff;
		String sWord = m_asSqlWords[iOffSet];

		if (sWord.equals("||")) {
			m_bFinded = true;
		}

		Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSqlServer.translateII Over");
		return iOffSet;
	}
}