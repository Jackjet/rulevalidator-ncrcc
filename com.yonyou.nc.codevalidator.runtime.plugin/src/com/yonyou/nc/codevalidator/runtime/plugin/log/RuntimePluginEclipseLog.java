package com.yonyou.nc.codevalidator.runtime.plugin.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.osgi.framework.log.FrameworkLogEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;

/**
 * The FrameworkLog implementation for Eclipse.
 * <p>
 * Clients may extend this class.
 * </p>
 * 
 * @since 3.1
 */
public class RuntimePluginEclipseLog {
	private static final String PASSWORD = "-password"; //$NON-NLS-1$	
	/** The session tag */
	protected static final String SESSION = "!SESSION"; //$NON-NLS-1$
	/** The entry tag */
	protected static final String ENTRY = "!ENTRY"; //$NON-NLS-1$
	/** The sub-entry tag */
	protected static final String SUBENTRY = "!SUBENTRY"; //$NON-NLS-1$
	/** The message tag */
	protected static final String MESSAGE = "!MESSAGE"; //$NON-NLS-1$
	/** The stacktrace tag */
	protected static final String STACK = "!STACK"; //$NON-NLS-1$
	/** The line separator used in the log output */
	protected static final String LINE_SEPARATOR;
	/** The tab character used in the log output */
	protected static final String TAB_STRING = "\t"; //$NON-NLS-1$
	// Constants for rotating log file
	/** The default size a log file can grow before it is rotated */
	public static final int DEFAULT_LOG_SIZE = 1000;
	/** The default number of backup log files */
	public static final int DEFAULT_LOG_FILES = 10;
	/** The minimum size limit for log rotation */
	public static final int LOG_SIZE_MIN = 10;
	/** The system property used to specify the log level */
	public static final String PROP_LOG_LEVEL = "eclipse.log.level"; //$NON-NLS-1$
	/**
	 * The system property used to specify size a log file can grow before it is
	 * rotated
	 */
	public static final String PROP_LOG_SIZE_MAX = "eclipse.log.size.max"; //$NON-NLS-1$
	/**
	 * The system property used to specify the maximim number of backup log
	 * files to use
	 */
	public static final String PROP_LOG_FILE_MAX = "eclipse.log.backup.max"; //$NON-NLS-1$
	/** The extension used for log files */
	public static final String LOG_EXT = ".log"; //$NON-NLS-1$
	/** The extension markup to use for backup log files */
	public static final String BACKUP_MARK = ".bak_"; //$NON-NLS-1$
	static {
		String s = System.getProperty("line.separator"); //$NON-NLS-1$
		LINE_SEPARATOR = s == null ? "\n" : s; //$NON-NLS-1$
	}
	/**
	 * Indicates if the console messages should be printed to the console
	 * (System.out)
	 */
	protected boolean consoleLog = false;
	/** Indicates if the next log message is part of a new session */
	protected boolean newSession = true;
	/**
	 * The File object to store messages. This value may be null.
	 */
	protected File outFile;
	/**
	 * The Writer to log messages to.
	 */
	protected Writer writer;
	int maxLogSize = DEFAULT_LOG_SIZE; // The value is in KB.
	int maxLogFiles = DEFAULT_LOG_FILES;
	int backupIdx = 0;
	private int logLevel = FrameworkLogEntry.OK;

	/**
	 * Constructs an EclipseLog which uses the specified Writer to log messages
	 * to
	 * 
	 * @param writer
	 *            a writer to log messages to
	 */
	public RuntimePluginEclipseLog(Writer writer) {
		if (writer == null)
			// log to System.err by default
			this.writer = logForStream(System.err);
		else
			this.writer = writer;
	}

	private Throwable getRoot(Throwable t) {
		Throwable root = null;
		if (t instanceof BundleException)
			root = ((BundleException) t).getNestedException();
		if (t instanceof InvocationTargetException)
			root = ((InvocationTargetException) t).getTargetException();
		// skip inner InvocationTargetExceptions and BundleExceptions
		if (root instanceof InvocationTargetException || root instanceof BundleException) {
			Throwable deeplyNested = getRoot(root);
			if (deeplyNested != null)
				// if we have something more specific, use it, otherwise keep
				// what we have
				root = deeplyNested;
		}
		return root;
	}

