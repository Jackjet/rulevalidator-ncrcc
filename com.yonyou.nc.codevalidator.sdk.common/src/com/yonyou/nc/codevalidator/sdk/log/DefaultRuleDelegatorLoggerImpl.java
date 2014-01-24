package com.yonyou.nc.codevalidator.sdk.log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultRuleDelegatorLoggerImpl implements IRuleLogger {

	private static final DefaultRuleDelegatorLoggerImpl INSTANCE = new DefaultRuleDelegatorLoggerImpl();

	private static List<IRuleLogger> ruleLoggerList;

	public DefaultRuleDelegatorLoggerImpl() {
		ruleLoggerList = new CopyOnWriteArrayList<IRuleLogger>();
	}

	public static DefaultRuleDelegatorLoggerImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public void debug(String msg) {
		for (IRuleLogger ruleLogger : ruleLoggerList) {
			ruleLogger.debug(msg);
		}
	}

	@Override
	public void info(String msg) {
		for (IRuleLogger ruleLogger : ruleLoggerList) {
			ruleLogger.info(msg);
		}
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		for (IRuleLogger ruleLogger : ruleLoggerList) {
			ruleLogger.warn(msg, throwable);
		}
	}

	@Override
	public void warn(String msg) {
		for (IRuleLogger ruleLogger : ruleLoggerList) {
			ruleLogger.warn(msg);
		}
	}

	@Override
	public void error(String msg, Throwable throwable) {
		for (IRuleLogger ruleLogger : ruleLoggerList) {
			ruleLogger.error(msg, throwable);
		}
	}

	@Override
	public void error(String msg) {
		for (IRuleLogger ruleLogger : ruleLoggerList) {
			ruleLogger.error(msg);
		}
	}

	public void addRuleLogger(IRuleLogger ruleLogger) {
		DefaultRuleDelegatorLoggerImpl.ruleLoggerList.add(ruleLogger);
	}

	public void removeRuleLogger(IRuleLogger ruleLogger) {
		DefaultRuleDelegatorLoggerImpl.ruleLoggerList.remove(ruleLogger);
	}

	@Override
	public void init() {
		for (IRuleLogger ruleLogger : ruleLoggerList) {
			ruleLogger.init();
		}
	}

}
