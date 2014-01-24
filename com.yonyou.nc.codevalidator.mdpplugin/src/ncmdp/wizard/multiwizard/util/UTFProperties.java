package ncmdp.wizard.multiwizard.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;

public class UTFProperties extends Hashtable {
	private static final String charset_8859_1 = "8859_1";

	private String charsetName = null;

	/**
	 * use serialVersionUID from JDK 1.1.X for interoperability
	 */
	private static final long serialVersionUID = 4112578634029874840L;

	/**
	 * Creates an empty property list with no default values.
	 */
	//    public UTFProperties() {
	//    	this(charset_8859_1);
	//    }
	/**
	 * Creates an empty property list with the specified defaults.
	 *
	 * @param   defaults   the defaults.
	 */
	public UTFProperties(String charsetName) {
		if (charsetName == null) {
			charsetName = charset_8859_1;
		}
		this.charsetName = charsetName;

	}

	/**
	  */
	public synchronized Object setProperty(String key, String value) {
		return put(key, value);
	}

	private static final String keyValueSeparators = "=: \t\r\n\f";

	private static final String strictKeyValueSeparators = "=:";

	private static final String specialSaveChars = "=: \t\r\n\f#!";

	private static final String whiteSpaceChars = " \t\r\n\f";

	/**
	 */
	public synchronized void load(InputStream inStream) throws IOException {
		if (inStream == null)
			return;
		BufferedReader in = new BufferedReader(new InputStreamReader(inStream, charsetName));
		while (true) {
			// Get next line
			String line = in.readLine();
			if (line == null)
				return;

			if (line.length() > 0) {

				// Find start of key
				int len = line.length();
				int keyStart;
				for (keyStart = 0; keyStart < len; keyStart++)
					if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1)
						break;

				// Blank lines are ignored
				if (keyStart == len)
					continue;

				// Continue lines that end in slashes if they are not comments
				char firstChar = line.charAt(keyStart);
				if ((firstChar != '#') && (firstChar != '!')) {
					while (continueLine(line)) {
						String nextLine = in.readLine();
						if (nextLine == null)
							nextLine = "";
						String loppedLine = line.substring(0, len - 1);
						// Advance beyond whitespace on new line
						int startIndex;
						for (startIndex = 0; startIndex < nextLine.length(); startIndex++)
							if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1)
								break;
						nextLine = nextLine.substring(startIndex, nextLine.length());
						line = new String(loppedLine + nextLine);
						len = line.length();
					}

					// Find separation between key and value
					int separatorIndex;
					for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
						char currentChar = line.charAt(separatorIndex);
						if (currentChar == '\\')
							separatorIndex++;
						else if (keyValueSeparators.indexOf(currentChar) != -1)
							break;
					}

					// Skip over whitespace after key if any
					int valueIndex;
					for (valueIndex = separatorIndex; valueIndex < len; valueIndex++)
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
							break;

					// Skip over one non whitespace key value separators if any
					if (valueIndex < len)
						if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1)
							valueIndex++;

