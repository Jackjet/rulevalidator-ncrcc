package com.yonyou.nc.codevalidator.crossdb;

import java.util.Calendar;
import java.util.StringTokenizer;

import com.yonyou.nc.codevalidator.connection.SQLStruct;

/**
 * @nopublish
 */

public class CrossDBObject {

	private static boolean bEnableGUConvert = false;
	private static boolean bEnableUGConvert = false;
	protected boolean GU_CONVERT = false;

	public static boolean isDebug = false;
	/*
	 * Simplelized_Chinese---->>>0---->>>简体中文
	 * Traditional_Chinese---->>>1---->>>繁体中文 English---->>>2---->>>英语(美国)
	 * 缺省设置:Simplelized_Chinese---->>>0---->>>简体中文;
	 */
	protected int nModuleLang = 0; // 是否转换字符集
	protected boolean UG_CONVERT = false;

	/**
	 * SqlObject 构造子注释。
	 */
	public CrossDBObject() {
		super();
		// 是否转换字符集
		UG_CONVERT = bEnableUGConvert;
		GU_CONVERT = bEnableGUConvert;
	}

	/**
	 * 在用jdbcOdbc桥连接数据库时，首先需要通过sun.jdbc.odbc.JdbcOdbc的SQLNativeSql()
	 * 来将SQL语句进行本地化，在本地化的过程中会调用sun.jdbc.odbc.JdbcOdbcObject的 CharsToBytes("GBK",
	 * sqlChars)来将char数组转换为byte数组。在此过程中，对于有N个
	 * 双字节的情况,对于每一满批量的转换(满批量转换的字符数为300Char),转换后的byte[]
	 * 比正确的byte[]少了最后的N个byte；最后一个批次(转换的字符数<300Char)的转换过程 当中，由于最后添加了一个截字符'\0',
	 * 因此将会丢失N-1个Byte和添加到最后的'\0'， 从而引起了中文乱码问题。 最新的解决办法:
	 * 1.对于每300个Char,根据其中所含的双字节字符的个数N，将被截去的N个byte添加到后续的 Char数组中，继续依此规则进行处理；
	 * 2.对于剩下的<300Char的部分，按照"GBK"编码方式得到的byte[]的最后(N-1)个byte + '\0'
	 * 补到语句最后，可以得到正确的结果。 3.对于没有双字节字符的Case，无需作任何处理； 游志强 2001-6-25 修改
	 */
	public String fixJdbcOdbcCharToByte(String originSQL)
			throws java.sql.SQLException {
		// 如果是空，则直接返回null
		if (originSQL == null || originSQL.equals(""))
			return originSQL;

		// 截去空格
		// originSQL = originSQL.trim();

		// 如果不含有双字节字符，则直接返回原来的字符串
		if (originSQL.getBytes().length == originSQL.length())
			return originSQL;

		// 根据注释中所描述的修补策略，来对含有双字节字符的SQL 语句进行修正
		String retSQL = "";
		boolean bFixError = false;
//		int nFixedTimeCounter = 0;

		try {
			int nBatchSize = 300;
			char[] batchChars = new char[nBatchSize];
			String unTreatedString = originSQL;
			boolean endOfSQL = false;
			String strByteCodingFormat = "GBK";
			int nCharCount = 0;
			char[] unTreatedChars = null;
			String retSQLBackup = "";
			String retStatusString = null;

			while (!endOfSQL) {
				// 判断是否还是在进行空格插补修正，如果不是，则将计数器清零；
				if (retStatusString == null || !retStatusString.equals(retSQL)) {
//					nFixedTimeCounter = 0;
					retStatusString = new String(retSQL);
				}
				// 首先进行现场备份
				// unTreatedStringBackup = new String(unTreatedString);
				retSQLBackup = new String(retSQL);
				unTreatedChars = unTreatedString.toCharArray();
				if (unTreatedChars.length >= nBatchSize) {
					nCharCount = nBatchSize;
					System.arraycopy(unTreatedChars, 0, batchChars, 0,
							nCharCount);

					char[] leftChars = new char[unTreatedChars.length
							- nCharCount];
					System.arraycopy(unTreatedChars, nCharCount, leftChars, 0,
							leftChars.length);
					unTreatedString = new String(leftChars);

					String strBatch = new String(batchChars);

					

					retSQL += strBatch;

					int nCountOfDoubleByteChar = strBatch.getBytes().length
							- nCharCount;
					if (nCountOfDoubleByteChar > 0) {
						byte[] bDif = new byte[nCountOfDoubleByteChar];
						byte[] byteBatch = strBatch.getBytes();
						System.arraycopy(byteBatch,
								(byteBatch.length - nCountOfDoubleByteChar),
								bDif, 0, nCountOfDoubleByteChar);
						int nNegativeByte = 0;
//						int nEndCharCount = 0;
						for (int i = 0; i < nCountOfDoubleByteChar; i++) {
							int nByte = (int) bDif[i];
							if (nByte < 0)
								nNegativeByte++;
//							else
//								nEndCharCount++;
						}
						if ((nNegativeByte % 2) != 0) {
							String fixedString = "";
//							nFixedTimeCounter++;
							// if (nc.bs.mw.fm.MWRuntimeParams.bAutoInsertSpace
							// && (nFixedTimeCounter <=
							// nc.bs.mw.fm.MWRuntimeParams.MAX_FIX_FAIL_TIME))
							// fixedString =
							// addOneSpaceChar(unTreatedStringBackup, nCharCount
							// - nEndCharCount - (nNegativeByte / 2) - 1);
							if (fixedString != null && !fixedString.equals("")) {
								unTreatedString = fixedString;
								retSQL = retSQLBackup;
								continue;
							} else {
								bFixError = true;
								break;
							}
						}
						unTreatedString = new String(bDif, strByteCodingFormat)
								+ unTreatedString;
					}
				} else {
					nCharCount = unTreatedChars.length;
					System.arraycopy(unTreatedChars, 0, batchChars, 0,
							nCharCount);
					unTreatedString = null;

					String strBatch = new String(batchChars, 0, nCharCount);

					int nCountOfDoubleByteChar = strBatch.getBytes().length
							- nCharCount;
					if (nCountOfDoubleByteChar > 0) {
						int nSpaceLeft = nBatchSize - nCharCount;
						String strNull = new String(new char[] { '\0' });
						// 没有增加处理批次，直接将'\0'添加到末尾即可
						if (nCountOfDoubleByteChar <= nSpaceLeft) {
							retSQL += strBatch;
							for (int i = 0; i < nCountOfDoubleByteChar; i++) {
								retSQL += strNull;
							}
						} else {
							retSQL += strBatch;
							// 首先用'\0'将该字符串填充到300Char
							for (int i = 0; i < nSpaceLeft; i++) {
								retSQL += strNull;
							}
							// 补偿被截去的(nCountOfDoubleByteChar - nSpaceLeft)
							// 个byte;
							byte[] byteBatch = strBatch.getBytes();
							int nFixLength = nCountOfDoubleByteChar
									- nSpaceLeft;
							byte[] byteFix = new byte[nFixLength];
							System.arraycopy(byteBatch,
									(byteBatch.length - nFixLength), byteFix,
									0, nFixLength);
							int nNegativeByte = 0;
//							int nEndCharCount = 0;
							for (int i = 0; i < nFixLength; i++) {
								int nByte = (int) byteFix[i];
								if (nByte < 0)
									nNegativeByte++;
//								else
//									nEndCharCount++;
							}
							if ((nNegativeByte % 2) != 0) {
								String fixedString = "";
//								nFixedTimeCounter++;
								// if
								// (nc.bs.mw.fm.MWRuntimeParams.bAutoInsertSpace
								// && (nFixedTimeCounter <=
								// nc.bs.mw.fm.MWRuntimeParams.MAX_FIX_FAIL_TIME))
								// fixedString =
								// addOneSpaceChar(unTreatedStringBackup,
								// nCharCount - nEndCharCount - (nNegativeByte /
								// 2) - 1);
								if (fixedString != null
										&& !fixedString.equals("")) {
									unTreatedString = fixedString;
									retSQL = retSQLBackup;
									continue;
								} else {
									bFixError = true;
									break;
								}
							}
							unTreatedString = new String(byteFix,
									strByteCodingFormat);
						}
					} else {
						retSQL += strBatch;
						// unTreatedString = null;
					}
				}
				if (unTreatedString == null || unTreatedString.length() <= 0)
					endOfSQL = true;
			}

		} catch (Exception e) {
			bFixError = true;
		}
		if (bFixError)
			throw new java.sql.SQLException("A error occured when correcting SQL statement with Chinese characters!!");
		// 请适当调整SQL语句中带有中文字符部分在SQL语句中出现的位置，可以绕过该错误！

		return retSQL;
	}


