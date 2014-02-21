package com.web.pub.fw.editor;

import java.beans.PropertyEditorSupport;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-2-11 обнГ1:40:57
 */
public class BooleanEditor extends PropertyEditorSupport {

	public BooleanEditor() {
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ("1".equals(text) || "true".equals(text)) {
			setValue(Boolean.TRUE);
		} else if ("0".equals(text) || "false".equals(text)) {
			setValue(Boolean.FALSE);
		} else {
			setValue(null);
		}
	}
}