					// Skip over white space after other separators if any
					while (valueIndex < len) {
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
							break;
						valueIndex++;
					}
					String key = line.substring(keyStart, separatorIndex);
					String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";
					if (charsetName.equals(charset_8859_1)) {
						key = MLUtil.gb2Unicode(key);
						value = MLUtil.gb2Unicode(value);
					}
					if (key == null || value == null) {
						MDPLogger.info("警告:加载资源文件时 key = " + key + "     value = " + value);
					} else {
						put(key, value);
					}
				}
			}
		}
	}

	/*
	 * Returns true if the given line is a line that must
	 * be appended to the next line
	 */
	private boolean continueLine(String line) {
		int slashCount = 0;
		int index = line.length() - 1;
		while ((index >= 0) && (line.charAt(index--) == '\\'))
			slashCount++;
		return (slashCount % 2 == 1);
	}

	/*
	 * Converts encoded &#92;uxxxx to unicode chars
	 * and changes special saved chars to their original forms
	 */
	private String loadConvert(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/*
	 * Converts unicodes to encoded &#92;uxxxx
	 * and writes out any of the characters in specialSaveChars
	 * with a preceding slash
	 */
	private String saveConvert(String theString, boolean escapeSpace) {
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len * 2);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			switch (aChar) {
				case ' ':
					if (x == 0 || escapeSpace)
						outBuffer.append('\\');

					outBuffer.append(' ');
					break;
				case '\\':
					outBuffer.append('\\');
					outBuffer.append('\\');
					break;
				case '\t':
					outBuffer.append('\\');
					outBuffer.append('t');
					break;
				case '\n':
					outBuffer.append('\\');
					outBuffer.append('n');
					break;
				case '\r':
					outBuffer.append('\\');
					outBuffer.append('r');
					break;
				case '\f':
					outBuffer.append('\\');
					outBuffer.append('f');
					break;
				default:
					if ((aChar < 0x0020) || (aChar > 0x007e)) {
						outBuffer.append('\\');
						outBuffer.append('u');
						outBuffer.append(toHex((aChar >> 12) & 0xF));
						outBuffer.append(toHex((aChar >> 8) & 0xF));
						outBuffer.append(toHex((aChar >> 4) & 0xF));
						outBuffer.append(toHex(aChar & 0xF));
					} else {
						if (specialSaveChars.indexOf(aChar) != -1)
							outBuffer.append('\\');
						outBuffer.append(aChar);
					}
			}
		}
		return outBuffer.toString();
	}

	/**
	 */
	public synchronized void save(OutputStream out) {
		try {
			store(out);
		} catch (IOException e) {
		}
	}

	/**
	 */
	public synchronized void store(OutputStream out) throws IOException {
		BufferedWriter awriter;
		awriter = new BufferedWriter(new OutputStreamWriter(out, charsetName));
		List<String> keyList = new ArrayList<String>();
		for (Enumeration e = keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			if (!StringUtil.isEmptyWithTrim(key)) {
				keyList.add(key);
			}
		}
		if (keyList.size() > 0) {
			Collections.sort(keyList, new Comparator() {
				public int compare(Object o1, Object o2) {
					return String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
				}
			});
		}
		for (String key : keyList) {
			String val = (String) get(key);
			if (charsetName.equals(charset_8859_1)) {
				key = MLUtil.uniCode2Gb(key);
				val = MLUtil.uniCode2Gb(val);
			}
			writeln(awriter, key + "=" + val);
		}
		awriter.flush();
	}

	private static void writeln(BufferedWriter bw, String s) throws IOException {

		bw.write(s);
		bw.newLine();
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * If the key is not found in this property list, the default property list,
	 * and its defaults, recursively, are then checked. The method returns
	 * <code>null</code> if the property is not found.
	 *
	 * @param   key   the property key.
	 * @return  the value in this property list with the specified key value.
	 * @see     #setProperty
	 * @see     #defaults
	 */
	public String getProperty(String key) {
		Object oval = super.get(key);
		String sval = (oval instanceof String) ? (String) oval : null;
		//	return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
		return sval;
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * If the key is not found in this property list, the default property list,
	 * and its defaults, recursively, are then checked. The method returns the
	 * default value argument if the property is not found.
	 *
	 * @param   key            the hashtable key.
	 * @param   defaultValue   a default value.
	 *
	 * @return  the value in this property list with the specified key value.
	 * @see     #setProperty
	 * @see     #defaults
	 */
	public String getProperty(String key, String defaultValue) {
		String val = getProperty(key);
		return (val == null) ? defaultValue : val;
	}

	/**
	 * Returns an enumeration of all the keys in this property list,
	 * including distinct keys in the default property list if a key
	 * of the same name has not already been found from the main
	 * properties list.
	 *
	 * @return  an enumeration of all the keys in this property list, including
	 *          the keys in the default property list.
	 * @see     java.util.Enumeration
	 * @see     java.util.Properties#defaults
	 */
	public Enumeration propertyNames() {
		Hashtable h = new Hashtable();
		enumerate(h);
		return h.keys();
	}

	/**
	 * Prints this property list out to the specified output stream.
	 * This method is useful for debugging.
	 *
	 * @param   out   an output stream.
	 */
	public void list(PrintStream out) {
		out.println("-- listing properties --");
		Hashtable h = new Hashtable();
		enumerate(h);
		for (Enumeration e = h.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = (String) h.get(key);
			if (val.length() > 40) {
				val = val.substring(0, 37) + "...";
			}
			out.println(key + "=" + val);
		}
	}

	/**
	 * Prints this property list out to the specified output stream.
	 * This method is useful for debugging.
	 *
	 * @param   out   an output stream.
	 * @since   JDK1.1
	 */
	/*
	 * Rather than use an anonymous inner class to share common code, this
	 * method is duplicated in order to ensure that a non-1.1 compiler can
	 * compile this file.
	 */
	public void list(PrintWriter out) {
		out.println("-- listing properties --");
		Hashtable h = new Hashtable();
		enumerate(h);
		for (Enumeration e = h.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = (String) h.get(key);
			if (val.length() > 40) {
				val = val.substring(0, 37) + "...";
			}
			out.println(key + "=" + val);
		}
	}

	/**
	 * Enumerates all key/value pairs in the specified hastable.
	 * @param h the hashtable
	 */
	private synchronized void enumerate(Hashtable h) {
		//	if (defaults != null) {
		//	    defaults.enumerate(h);
		//	}
		for (Enumeration e = keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			h.put(key, get(key));
		}
	}

	/**
	 * Convert a nibble to a hex character
	 * @param	nibble	the nibble to convert.
	 */
	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/** A table of hex digits */
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
			'B', 'C', 'D', 'E', 'F' };

	public static void main(String[] args) {
		InputStream in = null;
		String filename = "c:/2002.properties";
		String charsetName = "UTF-16";
		UTFProperties properties = new UTFProperties(charsetName);
		try {
			in = new FileInputStream(filename);
			if (in != null) {
				in = new BufferedInputStream(in);
				properties.load(in);
			} else {
				System.out.println("input is null");
			}
			properties.list(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
		}

	}
}