	/**
	 * 此处插入方法说明。 创建日期：(2001-12-13 11:21:53)
	 * 
	 * @param newGU_CONVERT
	 *            boolean
	 */
	public void setGU_CONVERT(boolean newGU_CONVERT) {
		GU_CONVERT = newGU_CONVERT;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-13 11:21:53)
	 * 
	 * @param newUG_CONVERT
	 *            boolean
	 */
	public void setUG_CONVERT(boolean newUG_CONVERT) {
		UG_CONVERT = newUG_CONVERT;
	}

	public String stampSQL(String sql) {
		return stampSQL(sql, false).getSql();
	}
	
	/**
	 * 此处插入方法说明。 创建日期：(2001-11-3 14:37:48)
	 * 
	 * @param sql
	 *            java.lang.String
	 * @return java.lang.String
	 */
	public SQLStruct stampSQL(String sql, boolean ptmtTs) {
		if (sql == null || sql.equals("") || sql.length() < 6)
			return new SQLStruct(sql, false);
		String strSQLCopy = sql.toLowerCase();
		String strSelectWord = "select ";
		String strInsertWord = "insert ";
		String strUpdateWord = "update ";
		if (strSQLCopy.trim().startsWith(strSelectWord))
			return new SQLStruct(sql, false);
		else if (strSQLCopy.trim().startsWith(strInsertWord)) {
			// PART I:可以处理的SQL语句:

			// 1.显式指定了插入列:
			// a.插入列中包含ts:INSERT INTO pub_systemplate (pk_busitype,ts,dr)
			// values(NULL,'2001-11-29 14:14:25',0)--->>>直接返回;

			// b.插入列不包含ts:INSERT INTO pub_systemplate (pk_busitype,dr)
			// values(NULL,0)--->>>处理为:
			// INSERT INTO pub_systemplate (pk_busitype,dr,ts)
			// values(NULL,0,'2001-11-29 14:14:25')

			// PART II:不考虑支持的SQL语句:
			// 2.暂不考虑支持不显式指定插入列的Insert语句:
			// insert into table_a values('zhangsd','ufsoft',5844)
			// 3.暂不考虑支持--INSERT...SELECT example
			// USE pubs
			// INSERT author_sales
			// SELECT 'SELECT', authors.au_id, authors.au_lname,
			// SUM(titles.price * sales.qty)
			// FROM authors INNER JOIN titleauthor
			// ON authors.au_id = titleauthor.au_id INNER JOIN titles
			// ON titleauthor.title_id = titles.title_id INNER JOIN sales
			// ON titles.title_id = sales.title_id
			// WHERE authors.au_id LIKE '8%'
			// GROUP BY authors.au_id, authors.au_lname

			// 4.暂不考虑支持INSERT...EXECUTE procedure example
			// INSERT author_sales EXECUTE get_author_sales

			// 5.暂不考虑支持INSERT...EXECUTE('string') example
			// INSERT author_sales
			// EXECUTE
			// ('
			// SELECT ''EXEC STRING'', authors.au_id, authors.au_lname,
			// SUM(titles.price * sales.qty)
			// FROM authors INNER JOIN titleauthor
			// ON authors.au_id = titleauthor.au_id INNER JOIN titles
			// ON titleauthor.title_id = titles.title_id INNER JOIN sales
			// ON titles.title_id = sales.title_id
			// WHERE authors.au_id like ''8%''
			// GROUP BY authors.au_id, authors.au_lname
			// ')
			boolean addTs = false;
			String strResSql = sql;

			int nIntoPos = strSQLCopy.indexOf(" into ");
			if (nIntoPos < 0) {
				nIntoPos = strSQLCopy.indexOf("into");
			}
			if (nIntoPos < 0) {
				nIntoPos = 0;
			}
			int nValuesPos = strSQLCopy.indexOf(" values");
			if (nValuesPos == -1)
				nValuesPos = strSQLCopy.indexOf("values");
			if (nValuesPos > 0) {
				int nLeftBracketPos = strSQLCopy.substring(0, nValuesPos)
						.indexOf("(", nIntoPos);
				int nRightBracketPos = strSQLCopy.substring(0, nValuesPos)
						.indexOf(")", nIntoPos);
				if (nLeftBracketPos > 0 && nRightBracketPos > 0
						&& nLeftBracketPos < nRightBracketPos) {
					StringTokenizer stColumns = new StringTokenizer(
							strSQLCopy.substring(nLeftBracketPos + 1,
									nRightBracketPos), ",");
//					boolean bHasTSColumn = false;
					while (stColumns.hasMoreTokens()) {
						String strToken = stColumns.nextToken().trim();
						if (strToken.equalsIgnoreCase("ts")) {
//							bHasTSColumn = true;
							break;
						}
					}
					//mazhqa 删除ts相关的处理逻辑
//					if (!bHasTSColumn) {
//
//						/*
//						 * zhaogb 2011-6-9 INSERT类型语句添加TS，将TS添加到第一个位置，方便参数添加
//						 * INSERT INTO TABLE1 (A,B,...) VALUES ('A','B',...) 将变成
//						 * INSERT INTO TABLE1 (TS,A,B,...) VALUES(?,'A','B',...)
//						 */
//						StringBuffer sbNewSql = new StringBuffer();
//						sbNewSql.append(sql.substring(0, nLeftBracketPos + 1));
//						sbNewSql.append("ts,");
//						int nLastLeftBracketPos = sql.indexOf("(", nValuesPos);
//						sbNewSql.append(sql.substring(nLeftBracketPos + 1,
//								nLastLeftBracketPos + 1));
//						if (ptmtTs) {
//							sbNewSql.append("?,");
//							addTs = true;
//						} else {
//							sbNewSql.append("'" + getTimeStampString() + "',");
//						}
//						sbNewSql.append(sql.substring(nLastLeftBracketPos + 1,
//								sql.length()));
//						strResSql = sbNewSql.toString();
//
//					}
				}
			}
			return new SQLStruct(strResSql, addTs);

		} else if (strSQLCopy.trim().startsWith(strUpdateWord)) {
			/*
			 * 处理UPDATE 语句（时间戳字段名称是统一的，都为TS）： UPDATE TABLE1 SET A=B，C=D，...
			 * WHERE R=T AND U=X... 将变成： UPDATE TABLE1 SET TS='当前时间戳',
			 * A=B，C=D，... WHERE R=T AND U=X... 对于今加过时间戳的语句则忽略；
			 */
			if (strSQLCopy.indexOf(" set ts") >= 0
					|| strSQLCopy.indexOf(",ts") >= 0
					|| strSQLCopy.indexOf(", ts") >= 0)
				return new SQLStruct(sql, false);
			int nReplaceIndex = strSQLCopy.indexOf(" set ");
			if (nReplaceIndex > 0) {
				boolean addTs = false;
				StringBuffer sbNewSql = new StringBuffer();
				sbNewSql.append(sql.substring(0, nReplaceIndex + 5));
				if (ptmtTs) {
					sbNewSql.append("ts").append("=?,");
					addTs = true;
				} else {
					sbNewSql.append("ts").append("='")
							.append(getTimeStampString()).append("',");
				}
				sbNewSql.append(sql.substring(nReplaceIndex + 5));
				return new SQLStruct(sbNewSql.toString(), addTs);
			} else {
				return new SQLStruct(sql, false);
			}
		} else {
			return new SQLStruct(sql, false);
		}
	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-7-5 11:08:25)
	 * 
	 * @return java.lang.String
	 */
	public static String getTimeStampString() {
		java.util.Calendar cl = java.util.Calendar.getInstance();
//		cl.setTimeInMillis(TimeService.getInstance().getTime());
		cl.setTimeInMillis(System.currentTimeMillis());
		int ia[] = new int[5];
		int year = cl.get(Calendar.YEAR);
		ia[0] = cl.get(Calendar.MONTH) + 1;
		ia[1] = cl.get(Calendar.DAY_OF_MONTH);
		ia[2] = cl.get(Calendar.HOUR_OF_DAY);
		ia[3] = cl.get(Calendar.MINUTE);

		ia[4] = cl.get(Calendar.SECOND);
		byte ba[] = new byte[19];
		ba[4] = ba[7] = (byte) '-';
		ba[10] = (byte) ' ';
		ba[13] = ba[16] = (byte) ':';
		ba[0] = (byte) (year / 1000 + '0');
		ba[1] = (byte) ((year / 100) % 10 + '0');
		ba[2] = (byte) ((year / 10) % 10 + '0');
		ba[3] = (byte) (year % 10 + '0');
		for (int i = 0; i < 5; i++) {
			ba[i * 3 + 5] = (byte) (ia[i] / 10 + '0');
			ba[i * 3 + 6] = (byte) (ia[i] % 10 + '0');
		}
		return new String(ba);
		// return getTimeStampString;
	}
}