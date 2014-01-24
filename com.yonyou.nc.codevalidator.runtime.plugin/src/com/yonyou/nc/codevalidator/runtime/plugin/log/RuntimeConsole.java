package com.yonyou.nc.codevalidator.runtime.plugin.log;

import java.io.PrintStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author zhujie1
 * @date 2011.09.08 22:23
 * 
 */
public class RuntimeConsole extends MessageConsole {
	private PrintStream errorPrintStream;
	private PrintStream infoPrintStream;
	private MessageConsoleStream errorConsoleStream;
	private MessageConsoleStream infoConsoleStream;

	public RuntimeConsole(String name, ImageDescriptor imageDescriptor) {
		super(name, imageDescriptor);
		errorConsoleStream = newMessageStream();
		errorConsoleStream.setColor(new Color(Display.getCurrent(), 255, 0, 0));
		errorPrintStream = new PrintStream(errorConsoleStream);
		infoConsoleStream = newMessageStream();
		infoConsoleStream.setColor(new Color(Display.getCurrent(), 0, 0, 0));
		infoPrintStream = new PrintStream(infoConsoleStream);
	}

	public PrintStream getErrorPrintStream() {
		return errorPrintStream;
	}

	public PrintStream getInfoPrintStream() {
		return infoPrintStream;
	}
}