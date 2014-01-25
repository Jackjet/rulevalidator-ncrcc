package com.yonyou.nc.codevalidator.runtime.plugin.console;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.ui.console.MessageConsoleStream;

public final class RuleCheckConsoleUtils {

	// private RuleRuntimeConsoleFactory consoleFactory;
	private MessageConsoleStream printer;

	private static RuleCheckConsoleUtils instance;

	private RuleCheckConsoleUtils() {
		// consoleFactory = new RuleRuntimeConsoleFactory();
		printer = RuleRuntimeConsoleFactory.getConsole().newMessageStream();
		printer.setActivateOnWrite(true);
	}

	public static RuleCheckConsoleUtils getInstance() {
		if (instance == null) {
			instance = new RuleCheckConsoleUtils();
		}
		return instance;
	}

	public void consolePrint(String message) {
		printer.print(message);
	}

	public void consolePrintln(String message, Object... arguments) {
		printer.println(String.format(message, arguments));
	}

	public void consolePrintln(String message) {
		printer.println(message);
	}

	public void close() {
		try {
			printer.close();
		} catch (IOException e) {
			IOUtils.closeQuietly(printer);
		}
	}

}
