package com.web.pub.fw.editor;

import java.beans.PropertyEditorSupport;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-2-11 обнГ1:41:11
 */
public class StringEditor extends PropertyEditorSupport {

	public StringEditor() {
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (null == text || "null".equals(text)) {
			setValue(null);
		} else {
			setValue(text);
		}
		return;
	}
}