	/**
	 * Helper method for writing out argument arrays.
	 * 
	 * @param header
	 *            the header
	 * @param args
	 *            the list of arguments
	 */
	protected void writeArgs(String header, String[] args) throws IOException {
		if (args == null || args.length == 0)
			return;
		write(header);
		for (int i = 0; i < args.length; i++) {
			// mask out the password argument for security
			if (i > 0 && PASSWORD.equals(args[i - 1]))
				write(" (omitted)"); //$NON-NLS-1$
			else
				write(" " + args[i]); //$NON-NLS-1$
		}
		writeln();
	}

	public void close() {
		try {
			if (writer != null) {
				Writer tmpWriter = writer;
				writer = null;
				tmpWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void log(FrameworkEvent frameworkEvent) {
		Bundle b = frameworkEvent.getBundle();
		Throwable t = frameworkEvent.getThrowable();
		String entry = b.getSymbolicName() == null ? b.getLocation() : b.getSymbolicName();
		int severity;
		switch (frameworkEvent.getType()) {
		case FrameworkEvent.INFO:
			severity = FrameworkLogEntry.INFO;
			break;
		case FrameworkEvent.ERROR:
			severity = FrameworkLogEntry.ERROR;
			break;
		case FrameworkEvent.WARNING:
			severity = FrameworkLogEntry.WARNING;
			break;
		default:
			severity = FrameworkLogEntry.OK;
		}
		FrameworkLogEntry logEntry = new FrameworkLogEntry(entry, severity, 0, "", 0, t, null); //$NON-NLS-1$
		log(logEntry);
	}

	public synchronized void log(FrameworkLogEntry logEntry) {
		if (logEntry == null)
			return;
		if (!isLoggable(logEntry))
			return;
		try {
			writeLog(0, logEntry);
			writer.flush();
		} catch (Exception e) {
			// any exceptions during logging should be caught
			System.err.println("An exception occurred while writing to the platform log:");//$NON-NLS-1$
			e.printStackTrace(System.err);
			System.err.println("Logging to the console instead.");//$NON-NLS-1$
			// we failed to write, so dump log entry to console instead
			try {
				writer = logForStream(System.err);
				writeLog(0, logEntry);
				writer.flush();
			} catch (Exception e2) {
				System.err.println("An exception occurred while logging to the console:");//$NON-NLS-1$
				e2.printStackTrace(System.err);
			}
		}
	}

	/**
	 * Returns a date string using the correct format for the log.
	 * 
	 * @param date
	 *            the Date to format
	 * @return a date string.
	 */
	protected String getDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		StringBuffer sb = new StringBuffer();
		appendPaddedInt(c.get(Calendar.YEAR), 4, sb).append('-');
		appendPaddedInt(c.get(Calendar.MONTH) + 1, 2, sb).append('-');
		appendPaddedInt(c.get(Calendar.DAY_OF_MONTH), 2, sb).append(' ');
		appendPaddedInt(c.get(Calendar.HOUR_OF_DAY), 2, sb).append(':');
		appendPaddedInt(c.get(Calendar.MINUTE), 2, sb).append(':');
		appendPaddedInt(c.get(Calendar.SECOND), 2, sb).append('.');
		appendPaddedInt(c.get(Calendar.MILLISECOND), 3, sb);
		return sb.toString();
	}

	private StringBuffer appendPaddedInt(int value, int pad, StringBuffer buffer) {
		pad = pad - 1;
		if (pad == 0)
			return buffer.append(Integer.toString(value));
		int padding = (int) Math.pow(10, pad);
		if (value >= padding)
			return buffer.append(Integer.toString(value));
		while (padding > value && padding > 1) {
			buffer.append('0');
			padding = padding / 10;
		}
		buffer.append(value);
		return buffer;
	}

	/**
	 * Returns a stacktrace string using the correct format for the log
	 * 
	 * @param t
	 *            the Throwable to get the stacktrace for
	 * @return a stacktrace string
	 */
	protected String getStackTrace(Throwable t) {
		if (t == null)
			return null;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		// ensure the root exception is fully logged
		Throwable root = getRoot(t);
		if (root != null) {
			pw.println("Root exception:"); //$NON-NLS-1$
			root.printStackTrace(pw);
		}
		return sw.toString();
	}

	/**
	 * Returns a Writer for the given OutputStream
	 * 
	 * @param output
	 *            an OutputStream to use for the Writer
	 * @return a Writer for the given OutputStream
	 */
	protected Writer logForStream(OutputStream output) {
		try {
			return new BufferedWriter(new OutputStreamWriter(output, "UTF-8")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			return new BufferedWriter(new OutputStreamWriter(output));
		}
	}

	/**
	 * Writes the log entry to the log using the specified depth. A depth value
	 * of 0 idicates that the log entry is the root entry. Any value greater
	 * than 0 indicates a sub-entry.
	 * 
	 * @param depth
	 *            the depth of th entry
	 * @param entry
	 *            the entry to log
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void writeLog(int depth, FrameworkLogEntry entry) throws IOException {
		// writeEntry(depth, entry);
		writeMessage(entry);
		writeStack(entry);
		FrameworkLogEntry[] children = entry.getChildren();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				writeLog(depth + 1, children[i]);
			}
		}
	}

	/**
	 * Writes the ENTRY or SUBENTRY header for an entry. A depth value of 0
	 * indicates that the log entry is the root entry. Any value greater than 0
	 * indicates a sub-entry.
	 * 
	 * @param depth
	 *            the depth of th entry
	 * @param entry
	 *            the entry to write the header for
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void writeEntry(int depth, FrameworkLogEntry entry) throws IOException {
		if (depth == 0) {
			writeln(); // write a blank line before all !ENTRY tags bug #64406
			write(ENTRY);
		} else {
			write(SUBENTRY);
			writeSpace();
			write(Integer.toString(depth));
		}
		writeSpace();
		write(entry.getEntry());
		writeSpace();
		write(Integer.toString(entry.getSeverity()));
		writeSpace();
		write(Integer.toString(entry.getBundleCode()));
		writeSpace();
		write(getDate(new Date()));
		writeln();
	}

	/**
	 * Writes the MESSAGE header to the log for the given entry.
	 * 
	 * @param entry
	 *            the entry to write the message for
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void writeMessage(FrameworkLogEntry entry) throws IOException {
		// write(MESSAGE);
		// writeSpace();
		writeln(entry.getMessage());
	}

	/**
	 * Writes the STACK header to the log for the given entry.
	 * 
	 * @param entry
	 *            the entry to write the stacktrace for
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void writeStack(FrameworkLogEntry entry) throws IOException {
		Throwable t = entry.getThrowable();
		if (t != null) {
			String stack = getStackTrace(t);
			write(STACK);
			writeSpace();
			write(Integer.toString(entry.getStackCode()));
			writeln();
			write(stack);
		}
	}

	/**
	 * Writes the given message to the log.
	 * 
	 * @param message
	 *            the message
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void write(String message) throws IOException {
		if (message != null) {
			writer.write(message);
			if (consoleLog)
				System.out.print(message);
		}
	}

	/**
	 * Writes the given message to the log and a newline.
	 * 
	 * @param s
	 *            the message
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void writeln(String s) throws IOException {
		write(s);
		writeln();
	}

	/**
	 * Writes a newline log.
	 * 
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void writeln() throws IOException {
		write(LINE_SEPARATOR);
	}

	/**
	 * Writes a space to the log.
	 * 
	 * @throws IOException
	 *             if any error occurs writing to the log
	 */
	protected void writeSpace() throws IOException {
		write(" "); //$NON-NLS-1$
	}

	/**
	 * Determines if the log entry should be logged based on log level.
	 */
	private boolean isLoggable(FrameworkLogEntry entry) {
		if (logLevel == 0)
			return true;
		return (entry.getSeverity() & logLevel) != 0;
	}
}
