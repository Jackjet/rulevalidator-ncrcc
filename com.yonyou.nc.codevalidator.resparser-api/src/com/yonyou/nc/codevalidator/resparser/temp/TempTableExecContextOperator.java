package com.yonyou.nc.codevalidator.resparser.temp;

import java.sql.Connection;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class TempTableExecContextOperator {
	
	private static final ThreadLocal<TempTableExecContext> CONTEXT_TL = new ThreadLocal<TempTableExecContext>() {
		protected TempTableExecContext initialValue() {
			return new TempTableExecContext();
		};
	};
	
	public static void setCurrentConnection(Connection connection) throws RuleBaseException {
		TempTableExecContext tempTableExecContext = CONTEXT_TL.get();
//		if(tempTableExecContext.getConnection() != null) {
//			throw new RuleBaseException("不能重复设置当前线程的connection");
//		}
		tempTableExecContext.setConnection(connection);
	}
	
	public static void clearCurrentConnection() {
		TempTableExecContext tempTableExecContext = CONTEXT_TL.get();
		tempTableExecContext.setConnection(null);
	}
	
	public static Connection getCurrentConnection() throws RuleBaseException{
		return CONTEXT_TL.get().getConnection();
	}
	
	public static void initialized(String resId) {
		CONTEXT_TL.get().initialized(resId);
	}
	
	public static boolean isInitialized(String resId) {
		return CONTEXT_TL.get().isInitialized(resId);
	}
	
	public static TempTableExecContext getTempTableExecContext() {
		return CONTEXT_TL.get();
	}
}
