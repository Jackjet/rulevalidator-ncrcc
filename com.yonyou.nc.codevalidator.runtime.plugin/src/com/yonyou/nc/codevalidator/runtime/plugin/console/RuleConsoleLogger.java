package com.yonyou.nc.codevalidator.runtime.plugin.console;

import com.yonyou.nc.codevalidator.sdk.log.IRuleLogger;

public class RuleConsoleLogger implements IRuleLogger {

	@Override
	public void debug(String msg) {
		RuleCheckConsoleUtils.getInstance().consolePrintln("DEBUG: %s", msg);
	}

	@Override
	public void info(String msg) {
		RuleCheckConsoleUtils.getInstance().consolePrintln("INFO : %s", msg);
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		RuleCheckConsoleUtils.getInstance().consolePrintln("WARN : %s, DETAIL :%s", msg, throwable.getMessage());
	}

	@Override
	public void warn(String msg) {
		RuleCheckConsoleUtils.getInstance().consolePrintln("WARN : %s", msg);
	}

	@Override
	public void error(String msg, Throwable throwable) {
		RuleCheckConsoleUtils.getInstance().consolePrintln("ERROR: %s", msg, throwable.getMessage());
	}

	@Override
	public void error(String msg) {
		RuleCheckConsoleUtils.getInstance().consolePrintln("ERROR: %s", msg);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
